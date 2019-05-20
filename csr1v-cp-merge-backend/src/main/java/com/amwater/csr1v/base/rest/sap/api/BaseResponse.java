package com.amwater.csr1v.base.rest.sap.api;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amwater.csr1v.base.rest.sap.CSR1VSapResponse;
import com.amwater.csr1v.base.rest.sap.SimpleResponse;
import com.amwater.csr1v.base.rest.sap.exception.SAPResponseException;
import com.amwater.csr1v.constants.SAPAPIFields;
import com.amwater.csr1v.constants.SAPResponseCodes;
import com.apporchid.common.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

public class BaseResponse {
	protected HttpResponse response;
	

	public BaseResponse(HttpResponse resp) {
		response = resp;
	}

	/**
	 *  This method sets the response fields to simple response. over ride this method to setup API specific responses. 
	 *  For example, for service order response contains structures for appointment, service type, locks etc. 
	 * @return 
	 *  
	 */
	protected String[] getResponseFields() {
		return SAPAPIFields.simpleResponseFields;
	}
	
	/** 
	 *  For authentication failure, response comes from gateway, not from SAP. Response contains auth failure specific
	 *  fields
	 * @return
	 */
	protected String[] getAuthFailureResponseFields() {
		return SAPAPIFields.authenticationFailureFields;
	}
	
	/**
	 *  Process Response - this does the following
	 *  	1. Check if security exception , if so, return exception code
	 *  	2. Simple reponse decode and return response string
	 *  
	 *  DONT OVERRIDE this method. 
	 * @return
	 * @throws SAPResponseException 
	 */
	public CSR1VSapResponse processResponse() throws SAPResponseException {
		String finalRespStr ="";
		CSR1VSapResponse respObj=null;
		
		 try {
			 	if(response == null) {
			 		throw new SAPResponseException("Invalid Response");
			 	}
			 	String responseStr = EntityUtils.toString(response.getEntity());
			 	
			 	
			 	Map<String, Object> responseMap = convertResponseToMap(responseStr);
			 	
			 	if(responseMap == null || responseMap.size()==0 ) {
			 		throw new SAPResponseException("Fail to convert response to Map");
			 	}
			 	
			 	//check for security exception
			 	if(isSecurityException(responseMap)) {		
			 		respObj= decodeSecurityException(responseMap);
			 	}else {
				 	respObj= decodeResponse(responseMap);				 	
			 	}
			 	
			 	return respObj;
			 	
		} catch (IOException | SAPResponseException e) {
			// TODO Auto-generated catch block
			throw new SAPResponseException(e.getMessage());
		} catch (Exception e) {
			throw new SAPResponseException(e.getMessage());
		}

	}
	
	/**
	 *  Convert response to Map. Override this for special cases
	 * @throws SAPResponseException 
	 * @throws JSONException 
	 * @throws JsonProcessingException 
	 */
	
	protected Map<String, Object> convertResponseToMap(String responseStr) throws SAPResponseException {
		
		try {
			return JsonUtils.jsonToMap(responseStr);
		} catch (JsonProcessingException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SAPResponseException(e.getMessage());
		}
	}
	
	/**
	 * Security Exception has following fields -  {"status", "error","detail"}
	 * parse and return simple response with Response =Error, ResponseCode = SECURITY_EXCEPTION
	 * @param responseMap
	 * @return
	 */
	private CSR1VSapResponse decodeSecurityException(Map<String, Object> responseMap) {

		SimpleResponse resp = new SimpleResponse();
		
		resp.setErrorCode(SAPResponseCodes.SECURITY_EXCEPTION);
		
		if(responseMap.containsKey(SAPAPIFields.SECURITY_EXCEPTION_DETAIL) )
			resp.setReturnStatusDescription(responseMap.get(SAPAPIFields.SECURITY_EXCEPTION_DETAIL).toString());

		if(responseMap.containsKey(SAPAPIFields.SECURITY_EXCEPTION_ERROR) )
			resp.setReturnStatus("E");
		
		resp.setIRNotes("Security Violation");
		
		
		return resp;
	}

	/**
	 * check for security exception	first; if failed to get secure token, then the response is different
	 * from simple SAP API response.
	 * @param responseMap
	 * @return
	 */
	private boolean isSecurityException(Map<String, Object> responseMap) {

		for(String str :  this.getAuthFailureResponseFields()) {
			if(responseMap.containsKey(str) )
			 	return true;
		}
		
		return false;
	}

	/**
	 *  Default behaviour is to get response as following 
	 *  	- ResponseCode : Success, failure, auth exception
	 *  	- ResponseDescription
	 *  	- IRNotes
	 *  
	 *  In case response is different from basic SAP response, then override this method
	 * @return
	 * @throws SAPResponseException 
	 */
	public CSR1VSapResponse decodeResponse(Map<String, Object> responseMap) {
		
		SimpleResponse simpleResponse = new SimpleResponse();
		try {
				if(responseMap.containsKey("returnStatuses")) {
					JSONArray array = (JSONArray)responseMap.get("returnStatuses");
					
					if(array != null && array.length() >0 ) {
						JSONObject json = (JSONObject) array.get(0);
						simpleResponse.setReturnStatus(json.get("returnStatus").toString());
						
						if(json.get("returnStatus").toString().equals("E")) {
							simpleResponse.setErrorCode(SAPResponseCodes.SAP_PROCESSING_ERROR);
						}
						simpleResponse.setReturnStatusDescription(json.get("returnStatusDescription").toString());				
					}
				}//end of return statuses
				
				//Set IRStatus
				if(responseMap.containsKey("IRNotes")) {
					simpleResponse.setIRNotes(responseMap.get("IRNotes").toString());
				}
				//No error, set error code to blank
				simpleResponse.setErrorCode("");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return simpleResponse;
	}
	
}
