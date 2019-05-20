package com.amwater.csr1v.customerinfo;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;

import com.amwater.csr1v.base.rest.sap.CSR1VSapResponse;
import com.amwater.csr1v.base.rest.sap.FlatJsonResponse;
import com.amwater.csr1v.base.rest.sap.api.BaseResponse;
import com.amwater.csr1v.base.rest.sap.exception.SAPResponseException;
import com.amwater.csr1v.util.JsonConverter;

public class CustomerDataSAPResponse extends BaseResponse {

	public CustomerDataSAPResponse(HttpResponse resp) {
		super(resp);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Override base converter to get flat json
	 */
	protected Map<String, Object> convertResponseToMap(String responseStr) throws SAPResponseException {
		
		JsonConverter jsonconverter = new JsonConverter();
		
		try {
			return jsonconverter.getJsonMap(responseStr);
		} catch (IOException e) {
			e.printStackTrace();
			throw new SAPResponseException(e.getMessage());
		}
	}

	
	public CSR1VSapResponse decodeResponse(Map<String, Object> responseMap) {
	
		FlatJsonResponse flatJson = new FlatJsonResponse();
		
		String[][] fieldMappings = getFieldMapping();
		
		for(String[] fieldMap : fieldMappings ) {
			
			String csrField = fieldMap[0];
			String sapField= fieldMap[1];
		
			if(!responseMap.containsKey(sapField))
			{
				//field not found 
				continue;
			}
		
			if(csrField.equalsIgnoreCase("collectiveenrollStatus")) {
				String accountNumber = ((String)responseMap.get(sapField));
				flatJson.addField(csrField, getEligibleStatus(accountNumber));	
			}else if(csrField.equalsIgnoreCase("creditWorthiness")) {
				Object creditWorthiness = responseMap.get(sapField);
				if(creditWorthiness != null  && !((String)creditWorthiness).isEmpty()) {
					String risk_level_explanation ="Low Risk";
					String cw = ((String)creditWorthiness);
					Integer creditscore  = Integer.parseInt(cw);
					if(creditscore <= 80) {
						risk_level_explanation="Low Risk";
					}else if(creditscore > 80 && creditscore <= 160) {
						risk_level_explanation="Medium Risk";
					}else if(creditscore > 160 && creditscore <= 320) {
						risk_level_explanation="High Risk";
					}else {
						risk_level_explanation="Very High Risk";
					}
						
					flatJson.addField(csrField, creditscore);
					flatJson.addField("riskLevelExplanation", risk_level_explanation);
				}
			
			}else if(csrField.equalsIgnoreCase("paperlessStatements")) {
				String paperLessFlag = ((String)responseMap.get(sapField));
				flatJson.addField(csrField, getEnrolledStatus(paperLessFlag));
			}else if(csrField.equalsIgnoreCase("budgetenrollStatus")) {
				String budgetBilling = ((String)responseMap.get(sapField));
				flatJson.addField(csrField, getEligibleStatus(budgetBilling));
			}else if(csrField.equalsIgnoreCase("taxJurisdictionCode")) {
				//replace - add prefix USMO to zip
				String zip = ((String)responseMap.get(sapField));
				zip = "USMO"+zip.replaceAll("-", "");
				flatJson.addField(csrField, zip);
			}else if(csrField.equalsIgnoreCase("autoPaymentPreferredDueData")) {
				String preferredDueData = ((String)responseMap.get(sapField));
				/*Integer dueDate =Integer.parseInt(preferredDueData.substring(2)); 
				LocalDate localDate = LocalDate.now();
				LocalDate preferredDueDate = LocalDate.of(localDate.getYear(),localDate.getMonth(), dueDate.intValue());*/
				flatJson.addField(csrField, preferredDueData);
			}
			else if(csrField.equalsIgnoreCase("bankDetails")) {
				JSONArray BankDetailsArray = (JSONArray) responseMap.get(sapField);
				flatJson.addField(csrField, BankDetailsArray);
			}
			else{
				flatJson.addField(csrField, responseMap.get(sapField));
			}
		
		}
			

		
		return flatJson;
	}

	private String getEnrolledStatus( String fieldValue) {
		if(fieldValue.isEmpty() || fieldValue.equalsIgnoreCase("N")) {
			return "Not Enrolled";
		}else {
			return  "Enrolled";
		}

	}
	/**
	 * Eligible Status
	 * @param fieldValue the String
	 * @return eligibleStatus as String
	 */
	private String getEligibleStatus( String fieldValue) {
		if(fieldValue.isEmpty() || fieldValue.equalsIgnoreCase("N")) {
			return "Not Eligible";
		}else {
			return  "Eligible";
		}

	}
	
	private String[][] getFieldMapping(){
		String[][] fieldMap= 
			{ 
				{"premiseType", "additionalInformation.accountDetailResponse.accountClassDescription"},
				{"customerStatus", "additionalInformation.accountDetailResponse.accountStatus"},
				{"creditWorthiness","creditScore"},
				{"budgetenrollStatus","additionalInformation.accountDetailResponse.budgetBillIndicator"},
				{"collectiveenrollStatus","additionalInformation.accountDetailResponse.collectiveAccountNumber"},
				{"paperlessStatements","additionalInformation.accountDetailResponse.ebillingEnrolledFlag"},
				{"cashOnly","additionalInformation.accountDetailResponse.cashOnlyFlag"},
				{"taxJurisdictionCode","additionalInformation.accountDetailResponse.mailingAddress.zip"},
				{"autoPaymentPreferredDueData","additionalInformation.accountDetailResponse.autoPaymentPreferredDueData"},
				{"bankDetails","additionalInformation.accountDetailResponse.bankAccounts"}
			};
		
		return fieldMap;
	}
}
