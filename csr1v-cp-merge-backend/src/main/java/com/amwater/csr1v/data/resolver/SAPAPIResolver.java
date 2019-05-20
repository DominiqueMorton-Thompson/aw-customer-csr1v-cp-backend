package com.amwater.csr1v.data.resolver;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.amwater.csr1v.base.rest.sap.CSR1VSapResponse;
import com.amwater.csr1v.customerinfo.CustomerData;
import com.amwater.csr1v.data.context.Context;

@Component(value = "sapAPIResolver")
public class SAPAPIResolver implements BaseResolver{

	@Override
	public void getData(Context context, String url) {
		try {
		CustomerData customerData = new CustomerData();
		//String url = "http://10.7.20.29:9090/api/sap-ecc-gateway-apis/v1/account/account-credit-score/" + businessPartnerNumber + "/" + connectionContractNumber;
		String token = "c1v_test";
		CSR1VSapResponse resp = customerData.getCustomerDataFromSAP(context.getUrl(), token);

		Map<String, Object> respMap = resp.getResponse();
		
		/*Merging data with */
		Map<String,Object> contextMap = context.getData();
		if(respMap!=null && !respMap.isEmpty())
			contextMap.putAll(respMap);
		}
		catch(Exception e) {
			System.out.println("");
		}
	}

}
