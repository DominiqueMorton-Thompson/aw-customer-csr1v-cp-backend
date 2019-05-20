package com.amwater.csr1v.constants;

import java.util.HashMap;
import java.util.Map;

public class SAPAPIFields {

	public static String[] autopayRequestFields ={ "bankRoutingNumber","customerName","bankAccountNumber","bankAccountType","fromDate","toDate"};
	public static String[] editAutopayRequestFields ={ "bankRoutingNumber","bankAccountNumber","bankAccountType","fromDate","toDate"};
	public static String[] deactivateAutopayRequestFields ={ "bankRoutingNumber","bankAccountNumber","fromDate","toDate"};
	public static String[] lockRequestFields = { "processCode", "lockType","lockReasonCode", "fromDate", "toDate" };
	public static String[] addressChangeFields = {"houseNumber","supplement","street","city","state","country","postalCode",
													"taxJurisdictionCode","primaryEmailAddress","primaryEmailOptIn",
													"alt1EmaiIAddress","alt2EmailAddress","primaryPhoneOptIn",
													"primaryPhoneSMSEnabtedFlag","printFormat","nonEmergencyNotificationType"};
	public static String[] serviceOrderRequestFields= {"maintenanceActivityType","requestedServiceOrderCreationDate","premiseNumber","businessPartnerNumber",
	"appointmentFromDate","appointmentFromTime","appointmentToDate","appointmentToTime","contractAccountNumber","serialNumber","selectedFlag","materialNumber","equipmentNumber","installationNumber","installation1","installation2","installation3","installation4","contactName","atHomeFlag","phoneAheadFlag","callbackPhoneNumber",
	"soCommentsNav","SoFee","SoWaiver","ReconnectReason"
	};

	public static String[] serviceOrderUpdateRequestFields= {"activityCode","serviceOrderComments",
			"appointmentFromDate","appointmentFromTime","appointmentToDate","appointmentToTime","atHomeFlag","phoneAheadFlag","callbackPhoneNumber","contactName","changeCRMFlag"		
			};
	
	public static String[] serviceOrderCancelRequestFields= {"activityCode","serviceOrderComments",
			"appointmentFromDate","appointmentFromTime","appointmentToDate","appointmentToTime","cancelStatus"		
			};
	
	public static String[] accountLockRequestFields= {"processCode","lockType","lockReasonCode","fromDate","toDate"
			};
	
	public static Map<String,String> accountLockAliasRequestFields= new HashMap<>();
	
	public static String[] budgetBillingRequestFields= {};

	public static String[] phoneNumberRequestFields = { "street","state","city", "country", "postalCode",
			"telephone","phonecountry","validTo","validFrom", "extension", "smsenableflag", "phonetype", "houseNumber","taxJurisdictionCode","phoneOptin"};
	
	// Response Fields
	public static String[] simpleResponseFields = { "returnStatus", "returnStatusDescription", "IRNotes", "errorCode" };
	public static String[] authenticationFailureFields = { "status", "error", "detail" };
	
	public static String[] PhoneRequestedFields = { "standardAddressFlag", "addressNumber", "phoneMode", "phoneSequenceNumber", "phoneNumber","phoneExt","standardPhoneFlag","phoneHomeFlag","phoneOptIn","phoneType","phoneSMSEnabled","phoneCountry"};
	public static String[] emailRequestFields= {"addressNumber","standardAddressFlag","emailAddress","emailMode","emailSequenceNumber","standardEmailFlag","emailHomeFlag","emailOptIn","emailValidTo"};
	public static String[] faxRequestFields= {"addressNumber","standardAddressFlag","faxNumber","faxMode","standardFaxFlag","faxHomeFlag","faxSequenceNumber","faxOptIn","faxCountry"};
	
	public static String SECURITY_EXCEPTION_STATUS = "status";
	public static String SECURITY_EXCEPTION_ERROR = "error";
	public static String SECURITY_EXCEPTION_DETAIL = "detail";

	// SAP Response Type
	public static enum SAP_RESPONSE_TYPE {
		SUCCESS, FAILURE, AUTHENTICATION_ERROR
	};
}
