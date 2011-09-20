//#preprocess

package com.xtremelabs.xtremeutil.util.json.parser;

import com.xtremelabs.xtremeutil.util.json.JsonException;
import com.xtremelabs.xtremeutil.util.json.pull.Json;
import com.xtremelabs.xtremeutil.util.logger.XLogger;
import net.rim.device.api.system.DeviceInfo;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public abstract class JsonDelegate {
	String _key;
	Class  _klass;
	
	JsonValueMapper _mapper;
	DelegateMap _delegateMap;
	
	public JsonDelegate(Class klass) {
		this(klass, null);
	}
	
	public JsonDelegate(Class klass, JsonValueMapper mapper) {
		_klass = klass;
		_mapper = mapper;
	}
	
	public JsonValueMapper getMapper() {
		return _mapper;
	}
	
	public void setMapper(JsonValueMapper mapper) {
		_mapper = mapper;
	}
	
	public void setKey(String key) {
		_key = key;
	}
	
	public void setDelegateMap(DelegateMap delegateMap) {
		_delegateMap = delegateMap;
	}
	
	abstract void parse(Json parser, Object parent) throws JsonException;
	
	
	public static array array(Class klass) {
		return new array(klass);
	}
	
	public static object object(Class klass) {
		return new object(klass);
	}
	
	public static map map(Class klass) {
	    return new map(klass);
	}
	
	public static class array extends JsonDelegate {
		public array(Class klass) {
			super(klass);
		}

		public void parse(Json parser, Object parent) throws JsonException {
			Vector items = parseArray(parser, _klass, _mapper, _delegateMap);
			
			if (items != null)
			    _mapper.setValue(parent, _key, items);
		}
	}
	
	public static class object extends JsonDelegate {
    	public object(Class klass) {
			super(klass);
		}

		public void parse(Json parser, Object parent) throws JsonException {
			Object o = parseObject(parser, _klass, _mapper, _delegateMap);
			
			if (o != null)
			    _mapper.setValue(parent, _key, o);
		}
    }

    public static class map extends JsonDelegate {
        public map(Class klass) {
            super(klass);
        }

        public void parse(Json parser, Object parent) throws JsonException {
            Object o = parseMap(parser, _klass, _mapper, _delegateMap);

            if (o != null)
                _mapper.setValue(parent, _key, o);
        }
    }

	
	
	public static Vector parseArray(Json parser, Class klass, JsonValueMapper mapper, DelegateMap map) throws JsonException {
	    if (parser.isNullNext()) {
            return null;
        }
	    
	    Vector items = new Vector();
	    
		parser.eat(Json.START_ARRAY);

		Enumeration e = parser.arrayElements();
		while (e.hasMoreElements()) {
		    Object o = null;
		    if (klass.equals(String.class)) {
		        o = parser.getStringValue();
		    } else {
		        o = parseObject(parser, klass, mapper, map);	                
		    }		    
		    items.addElement(o);			
		}
		
		return items;
	}
	
	public static Hashtable parseMap(Json parser, Class klass, JsonValueMapper mapper, DelegateMap map) throws JsonException {
        if (parser.isNullNext()) {
            return null;
        }
        
        Hashtable items = new Hashtable();
        
        // TODO: HACK with FB API returning empty array instead of empty object {}
        if (parser.peek() != Json.START_OBJECT)
            return items;
        
        parser.eat(Json.START_OBJECT);
        Enumeration e = parser.objectElements();

        while (e.hasMoreElements()) {
            String key = parser.getKey();
            Object value = parseObject(parser, klass, mapper, map);
            
            if (key != null && value != null) {
                items.put(key, value);
            }
        }
        
        return items;
    }
	    
	public static Object parseObject(Json parser, Class klass, JsonValueMapper mapper, DelegateMap map) throws JsonException {
		if (parser.isNullNext()) {
			return null;
		}

		Object o = null;

		try {
			o = klass.newInstance();
		} catch (Exception exception) {
			throw new IllegalArgumentException(klass.toString() + " does not have a default constructor");
		}

		
		Hashtable delegates = map == null ? null : map.getDelegates(klass);

		parser.eat(Json.START_OBJECT);
		Enumeration e = parser.objectElements();

		while (e.hasMoreElements()) {
			String key = parser.getKey();
			
			 if (delegates != null && delegates.containsKey(key)) {
				JsonDelegate delegate = (JsonDelegate) delegates.get(key);
                delegate.parse(parser, o);
            } else if (parser.peek() == Json.STRING || parser.isValueNext()) {
                String value = parser.getStringValue();
                mapper.setValue(o, key, value);
            } else {
				e.nextElement();
				 //#ifdef DEBUG
		        if (DeviceInfo.isSimulator()) {
		            XLogger.info(klass, "Skipped over parsing key: " + key);
		        }
				//#endif
			}

		}

		return o;
	}

}
