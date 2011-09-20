package com.changeme.app.barebone_changename.ui;

import com.changeme.app.barebone_changename.net.BareBoneRequestQueue;
import com.changeme.app.barebone_changename.net.request.URLRequest;
import com.xtremelabs.xtremeutil.net.api.Request;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class ConnectionTestView extends VerticalFieldManager {
    TextField urlField;
    ObjectChoiceField priorityField;
    LabelField countField;
    int countRequest;
    int countResponse;

    public ConnectionTestView() {

        // URL to ping
        urlField = new TextField();
        urlField.setLabel("URL: ");
        urlField.setText("http://www.google.com/");

        // Priority of the ping
        priorityField = new ObjectChoiceField("Priority: ", new String[]{Request.MAX_PRIORITY + "", Request.NORM_PRIORITY + "", Request.MIN_PRIORITY + ""});

        ButtonField queueButton = new ButtonField("Queue Request") {
            protected boolean navigationClick(int status, int time) {
                countRequest++;
                countField.setText(countResponse + " of " + countRequest + " (response/request)");
                String choice = (String) priorityField.getChoice(priorityField.getSelectedIndex());
                int priority = Integer.parseInt(choice);
                queueRequest(urlField.getText(), priority);
                return true;
            }
        };

        ButtonField startQueue = new ButtonField("Start Requests") {
            protected boolean navigationClick(int status, int time) {
                startQueue();
                return true;
            }
        };

        ButtonField stopQueue = new ButtonField("Pause Requests") {
            protected boolean navigationClick(int status, int time) {
                stopQueue();
                return true;
            }
        };

//        ButtonField stopRequest = new ButtonField("Stop Most Recent Request") {
//            protected boolean navigationClick(int status, int time) {
//                if(request!=null) {
//                    countRequest--;
//                    countField.setText(countResponse + " of " + countRequest);
//                    stopRequest();
//                }
//                return true;
//            }
//        };


        countField = new LabelField();

        add(urlField);
        add(priorityField);
        add(countField);
        add(queueButton);
        add(startQueue);
        add(stopQueue);
//        add(stopRequest);
    }

    private void queueRequest(String url, int priority) {
        URLRequest req = new URLRequest(url, this);
        req.setPriority(priority);
        BareBoneRequestQueue.getInstance().enqueue(req);
    }

    private void startQueue() {
        BareBoneRequestQueue.getInstance().sendAllQueued();
    }

    private void stopQueue() {
        BareBoneRequestQueue.getInstance().pauseAllQueued();
    }

//    private void stopRequest() {
//        requestQueue.stopRequest(request);
//    }

    public void newResponse(final byte[] responseData) {
        synchronized (UiApplication.getEventLock()) {
            HorizontalFieldManager responseFieldManager = new HorizontalFieldManager();
            LabelField label = new LabelField("Success", FIELD_VCENTER);
            responseFieldManager.add(label);

            ButtonField responseButton = new ButtonField("Response") {
                protected boolean navigationClick(int status, int time) {
                    Dialog.inform(new String(responseData));
                    return true;
                }
            };
            responseFieldManager.add(responseButton);
            add(responseFieldManager);

            countResponse++;
            countField.setText(countResponse + " of " + countRequest + " (response/request)");
        }
    }

    public void newError(int responseCode) {
        synchronized (UiApplication.getEventLock()) {
            HorizontalFieldManager responseFieldManager = new HorizontalFieldManager();
            LabelField label = new LabelField("Error: " + responseCode);
            responseFieldManager.add(label);

            ButtonField responseButton = new ButtonField("Response") {
                protected boolean navigationClick(int status, int time) {
                    Dialog.inform("The server response was unsuccessful");
                    return true;
                }
            };
            responseFieldManager.add(responseButton);
            add(responseFieldManager);

            countResponse++;
            countField.setText(countResponse + " of " + countRequest + " (response/request)");
        }
    }
}