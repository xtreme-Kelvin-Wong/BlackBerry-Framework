package com.xtremelabs.xtremeutil.net.api.exception;

import java.io.IOException;

public class NoTransportsException extends IOException {
        public NoTransportsException() {
                super("Could not connect to the server");
        }
}
