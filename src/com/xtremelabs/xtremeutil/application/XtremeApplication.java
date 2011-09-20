package com.xtremelabs.xtremeutil.application;

import java.util.Timer;

public interface XtremeApplication {
    String SIMTESTS_ARGUMENT = "simtests";
    String DEVMODE_ARGUMENT = "devmode";
    String RUNTESTS_ARGUMENT = "runtests";

    String NO_IMAGE_PNG_NAME = "no-photo.png";
    String LOADING_PNG_NAME = "loading.png";
    String HEADER_PNG_NAME = "header.png";

    void initialize(String[] args);

    void exit(int i);

    void exitIfInferiorOsVersion();

    void exitIfScreenResolutionNotSupported();

    Timer getTimer();

    String getJadUrl();

    TestRunner getTestRunner();

    /**
     * @deprecated
     * @param errorMessage don't use
     * @param requestObj don't use
     * @param exitRequested don't use
     * @param silenceRequested don't use
     */
    void onConnectionError(String errorMessage, Object requestObj, boolean exitRequested, boolean silenceRequested);
}
