package com.xtremelabs.xtremeutil.ui.component.tab;

import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.VerticalFieldManager;

/**
 * A PaneManager shows one pane at a time. Each pane is a manager in an array of managers that is passed into
 * the constructor. This PaneManager instance could be useful in conjunction with a TabManager.
 * Created by Xtreme Labs Inc.
 * All Rights Reserved
 * Date: 2-Jul-2009
 * Time: 9:55:55 AM
 */
public class PaneManager extends VerticalFieldManager {
    Manager[] panes;
    Manager displayedPane;

    public PaneManager(Manager[] panes) {
        super(USE_ALL_HEIGHT | NO_VERTICAL_SCROLL | NO_VERTICAL_SCROLLBAR);
        if (panes.length < 2)
            throw new IllegalArgumentException("Must initialize pane manager with more than one pane");
        this.panes = panes;
        displayPane(0);
    }

    /**
     * displays the pane with the specified paneId
     *
     * @param paneIndex the index of the pane as it was to the constructor
     * @return paneId on success, -1 on out of bound
     */
    public int displayPane(int paneIndex) {
        if (paneIndex >= 0 && paneIndex < panes.length) {
            if (displayedPane != panes[paneIndex]) {
                if (displayedPane != null) {
                    delete(displayedPane);
                }
                displayedPane = panes[paneIndex];
                add(displayedPane);
            }
            return paneIndex;
        } else {
            return -1;
        }
    }
}
