package instrumentation.junit;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import instrumentation.avio.AvioMetric;
import instrumentation.concurpairs.ConcurPairsMetric;
import instrumentation.dupairs.DUPairsMetric;
import instrumentation.ereaser.EreaserMetric;
import instrumentation.goodlock.GoodLockMetric;
import instrumentation.objectid.ObjectIdIdentifier;
import instrumentation.objectid.ObjectRenamer;
import instrumentation.sync.SyncMetric;
import instrumentation.threadid.ThreadIdentifier;
import instrumentation.threadid.ThreadRenamer;

public class ParallelMetricRule implements org.junit.rules.TestRule {

	private static class ParallelMetricStatement extends Statement {

		private final StopCondition stopCondition;
		private final Statement statement;

		private ParallelMetricStatement(StopCondition stopCondition, Statement statement) {
			this.stopCondition = stopCondition;
			this.statement = statement;
		}

		@Override
		public void evaluate() throws Throwable {
			try {
				new AvioMetric().reset();
				new ConcurPairsMetric().reset();
				new DUPairsMetric().reset();
				new EreaserMetric().reset();
				new GoodLockMetric().reset();
				new SyncMetric().reset();
				ThreadIdentifier.clear();
				ObjectIdIdentifier.clear();

				ThreadRenamer tr = new ThreadRenamer();
				ObjectRenamer or = new ObjectRenamer();
				stopCondition.prepare();

				int round = 0;
				while (!stopCondition.canStop(round)) {
					try {
						statement.evaluate();
					} catch (Throwable e) {
						stopCondition.update(tr, or);
						stopCondition.log(round);
						stopCondition.finish();
						throw e;
					}

					// Update
					stopCondition.update(tr, or);

					// Log
					stopCondition.log(round);

					// Clean up
					ThreadIdentifier.clear();
					ObjectIdIdentifier.clear();

					new AvioMetric().reset();
					new ConcurPairsMetric().reset();
					new DUPairsMetric().reset();
					new EreaserMetric().reset();
					new GoodLockMetric().reset();
					new SyncMetric().reset();

					// Next round
					++round;
				}
			} finally {
				stopCondition.finish();
			}
		}
	}

	@Override
	public Statement apply(Statement base, Description description) {
		StopCondition stopConditon = new StopCondition();

		stopConditon.setMax(description.getAnnotation(MaximumTimes.class));
		stopConditon.setAvio(description.getAnnotation(UseAvioMetric.class));
		stopConditon.setConcurPairs(description.getAnnotation(UseConcurPairsMetric.class));
		stopConditon.setDUPairs(description.getAnnotation(UseDUPairsMetric.class));
		stopConditon.setEreaser(description.getAnnotation(UseEreaserMetric.class));
		stopConditon.setGoodLock(description.getAnnotation(UseGoodLockMetric.class));
		stopConditon.setSync(description.getAnnotation(UseSyncMetric.class));

		stopConditon.setLog(description.getAnnotation(ParallelMetricLog.class));

		return new ParallelMetricStatement(stopConditon, base);
	}

}
