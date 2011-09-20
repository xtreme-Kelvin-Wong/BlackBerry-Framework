package com.xtremelabs.xtremeutil.net;

import javax.microedition.io.StreamConnection;
import java.io.IOException;

/**
 * IConnectionModifier
 * 
 * Classes who wish to modify a connection after Connector.open() 
 * is called should inherit this interface
 * 
 * @author Xtreme Labs
 * 
 * Copyright 2010 Xtreme Labs Inc.  All Rights Reserved. 
 *
 */
public interface IConnectionModifier {

        /**
         * Modifies the given connection
         * 
         * @param connection The connection that is to be modified
         */
        void modify(StreamConnection connection) throws IOException;
}
