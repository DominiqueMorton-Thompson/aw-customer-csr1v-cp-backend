package com.amwater.csr1v.base.rest.sap;

import java.util.HashMap;
import java.util.Map;

public class FlatJsonResponse implements CSR1VSapResponse {

	private  Map<String, Object> jsonMap = new  HashMap<String, Object>();
	
	public void addField(String key, Object value) {
		jsonMap.put(key, value);
	}
	
	@Override
	public Map<String, Object> getResponse() {
		
		return jsonMap;
	}


}
