package com.changeme.app.barebone_changename.application;

import com.changeme.app.barebone_changename.ui.screen.BareBoneScreen;
import com.xtremelabs.xtremeutil.application.AbstractXtremeApplicationImpl;
import com.xtremelabs.xtremeutil.application.TestRunner;
import com.xtremelabs.xtremeutil.util.logger.XLogger;

//TODO:Chanage name
public class BareBoneApplication extends AbstractXtremeApplicationImpl {

    public BareBoneApplication(final String[] args) {
        initialize(args);
        invokeLater(new Runnable() {
            public void run() {
                pushScreen(new BareBoneScreen());
            }
        });
    }

    // XtremeApplication implementation
    public void exitIfInferiorOsVersion() {
    }

    public void exitIfScreenResolutionNotSupported() {
    }

    public String getJadUrl() {
        return "";
    }

    public TestRunner getTestRunner() {
        return TestRunnerImpl.getInstance();
    }

    /**
     * @deprecated
     */
    public void onConnectionError(String errorMessage, Object requestObj, boolean exitRequested, boolean silenceRequested) {
      /*  XLogger.error(getClass(), errorMessage);
        if (!silenceRequested || errorMessage!=null) {
            // pop a global dialog
        	synchronized (getEventLock()) {
				Dialog.alert(errorMessage);
			}
        }
        if (exitRequested) {
            exit(1);
        }*/
    }

    // TaskEventListener implementation
    public void onUIException(Throwable t) {
        XLogger.error(getClass(), t.getMessage());
    }

}