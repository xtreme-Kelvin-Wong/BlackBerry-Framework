package com.changeme.app.barebone_changename.net.response;

import com.xtremelabs.xtremeutil.net.api.ResponseObject;
import net.rim.device.api.io.IOUtilities;

import java.io.IOException;
import java.io.InputStream;

public class URLResponse implements ResponseObject {

    private byte[] _data;

    public void initializeWithStream(InputStream in) {
        try {
            _data = IOUtilities.streamToBytes(in);
        } catch (IOException e) {

        }
    }

    public byte[] getData() {
        return _data;
    }
}
