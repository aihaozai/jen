package com.example.cycleview.cache.manager;



import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManager {
	private static ThreadManager instance = new ThreadManager();

	private ThreadManager() {
	}

	public static ThreadManager getInstance() {
		return instance;
	}

	private ThreadPoolProxy shortPool;
	private ThreadPoolProxy longPool;

	/** 创建短线程池(处理文件操作) */
	public synchronized ThreadPoolProxy createShortPool() {
		if (shortPool == null) {
			shortPool = new ThreadPoolProxy(3, 3, 5000);
		}
		return shortPool;
	}

	/** 创建长线程池(处理网络请求) */
	public synchronized ThreadPoolProxy creatLongPool() {
		if (longPool == null) {
			longPool = new ThreadPoolProxy(5, 5, 5000);
		}
		return longPool;
	}

	/**
	 * 线程池配置类(每一对象都是一个单独线程池)
	 *
	 * @author Administrator
	 *
	 */
	public class ThreadPoolProxy {
		private ThreadPoolExecutor mThreadPoolExecutor;
		private int corePoolSize;
		private int maximumPoolSize;
		private long keepAliveTime;

		public ThreadPoolProxy(int corePoolSize, int maximumPoolSize,
							   long keepAliveTime) {
			this.corePoolSize = corePoolSize;
			this.maximumPoolSize = maximumPoolSize;
			this.keepAliveTime = keepAliveTime;
		}

		/** 执行一个线程任务 */
		public void execute(Runnable runnable) {
			if (mThreadPoolExecutor == null) {

				/**
				 *
				 * 参数解释；
				 * 第一个参数：核心的线程数量（corePoolSize一般使用cpu核心数 * 2 + 1）
				 * 第二个参数：如果corePoolSize都用完了，再创建多少个线程（跟corePoolSize的数量一致）
				 * 第三个参数：每个线程存活的时间（和第四个参数配合使用）
				 * 第四个参数：第三个参数的单位，传递一个任务队列，用来保存任务。
				 * 	（这个是为了方便用户自定义存储的数据结构才当做参数提供出来）
				 *
				 * 1. corePoolSize和maximumPoolSize的数量建议相等 2.
				 * corePoolSize一般使用cpu核心数 * 2 + 1
				 */
				mThreadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
						maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS,
						new LinkedBlockingDeque<Runnable>(10));
			}

			mThreadPoolExecutor.execute(runnable);

		}

		/** 取消一个线程任务 */
		public void cancel(Runnable runnable) {
			if (mThreadPoolExecutor != null && !mThreadPoolExecutor.isShutdown() &&!mThreadPoolExecutor.isTerminated()) {
				mThreadPoolExecutor.remove(runnable);
			}

		}
	}

}
