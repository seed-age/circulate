package com.sunnsoft.util.mail;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SendMailRejectionHandler implements RejectedExecutionHandler {

	private Log logger = LogFactory.getLog(SendMailRejectionHandler.class);

	private int randomReentrantSeconds = 1;

	private static final Timer TIMER = new Timer(true);

	public void rejectedExecution(final Runnable r,
			final ThreadPoolExecutor executor) {
		logger.warn(r + " has been rejected,waiting");
		// logger.warn("	getActiveCount:"+executor.getActiveCount());
		// logger.warn("	getCorePoolSize:"+executor.getCorePoolSize());
		// logger.warn("	getCompletedTaskCount:"+executor.getCompletedTaskCount());
		// logger.warn("	getKeepAliveTime:"+executor.getKeepAliveTime(TimeUnit.MILLISECONDS));
		// logger.warn("	getLargestPoolSize:"+executor.getLargestPoolSize());
		// logger.warn("	getPoolSize:"+executor.getPoolSize());
		// logger.warn("	getMaximumPoolSize:"+executor.getMaximumPoolSize());
		// logger.warn("	getTaskCount:"+executor.getTaskCount());
		// logger.warn("	getQueue().size:"+executor.getQueue().size());
		// logger.warn("	getQueue().remainingCapacity:"+executor.getQueue().remainingCapacity());
		TIMER.schedule(new TimerTask() {

			@Override
			public void run() {
				if (executor.isShutdown()) {
					new Thread(r).start();
				} else {
					executor.execute(r);
				}
			}

		}, (long) (Math.random() * 1000 * randomReentrantSeconds));

	}

	public void setRandomReentrantSeconds(int randomReentrantSeconds) {
		this.randomReentrantSeconds = randomReentrantSeconds;
	}

}
