package com.xtremelabs.xtremeutil.net.api.response;

import com.xtremelabs.xtremeutil.util.json.JsonException;
import com.xtremelabs.xtremeutil.util.json.pull.Json;
import com.xtremelabs.xtremeutil.net.api.ResponseObject;
import com.xtremelabs.xtremeutil.net.api.exception.InvalidModelException;
import com.xtremelabs.xtremeutil.net.api.exception.ParsingException;

import java.io.IOException;
import java.io.InputStream;

public abstract class JsonResponse implements ResponseObject {

        public void initializeWithStream(InputStream in) throws InvalidModelException, ParsingException, IOException {
            Json parser = null;
            try {
                parser = new Json(in);
                
                onParseStart(parser);
                        parse(parser);
                        onParseComplete(parser);
                        
                } catch (JsonException e) {
                        if (e.getCause() == null) {
                                throw new ParsingException(e);
                        } else if (e.getCause() instanceof IOException){
                                throw (IOException) e.getCause();
                        }
                }
                
                validateModel();
        }
        
        protected void onParseStart(Json parser) {
            
        }
        
        protected void onParseComplete(Json parser) {
            
        }
        
        /**
         * Construct the response with the JSON parser
         * 
         * @param parser
         * @return true if the parsing was successfully completed
         * @throws IOException
         * @throws com.xtremelabs.xtremeutil.util.json.JsonException
         */
        public abstract void parse(Json parser) throws JsonException, IOException, InvalidModelException;        
        
        /**
         * Ensures the model is valid. Subclasses should throw an InvalidModelException when
         * the json response is not valid or is an error response
         * 
         * @throws InvalidModelException
         */
        public abstract void validateModel() throws InvalidModelException;        
}
