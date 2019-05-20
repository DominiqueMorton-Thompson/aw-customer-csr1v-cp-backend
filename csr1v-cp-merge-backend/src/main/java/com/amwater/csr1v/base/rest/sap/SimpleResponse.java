package com.amwater.csr1v.base.rest.sap;

import java.util.HashMap;
import java.util.Map;

public class SimpleResponse implements CSR1VSapResponse{

	public String returnStatus ="";
	public String returnStatusDescription ="";
	public String IRNotes ="";	
	public String errorCode ="";
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getReturnStatus() {
		return returnStatus;
	}
	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}
	public String getReturnStatusDescription() {
		return returnStatusDescription;
	}
	public void setReturnStatusDescription(String returnStatusDescription) {
		this.returnStatusDescription = returnStatusDescription;
	}
	public String getIRNotes() {
		return IRNotes;
	}
	public void setIRNotes(String iRNotes) {
		IRNotes = iRNotes;
	}
  	
	public Map<String, Object> getResponse()  {
		
		Map<String, Object> map = new HashMap <>();

		map.put("returnStatus", this.getReturnStatus());
		map.put("returnStatusDescription", this.getReturnStatusDescription());
		map.put("IRNotes", this.getIRNotes());
		map.put("errorCode", this.getErrorCode());

		return map;
	}
	
	private String addAttrToJson(String attrName, String attr) {	
		String str = "\""+attrName+"\":";
		str+="\""+attr+"\"";
		return str;
	}

}
