package com.xtremelabs.xtremeutil.net.api.http;

import com.xtremelabs.xtremeutil.net.ConnectionType;
import com.xtremelabs.xtremeutil.net.IConnectionModifier;
import com.xtremelabs.xtremeutil.net.api.ResponseHandler;
import com.xtremelabs.xtremeutil.net.api.Request;
import com.xtremelabs.xtremeutil.net.api.data.BytePayload;
import com.xtremelabs.xtremeutil.net.api.data.IRequestPayload;
import com.xtremelabs.xtremeutil.net.api.exception.NoTransportsException;
import com.xtremelabs.xtremeutil.net.api.http.exception.BadResponseCodeException;
import com.xtremelabs.xtremeutil.net.api.response.StringResponse;
import net.rim.device.api.compress.GZIPInputStream;
import net.rim.device.api.io.http.HttpProtocolConstants;
import net.rim.device.api.system.Branding;
import net.rim.device.api.system.DeviceInfo;

import javax.microedition.io.HttpConnection;
import javax.microedition.io.StreamConnection;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

public class HttpRequest extends Request implements HttpProtocolConstants {
    private static String GZIP = "gzip";

    ConnectionType[] _priority = {
            ConnectionType.WIFI,
            ConnectionType.BIS,
            ConnectionType.MDS,
//            ConnectionType.WAP2, /*Disable WAP2 by default since some carrier SSL connections over WAP2 is buggy (Jun2011 Sina)*/
            ConnectionType.DIRECT };

    private String _userAgent;
    private String _requestMethod;
    private int _responseCode;

    private Hashtable auxHeaderData = new Hashtable();

    private InputStream _compressedStream;


    public HttpRequest() {
        this(null);
    }

    public HttpRequest(ResponseHandler delegate) {
        super(delegate);

        setErrorResponseClass(StringResponse.class);
        setResponseClass(StringResponse.class);

        if(DeviceInfo.isSimulator()) {
            setConnectionOrder(new ConnectionType[] { ConnectionType.BIS,
                    ConnectionType.DIRECT });
        }


        setUserAgent(getDefaultUserAgent());
    }

    public static String getDefaultUserAgent() {
        return new StringBuffer(75).append("BlackBerry")
                .append(DeviceInfo.getDeviceName()).append('/')
                .append(DeviceInfo.getSoftwareVersion())
                .append(" Profile/")
                .append(System.getProperty("microedition.profiles"))
                .append(" Configuration/")
                .append(System.getProperty("microedition.configuration"))
                .append(" VendorID/").append(Branding.getVendorId())
                .toString();
    }

    public void setUserAgent(String userAgent) {
        _userAgent = userAgent;
    }

    public void setConnectionOrder(ConnectionType[] order) {
        _priority = order;
    }

    public void setRequestMethod(String method) {
        _requestMethod = method;
    }

    public String getRequestMethod() {
        return _requestMethod;
    }

    public void setPostParameters(String url, Class responseClass) {
        setRequestParameters(HTTP_METHOD_POST, url, responseClass, null);
    }

    public void setPostParameters(String url, Class responseClass, byte[] requestData) {
        setPostParameters(url, responseClass, new BytePayload(requestData));
    }

    public void setPostParameters(String url, Class responseClass, IRequestPayload requestPayload) {
        setRequestParameters(HTTP_METHOD_POST, url, responseClass, requestPayload);
    }

    public void setGetParameters(String url, Class responseClass) {
        setRequestParameters(HTTP_METHOD_GET, url, responseClass, null);
    }

    public void setDeleteParameters(String url, Class responseClass, IRequestPayload requestPayload) {
        setRequestParameters(HTTP_METHOD_DELETE, url, responseClass, null);
    }

    protected void setRequestParameters(String method, String url, Class responseClass, IRequestPayload requestPayload) {
        setRequestMethod(method);
        setUrl(url);
        setResponseClass(responseClass);
        setRequestData(requestPayload);
    }

    protected Exception checkResponse() {
        if(_responseCode >= HttpConnection.HTTP_OK && _responseCode <= HttpConnection.HTTP_PARTIAL) {
            return null;
        } else {
            return new BadResponseCodeException(_responseCode);
        }
    }

    protected InputStream openInputStream(StreamConnection connection) throws IOException {
        InputStream stream = super.openInputStream(connection);

        if(connection instanceof HttpConnection) {
            HttpConnection c = (HttpConnection) connection;
            String encoding = c.getEncoding();

            if (encoding != null && encoding.indexOf(GZIP) != -1) {
                _compressedStream = stream;
                GZIPInputStream uncompresedStream = new GZIPInputStream(_compressedStream);
                return uncompresedStream;
            }
        }

        return stream;
    }

    protected void closeInputStream() {
        super.closeInputStream();
        closeInputStream(_compressedStream);
        _compressedStream = null;
    }

    protected StreamConnection createConnection(IConnectionModifier[] modifiers) throws IOException {
        HttpConnection connection = null;
        IOException lastException = null;

        for (int i = 0; i < _priority.length; i++) {
            ConnectionType type = _priority[i];

            //Quit if this connection type doesn't have coverage or a service book
            if(!type.canConnect())
                continue;

            String url = getUrl();

            try {
                //We follow redirects
                while(true) {
                    String formattedUrl = type.formatUrl(url);
                    connection = createHttpConnection(formattedUrl, modifiers);

                    if (connection == null)
                        break;

                    _responseCode = connection.getResponseCode();

                    if (_responseCode >= HttpConnection.HTTP_MULT_CHOICE && _responseCode <= HttpConnection.HTTP_TEMP_REDIRECT) {
                        url = connection.getHeaderField("Location");

                        closeConnection(connection); // Close the previous connection if we want to redirect
                        if (url == null) {
                            throw new IOException("Tried to redirect " + formattedUrl + " without a location");
                        }

                    } else {
                        return connection;
                    }
                }
            } catch (IOException e) {
                closeConnection(connection);
                connection = null;
                lastException = e;
            }
        }

        if (lastException == null)
            throw new NoTransportsException();
        else
            throw lastException;
    }

    protected HttpConnection createHttpConnection(String formattedUrl, IConnectionModifier[] modifiers) throws IOException {
        HttpConnection connection    = null;
        IRequestPayload requestPayload = null;

        try {
            connection = (HttpConnection) getConnector().open(formattedUrl);
            connection.setRequestMethod(getRequestMethod());

            connection.setRequestProperty(HEADER_USER_AGENT, _userAgent);
            connection.setRequestProperty(HEADER_ACCEPT_ENCODING, "gzip, deflate");
            connection.setRequestProperty("x-rim-transcode-content", "none");

            Enumeration auxHeaderEnum = auxHeaderData.keys();

            while(auxHeaderEnum.hasMoreElements()) {
                String key = (String)auxHeaderEnum.nextElement();
                String value = (String)auxHeaderData.get(key);

                connection.setRequestProperty(key, value);
            }

            modifyConnection(connection, modifiers);

            requestPayload = getRequestData();

            if (requestPayload != null) {

                connection.setRequestProperty(HEADER_CONTENT_LENGTH, "" + requestPayload.getLength());
                connection.setRequestProperty(HEADER_CONTENT_TYPE, requestPayload.getContentType());

                setOutputStream(connection.openDataOutputStream());
                requestPayload.writeData(getOuputStream());
                getOuputStream().flush();
            }
        } catch(IOException e) {
            closeConnection(connection);
            connection = null;
            throw e;
        } finally {
            closeOutputStream();
            cleanUpRequestData();
        }

        return connection;
    }

    protected void putAuxHeader(String name, String value) {
        auxHeaderData.put(name, value);
    }
}
