package com.xtremelabs.xtremeutil.util.json.parser;

import java.util.Hashtable;

public class DelegateMap {
	Hashtable _delegates;
	JsonValueMapper _mapper;

	public DelegateMap() {
		_delegates = new Hashtable();
	}
	
	public void setGlobalMapper(JsonValueMapper mapper) {
		_mapper = mapper;
	}
	
	public void addDelegate(Class klass, String key, JsonDelegate delegate) {
		Hashtable t = (Hashtable) _delegates.get(klass);
		
		if (t == null) {
			t = new Hashtable();
			_delegates.put(klass, t);
		}
		
		
		if (delegate.getMapper() == null) {
			delegate.setMapper(_mapper);
		}
		
		delegate.setKey(key);
		delegate.setDelegateMap(this);
		
		t.put(key, delegate);			
	}
	
	public Hashtable getDelegates(Class klass) {
		return (Hashtable) _delegates.get(klass);
	}		
}