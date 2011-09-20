package com.xtremelabs.xtremeutil.ui.manager;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.VerticalFieldManager;


public class CarouselScreenManager extends Manager implements Runnable {
    private volatile boolean animationStopped = true;
    private volatile int x = 0;
    private Thread pageThread;
    private volatile int currentDirection;
    private volatile int currentContainerOrigin = 0;
    private final Object runLock = new Object();
    private final int containerWidth = Display.getWidth();

    protected Field[] fields;
    protected boolean[] fieldFocusAttrib;
    private boolean firstFieldAdded = false;

    protected CarouselScreenManager() {
        super(NO_HORIZONTAL_SCROLL | NO_VERTICAL_SCROLL | NO_VERTICAL_SCROLLBAR);
    }

    public CarouselScreenManager(Field[] fields) {
        this();
        this.fields = fields;

        for (int i = 0; i < fields.length; i++) {
            add(fields[i]);
        }
    }

    public void add(Field field) {
        FocusSettableManager fsm = new FocusSettableManager();

        if (!firstFieldAdded) {
            firstFieldAdded = true;
            fsm.setFocusable(true);
        }

        fsm.add(field);
        super.add(fsm);
    }

    protected void setFields(Field[] fields) {
        this.fields = fields;
    }


    protected void sublayout(int width, int height) {

        final int displayHeight = Display.getHeight();
        for (int i = 0; i < getFieldCount(); i++) {
            Field fieldi = getField(i);
            if (fieldi != null) {
                setPositionChild(fieldi, i * containerWidth, 0);
                layoutChild(fieldi, containerWidth, displayHeight);
            }
        }

        setExtent(containerWidth, displayHeight);
        setVirtualExtent(2 * containerWidth, displayHeight);
    }

    public void changePageNoAnimation(int direction) {
        if (direction == -1) {
            Field moveField = getField(fields.length - 1);
            delete(moveField);
            insert(moveField, 0);

        } else if (direction == 1) {
            Field moveField = getField(0);
            delete(moveField);
            add(moveField);
        }
    }

    public void changePage(int direction) {
        if (pageThread != null && pageThread.isAlive() ||
                getFieldCount() < 1) {
            return;
        }
        currentDirection = direction;

        animationStopped = false;

        pageThread = new Thread(this);
        pageThread.start();
    }

    public void run() {
        synchronized (runLock) {
            if (currentDirection == -1) {
                currentContainerOrigin = containerWidth;
                final Field moveField = this.getField(fields.length - 1);
                synchronized (Application.getEventLock()) {
                    delete(moveField);
                    insert(moveField, 0);
                }
            } else {
                invalidate();
            }
            double angularOffset = 0.5 * Math.PI / 180;
            int xOffset;
            while (!animationStopped) {
                xOffset = (int) (1 / Math.sin(angularOffset));
                x = x + xOffset;
                if (x >= containerWidth) {
                    x = containerWidth;
                }
                invalidate();
                try {
                    Thread.sleep(75);
                } catch (InterruptedException e) {//
                }
                angularOffset = angularOffset + 0.05 * Math.PI / 180;
                if (x == containerWidth) {
                    animationStopped = true;
                }
            }
            x = 0;
            currentContainerOrigin = 0;
            if (currentDirection == 1) {
                final Field moveField = this.getField(0);
                synchronized (Application.getEventLock()) {
                    delete(moveField);
                    add(moveField);
                }
            }
            synchronized (Application.getEventLock()) {
                ((FocusSettableManager) getField(0)).setFocusable(true);
                ((FocusSettableManager) getField(1)).setFocusable(false);
                getField(0).setFocus();
            }
        }
    }

    protected void paint(Graphics g) {
        if (getFieldCount() < 1) {
            return;
        }
        if (pageThread != null && pageThread.isAlive()) {
            if (currentDirection == 1) {
                g.translate(-1 * (currentContainerOrigin + x), 0);
            }
            if (currentDirection == -1) {
                g.translate(-1 * (currentContainerOrigin - x), 0);
            }
        } else {
            g.translate(-1 * currentContainerOrigin, 0);
        }
        super.paint(g);
    }

    private class FocusSettableManager extends VerticalFieldManager {
        private boolean focusable = false;

        public boolean isFocusable() {
            return focusable;
        }

        public void setFocusable(boolean focusable) {
            this.focusable = focusable;
        }
    }

    protected boolean keyDown(int keycode, int time) {
        return (Keypad.key(keycode) == Keypad.KEY_ESCAPE && !animationStopped) || super.keyDown(keycode, time);
    }
}
