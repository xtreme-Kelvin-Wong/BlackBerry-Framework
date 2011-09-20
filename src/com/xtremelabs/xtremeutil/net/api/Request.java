package com.xtremelabs.xtremeutil.net.api;

import com.xtremelabs.xtremeutil.net.ConnectionStatus;
import com.xtremelabs.xtremeutil.net.IConnectionModifier;
import com.xtremelabs.xtremeutil.net.XConnector;
import com.xtremelabs.xtremeutil.net.api.data.IRequestPayload;
import com.xtremelabs.xtremeutil.net.api.exception.NoTransportsException;
import net.rim.device.api.synchronization.UIDGenerator;

import javax.microedition.io.StreamConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

public abstract class Request implements Runnable {
    private static final int NUM_RETRIES = 2;

    public static final int MAX_PRIORITY = 0;
    public static final int NORM_PRIORITY = 5000;
    public static final int MIN_PRIORITY = 10000;

    private ResponseHandler _delegate;
    private Exception _error;

    private StreamConnection _connection;
    private OutputStream _outputStream;
    private InputStream _inputStream;
    private IRequestPayload _requestPayload;
    private XConnector _connector;

    private Class _responseClass;
    private Class _errorResponseClass;

    private ResponseObject _responseObject;

    private int _identifier;
    private int _priority;

    private boolean _cancelled;
    private boolean _finished;

    private Object _context;
    private String _url;
    private Vector _connectionModifiers;

    protected Request(ResponseHandler delegate) {
        _delegate = delegate;
        _identifier = UIDGenerator.getUID();
        _priority = MIN_PRIORITY;
        _connectionModifiers = new Vector();
        _connector = new XConnector();
    }

    public void addConnectionModifier(IConnectionModifier modifier) {
        _connectionModifiers.addElement(modifier);
    }

    public void removeConnectionModifier(IConnectionModifier modifier) {
        _connectionModifiers.removeElement(modifier);
    }

    /**
     * Set the priority of this request. Low numbers are treated with higher
     * priority. In other words, zero is the higher priority.
     * 
     * @param priority
     *            the priority number of this request
     */
    public void setPriority(int priority) {
        _priority = priority;
    }

    public int getPriority() {
        return _priority;
    }

    public boolean isCancelled() {
        return _cancelled;
    }

    public String getUrl() {
        return _url;
    }

    public void setUrl(String url) {
        _url = url;
    }

    public ResponseHandler getDelegate() {
        return _delegate;
    }

    protected void setDelegate(ResponseHandler handler) {
        _delegate = handler;
    }

    public Object getContext() {
        return _context;
    }

    public void setContext(Object context) {
        _context = context;
    }

    public int getIdentifier() {
        return _identifier;
    }

    public boolean isFinished() {
        return _finished;
    }

    public void cancel() {
        _cancelled = true;

        cancelConnector();
        closeOutputStream();
        closeInputStream();
        closeConnection();
        cleanUpRequestData();
    }

    public void setErrorResponseClass(Class errorResponseClass) {
        _errorResponseClass = errorResponseClass;
    }

    public void setResponseClass(Class responseClass) {
        _responseClass = responseClass;
    }

    protected OutputStream getOuputStream() {
        return _outputStream;
    }

    protected void setOutputStream(OutputStream stream) {
        _outputStream = stream;
    }

    protected void setInputStream(InputStream stream) {
        _inputStream = stream;
    }

    protected XConnector getConnector() {
        return _connector;
    }

    public void setRequestData(IRequestPayload payload) {
        _requestPayload = payload;
    }

    public IRequestPayload getRequestData() {
        return _requestPayload;
    }

    public void run() {
        executeRequest();
    }

    public void executeRequest() {
        if (!isCancelled() && !isFinished()) {
            boolean hasConnection = ConnectionStatus.hasValidConnection();

            if (!hasConnection) {
                _error = new NoTransportsException();
            }

            if (hasConnection) {
                performRequest();
            }
        }

        postProcess();
    }

    protected abstract StreamConnection createConnection(
            IConnectionModifier[] modifiers) throws IOException;

    protected void sleep() {
        //no sleep by default
      /*  try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
        }*/
    }

    protected Exception checkResponse() {
        return null;
    }

    protected void processConnection(StreamConnection connection)
            throws Exception {
        try {
            _error = checkResponse();

            setInputStream(openInputStream(connection));

            if (_error == null)
                _responseObject = (ResponseObject) _responseClass.newInstance();
            else {
                _responseObject = (ResponseObject) _errorResponseClass.newInstance();
            }

            _responseObject.initializeWithStream(_inputStream);
        } catch (Exception e) {
            // If an exception occurs while we initialize the object
            // it will be in an unknown state thus we ignore what was parsed
            _responseObject = null;
            throw e;
        } finally {
            closeInputStream();
        }
    }

    protected InputStream openInputStream(StreamConnection connection)
            throws IOException {
        return connection.openDataInputStream();
    }

    private void performRequest() {
        if (isCancelled())
            return;

        try {

            int size = _connectionModifiers.size();

            IConnectionModifier[] modifiers = new IConnectionModifier[size];
            _connectionModifiers.copyInto(modifiers);

            _connection = createConnection(modifiers);

            if (!isCancelled()) {
                sleep();
                processConnection(_connection);
            }

        } catch (Exception e) {
            handleException(e);
        } finally {
            closeConnection();
            cleanUpRequestData();
            sleep();
        }
    }

    protected void modifyConnection(StreamConnection connection,
            IConnectionModifier[] modifiers) throws IOException {
        if (modifiers != null) {
            for (int i = 0; i < modifiers.length; i++) {
                if (modifiers[i] != null)
                    modifiers[i].modify(connection);
            }
        }
    }

    protected void handleException(Exception e) {
        _error = e;
    }

    public void postProcess() {
        _finished = true;
        if (_delegate != null && !isCancelled()) {
            if (_error != null) {
                _delegate.requestFailed(this, _error, _responseObject);
            } else {
                _delegate.requestReturned(this, _responseObject);
            }
        }
    }

    protected void cleanUpRequestData() {
        if (_requestPayload != null) {
            try {
                _requestPayload.cleanUp();
            } catch (Throwable e) {

            }
        }

        // _requestPayload = null;
    }

    protected void closeConnection() {
        closeConnection(_connection);
    }

    protected void closeOutputStream() {
        if (_outputStream != null) {
            try {
                _outputStream.close();
            } catch (Throwable t) {

            }
        }

        _outputStream = null;
    }

    protected void closeInputStream() {
        closeInputStream(_inputStream);
        _inputStream = null;
    }

    protected void closeConnection(StreamConnection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Throwable e) {

            }
        }
        sleep();
    }

    protected void closeInputStream(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (Throwable t) {

            }
        }
    }

    protected void cancelConnector() {
        if (_connector != null) {
            try {
                _connector.cancel();
            } catch (Throwable t) {

            }
        }
    }
}
