package instrumentation.inject;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class InjectableMethods {

	private static Random r = new Random(System.currentTimeMillis());

	public static int SEMAPHORE_PERMITS = 4;
	public static int YIELD_MAX = 500;
	public static int WAIT_MAX = 2;
	public static int BUSYWAIT_LOOPS = 1000000;
	public static int SLEEP_MAX = 2;

	public static void randomYield() {
		int l = (int) (r.nextFloat() * YIELD_MAX);
		for (int i = 0; i < l; ++i) {
			Thread.yield();
		}
	}

	private static Object waitable = new Object();

	public static void randomWait() {
		synchronized (waitable) {
			try {
				waitable.wait((int) (r.nextFloat() * WAIT_MAX) + 1);
			} catch (InterruptedException e) {
			}
		}
	}

	public static void synchronizedYield() {
		synchronized (waitable) {
			randomYield();
		}
	}

	public static void busywait() {
		int l = new Random().nextInt(BUSYWAIT_LOOPS);
		for (int i = 0; i < l; ++i)
			;
	}

	public static void randomSleep() {
		try {
			Thread.sleep((long) (r.nextFloat() * SLEEP_MAX));
		} catch (InterruptedException e) {
		}
	}

	private static Semaphore available = new Semaphore(SEMAPHORE_PERMITS, true);

	public static void setSemaphorePermits(int permits) {
		SEMAPHORE_PERMITS = permits;
		available = new Semaphore(SEMAPHORE_PERMITS, true);
	}

	public static void semaphoreOne() {
		try {
			if (available.availablePermits() > 0 && Thread.activeCount() > 1) {
				available.acquire();
				randomYield();
				available.release();
			} else {
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
