package com.xtremelabs.xtremeutil.device.api.permission;

import com.xtremelabs.xtremeutil.util.logger.XLogger;
import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.system.ApplicationManager;
import net.rim.device.api.util.IntVector;

public class XPermissionsManager {

    private PermissionKeyProvider keyProvider;
    private XPermissionsListener listener;

    public XPermissionsManager(PermissionKeyProvider permissionKeyProvider,
                               XPermissionsListener permissionsListener) {
        listener = permissionsListener;
        keyProvider = permissionKeyProvider;
    }

    public void updatePermissions() {
        ApplicationManager applicationManager = ApplicationManager.getApplicationManager();
        while (applicationManager.inStartup()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                XLogger.warn(getClass(), "sleep interrupted in");
            }
        }
        setPermissions();
    }

    private void setPermissions() {
        ApplicationPermissionsManager permissionsManager = ApplicationPermissionsManager.getInstance();
        ApplicationPermissions newpermission = new ApplicationPermissions();

        IntVector forbiddenPermissionKeys = new IntVector();
        int[] permissionKeys = keyProvider.getPermissionKeys();
        permissionKeys = permissionKeys == null ? new int[]{} : permissionKeys;
        boolean promptNeeded = false;
        StringBuffer keyValue = new StringBuffer("Permission key/value pairs:\n");
        StringBuffer newPermissionKeys = new StringBuffer("new permissions added:\n");

        for (int i = 0; i < permissionKeys.length; i++) {
            int permissionKey = permissionKeys[i];
            keyValue.append(permissionKey).append(": ").append(permissionsManager.getPermission(permissionKey)).append(", ");

            if (permissionsManager.getPermission(permissionKey) != ApplicationPermissions.VALUE_ALLOW) {
                if (permissionsManager.getMaxAllowable(permissionKey) == ApplicationPermissions.VALUE_ALLOW) {
                    try {
                        if (!newpermission.containsPermissionKey(permissionKey)) {
                            newpermission.addPermission(permissionKey);
                            newPermissionKeys.append("key added: ").append(permissionKey).append(", ");
                            // TODO: massive hack to stop empty permission requests from appearing on non-BES and non WIFI devices
                            if (permissionKey != ApplicationPermissions.PERMISSION_WIFI && permissionKey != 10) {
                                promptNeeded = true;
                            }

                        } else {
                            XLogger.info(getClass(), "key not added (already present in newpermission): " + permissionKey);
                        }
                    } catch (IllegalArgumentException e) {
                        XLogger.error(getClass(), new StringBuffer()
                                .append("Invalid permission! ")
                                .append(permissionKey)
                                .append(" was not added:\n")
                                .toString() + e);
                    }
                } else if (permissionsManager.getMaxAllowable(permissionKey) == ApplicationPermissions.VALUE_DENY) {
                    forbiddenPermissionKeys.addElement(permissionKey);
                }
            }
        }

        XLogger.info(getClass(), keyValue.toString());
        XLogger.info(getClass(), newPermissionKeys.toString());
        if (listener != null) {
            if (forbiddenPermissionKeys.size() > 0) {
                forbiddenPermissionKeys.trimToSize();
                listener.handleForbiddenPermissions(forbiddenPermissionKeys.getArray());
            }
            listener.permissionsRequired(newpermission.getPermissionKeys());
        }

        boolean permissionSet = false;


        if (promptNeeded) {
            try {
                permissionSet = permissionsManager.invokePermissionsRequest(newpermission);
            } catch (Exception e) {
                XLogger.info(getClass(), e);
            }
            XLogger.info(getClass(), "Permissions were set successfully? " + permissionSet);
        } else {
            XLogger.info(getClass(), "permissions not prompted");
            permissionSet = true;
        }

        if (listener != null) {
            listener.permissionSet(permissionSet);
        }
    }
}
