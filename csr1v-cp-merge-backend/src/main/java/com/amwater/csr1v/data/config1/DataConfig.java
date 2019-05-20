package com.amwater.csr1v.data.config1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.amwater.csr1v.data.config.model.Attribute;
import com.amwater.csr1v.data.config.model.Resolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class DataConfig {
	List<Attribute> attributes = new ArrayList<>();
	List<Resolver> resolvers = new ArrayList<>();
	Object criteriaParameters = null;
	String criteriaClause = null;
	
	
	public List<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	public List<Resolver> getResolvers() {
		return resolvers;
	}
	public void setResolvers(List<Resolver> resolvers) {
		this.resolvers = resolvers;
	}
	public void setCriteriaParameters(Object criteriaParameters)
	{
		this.criteriaParameters = criteriaParameters;
	}
	public String getCriteriaParameters()
	{
		Gson gson = new Gson(); 
		String json = gson.toJson(this.criteriaParameters);
		return json;
		
	}
	public void setCriteriaClause(String criteriaClause)
	{
		this.criteriaClause = criteriaClause;
	}
	public String getCriteriaCluase()
	{
		return this.criteriaClause;
	}
	
}
