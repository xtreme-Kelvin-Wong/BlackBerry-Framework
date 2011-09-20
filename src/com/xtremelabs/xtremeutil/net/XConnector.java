package com.xtremelabs.xtremeutil.net;

import com.xtremelabs.xtremeutil.util.logger.XLogger;

import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import java.io.IOException;

/**
 * XConnector wraps a Connector.open call with additional logic.
 * For example being able to cancel a Connector.open without having to 
 * wait for it to timeout
 * 
 * @author Xtreme Labs
 *
 */
public class XConnector {
        
        Thread _currentThread;
        boolean _cancelled;
        
        public Connection open(String url, int mode) throws IOException {
                Connection c = null;
                _cancelled = false;
                _currentThread = Thread.currentThread();                
                
                try {
                        c = Connector.open(url, mode);
                } catch(IOException e) {
                        XLogger.error(getClass(), e);
                        XLogger.error(getClass(), url);
                        throw e;
                } finally {
                        cleanUp();
                }                
                
                if (_cancelled && c != null) {
                    XLogger.error(getClass(),  "Request was cancelled but we still opened the connection");
                }
                
                return c;
        }
        
        
        public Connection open(String url) throws IOException {
                Connection c = null;
                _cancelled = false;
                _currentThread = Thread.currentThread();                
                
                try {
                        c = Connector.open(url);
                } catch(IOException e) {
                        XLogger.error(getClass(), e);
                        XLogger.error(getClass(), url);
                        throw e;
                } finally {
                        cleanUp();
                }                
                
                if (_cancelled && c != null) {
                    XLogger.error(getClass(),  "Request was cancelled but we still opened the connection");
                }
                
                return c;
        }
        
        private void cleanUp() {
                _currentThread = null;                
        }
        
        public void cancel() {
                if(_currentThread != null && _currentThread.isAlive()) {
                        _currentThread.interrupt();
                        _cancelled = true;
                }
                cleanUp();
        }
}
