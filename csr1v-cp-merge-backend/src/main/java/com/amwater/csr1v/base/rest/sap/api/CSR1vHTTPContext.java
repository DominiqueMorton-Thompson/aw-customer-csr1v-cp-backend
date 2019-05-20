package com.amwater.csr1v.base.rest.sap.api;


import javax.servlet.http.HttpServletRequest;

import com.amwater.csr1v.base.rest.sap.exception.SAPRequestException;

public class CSR1vHTTPContext {

	public String strInputRequest="";
	
	HttpServletRequest request =null;
	
	public CSR1vHTTPContext(HttpServletRequest req){
		request = req;
	}
	
	public String getSetRequestContext() {
		return strInputRequest;
	}

	public void setSetRequestContext() throws SAPRequestException {
		strInputRequest = CSR1VHttpUtils.getRequestBody(request);
	}
	

	
}
