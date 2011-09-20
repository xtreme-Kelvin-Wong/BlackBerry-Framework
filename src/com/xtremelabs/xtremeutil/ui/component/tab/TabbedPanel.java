package com.xtremelabs.xtremeutil.ui.component.tab;

import com.xtremelabs.xtremeutil.ui.ActionHandler;
import com.xtremelabs.xtremeutil.ui.component.button.ToggleBitmapButtonField;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Manager;

/**
 * Combines a TabManager with a PaneManager to create a tabbed interface. By default it takes up the entire width of
 * the display.
 * Created by Xtreme Labs Inc.
 * All Rights Reserved
 * Date: 2-Jul-2009
 * Time: 12:00:24 PM
 */
public class TabbedPanel extends Manager implements TabChangeListener {
    private PaneManager paneManager;
    private TabManager tabBar;
    private int height;
    private int tabLocation;

    public static final int TAB_AT_TOP = 0;
    public static final int TAB_AT_BOTTOM = 1;


    /**
     * groups the tabs and constrains the height of the manager
     *
     * @param tabs   array of Tab instances making up the interface
     * @param height the height the manager will take up
     */
    public TabbedPanel(Tab[] tabs, int height) {
        this(tabs, height, TAB_AT_TOP);
    }

    /**
     * groups the tabs and constrains the height of the manager
     *
     * @param tabs        array of Tab instances making up the interface
     * @param height      the height the manager will take up
     * @param tabLocation either TAB_AT_TOP or TAB_AT_BOTTOM
     */
    public TabbedPanel(Tab[] tabs, int height, int tabLocation) {
        super(USE_ALL_HEIGHT | NO_VERTICAL_SCROLL | NO_VERTICAL_SCROLLBAR);
        this.height = height;
        this.tabLocation = tabLocation;
        populateTabsAndPanes(tabs);
        tabBar.setTabChangeListener(this);
        if (tabLocation == TAB_AT_TOP) {
            add(tabBar);
            add(paneManager);
        } else {
            add(paneManager);
            add(tabBar);
        }
    }

    private void populateTabsAndPanes(final Tab[] tabs) {
        final int numTabs = tabs.length;
        ToggleBitmapButtonField[] tabButtons = new TabButton[numTabs];
        Manager[] panes = new Manager[numTabs];
        int position;
        for (int i = 0; i < numTabs; i++) {
            if (i == 0) {
                position = TabButton.LEFT;
            } else if (i == numTabs - 1) {
                position = TabButton.RIGHT;
            } else {
                position = TabButton.MIDDLE;
            }
            TabButton tabButton = new TabButton(tabs[i].onPic, tabs[i].offPic, tabs[i].actionHandler, position);
            tabButtons[i] = tabButton;
            panes[i] = tabs[i].getContent();
        }

        paneManager = new PaneManager(panes);
        tabBar = new TabManager(tabButtons);
    }

    protected void sublayout(int width, int height) {
        if (tabLocation == TAB_AT_TOP) {
            if (paneManager != null) {
                setPositionChild(paneManager, 0, tabBar.getPreferredHeight());
                layoutChild(paneManager, getPreferredWidth(), height - tabBar.getPreferredHeight()); // TODO: check width, height
            }
            if (tabBar != null) {
                setPositionChild(tabBar, 0, 0);
                layoutChild(tabBar, tabBar.getPreferredWidth(), tabBar.getPreferredHeight()); // TODO: check width, height
            }
        } else {
            if (paneManager != null) {
                setPositionChild(paneManager, 0, 0);
                layoutChild(paneManager, getPreferredWidth(), height - tabBar.getPreferredHeight()); // TODO: check width, height
            }
            if (tabBar != null) {
                setPositionChild(tabBar, 0, height - tabBar.getPreferredHeight());
                layoutChild(tabBar, tabBar.getPreferredWidth(), tabBar.getPreferredHeight()); // TODO: check width, height
            }
        }

        setExtent(getPreferredWidth(), height); // TODO: check width, height
    }

    /**
     * By default takes the entire width of the display
     *
     * @return width of the manager
     */
    public int getPreferredWidth() {
        return Display.getWidth();
    }

    /**
     * Height of the manager as specified by the constructor
     *
     * @return height of the manager
     */
    public int getPreferredHeight() {
        return height;
    }

    public void tabChanged(ToggleBitmapButtonField selectedTab, int tabId) {
        if (paneManager != null) {
            paneManager.displayPane(tabId);
        }
    }

    //TODO: pass bitmaps to make performance better by not recreating bitmaps from pngs
    public static class Tab {
        private String onPic;
        private String offPic;
        private Manager content;
        private ActionHandler actionHandler = new ActionHandler() {
            public boolean executePressCommand() {
                return true;
            }
        };

        public Tab(String onPic, String offPic, Manager content) {
            this.onPic = onPic;
            this.offPic = offPic;
            this.content = content;
        }

        public Tab(String onPic, String offPic, Manager content, ActionHandler actionHandler) {
            this.onPic = onPic;
            this.offPic = offPic;
            this.content = content;
            this.actionHandler = actionHandler;
        }

        public ActionHandler getActionHandler() {
            return actionHandler;
        }

        public String getOnPic() {
            return onPic;
        }

        public String getOffPic() {
            return offPic;
        }

        public Manager getContent() {
            return content;
        }
    }

    private static class TabButton extends ToggleBitmapButtonField {
        private ActionHandler actionHandler;
        private static final int LEFT = -1;
        private static final int MIDDLE = 0;
        private static final int RIGHT = 1;
        private int position = MIDDLE;

        // TODO: Figure out why highlighted and unhighlighted bitmaps need to be set in both super and sub classes
        public TabButton(String onPNGName, String offPNGName, ActionHandler actionHandler, int position) {
            super(Bitmap.getBitmapResource(onPNGName), Bitmap.getBitmapResource(offPNGName));
            this.position = position;
            highlighted = Bitmap.getBitmapResource(onPNGName);
            unHighlighted = Bitmap.getBitmapResource(offPNGName);
            this.actionHandler = actionHandler;
        }

        public void importBitmaps() {
        }

        public boolean handleAction() {
            return actionHandler.executePressCommand();
        }

        protected boolean navigationMovement(int dx, int dy, int status, int time) {
            return (position == LEFT && dx < 0) || (position == RIGHT && dx > 0) || super.navigationMovement(dx, dy, status, time);
        }
    }


}
