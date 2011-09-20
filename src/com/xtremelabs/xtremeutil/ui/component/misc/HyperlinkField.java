package com.xtremelabs.xtremeutil.ui.component.misc;

import com.xtremelabs.xtremeutil.util.concurrency.UITask;
import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.browser.BrowserSession;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;


public class HyperlinkField extends LabelField {
	private int _color = Color.MAGENTA; // Should never use magenta.
	
    private String _urlString;

    public HyperlinkField(String label, String urlString) {
        super(label, FOCUSABLE);
        _urlString = urlString;
    }

    public void setColor(int color) {
    	_color = color;
    }
    
    protected boolean trackwheelUnclick(int status, int time) {
        return handleAction();
    }

    protected boolean trackwheelClick(int status, int time) {
        return true;
    }

    protected boolean keyChar(char c, int status, int time) {
        if (c == Characters.ENTER) {
            return handleAction();
        }
        return super.keyChar(c, status, time);
    }
    
    protected boolean handleAction() {
        new UITask() {
            public void execute() {
                BrowserSession session = Browser.getDefaultSession();
                session.displayPage(_urlString);
            }
        }.invokeLater();
        return true;
    }
    
    protected void paint(Graphics g) {
    	if(_color != Color.MAGENTA) {
    		g.setColor(_color);
    	}
        super.paint(g);
    }
}

