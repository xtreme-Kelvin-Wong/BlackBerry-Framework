package com.xtremelabs.xtremeutil.device.api.permission;

public interface XPermissionsListener {
    /**
     * If IT administrator does not allow an application to switch the permission values for a set of keys,
     * this method is called with an array of keys
     *
     * @param forbiddenKeys set of permission keys that the IT administrator has blocked
     */
    void handleForbiddenPermissions(int[] forbiddenKeys);

    /**
     * Sends a list of permission keys that are required for the application and
     * do not have their value set to ALLOW yet
     *
     * @param permissionKeys an array of permission keys to be included in the application permissions request
     */
    void permissionsRequired(int[] permissionKeys);

    /**
     * Once the requst screen is shown and dismissed the permission keys are rechecked to make sure all are set to ALLOW
     *
     * @param successful true if permission change was successful
     */
    public void permissionSet(boolean successful);
}

