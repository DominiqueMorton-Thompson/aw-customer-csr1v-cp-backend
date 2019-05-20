package com.amwater.csr1v.data.resolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amwater.csr1v.data.context.Context;
import com.apporchid.common.AOContextHolder;
import com.apporchid.common.UIRequestParameters;
import com.apporchid.core.common.cache.PipelineCache;
import com.apporchid.domain.cloudseer.pipeline.PipelineModel;
import com.apporchid.foundation.pipeline.IPipeline;
import com.apporchid.foundation.pipeline.IPipelineOutput;
import com.apporchid.foundation.pipeline.IPipelineResult;
import com.apporchid.foundation.ui.IRequestParameters;
import com.apporchid.service.cloudseer.pipeline.PipelineCrudService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Component(value = "pipelineResolver")
public class PipelineResolver implements BaseResolver{
	
	@Autowired(required=false)
	private PipelineCrudService pipelineCrudService;
	
	
	
	@Override
	public void getData(Context context,String pipelineId) {
		Map<String, Object> pipelineResultMap=null;
		IRequestParameters params = new UIRequestParameters();
		if (context.getCriteria() != null) {
			params.setCriteria(context.getCriteria());
		}

		IPipelineResult pipelineResult = runPipeline(pipelineId, params,context.getLimitRecords());
		Object outputObject = pipelineResult.getOutputObject();
		ObjectMapper objMapper = new ObjectMapper();
		if(outputObject instanceof List )
		{
			Gson gson = new Gson();
			JSONObject finalJson = new JSONObject();
			JSONArray errorJson = new JSONArray();
			ArrayList list = (ArrayList)outputObject;
			for(Object object : list)
			{
            	errorJson.put(gson.toJson(object));
				//outputObject = list.get(0);
			}
			finalJson.put("results", errorJson);
			pipelineResultMap=context.toMap(finalJson);
		}
		else
		{	
			pipelineResultMap=objMapper.convertValue(outputObject, Map.class);
		}
		Map<String, Object> contextData = context.getData();
		if(pipelineResultMap!=null && !pipelineResultMap.isEmpty())
			contextData.putAll(pipelineResultMap);	
	}
	
	private IPipelineResult runPipeline(String pipelineId,IRequestParameters params,int limitRecords) {
		IPipeline pipeline = null;
		if (PipelineCache.INSTANCE.containsByName(pipelineId)) {
			pipeline = PipelineCache.INSTANCE.getByName(pipelineId);
		} else {
			Optional<PipelineModel> optPipeline = pipelineCrudService.findById(pipelineId);
			if (optPipeline.isPresent()) {
				PipelineModel pipelineModel = optPipeline.get();
				pipeline = pipelineModel.getPipeline();
			}
		}
		if (pipeline != null) {
			AOContextHolder.getContext().setRequestParameters(params);
			try {
				
				if (limitRecords > 0) {
					pipeline.getPipelineOptions().fetchRecords(limitRecords);
				}
				return pipeline.run();
			} catch (Exception e) {
			}
		}
		return null;
	}
}
