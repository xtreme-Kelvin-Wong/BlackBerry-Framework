package com.xtremelabs.xtremeutil.application.descriptor;

import rimunit.TestCase;

public class AppWorldDataTest {

    public static class TestInitialization extends TestCase {
        public void run() throws Exception {
            AppWorldData data = new AppWorldData("dfezxdfe");
            assertEquals("id returned should be empty when not an app world build", "", data.getAppWorldId());
            assertEquals("email returned should be empty when not an app world build", "", data.getEmail());
            assertFalse("update returned should be empty when not an app world build", data.isUpdateAvailable());
        }
    }
}
