package com.amwater.csr1v.ui.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.amwater.csr1v.constants.ICSR1VSolutionConstants;
import com.amwater.csr1v.pipeline.builder.CSR1VPipelineBuilder;
import com.apporchid.config.builder.BaseConfigurationBuilder;
import com.apporchid.foundation.ui.config.solution.ISolutionConfig;
import com.apporchid.foundation.ui.config.solution.ISolutionHeaderConfig;
import com.apporchid.foundation.ui.config.solution.ISolutionPageConfig;
import com.apporchid.vulcanux.config.builder.BaseSolutionConfigurationBuilder;
import com.apporchid.vulcanux.ui.config.solution.SolutionHeaderConfig;

@Component
public class CSR1VSolutionBuilder extends BaseSolutionConfigurationBuilder implements ICSR1VSolutionConstants {

	public CSR1VSolutionBuilder() {
		super(DEFAULT_DOMAIN_ID, DEFAULT_SUB_DOMAIN_ID);
	}

	@Override
	protected List<Class<? extends BaseConfigurationBuilder<?>>> getDependentConfigBuilders() {
		List<Class<? extends BaseConfigurationBuilder<?>>> builders = new ArrayList<>();

		
		
		builders.add(CSR1VPipelineBuilder.class);
		//builders.add(CreateInstallmentPlanAppBuilder.class);

		return builders;
	}

	@Override
	protected ISolutionConfig getSolution() {
		List<ISolutionPageConfig> solutionPages = new ArrayList<>();

		
		ISolutionHeaderConfig solutionHeaderConfig = new SolutionHeaderConfig.Builder()
				.withDisplayName(SOLUTION_NAME).withCustomLogoPath(ICSR1VSolutionConstants.SOLUTION_LOGO)
				.withCssClass("csr1v-sol").build();

		return createSolution(SOLUTION_ID, SOLUTION_NAME, false, true, solutionPages, solutionHeaderConfig, SOLUTION_ICON);
	}
//	@Override
//	protected String getAppIdAsContainerComponent() {
//		return getId("containerapp");
//	}
	@Override
	public String getSolutionId() {
		return getId(SOLUTION_ID);
	}

	@Override
	protected int getApplicationHeight(String solutionPageId) {
		return 500;
	}

	@Override
	protected int getApplicationWidth(String solutionPageId) {
		return 400;
	}

	protected String[] getLandigPageApps() {
		return new String[] { };
	}

	@Override
	protected String[] getJsFiles() {

		return new String[] {};
	}

	@Override
	protected String[] getCssFiles() {
		return new String[] {};
	}
	
	@Override
    protected Boolean isContainerComponentRequired() {
        return Boolean.FALSE;
    }

}
