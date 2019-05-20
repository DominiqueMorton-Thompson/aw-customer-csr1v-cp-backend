package com.amwater.csr1v.data.context;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.amwater.csr1v.data.config1.ConfigLoader;
import com.amwater.csr1v.data.config1.DataConfig;
import com.amwater.csr1v.data.criteria.CriteriaLookupFactory;
import com.apporchid.common.utils.ContextHelper;
import com.apporchid.foundation.criteria.ICriteria;
import com.apporchid.jpa.cpool.SqlDatasourceHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Context {
	private DataConfig dataConfig;
	private Map<String,Object> dataFromResolvers = new HashMap<String,Object>();
	private Map<String,Object> responseMap;
	private ICriteria<?> criteria;
	public ConfigLoader configLoader = null;
	private String msoName="";
	private String url;
	private int limitRecords;
	private Map<String,Object> requestErrorObject;
//	@Inject
//	private CustomerCriteria customerCriteria;
	
	public Context(String msoName) {
		this.msoName = msoName;
		setDataConfig(msoName);
	}
	
	public String getMsoName()
	{
		return this.getMsoName();
	}

	public ICriteria<?> getCriteria() {
		return criteria;
	}

	public void setCriteria(ICriteria<?> criteria) {
		this.criteria = criteria;
	}

	public Map<String, Object> getResponseMap() {
		return responseMap;
	}

	public void setResponseMap(Map<String, Object> responseMap) {
		this.responseMap = responseMap;
	}

	public Map<String, Object> getData() {
		return dataFromResolvers;
	}

	public void setData(Map<String, Object> data) {
		this.dataFromResolvers = data;
	}

	public DataConfig getDataConfig() {
		return dataConfig;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getUrl()
	{
		return this.url;
	}
	public void setLimitRecords(int limitRecords)
	{
		this.limitRecords = limitRecords;
	}
	public int getLimitRecords()
	{
		return this.limitRecords;
	}
	
	public void setRequestErrorObject(HashMap<String,Object> requestErrorObject)
	{
		this.requestErrorObject = requestErrorObject;
	}
	public Map<String,Object> getRequestErrorObject()
	{
		return this.requestErrorObject;
	}

//	public void setDataConfig(DataConfig dataConfig) {
//		this.dataConfig = dataConfig;
//	}
	
	public void setDataConfig(String dataType) {
        ResultSet resultSet = null;
        Connection connection = null;
        Statement statement = null;
        String configJson =null;
        
        String configQuery =" select csr1vcp_pipeline_configuration.pileline_mso_configuration as configuration from public.csr1vcp_pipeline_configuration where mso_name = '" + dataType +"'";
        try {
            SqlDatasourceHelper sqlDatasourceHelper = ContextHelper.getBean(SqlDatasourceHelper.class);
            DataSource ds = sqlDatasourceHelper.getDatasourceByName("ao-csrcp2");
            connection = ds.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(configQuery);

            if (resultSet != null) {
 
                final ResultSet resultSetTemp = resultSet;
                while (resultSet.next()) {
                	configJson =  resultSetTemp.getString("configuration");
                }
            }

			Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(configJson);// response will be the json String

			this.dataConfig = (gson.fromJson(object, DataConfig.class));
			
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(resultSet, connection, statement);
        }
    }
	public static void close(ResultSet resultSet, Connection connection, Statement statement) {
		DbUtils.closeQuietly(resultSet);
		DbUtils.closeQuietly(statement);
		try {
			DbUtils.close(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void buildCriteria(JSONObject requestJson) {
		//criteria = new CustomerCriteria(request);
		this.setCriteria(CriteriaLookupFactory.getInstance().lookupCriteria(this.msoName, requestJson,this.getDataConfig()));
		this.setUrl(CriteriaLookupFactory.getInstance().lookupUrl(this.msoName, requestJson,this.getDataConfig()));
	}
	
	public boolean validateRequestParameters(JSONObject requestJson)
	{
		requestErrorObject = new HashMap<>();
		 ObjectMapper om = new ObjectMapper();
	        try {
	            Map<String, Object> requestedMap = (Map<String, Object>)(om.readValue(requestJson.toString(), Map.class));
	            Map<String, Object> expectedMap = (Map<String, Object>)(om.readValue(this.dataConfig.getCriteriaParameters().toString(), Map.class));
	            if(requestedMap.keySet().equals(expectedMap.keySet()))
	            {
	            	return true;
	            }
	            else
	            {
	            	
	            	JSONObject errorJson = new JSONObject();
	            	errorJson.put("reason", "Error");
	            	errorJson.put("description", "Error Occured while processing request due to incorrect parameters in request.\nCorrect Parameters as showned in expected");
	            	JSONArray expectedJsonArray = new JSONArray();
	            	Iterator<String> expectedKeys = new JSONObject(this.dataConfig.getCriteriaParameters()).keys();
	            	while(expectedKeys.hasNext())
	            	{
	            		expectedJsonArray.put(expectedKeys.next());
	            	}
	            	errorJson.put("expected", expectedJsonArray);
	            	
	            	JSONArray actualJsonArray = new JSONArray();
	            	Iterator<String> actualKeys =requestJson.keys();
	            	while(actualKeys.hasNext())
	            	{
	            		actualJsonArray.put(actualKeys.next());
	            	}
	            	errorJson.put("actual", actualJsonArray);
	            	
	            	
	            	//Gson gson = new Gson();
	            	//errorJson.put("expected", gson.toJson(this.dataConfig.getCriteriaParameters()));
	            	//errorJson.put("actual", gson.toJson(requestJson));
	            	requestErrorObject=toMap(errorJson);
	            	//requestErrorObject.put("", errorJson);
	            	return false;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		return false;
	}
	
	public static Map<String, Object> toMap(JSONObject object) throws JSONException {
	    Map<String, Object> map = new HashMap<String, Object>();

	    Iterator<String> keysItr = object.keys();
	    while(keysItr.hasNext()) {
	        String key = keysItr.next();
	        Object value = object.get(key);

	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        map.put(key, value);
	    }
	    return map;
	}
	public static List<Object> toList(JSONArray array) throws JSONException {
	    List<Object> list = new ArrayList<Object>();
	    for(int i = 0; i < array.length(); i++) {
	        Object value = array.get(i);
	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        list.add(value);
	    }
	    return list;
	}
}
