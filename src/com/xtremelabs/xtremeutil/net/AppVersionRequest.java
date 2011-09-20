package com.xtremelabs.xtremeutil.net;

import com.xtremelabs.xtremeutil.application.XtremeApplication;
import com.xtremelabs.xtremeutil.application.descriptor.Version;
import com.xtremelabs.xtremeutil.net.api.RequestQueue;
import com.xtremelabs.xtremeutil.net.api.ResponseHandler;
import com.xtremelabs.xtremeutil.net.api.ResponseObject;
import com.xtremelabs.xtremeutil.net.api.exception.InvalidModelException;
import com.xtremelabs.xtremeutil.net.api.exception.ParsingException;
import com.xtremelabs.xtremeutil.net.api.http.HttpRequest;
import net.rim.device.api.io.IOUtilities;
import net.rim.device.api.system.Application;

import java.io.IOException;
import java.io.InputStream;

public class AppVersionRequest extends HttpRequest {

    public AppVersionRequest(ResponseHandler handler) {
        super(handler);
    }

    public void sendJadRequest(RequestQueue q) {

        setGetParameters(((XtremeApplication) Application.getApplication()).getJadUrl(), AppVersionResponse.class);

        //don't exit on error
        q.send(this);
    }

    public static class AppVersionResponse implements ResponseObject {

        private Version version;

        public void initializeWithStream(InputStream in) throws InvalidModelException, ParsingException, IOException {

            String data = new String(IOUtilities.streamToBytes(in));

            version = parseJadForVersion(data);
        }

        protected Version parseJadForVersion(String jadString) {
            int begin = jadString.indexOf("MIDlet-Version: ");
            int end = jadString.indexOf("\n", begin);
            String version = jadString.substring(begin + "MIDlet-Version: ".length(), end);

            return new Version(version);
        }

        public Version getVersion() {
            return version;
        }
    }
}