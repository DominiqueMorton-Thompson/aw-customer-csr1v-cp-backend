package com.amwater.csr1v.data.resolver;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ResolverFactory {
	
	@Inject
	@Qualifier(value="sapAPIResolver")
	private SAPAPIResolver sapAPIResolver;
	
	@Inject
	@Qualifier(value="pipelineResolver")
	private PipelineResolver pipelineResolver;
	
	
	public BaseResolver getResolverInstance(String type) {
		BaseResolver baseResolver;
		switch(type) {
		case "sapapi":
			baseResolver = sapAPIResolver;
			break;
		case "pipeline":
			baseResolver = pipelineResolver;
			break;
		default:
			baseResolver = sapAPIResolver;
		}
		return baseResolver;
	}
}
