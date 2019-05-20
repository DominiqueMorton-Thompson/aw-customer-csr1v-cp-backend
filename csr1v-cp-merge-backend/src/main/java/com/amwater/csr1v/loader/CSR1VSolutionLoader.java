package com.amwater.csr1v.loader;

import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.amwater.csr1v.ui.builder.CSR1VSolutionBuilder;
import com.apporchid.vulcanux.config.loader.BaseSolutionLoader;

@Profile("!solution-dev")
@Component
@DependsOn(value = { "VuxCacheLoader" })
public class CSR1VSolutionLoader extends BaseSolutionLoader<CSR1VSolutionBuilder> {

	@Override
	protected Class<CSR1VSolutionBuilder> getSolutionBuilderType() {
		return CSR1VSolutionBuilder.class;
	}

	@Override
	protected boolean isReloadOnServerStartup() {
		return false;
	}

}
