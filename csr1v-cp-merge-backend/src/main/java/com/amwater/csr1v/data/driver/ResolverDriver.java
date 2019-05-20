package com.amwater.csr1v.data.driver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.amwater.csr1v.data.config.model.Resolver;
import com.amwater.csr1v.data.context.Context;
import com.amwater.csr1v.data.resolver.BaseResolver;
import com.amwater.csr1v.data.resolver.ResolverFactory;

@Component
public class ResolverDriver {
	public BaseResolver baseResolver = null;
	@Inject
	ResolverFactory resolverFactory;
	Map<String,Object> response = new HashMap();
	
	public void getData(Context context) {
		List<Resolver> resolvers = context.getDataConfig().getResolvers();
		for (Resolver resolver:resolvers) {
			baseResolver = resolverFactory.getResolverInstance(resolver.getType());
			baseResolver.getData(context,resolver.getUrl());
		}
	}
}
