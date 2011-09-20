package com.xtremelabs.xtremeutil.util.concurrency;

import com.xtremelabs.xtremeutil.util.logger.XLogger;
import net.rim.device.api.system.SystemListener2;

import java.lang.ref.WeakReference;
import java.util.Vector;

/**
 * A runnable object that can be used in an animation thread.  Purpose of this is to make sure all Animatable 
 * that animate use only one thread to repaint itself. eg. multiple loading cells get added to the animator.
 * Steps:
 * 1. Create an Animatable object
 * 2. Create an Animator
 * 3. Add Animatable to Animator
 * 4. Start a new thread using Animator
 *
 * Note: 
 * - call animator.refresh() after each paint.
 * - if Animatable is a Field, shouldUpdate() should return true only if it's visible on screen  
 * 
 * Example Usage:  
 * 
 * 	private Animator animator;
 *
 *	public startAnimation() {
 *		Loading loading = new Loading();
 *		add(loading);
 *		
 *		animator = new Animator();
 *		animator.addAnimatedManager(loading);
 *		new Thread(animator).start();
 *	}
 *
 *	public class Loading extends Field implements Animatable {
 *		int framecount = 0;

 *		protected void layout(int width, int height) {
 *			setExtent(480, 50);
 *		}
 *
 *		protected void paint(Graphics graphics) {
 *			graphics.setColor(Color.RED);			
 *			graphics.drawLine(framecount, 0, framecount, getHeight());
 *			animator.refresh();
 *		}
 *
 *		public boolean shouldUpdate() {
 *			return getIndex() > -1 && isVisible();
 *		}
 *
 *		public void update() {
 *			framecount++;
 *			invalidate();
 *		}
 *	}     
 *        
 * @author Xtreme Labs
 *
 */
public class Animator implements Runnable, SystemListener2 {
    private static String ANIMATION_PAUSE_BROKEN = "animation pause broken";
    public int frameRate = 24; // FPS
    final Vector weakReferenceContainer = new Vector(); // Vector of WeakReference objects holding Animatable 
    private volatile boolean pause = false;
    private volatile boolean suspend = true; // start with suspend state
    private volatile boolean stopped = true; //initialize with stopped
    private final Object pauseLock = new Object();
    private final Object suspendLock = new Object();

    // add an Animatable to this Animator
    public void addAnimatedManager(Animatable animatable) {
        WeakReference wr = new WeakReference(animatable);
        weakReferenceContainer.addElement(wr);
    }

    /*
     * Set the frame rate for this animator
     */
    public void setFrameRate(int fps) {
    	frameRate = fps;
    }
    
    /*
     *  Call this to tell the animator to update all it's Animatable objects 
     */
    public void update() {
        synchronized (suspendLock) {
            suspend = false;
            suspendLock.notifyAll();
        }
    }

    public void setPaused(boolean paused) {
        synchronized (pauseLock) {
            pause = paused;
            if (!paused) {
                pauseLock.notifyAll();
            }
        }
    }
    
    public void run() {
        try {
        	stopped = false;
            while (!stopped) {
                checkSuspendState();
                checkPausedState();
                try {
                	// Sleep for a set amount of time to keep framerate constant
                    Thread.sleep(1000/frameRate); 
                }
                catch (InterruptedException iex) {
                    //ignore
                }

                int visibleCount = 0;
                int size = weakReferenceContainer.size();
                for (int i = 0; i < size; i++) {
                    WeakReference reference = (WeakReference) weakReferenceContainer.elementAt(i);
                    final Object obj = reference.get();
                    if (obj == null || !(obj instanceof Animatable)) {
                        weakReferenceContainer.removeElement(reference);
                        size--;
                    } else {
                        final Animatable animatable = (Animatable) obj;
                        if (animatable.shouldUpdate()) {
                        	animatable.update();
                            visibleCount++;
                        }
                    }
                }
                if (visibleCount < 1) {
                    suspend = true;
                } else {
                }
            }
            weakReferenceContainer.removeAllElements();
        }
        catch (Exception e) {
            XLogger.warn(getClass(), "Animator returned exception:\n" + e);
        }
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    private void checkPausedState() {
        synchronized (pauseLock) {
            if (pause) {
                try {
                    pauseLock.wait();
                } catch (InterruptedException e) {
                	XLogger.warn(getClass(), ANIMATION_PAUSE_BROKEN);
                }
            }
        }
    }

    private void checkSuspendState() {
        synchronized (suspendLock) {
            if (suspend) {
                try {
                    suspendLock.wait();
                } catch (InterruptedException e) {
                    //
                }
            }
        }
    }

    private boolean isPaused() {
        synchronized (pauseLock) {
            return pause;
        }
    }
    
    /**
     * SystemListener2 methods
     */
    public void powerOffRequested(int i) {
    }

    public void cradleMismatch(boolean b) {
    }

    public void fastReset() {
    }

    public void backlightStateChange(final boolean on) {
        setPaused(!on);
    }


    public void usbConnectionStateChange(int i) {
    }

    public void powerOff() {
    }

    public void powerUp() {
    }

    public void batteryLow() {
    }

    public void batteryGood() {
    }

    public void batteryStatusChange(int i) {
    }
}
