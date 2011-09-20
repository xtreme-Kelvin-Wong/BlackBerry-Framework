package com.xtremelabs.xtremeutil.application.descriptor;

import com.xtremelabs.xtremeutil.util.lang.AggregateEnumerationTest;

public class JadParserTest {
    public static class TestIsFromAppWorld extends AggregateEnumerationTest.TestCase {

        public void run() throws Exception {
            AppWorldData data = new AppWorldData("mockmodule");
            JadParser parser = new JadParser(data);
            assertFalse("If appworldId returned is null app is NOT from app world. ", parser.isFromAppWorld());

            data.setAppWorldId("");
            assertFalse("If appworldId returned is empty app is NOT from app world. ", parser.isFromAppWorld());

            data.setAppWorldId(null);
            assertFalse("If appworldId returned is null app is NOT from app world. ", parser.isFromAppWorld());

            data.setAppWorldId("some_id");
            assertTrue("If appworldId is present in jad, app was downloaded from app world. ", parser.isFromAppWorld());
        }
    }
}
