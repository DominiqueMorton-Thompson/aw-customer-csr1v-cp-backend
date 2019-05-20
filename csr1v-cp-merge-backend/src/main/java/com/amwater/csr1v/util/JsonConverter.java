package com.amwater.csr1v.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

public class JsonConverter {

								
	public Map<String, Object> getJsonMap(String json) throws JsonProcessingException, IOException {
	   Map<String, Object> map = new HashMap<String, Object>();
	   addKeys("", new ObjectMapper().readTree(json), map);
	   return map;
	 }
	
	private void addKeys(String currentPath, JsonNode jsonNode, Map<String, Object> map) {
	    if (jsonNode.isObject()) {
	      ObjectNode objectNode = (ObjectNode) jsonNode;
	      Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
	      String pathPrefix = currentPath.isEmpty() ? "" : currentPath + ".";

	      while (iter.hasNext()) {
	        Map.Entry<String, JsonNode> entry = iter.next();
	        addKeys(pathPrefix + entry.getKey(), entry.getValue(), map);
	      }
	    } else if (jsonNode.isArray()) {
	      ArrayNode arrayNode = (ArrayNode) jsonNode;
	      for (int i = 0; i < arrayNode.size(); i++) {
	        addKeys(currentPath + "[" + i + "]", arrayNode.get(i), map);
	      }
	    } else if (jsonNode.isValueNode()) {
	      ValueNode valueNode = (ValueNode) jsonNode;
	      map.put(currentPath, valueNode.asText());
	    }
	  }
}
