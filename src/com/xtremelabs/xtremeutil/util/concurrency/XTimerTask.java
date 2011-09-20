package com.xtremelabs.xtremeutil.util.concurrency;

import com.xtremelabs.xtremeutil.util.logger.XLogger;
import net.rim.device.api.system.ApplicationDescriptor;

import java.util.TimerTask;

public abstract class XTimerTask extends TimerTask {
	private volatile boolean stopRequested = false;
	private volatile boolean running = false;

	protected abstract void execute();

	/**
	 * @param runnable
	 *            the object passed in with the run method that is executed in
	 *            the timertask
	 * @return an instance of XttremeTimerTask that runs the passing runnable's
	 *         run method
	 */
	public static XTimerTask makeTimerTask(final Runnable runnable) {
		return new XTimerTask() {
			public synchronized void execute() {
				runnable.run();
			}
		};
	}

	public final void run() {
		if (!stopRequested && !running) {
			synchronized (this) {
				running = true;
				try {
					execute();
				} catch (final Throwable t) {
					t.printStackTrace();
					
					StringBuffer message = new StringBuffer("An error occurred in ");
					message.append(ApplicationDescriptor.currentApplicationDescriptor().getName());
					message.append("\nException detail: ");
					message.append(t);
					XLogger.error(getClass(), message.toString());
				}
				running = false;
			}
		} else {
			super.cancel();
		}
	}

	public boolean cancel() {
		stopRequested = true;
		return super.cancel();
	}

	public boolean isStopRequested() {
		return stopRequested;
	}
}
