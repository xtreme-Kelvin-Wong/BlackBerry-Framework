package com.xtremelabs.xtremeutil.ui.component.misc;

import com.xtremelabs.xtremeutil.ui.FieldFactory;
import com.xtremelabs.xtremeutil.ui.Theme;
import com.xtremelabs.xtremeutil.ui.UpdateListener;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;


public class SettingsDropdownChoiceField extends HorizontalFieldManager implements FieldChangeListener {
    private ObjectChoiceField choiceField;
    protected LabelField labelField;
    private UpdateListener updateListener;
    private static final int TOP_BOTTOM_PADDING = 5;
    protected Manager manager;

    public SettingsDropdownChoiceField(String label, Object[] choices, int defaultChoice, long style) {
        super(style);
        manager = new VerticalFieldManager(USE_ALL_WIDTH);

        labelField = new LabelField(label) {
            protected void paint(Graphics g) {
                g.setColor(Color.WHITE);
                invalidate();
                super.paint(g);
            }

            protected void sublayout() {
                setExtent(243, 22);
            }
        };
        labelField.setFont(Theme.SETTINGS_LABEL_FONT);
        choiceField = new XtremeObjectChoiceField(choices, defaultChoice);
        choiceField.setChangeListener(this);

        manager.add(FieldFactory.makeVerticalSpacerField(TOP_BOTTOM_PADDING));
        manager.add(labelField);
        manager.add(FieldFactory.makeVerticalSpacerField(Display.getHeight() / 360));
        manager.add(choiceField);
        manager.add(FieldFactory.makeVerticalSpacerField(TOP_BOTTOM_PADDING));

        add(manager);
    }

    public void setFont(Font font) {
        choiceField.setFont(font);
        labelField.setFont(font);
        invalidate();
    }

    public void setUpdateListener(UpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public void setSelectedIndex(Object object) {
        choiceField.setSelectedIndex(object);
    }

    public int getSelectedIndex() {
        return choiceField.getSelectedIndex();
    }

    public Object getSelectedChoice() {
        return getChoice(getSelectedIndex());
    }

    public Object getChoice(int index) {
        return choiceField.getChoice(index);
    }

    public void fieldChanged(Field field, int i) {
        if (updateListener != null) {
            updateListener.update();
        }
    }

}