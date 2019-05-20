package com.amwater.csr1v.data.rest;

import java.util.Map;

import javax.inject.Inject;

import org.json.JSONObject;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amwater.csr1v.data.context.Context;
import com.amwater.csr1v.data.driver.AttributeDriver;
import com.amwater.csr1v.data.driver.ResolverDriver;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("csr1vcp")
@Profile("!solution-dev")
public class CsrCpRestController{

	@Inject
	private ResolverDriver resolverDriver;
	
	@Inject 
	private AttributeDriver attributeDriver;
	
	@GetMapping(path = "/csrcpcustomerData", consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ApiOperation(value = "apirequest", tags = "CSR1V", notes = "retrives customer data from cloudseer and sap")
	@ResponseBody
	public Object getCustomerData(@RequestParam Map<String, String> requestJson)
	{
		Context context =null;
		Map<String,Object> responseMap = null;
		if(requestJson.containsKey("msoName"))
		{	
			
			try
			{
				context = new Context(requestJson.get("msoName"));
				JSONObject requestJsonObj = new JSONObject(requestJson);
				requestJsonObj.remove("msoName");
				if(requestJsonObj.has("limitRecords"))
				{
					context.setLimitRecords(Integer.parseInt(requestJson.get("limitRecords")));
					requestJsonObj.remove("limitRecords");
				}
				if(context.validateRequestParameters(requestJsonObj) && context.getRequestErrorObject().isEmpty())
				{	
					context.buildCriteria(requestJsonObj);
					resolverDriver.getData(context);
					attributeDriver.attributeMapper(context);
					responseMap = context.getResponseMap();
				}
				else
				{
					responseMap = context.getRequestErrorObject();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		else
		{
			return  new ResponseEntity<>("msoName parameter is required as query Parameter (ex: msoName=customer)",HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(responseMap,HttpStatus.OK);
	}
	
}
