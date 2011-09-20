package com.xtremelabs.xtremeutil.ui.component.misc;

import com.xtremelabs.xtremeutil.ui.FieldFactory;
import com.xtremelabs.xtremeutil.util.concurrency.UITask;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.GIFEncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.container.HorizontalFieldManager;

//A field that displays an animated GIF.  The modification is by xtreme labs inc. only a single animated field can be displayed at a time
//ref: http://www.blackberry.com/knowledgecenterpublic/livelink.exe/fetch/2000/348583/800332/800505/800345/How_To_-_Display_an_animated_GIF.html?nodeid=1405903&vernum=0

// IMPORTANT NOTE: Images added to a project are automatically converted into the Portable Network Graphics (PNG)
// format when the application is built into a .cod file. This can cause an issue when adding an animated GIF because
// this process will strip out the animation. There are two workaround options for this issue...
// The workaround for an individual image is to change the extension of your GIF image from .gif to something else
// (such as .bin). This will prevent the RIM Application Program Compiler (RAPC) from converting the image into a .png.

public class AnimatedGIFField extends BitmapField {
    private static final String SPINNER_GIF_FILE_NAME = "spinner.bin";

    private GIFEncodedImage _image;//The image to draw.
    private volatile int _currentFrame;//The current frame in
    //(background frame).
    private AnimatorThread _animatorThread;

    public AnimatedGIFField(GIFEncodedImage image) {
        this(image, Field.FIELD_HCENTER);
    }

    public AnimatedGIFField(EncodedImage image) {
        this((GIFEncodedImage) image, 0);
    }

    public int getPreferredWidth() {
        return getBitmapWidth();
    }

    public int getPreferredHeight() {
        return getBitmapHeight();
    }

    public AnimatedGIFField(GIFEncodedImage image, long style) {
        //Call super to setup the field with the specified style.
        //The image is passed in as well for the field to
        //configure its required size.
        super(image.getBitmap(), style);

        //Store the image and it's dimensions.
        _image = image;
    }

    static public AnimatedGIFField makeSpinnerField() {
        return makeSpinnerField(SPINNER_GIF_FILE_NAME);
    }

    static public AnimatedGIFField makeSpinnerField(String animatedGifName) {
        animatedGifName = animatedGifName != null ? animatedGifName : SPINNER_GIF_FILE_NAME;
        GIFEncodedImage spinner = (GIFEncodedImage) EncodedImage.getEncodedImageResource(animatedGifName);
        return new AnimatedGIFField(spinner);
    }

    static public Field makeCenteredSpinnerField(String animatedGifName, int width) {
        HorizontalFieldManager manager = new HorizontalFieldManager();
        AnimatedGIFField spinner = makeSpinnerField(animatedGifName);
        manager.add(FieldFactory.makeHorizontalSpacerField(((Math.max(width - spinner.getBitmapWidth(), 0) / 2))));
        manager.add(spinner);
        return manager;
    }


    static public Field makeCenteredSpinnerField(int width) {
        return makeCenteredSpinnerField(SPINNER_GIF_FILE_NAME, width);
    }


    protected void paint(Graphics graphics) {
        //Call super.paint. This will draw the first background
        //frame and handle any required focus drawing.
        super.paint(graphics);

        //Don't redraw the background if this is the first frame.
        //clear graphics region first
        graphics.clear();
        //draw background
        graphics.drawImage(_image.getFrameLeft(0), _image.getFrameTop(0),
                _image.getFrameWidth(0), _image.getFrameHeight(0), _image, 0, 0, 0);
        //Draw the animation frame.
        graphics.drawImage(_image.getFrameLeft(_currentFrame), _image.getFrameTop(_currentFrame),
                _image.getFrameWidth(_currentFrame), _image.getFrameHeight(_currentFrame), _image, _currentFrame,
                0, 0);
    }

    //Stop the animation thread when the screen the field is on is
    //popped off of the display stack.
    protected void onUndisplay() {
        if (_animatorThread != null) {
            _animatorThread.stopAnimation();
        }
        super.onUndisplay();
    }

    protected void onDisplay() {
        animate();
    }

    private void animate() {
        if ((_animatorThread == null || !_animatorThread.isAlive()) && Thread.activeCount() < 15) {
            _animatorThread = new AnimatorThread(this);
            _animatorThread.setPriority(Thread.MAX_PRIORITY - 1);
            _animatorThread.start();
        } else {
            _animatorThread.interrupt();
            new UITask() {
                public void execute() {
                    animate();
                }
            }.invokeLater(100L, false);
        }
    }

    protected void onVisibilityChange(boolean visible) {
        if (!visible) {
            _animatorThread.stopAnimation();
        } else {
            animate();
        }
        super.onVisibilityChange(visible);
    }

    //A thread to handle the animation.
    private class AnimatorThread extends Thread {
        private AnimatedGIFField _theField;
        private volatile boolean _keepGoing = true;
        private int _totalFrames;//The total number of
        //  frames in the image.
        private int _loopCount;//The number of times the
        //animation has looped (completed).
        private int _totalLoops;//The number of times the animation should loop (set in the image).

        public AnimatorThread(AnimatedGIFField theField) {
            _theField = theField;
            _totalFrames = _image.getFrameCount();
            _totalLoops = _image.getIterations();
        }

        public void stopAnimation() {
            _keepGoing = false;
        }

        public void run() {
            while (_keepGoing) {
                //Invalidate the field so that it is redrawn.
                if (_currentFrame != 0) {
                    _theField.invalidate();
                    int delay = _image.getFrameDelay(_currentFrame);
                    int period = delay > 0 ? delay * 15 : 1000 / 24;//if delay is zero default to 24 frames per second
                    try {
                        sleep(period);
                    }
                    catch (InterruptedException iex) {
                        _keepGoing = false;
                        return;
                    }
                }
                //Increment the frame.
                if (++_currentFrame == _totalFrames) {
                    //Reset back to frame 0 if we have reached the end.
                    _currentFrame = 0;
                    //Check if the animation should continue.
                    if (++_loopCount == _totalLoops) {
                        _keepGoing = false;
                    }
                }
            }
        }
    }
}