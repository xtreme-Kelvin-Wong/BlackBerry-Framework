package com.xtremelabs.xtremeutil.ui.component.tab;

import com.xtremelabs.xtremeutil.ui.component.button.ToggleBitmapButtonField;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.container.HorizontalFieldManager;

import java.util.Vector;

/**
 * Created by Xtreme Labs Inc.
 * All Rights Reserved
 * Date: 29-Jun-2009
 * Time: 11:57:30 AM
 */
public class TabManager extends HorizontalFieldManager implements FocusChangeListener {
    private ToggleBitmapButtonField currentSelected;
    private int numTabs;
    private ToggleBitmapButtonField[] tabs;
    private Vector listeners = new Vector();

    public TabManager(ToggleBitmapButtonField[] tabs) {
        super();
        if (tabs.length < 2)
            throw new IllegalArgumentException("Must initialize tab manager with more than one tab");
        this.tabs = tabs; // TODO: check that this is not a copy constructor
        numTabs = tabs.length;
        initTabs(tabs);
    }

    public void onFocus(int direction) {
        if (!currentSelected.isFocus()) {
            currentSelected.setFocus();
        }
    }

    private void initTabs(ToggleBitmapButtonField[] tabs) {
        for (int i = 0; i < numTabs; i++) {
            tabs[i].setFocusListener(this);
            add(tabs[i]);
        }
        if (numTabs != 0) {
            // use the first element as default
            currentSelected = tabs[0];
        }
    }

    public void onDisplay() {
        super.onDisplay();
        selectTab(tabs[0]);
    }

    public void focusChanged(Field newlySelected, int eventType) {
        if (eventType == FOCUS_GAINED && currentSelected != newlySelected) {
            alertListeners((ToggleBitmapButtonField) newlySelected);
            selectTab((ToggleBitmapButtonField) newlySelected);
        }
    }

    public void setTabChangeListener(TabChangeListener listener) {
        listeners.addElement(listener);
    }

    private void alertListeners(ToggleBitmapButtonField selectedTab) {
        int tabId = -1;
        for (int i = 0; i < numTabs; i++) {
            if (tabs[i] == selectedTab) {
                tabId = i;
            }
        }

        int numListeners = listeners.size();
        for (int i = 0; i < numListeners; i++) {
            ((TabChangeListener) (listeners.elementAt(i))).tabChanged(selectedTab, tabId);
        }
    }

    private void selectTab(ToggleBitmapButtonField newlySelected) {
        if (currentSelected != newlySelected) {
            currentSelected.setSelected(ToggleBitmapButtonField.DESELECT);
            currentSelected = newlySelected;
        }
        currentSelected.setSelected(ToggleBitmapButtonField.SELECT);
    }


}