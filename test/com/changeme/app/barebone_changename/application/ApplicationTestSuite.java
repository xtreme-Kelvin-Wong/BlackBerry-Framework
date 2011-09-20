package com.changeme.app.barebone_changename.application;

import com.xtremelabs.xtremeutil.application.XtremeUtilTestSuite;
import rimunit.TestSuite;

public class ApplicationTestSuite extends TestSuite {
    protected Class[] suite() {
        return new Class[]{
                XtremeUtilTestSuite.class,
        };
    }
}
