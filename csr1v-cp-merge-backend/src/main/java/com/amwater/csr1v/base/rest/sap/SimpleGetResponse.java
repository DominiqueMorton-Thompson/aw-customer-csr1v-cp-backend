package com.amwater.csr1v.base.rest.sap;

import java.util.Map;

public class SimpleGetResponse implements CSR1VSapResponse{

	public Map<String, Object> responseMap = null;;
	
  	

	public void setResponseMap(Map<String, Object> respMap) {
		this.responseMap = respMap;
	}

	public Map<String, Object> getResponse()  {
		
		return responseMap;
		
	}
	
	private String addAttrToJson(String attrName, String attr) {	
		String str = "\""+attrName+"\":";
		str+="\""+attr+"\"";
		return str;
	}

}
