//#preprocess

package com.changeme.app.barebone_changename.application;

import com.xtremelabs.xtremeutil.application.TestRunner;
import com.xtremelabs.xtremeutil.util.logger.XLogger;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;

public class TestRunnerImpl implements TestRunner {
    private static TestRunnerImpl ourInstance = new TestRunnerImpl();
    private Screen testRunnerScreen;
    private volatile boolean testsRunning;

    public static TestRunnerImpl getInstance() {
        return ourInstance;
    }

    private TestRunnerImpl() {
    }

    public void runUnitTests() {
        //#ifdef RUNTESTS
        if (!testsRunning) {
            XLogger.info(getClass(), "new test runner screen...");
            testRunnerScreen = new rimunit.TestRunnerScreen(new ApplicationTestSuite(), new TestListener());
        }
        if (!testRunnerScreen.isDisplayed()) {
            XLogger.info(getClass(), "pushing test runner screen...");
            UiApplication.getUiApplication().pushScreen(testRunnerScreen);
        }
        //#endif
    }

    public boolean areTestsRunning() {
        return testsRunning;
    }


    //#ifdef RUNTESTS
    private class TestListener implements rimunit.TestStartStopListener {
        public synchronized void testStarted() {
            testsRunning = true;

        }

        public synchronized void testsFinished() {
            testsRunning = false;

        }
    }
    //#endif
}
