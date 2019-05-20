package com.amwater.csr1v.data.criteria;



import org.json.JSONArray;
import org.json.JSONObject;

import com.amwater.csr1v.data.config.model.Resolver;
import com.amwater.csr1v.data.config1.DataConfig;
import com.apporchid.criteria.AndCriteria;
import com.apporchid.criteria.OrCriteria;
import com.apporchid.criteria.StringCriteria;
import com.apporchid.foundation.criteria.ICriteria;
import com.apporchid.foundation.criteria.operator.EStringOperator;

import antlr.StringUtils;

public class CustomerCriteria extends Criteria {
//	private String businessPartnerNumber = "";
//	private String connectionContractNumber = "";
	private String url = "";
	private ICriteria<?> criteria = null;
	//private HttpServletRequest request;
	//private Json requestJson;
	private JSONObject requestJsonObj;
	private DataConfig dataConfig;
//	private Context context;
	
//	public CustomerCriteria(String businessPartnerNumber, String connectionContractNumber,Context context) {
//		this.businessPartnerNumber = businessPartnerNumber;
//		this.connectionContractNumber = connectionContractNumber;
//		this.context = context;
//	}
	/*public CustomerCriteria(HttpServletRequest request) {
		this.request = request;
	}*/
	
	public CustomerCriteria(JSONObject requestJson,DataConfig dataConfig)
	{
		requestJsonObj = requestJson;
		this.dataConfig = dataConfig;
		
	}
	
	private void buildUrl() {
//		List<Resolver> resolvers = context.getDataConfig().getResolvers();
//		for(Resolver resolver:resolvers) {
//			if(resolver.getType().equals("sapapi")) {
//				getUrl = resolver.getUrl();
//			}
//		}
		
		/*request.getParameter("businessPartnerNumber");
		request.getParameter("connectionContractNumber");
		getUrl = "https://api-dev.amwaternp.com/api/sap-ecc-gateway-apis/v1/account/account-credit-score/" + "1102428428" + "/" + "210027097745";
		*/
		try
		{
			StringBuilder builder = new StringBuilder();
			for(Resolver resolver : this.dataConfig.getResolvers())
			{
				if(resolver.getType().equalsIgnoreCase("sapapi"))
				{
					 for (Object keyStr : requestJsonObj.keySet()) {
						 	builder.append(requestJsonObj.get(keyStr.toString()));
						 	builder.append("/");
					    }
					 this.url = resolver.getUrl() + builder.substring(0, builder.length()-1).toString();
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void buildCriteria() {
		ICriteria criteria= null;
		if(this.dataConfig!=null && this.dataConfig.getCriteriaCluase()!=null)
		{
			 switch (this.dataConfig.getCriteriaCluase().toUpperCase()) {
		        case "AND":	
				        criteria = buildAndCriteria();
				        break;
		        case "OR":
		        	criteria = buildOrCriteria();
		            break;
		        default:
		        	  criteria = buildAndCriteria();
				      break;
		    }
		}
		/*AndCriteria andCriteria = new AndCriteria();
		 for (Object keyStr : requestJsonObj.keySet()) {
		        Object keyValue = requestJsonObj.get(keyStr.toString());
		        andCriteria.addCriteria(new StringCriteria(convertToDBColumn(keyStr.toString()), EStringOperator.Equals, keyValue.toString()));
		    }*/
		this.criteria = criteria;
		
		
		/*request.getParameter("businessPartnerNumber");
		request.getParameter("connectionContractNumber");
		ICriteria<?> businessPartnerCriteria = new StringCriteria("business_partner_number", EStringOperator.Equals, "1102428428");
        ICriteria<?> connectionContractNumberzCriteria = new StringCriteria("connection_contract_number",EStringOperator.Equals,"210027097745");
        AndCriteria andCriteria = new AndCriteria();
        andCriteria.addCriteria(businessPartnerCriteria);
        andCriteria.addCriteria(connectionContractNumberzCriteria);
        this.criteria = andCriteria;*/
        
	}

	@Override
	public String getUrl() {
		buildUrl();
		return url;
	}

	@Override
	public ICriteria<?> getCriteria() {
		buildCriteria();
		return criteria;
	}
	
	//Converts POJO Bean name to DB Column
	public String convertToDBColumn(String columnName)
	{
		StringBuilder columnNameStr = new StringBuilder();
		if(columnName != null && !columnName.isEmpty())
		{
			char[] columnNameArray = columnName.toCharArray();
			
			for(Character c : columnNameArray)
			{
				if(Character.isUpperCase(c))
				{
					columnNameStr.append("_");
					columnNameStr.append(Character.toLowerCase(c));
				}
				else
				{
					columnNameStr.append(c);
				}
			}
		}
		return columnNameStr.toString();
	}
	private ICriteria buildAndCriteria()
	{
		 AndCriteria andCriteria = new AndCriteria();
		 JSONObject criteriaObject = new JSONObject(this.dataConfig.getCriteriaParameters());
   		 for (Object keyStr : requestJsonObj.keySet()) {
   		        Object keyValue = requestJsonObj.get(keyStr.toString());
   		        andCriteria.addCriteria(new StringCriteria(criteriaObject.getString(keyStr.toString()), EStringOperator.Equals, keyValue.toString()));
   		    }
   		 return andCriteria;
	}
	private ICriteria buildOrCriteria()
	{
		 OrCriteria orCriteria = new OrCriteria();
		 JSONObject criteriaObject = new JSONObject(this.dataConfig.getCriteriaParameters());
   		 for (Object keyStr : requestJsonObj.keySet()) {
   		        Object keyValue = requestJsonObj.get(keyStr.toString());
   		     orCriteria.addCriteria(new StringCriteria(criteriaObject.getString(keyStr.toString()), EStringOperator.Equals, keyValue.toString()));
   		    }
   		 return orCriteria;
	}
}
