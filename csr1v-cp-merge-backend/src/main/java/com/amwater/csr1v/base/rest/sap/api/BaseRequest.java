package com.amwater.csr1v.base.rest.sap.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.amwater.csr1v.base.rest.sap.exception.SAPRequestException;
import com.apporchid.common.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

public abstract class BaseRequest {

	protected HttpServletRequest request;

	protected String[] requestFields = null;

	protected String inputJson;

	CSR1vHTTPContext currentContext = null;

	protected Map<String, Object> inputMap = null;

	public BaseRequest(HttpServletRequest req) {
		this.request = req;
	}

	public BaseRequest(CSR1vHTTPContext context) {
		currentContext = context;
	}

	/***
	 * abstract method to set fields for each request type. These fields will be
	 * used for validating the input request From UI forms, there might be a
	 * scenario to have additional fields in input json than actual SAP API
	 * requires. validate and prepareRequest methods will use this method to extract
	 * required fields from the input json.
	 * 
	 * @param fields
	 */
	public abstract String[] getRequestFields();

	public abstract Map<String, String> getRequestAliasFields();

	/**
	 * Based on the sap api required fields, validate the following 1. All required
	 * fields passed as part of input request 2. validate each parameter has valid
	 * values
	 * 
	 * In case of error throws SAPRequestException
	 * 
	 * @return
	 */
	public boolean validate() throws SAPRequestException {
		// get request fields
		requestFields = getRequestFields();

		if (requestFields == null)
			throw (new SAPRequestException("input request fields list empty"));

		if (request == null && currentContext == null)
			throw (new SAPRequestException("Invalid Request - HttpServletRequest null"));

		// Set inputJson
		setInputJson();

		// validate all required fields exists in input json
		validateJson(inputJson);

		// do additional field level validations
		doFieldLevelValidation(inputMap);

		return true;
	}

	/**
	 * Set input json from the current context. In case current context is not set,
	 * try using request to get the input json TBD: REMOVE getting input json from
	 * the request body , only context should be used.
	 * 
	 * @throws SAPRequestException
	 */
	public void setInputJson() throws SAPRequestException {
		if (currentContext != null) {
			inputJson = currentContext.getSetRequestContext();
		} else {
			inputJson = getRequestBody();
		}

	}

	/**
	 * Input json should contain all required fields
	 * 
	 * @param jsonStr
	 * @return
	 * @throws SAPRequestException
	 */
	protected boolean validateJson(String jsonStr) throws SAPRequestException {
		try {
			inputMap = JsonUtils.jsonToMap(jsonStr);

			if (inputMap == null || inputMap.isEmpty()) {
				throw new SAPRequestException("Invalid input Json ");
			}
			HashMap<String, String> requestAliasFields = (HashMap<String, String>) getRequestAliasFields();

			//String[] aliasFields = getRequestAliasFieldsFromMap(requestAliasFields);

			//String[] fields = (String[]) ArrayUtils.addAll(requestFields, aliasFields);

			/*
			 *  FIX BY RAMA: don't do add all, as the alias fields should be replaced.
			 */
			for (String field : requestFields) {
				String fieldToValidate = getAliasField(field, requestAliasFields);
				if (inputMap.get(fieldToValidate) == null) {
					throw new SAPRequestException("Input filed " + fieldToValidate + " must be part of request");
				}
			}
		} catch (JsonProcessingException | JSONException e) {

			throw new SAPRequestException(e.getMessage());
		}

		return true;
	}

	protected String getAliasField(String field,HashMap<String, String> requestAliasFields) {
		
		if(requestAliasFields!=null && requestAliasFields.containsKey(field)) {
			return requestAliasFields.get(field);
		
		}else {
			return field;
		}
	}
	protected abstract boolean doFieldLevelValidation(Map<String, Object> inputJsonMap);

	/**
	 * Parse request body to get input json passed from UI
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public String getRequestBody() throws SAPRequestException {
		StringBuffer sb = new StringBuffer();
		BufferedReader bufferedReader = null;
		String content = "";

		try {
			bufferedReader = request.getReader(); // new BufferedReader(new InputStreamReader(inputStream));
			char[] charBuffer = new char[128];
			int bytesRead;
			while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
				sb.append(charBuffer, 0, bytesRead);
			}

		} catch (IOException ex) {
			SAPRequestException sapException = new SAPRequestException(ex.getMessage());
			throw sapException;
		} finally {
			// if (bufferedReader != null) {
			// try {
			// bufferedReader.close();
			// } catch (IOException ex) {
			// SAPRequestException sapException = new SAPRequestException(ex.getMessage());
			// throw sapException;
			// }
			// }
		}

		System.out.println(sb.toString());
		return sb.toString();
	}

	/**
	 * By default, this method returns input string from http request over ride this
	 * method for API specific behaviour. For example in case of preferred due date,
	 * UI returns only the selected date Override this method to decode date to
	 * corresponding payment term code
	 * 
	 * @return
	 * @throws SAPRequestException
	 * @throws JSONException
	 * @throws JsonProcessingException
	 */

	public String prepareRequest() throws SAPRequestException {

		JSONObject json = new JSONObject();
		try {
			inputMap = JsonUtils.jsonToMap(this.inputJson);

			HashMap<String, String> requestAliasFields = (HashMap<String, String>) getRequestAliasFields();

			String[] aliasFields = getRequestAliasFieldsFromMap(requestAliasFields);

			String[] fields = (String[]) ArrayUtils.addAll(requestFields, aliasFields);

			for (String field : fields) {
				if (requestAliasFields!=null&&requestAliasFields.containsKey(field)) {
					json.put(field, inputMap.get(requestAliasFields.get(field)));
				}
				else if(Arrays.stream(aliasFields).anyMatch(field::equals)){
					
				}else {
					json.put(field, inputMap.get(field));
				}
			}
		} catch (JsonProcessingException | JSONException e) {
			throw new SAPRequestException(e.getMessage());
		}

		return json.toString();
	}

	private String[] getRequestAliasFieldsFromMap(HashMap<String, String> requestAliasFields) {
		String[] aliasFields = {};
		if (requestAliasFields != null) {
			aliasFields=new String[requestAliasFields.size()];
			int i = 0;
			for (Entry<String, String> entry : requestAliasFields.entrySet()) {
				aliasFields[i] = entry.getValue();
				i++;
			}

		}
		return aliasFields;
	}

}
