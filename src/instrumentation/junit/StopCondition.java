package instrumentation.junit;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import instrumentation.Metric;
import instrumentation.avio.AvioMetric;
import instrumentation.avio.AvioTask;
import instrumentation.concurpairs.ConcurPairsMetric;
import instrumentation.concurpairs.ConcurPairsTask;
import instrumentation.dupairs.DUPairsMetric;
import instrumentation.dupairs.DUPairsTask;
import instrumentation.ereaser.EreaserMetric;
import instrumentation.ereaser.EreaserTask;
import instrumentation.goodlock.GoodLockMetric;
import instrumentation.goodlock.GoodLockTask;
import instrumentation.objectid.ObjectIdIdentifier;
import instrumentation.objectid.ObjectRenamer;
import instrumentation.sync.SyncMetric;
import instrumentation.sync.SyncTask;
import instrumentation.threadid.ThreadIdentifier;
import instrumentation.threadid.ThreadRenamer;

public class StopCondition {

	private MaximumTimes max = null;
	private boolean hasMetric = false;
	private Optional<ChangeTracker<AvioTask>> avio = Optional.empty();
	private Optional<ChangeTracker<ConcurPairsTask>> concurpairs = Optional.empty();
	private Optional<ChangeTracker<DUPairsTask>> dupairs = Optional.empty();
	private Optional<ChangeTracker<EreaserTask>> ereaser = Optional.empty();
	private Optional<ChangeTracker<GoodLockTask>> goodlock = Optional.empty();
	private Optional<ChangeTracker<SyncTask>> sync = Optional.empty();

	private String log = null;
	private BufferedWriter out = null;

	public void setMax(MaximumTimes max) {
		this.max = max;
	}

	public void setAvio(UseAvioMetric avio) {
		if (avio != null) {
			this.hasMetric = true;
			this.avio = Optional
					.of(new ChangeTracker<>(avio.threshold(), avio.optional(), avio.window(), new AvioMetric()));
		}
	}

	public void setConcurPairs(UseConcurPairsMetric concurpairs) {
		if (concurpairs != null) {
			this.hasMetric = true;
			this.concurpairs = Optional.of(new ChangeTracker<>(concurpairs.threshold(), concurpairs.optional(),
					concurpairs.window(), new ConcurPairsMetric()));
		}
	}

	public void setDUPairs(UseDUPairsMetric dupairs) {
		if (dupairs != null) {
			this.hasMetric = true;
			this.dupairs = Optional.of(new ChangeTracker<>(dupairs.threshold(), dupairs.optional(), dupairs.window(),
					new DUPairsMetric()));
		}
	}

	public void setEreaser(UseEreaserMetric ereaser) {
		if (ereaser != null) {
			this.hasMetric = true;
			this.ereaser = Optional.of(new ChangeTracker<>(ereaser.threshold(), ereaser.optional(), ereaser.window(),
					new EreaserMetric()));
		}
	}

	public void setGoodLock(UseGoodLockMetric goodlock) {
		if (goodlock != null) {
			this.hasMetric = true;
			this.goodlock = Optional.of(new ChangeTracker<>(goodlock.threshold(), goodlock.optional(),
					goodlock.window(), new GoodLockMetric()));
		}
	}

	public void setSync(UseSyncMetric sync) {
		if (sync != null) {
			this.hasMetric = true;
			this.sync = Optional
					.of(new ChangeTracker<>(sync.threshold(), sync.optional(), sync.window(), new SyncMetric()));
		}
	}

	public void update(ThreadRenamer tr, ObjectRenamer or) throws IOException {
		avio.ifPresent(x -> x.update(tr, or));
		concurpairs.ifPresent(x -> x.update(tr, or));
		dupairs.ifPresent(x -> x.update(tr, or));
		ereaser.ifPresent(x -> x.update(tr, or));
		goodlock.ifPresent(x -> x.update(tr, or));
		sync.ifPresent(x -> x.update(tr, or));
	}

	public boolean canStop(int round) {
		// Not used, run once.
		if (max == null && !hasMetric && round > 0) {
			return true;
		}

		// Maximum number of runs?
		if (max != null && max.amount() <= round && max.amount() != 0) {
			return true;
		} else if (max == null && round >= 10000) {
			// default max
			return true;
		}

		if (avio.map(x -> x.canStop()).orElse(false)) {
			return true;
		}
		if (concurpairs.map(x -> x.canStop()).orElse(false)) {
			return true;
		}
		if (dupairs.map(x -> x.canStop()).orElse(false)) {
			return true;
		}
		if (ereaser.map(x -> x.canStop()).orElse(false)) {
			return true;
		}
		if (goodlock.map(x -> x.canStop()).orElse(false)) {
			return true;
		}
		if (sync.map(x -> x.canStop()).orElse(false)) {
			return true;
		}

		return false;
	}

	private class ChangeTracker<T> {
		private double treshold;
		private boolean optional;
		private Metric<T> metric;
		private Set<T> tasks;
		private int previous;
		private Queue<Integer> changes;
		private int changesLength;

		public ChangeTracker(double treshold, boolean optional, int window, Metric<T> metric) {
			this.treshold = treshold;
			this.optional = optional;
			this.metric = metric;
			this.tasks = new HashSet<>();
			this.previous = 0;
			this.changes = new ArrayDeque<>();
			this.changesLength = window;
		}

		public void update(ThreadRenamer tr, ObjectRenamer or) {
			tasks.addAll(metric.getTranslatedTasks(tr, ThreadIdentifier.threadMapping, or, ObjectIdIdentifier.map));

			int delta = tasks.size() - previous;

			this.changes.add(delta);

			while (this.changes.size() > this.changesLength) {
				this.changes.poll();
			}

			this.previous = tasks.size();
		}

		public boolean canStop() {
			if (this.changes.size() < this.changesLength) {
				return false;
			}

			if (tasks.size() == 0 && optional) {
				return false;
			}

			double avg = this.changes.stream().mapToInt(x -> x.intValue()).average().getAsDouble();

			if (avg <= treshold) {
				return true;
			} else {
				return false;
			}
		}

		public int getSize() {
			return tasks.size();
		}
	}

	public void setLog(ParallelMetricLog annotation) {
		if (annotation != null) {
			log = annotation.file();
		}
	}

	public void prepare() throws IOException {
		if (log != null) {
			out = Files.newBufferedWriter(Paths.get(log));
		}
	}

	public void finish() throws IOException {
		if (log != null && out != null) {
			out.close();
			out = null;
		}
	}

	public void log(int round) throws IOException {
		if (out != null) {
			out.write(String.format("%d ", round));
			out.write(String.format("%d ", avio.map(x -> x.getSize()).orElse(10)));
			out.write(String.format("%d ", concurpairs.map(x -> x.getSize()).orElse(0)));
			out.write(String.format("%d ", dupairs.map(x -> x.getSize()).orElse(0)));
			out.write(String.format("%d ", ereaser.map(x -> x.getSize()).orElse(0)));
			out.write(String.format("%d ", goodlock.map(x -> x.getSize()).orElse(0)));
			out.write(String.format("%d\n", sync.map(x -> x.getSize()).orElse(0)));
		}
	}
}
