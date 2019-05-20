package com.amwater.csr1v.data.driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.amwater.csr1v.data.config.model.Attribute;
import com.amwater.csr1v.data.context.Context;

@Component
public class AttributeDriver {
	public void attributeMapper(Context context) {
		try {

			Map<String, Object> responseMap = new HashMap<>();
			Map<String, Object> dataFromResolver = context.getData();
			List<Attribute> attributes = context.getDataConfig().getAttributes();
			
			/*Mapping attributes from db to UI*/
			
			if(dataFromResolver!=null && dataFromResolver.get("results")!=null)
			{
				ArrayList<?> list = (ArrayList<?>) dataFromResolver.get("results");
				JSONObject finalJson = new JSONObject();
				JSONArray arrayJson = new JSONArray();
				for(Object obj1 : list)
				{
					JSONObject json = new JSONObject();
					JSONObject jsonObj = new JSONObject(obj1.toString());
					for(Attribute attribute : attributes)
					{
						if(jsonObj.has(attribute.getName()))
							json.put(attribute.getMappingattribute(), jsonObj.get(attribute.getName()));
						else
							json.put(attribute.getMappingattribute(), "");
					}
					arrayJson.put(json);
/*					jsonObj.get()
					Iterator<String> jsonKeys =jsonObj.keys();
					while(jsonKeys.hasNext())
					{
						
						arrayJson.put(jsonKeys.next());
					}
*/				}
				finalJson.put("results", arrayJson);
				responseMap = context.toMap(finalJson);
			}
			else
			{
			
				for (Attribute attribute : attributes) {
					if(attribute==null) {
						break;
					}
	
					responseMap.put(attribute.getMappingattribute(), dataFromResolver.get(attribute.getName()));
				}
			}
			
			/*Setting response to context*/
			
			context.setResponseMap(responseMap);
		} catch (Exception e) {
			//System.out.println("sdfsd"+e);
		}
	}
}
