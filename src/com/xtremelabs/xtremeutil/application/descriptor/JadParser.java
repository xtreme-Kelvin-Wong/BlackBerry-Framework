package com.xtremelabs.xtremeutil.application.descriptor;

import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.CodeModuleGroup;
import net.rim.device.api.system.CodeModuleGroupManager;


public class JadParser {
    protected AppWorldData appWorldData;

    public JadParser() {
        appWorldData = new AppWorldData(ApplicationDescriptor.currentApplicationDescriptor().getModuleName());
    }

    public JadParser(AppWorldData data) {
        this.appWorldData = data;
    }

/*FROM:  DB-00638: How To - Programmatically read the attributes of a JAD file
* (http://www.blackberry.com/knowledgecenterpublic/livelink.exe/fetch/2000/348583/800708/800646/How_To_-_Programmatically_read_the_attributes_of_a_JAD_file.html?nodeid=1382517&vernum=0)
* 
*/

    public static CodeModuleGroup getGroup(final String moduleName) {
        CodeModuleGroup group = null;
        int numGroups = CodeModuleGroupManager.getNumberOfGroups();
        if (numGroups > 0) {
            CodeModuleGroup[] allGroups = CodeModuleGroupManager.loadAll();
            for (int i = 0; i < allGroups.length; i++) {
                if (allGroups[i].containsModule(moduleName)) {
                    group = allGroups[i];
                    break;
                }
            }
        }
        return group;
    }

    public boolean isFromAppWorld() {
        final String id = appWorldData.getAppWorldId();
        return id != null && id.length() > 0;
    }

    public void setAppWorldData(AppWorldData appWorldData) {
        this.appWorldData = appWorldData;
    }

    public boolean isAppWorldUpdateAvailable() {
        return appWorldData.isUpdateAvailable();
    }
}
