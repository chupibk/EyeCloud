package fi.eyecloud.gui.heatmap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MulticoreTest {

	class FibTask implements Callable<Long> {
		private int n;

		public FibTask(int n) {
			this.n = n;
		}

		@Override
		public Long call() throws Exception {
			return fib(this.n);
		}
	}

	public static long fib(int number) {
		if (number <= 2) {
			return 1;
		}
		long tmp;
		tmp = fib(number - 1) + fib(number - 2);
		return tmp;
	}

	public MulticoreTest() throws InterruptedException {
		int cpus = Runtime.getRuntime().availableProcessors();
		ExecutorService pool = Executors.newFixedThreadPool(cpus);
		List<FibTask> tasks = new ArrayList<FibTask>();
		for (int i = 0; i < cpus; i++) {
			tasks.add(new FibTask(45));
		}
		List<Future<Long>> results = pool.invokeAll(tasks);

		for (Future<Long> result : results) {
			if (result.isCancelled()) {
				System.err.println("Life is a bitch");
			} else {
				try {
					System.out.println("result: " + result.get());
				} catch (ExecutionException e) {
					System.out.println(e.getCause());
				}
			}
		}
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		//new MulticoreTest();
		System.out.println(MulticoreTest.fib(45));
		System.out.println(MulticoreTest.fib(45));
		System.out.println("Running Time: " + (System.currentTimeMillis() - start));
	}

}
