package com.amwater.csr1v.customerinfo;

import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;

import com.amwater.csr1v.base.rest.controller.BaseRestController;
import com.amwater.csr1v.base.rest.sap.CSR1VSapResponse;
import com.amwater.csr1v.base.rest.sap.SimpleResponse;
import com.amwater.csr1v.base.rest.sap.exception.SAPRequestException;
import com.amwater.csr1v.base.rest.sap.exception.SAPResponseException;

public class CustomerData extends BaseRestController{

	
	public CSR1VSapResponse getCustomerDataFromSAP(String url, String tokenConfig) {
	
		int noOfAttempts = 1;
		return getCustomerDataFromSAP(url, tokenConfig, noOfAttempts);
	}
	public CSR1VSapResponse getCustomerDataFromSAP(String url, String tokenConfig, int retryAttempts) {

		CSR1VSapResponse flatJsonResponse = null;
		
		try {

			for(int i=0; i<retryAttempts; i++) {
				
				// get header tags and invoke API
				BasicHeader headerWithAuthToken = getSecureHeaderWithToken(tokenConfig);
				HttpResponse response = this.getRequestToSAP(url, "application/json", headerWithAuthToken);

				CustomerDataSAPResponse sapResponse = new CustomerDataSAPResponse(response);
				// check response and set appropriate fields
				flatJsonResponse = sapResponse.processResponse();
				//if security exception, then retry
				if(flatJsonResponse instanceof SimpleResponse) {
					continue;
				}else {
					break;
				}
				
				
			}

		} catch (SAPRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// set error here

		} catch (SAPResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// }
		}
		
		return flatJsonResponse;
	}
}
