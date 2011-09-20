package com.changeme.app.barebone_changename.ui.screen;

import com.changeme.app.barebone_changename.application.BareBoneApplication;
import com.changeme.app.barebone_changename.ui.ConnectionTestView;
import com.xtremelabs.xtremeutil.ui.DebugLogScreen;
import net.rim.device.api.system.Application;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;

public class BareBoneScreen extends MainScreen {
    final BareBoneApplication application = (BareBoneApplication) Application.getApplication();

    public BareBoneScreen() {
        setTitle("Hold alt and press 't' + 'e' + 's' + 't'");
//        add(new DeprecatedConnectionTestView()); // An edit field and button to ping a server
        add(new ConnectionTestView());
    }

    protected boolean openProductionBackdoor(int code) {

        if (application.isDevmode()) {
            switch (code) {
                case ('T' << 24) | ('E' << 16) | ('S' << 8) | 'T':
                    UiApplication.getUiApplication().invokeLater(new Runnable() {
                        public void run() {
                            application.getTestRunner().runUnitTests();
                        }
                    });
                    return true;
            }
        }
        return super.openProductionBackdoor(code);
    }

    protected void makeMenu(Menu menu, int instance) {
        super.makeMenu(menu, instance);
        final int[] ordinal = new int[]{1};
        if (application.isDevmode()) {
            menu.add(new MenuItem("Debug Log", ordinal[0]++, 1000) {
                public void run() {
                    application.pushScreen(new DebugLogScreen());
                }
            });
        }

    }
}
