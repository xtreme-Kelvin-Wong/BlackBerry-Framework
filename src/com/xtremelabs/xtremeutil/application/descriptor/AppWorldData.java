package com.xtremelabs.xtremeutil.application.descriptor;

import net.rim.device.api.system.CodeModuleGroup;

public class AppWorldData {
    /**
     * The App World specific ID of this application.
     */
    public static String RIM_APP_WORLD_ID = "RIM_APP_WORLD_ID";
    /**
     * A flag (true or false) that specifies whether there is
     * currently an update for this app available.
     */
    private static String RIM_APP_WORLD_UPDATE_AVAIL = "RIM_APP_WORLD_UPDATE_AVAIL";
    /**
     * The license key for this application, as specified by the
     * App World purchase.
     */
    private static String RIM_APP_WORLD_LICENSE_KEY = "RIM_APP_WORLD_LICENSE_KEY";
    /**
     * The name of the application, as specified in the App World
     * ISV Portal.
     */
    public static String RIM_APP_WORLD_NAME = "RIM_APP_WORLD_NAME";
    /**
     * The email address of the purchaser. This will be their
     * PayPal email address.
     */
    public static String RIM_APP_WORLD_EMAIL = "RIM_APP_WORLD_EMAIL";
    /**
     * The PIN of the purchaser.
     */
    public static String RIM_APP_WORLD_PIN = "RIM_APP_WORLD_PIN";
    /**
     * The version of the application, as specified in the App
     * World ISV Portal.
     */
    public static String RIM_APP_WORLD_VERSION = "RIM_APP_WORLD_VERSION";

    private String appWorldId;
    private String key;
    private boolean updateAvailable;
    private String name;
    private String email;
    private String pin;
    private String version;

    public AppWorldData(final String moduleName) {
        CodeModuleGroup moduleGroup = JadParser.getGroup(moduleName);
        if (moduleGroup != null) {
            final String updateString = moduleGroup.getProperty(RIM_APP_WORLD_UPDATE_AVAIL);
            updateAvailable = updateString != null && "true".equalsIgnoreCase(updateString.trim());
            key = moduleGroup.getProperty(RIM_APP_WORLD_LICENSE_KEY);
            appWorldId = moduleGroup.getProperty(RIM_APP_WORLD_ID);
            appWorldId = moduleGroup.getProperty(RIM_APP_WORLD_ID);
            name = moduleGroup.getProperty(RIM_APP_WORLD_NAME);
            email = moduleGroup.getProperty(RIM_APP_WORLD_EMAIL);
            pin = moduleGroup.getProperty(RIM_APP_WORLD_PIN);
            version = moduleGroup.getProperty(RIM_APP_WORLD_VERSION);
        }
    }

    public String getAppWorldId() {
        return appWorldId == null ? "" : appWorldId.trim();
    }

    public String getKey() {
        return key == null ? "" : key.trim();
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public String getName() {
        return name == null ? "" : name.trim();
    }

    public String getEmail() {
        return email == null ? "" : email.trim();
    }

    public String getPin() {
        return pin == null ? "" : pin.trim();
    }

    public String getVersion() {
        return version == null ? "" : version.trim();
    }

    public void setAppWorldId(String appWorldId) {
        this.appWorldId = appWorldId;
    }
}
