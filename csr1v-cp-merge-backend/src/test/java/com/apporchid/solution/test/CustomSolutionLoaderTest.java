package com.apporchid.solution.test;

import org.testng.annotations.Test;

import com.amwater.csr1v.pipeline.builder.CSR1VPipelineBuilder;
import com.apporchid.solution.common.BaseSolutionTestApplication;

public class CustomSolutionLoaderTest extends BaseSolutionTestApplication {

	@Test(groups = { "solutionMain" })
	@Override
	public void setupSolution() {
		//deploy everything
		//deploy(SamplesSolutionBuilder.class);

		//deploy only pipelines
		deploy(CSR1VPipelineBuilder.class);

		//deploy only applications
		//deploy(TableAppsBuilder.class);

		//run a pipeline 
		//runPipeline("SmallDataTablePipeline");
	}

	@Override
	protected String getSolutionPackage() {
		return "com.amwater.csr1v";
	}
}
