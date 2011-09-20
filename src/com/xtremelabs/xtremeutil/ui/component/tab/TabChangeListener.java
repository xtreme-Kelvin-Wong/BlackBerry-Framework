package com.xtremelabs.xtremeutil.ui.component.tab;

import com.xtremelabs.xtremeutil.ui.component.button.ToggleBitmapButtonField;

/**
 * managers listening for tab changes need to implement this listener
 * User: Tony
 * Date: 30-Jun-2009
 * Time: 5:15:13 PM
 */
public interface TabChangeListener {
    /**
     * called by the TabManager whenever tab focus changes
     *
     * @param selectedTab tab that is getting focus
     * @param tabId       corresponding the array passed to constructor
     */
    void tabChanged(ToggleBitmapButtonField selectedTab, int tabId);
}
