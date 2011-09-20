package com.xtremelabs.xtremeutil.ui;

import com.xtremelabs.xtremeutil.util.logger.XLogger;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import java.util.Enumeration;
import java.util.Vector;

public class DebugLogScreen extends MainScreen {
    VerticalFieldManager manager;
    MainScreen keyCodesScreen;

    public DebugLogScreen() {
        manager = new VerticalFieldManager(Manager.USE_ALL_WIDTH | Manager.USE_ALL_HEIGHT |
                Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR);
        populateManager();
        this.add(manager);
        addMenuItems();
    }

    private void populateManager() {
        Vector tempLog = XLogger.getInstance().dumpLog();
        manager.add(new SeparatorField());
        int fontHeight = Display.getHeight() / 20;
        for (int i = 0; i < tempLog.size(); i++) {
            Font font = Theme.DEFAULT_FONT.getFontFamily().getFont(Font.PLAIN, fontHeight);
            RichTextField richTextField = new RichTextField("", LabelField.FOCUSABLE);
            richTextField.setText(tempLog.elementAt(i).toString());
            richTextField.setFont(font);
            manager.add(richTextField);
            manager.add(FieldFactory.makeVerticalSpacerField(8));
            manager.add(new SeparatorField(Field.NON_FOCUSABLE));
        }
    }


    public int addMenuItems() {

        int ordinal = 10;

        addMenuItem(
                new MenuItem("Back", ordinal++, 100) {
                    public void run() {
                        onClose();
                    }
                }
        );

        addMenuItem(new MenuItem("Reset Log", ordinal++, 100) {
            public void run() {
                XLogger.resetLog();
                manager.deleteAll();
                populateManager();
            }
        });


        addMenuItem(new MenuItem("Show All Fonts", ordinal++, 100) {
            public void run() {
                MainScreen screen = new MainScreen();
                Enumeration enumeration = GuiUtils.labelFieldsOfEveryFont().elements();
                while (enumeration.hasMoreElements()) {
                    Field field = (Field) enumeration.nextElement();
                    screen.add(field);
                }
                UiApplication.getUiApplication().pushScreen(screen);
            }
        });

        addMenuItem(new MenuItem("Show All Colors", ordinal++, 100) {
            public void run() {
                MainScreen screen = new MainScreen();
                Vector vector = GuiUtils.labelFieldsOfEveryColor();
                int size = vector.size();
                Field[] fields = new Field[size];
                vector.copyInto(fields);
                for (int i = 0; i < fields.length; i++) {
                    screen.add(fields[i]);
                }
                UiApplication.getUiApplication().pushScreen(screen);
            }
        });

        addMenuItem(new MenuItem("Show Key Codes", ordinal++, 100) {
            public void run() {
                if (keyCodesScreen == null) {
                    keyCodesScreen = new MainScreen();
                    final VerticalFieldManager manager = new VerticalFieldManager() {
                        protected boolean keyDown(int keycode, int time) {
                            int key = Keypad.key(keycode);
                            int status = Keypad.status(keycode);
                            char keychar = KeypadUtil.getKeyChar(keycode, KeypadUtil.MODE_UI_CURRENT_LOCALE);
                            LabelField label = new LabelField("'" + keychar + "'" + "  " + keycode + "  " + key + "  " + status, FOCUSABLE);
                            this.add(label);
                            return super.keyDown(keycode, time);
                        }
                    };
                    LabelField title = new LabelField("Enter a key to view it's key Code", FOCUSABLE);
                    manager.add(title);
                    manager.add(new LabelField("keychar:  keycode:   key:   status:"));
                    keyCodesScreen.add(manager);
                }
                if (!keyCodesScreen.isDisplayed()) {
                    UiApplication.getUiApplication().pushScreen(keyCodesScreen);
                }
            }
        });

        return ordinal;
    }

    protected boolean keyChar(char key, int status, int time) {
        if (key == Characters.LATIN_SMALL_LETTER_B || key == Characters.LATIN_CAPITAL_LETTER_B
                || key == Characters.EXCLAMATION_MARK) {
            return scrollToBottom();
        }
        if (key == Characters.LATIN_SMALL_LETTER_T || key == Characters.LATIN_CAPITAL_LETTER_T
                || key == Characters.LEFT_PARENTHESIS) {
            scrollToTop();
            return true;
        }
        return super.keyChar(key, status, time);
    }

    private void scrollToTop() {
        manager.getField(1).setFocus();
    }

    private boolean scrollToBottom() {
        manager.getField(manager.getFieldCount() - 3).setFocus();
        return true;
    }
}
