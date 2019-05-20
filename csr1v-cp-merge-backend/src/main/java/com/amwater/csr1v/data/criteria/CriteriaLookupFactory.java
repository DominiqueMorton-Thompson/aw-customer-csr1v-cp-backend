package com.amwater.csr1v.data.criteria;

import javax.json.Json;

import com.amwater.csr1v.data.config1.DataConfig;
import com.apporchid.foundation.criteria.ICriteria;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class CriteriaLookupFactory {

	
	private static CriteriaLookupFactory criteriaFactory;
	
	private CriteriaLookupFactory()
	{
		
	}
	
	public static CriteriaLookupFactory getInstance()
	{
		if(criteriaFactory == null)
		{
			criteriaFactory = new CriteriaLookupFactory();
		}
		return criteriaFactory;
	}
	
	public ICriteria<?> lookupCriteria(String contextType,JSONObject requestJson,DataConfig dataConfig)
	{
		//if(contextType!=null && contextType.equalsIgnoreCase(CriteriaEnum.customer.name()))
		{
			Criteria customer = new CustomerCriteria(requestJson,dataConfig);
			
			return customer.getCriteria();
		}
		/*if(contextType!=null && contextType.equalsIgnoreCase(CriteriaEnum.billing.name()))
		{
			Criteria customer = new CustomerCriteria(requestJson,dataConfig);
			
			return customer.getCriteria();
		}*/
		//return null;
	}
	
	public String lookupUrl(String contextType,JSONObject requestJson,DataConfig dataConfig)
	{
		if(contextType!=null && contextType.equalsIgnoreCase(CriteriaEnum.customer.name()))
		{
			Criteria customer = new CustomerCriteria(requestJson,dataConfig);
			return customer.getUrl();
		}
		return null;
	}
}
