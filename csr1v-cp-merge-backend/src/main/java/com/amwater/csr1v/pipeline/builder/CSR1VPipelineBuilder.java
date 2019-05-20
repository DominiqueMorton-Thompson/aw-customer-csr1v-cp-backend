package com.amwater.csr1v.pipeline.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.amwater.csr1v.constants.ICSR1VPipelineConstants;
import com.apporchid.cloudseer.common.pipeline.tasktype.DatasinkTaskType;
import com.apporchid.cloudseer.common.pipeline.tasktype.DatasourceTaskType;
import com.apporchid.cloudseer.common.pipeline.tasktype.TransformerTaskType;
import com.apporchid.cloudseer.common.transformer.properties.TransformerProperties;
import com.apporchid.cloudseer.config.builder.BasePipelineConfigurationBuilder;
import com.apporchid.cloudseer.datasource.db.RelationalDBDatasource;
import com.apporchid.cloudseer.datasource.db.RelationalDBDatasourceProperties;
import com.apporchid.cloudseer.datasource.rest.RestDatasource;
import com.apporchid.cloudseer.datasource.rest.RestDatasourceProperties;
import com.apporchid.common.Variables;
import com.apporchid.common.enums.EHttpRequestMethod;
import com.apporchid.criteria.AndCriteria;
import com.apporchid.criteria.StringCriteria;
import com.apporchid.foundation.common.IVariables;
import com.apporchid.foundation.criteria.ICriteria;
import com.apporchid.foundation.criteria.operator.EStringOperator;
import com.apporchid.foundation.pipeline.IPipeline;
import com.apporchid.foundation.pipeline.tasktype.ITaskType;

@Component
public class CSR1VPipelineBuilder extends BasePipelineConfigurationBuilder implements ICSR1VPipelineConstants {

	public CSR1VPipelineBuilder() {
		super(DEFAULT_DOMAIN_ID, DEFAULT_SUB_DOMAIN_ID);
	}

	@Override
	protected List<IPipeline> getPipelines() {

		List<IPipeline> pipelinesList = new ArrayList<>();
		
		pipelinesList.add(createPipeline("BillingSummaryTemplatePipeline",getBillSummaryDBDatasourceTasks(BILL_SUMMARY_QUERY,true) ));
		pipelinesList.add(createPipeline("BllBreakdownTemplatePipeline", getDBDatasourceTasks(
				BILL_BREAKDOWN_QUERY,
				true)));
		pipelinesList.add(createPipeline("UsageQuickViewPipeline", getDBDatasourceTasks(
				USAGE_QUICKVIEW_QUERY,
				true)));
		
		pipelinesList.add(createPipeline("ConsumptionTemplatePipeline", getDBDatasourceTasks(
				CONSUMPTION_QUERY,
				true)));
		pipelinesList.add(createPipeline("ServiceTemplatePipeline", getExcelTasks("servicehistory_template_data")));
		pipelinesList.add(createPipeline("sidebarpipeline", getCustomerInfoTasks(CUSTOMER_LANDING_PAGE_QUERY, true)));
		pipelinesList.add(createPipeline("billmonthspipeline", getDBDatasourceTasks(BILL_MONTHS_QUERY, true)));
		
		//limit Service History LandingPage records to 3
		Map<String, Object> addlProps = new HashMap<>();
        addlProps.put("limit", 3);

        pipelinesList.add(createPipeline(AUTOPAY_CONTRACT_NUMBERS_PIPELINE, getDBDatasourceTasks(AUTOPAY_CONTRACT_NUMBERS_QUERY,true)));
        pipelinesList.add(createPipeline(AUTOPAY_LOCK_REASONS_PIPELINE, getDBDatasourceTasks(LOCK_REASONS_AUTO_PAY,false)));
        pipelinesList.add(createPipeline(SERVICE_HISTORY_PIPELINE,  dbTaskBuilderHelper().getDBDatasourceTasks(DEFAULT_SQL_DATASOURCE_NAME,SERVICE_HISTORY_QUERY, true,false,addlProps)));
		pipelinesList.add(createPipeline(FULL_BILLING_HISTORY_PIPELINE, getDBDatasourceTasks(PREVIOUS_INVOICES_QUERY, true)));
		pipelinesList.add(createPipeline(CONNECTION_CONTRACTS_PIPELINE, getDBDatasourceTasks(CONNECTION_CONTRACTS_QUERY, true)));
		pipelinesList.add(createPipeline(VIEW_FULL_SERVICE_HISTORY_PIPELINE, getDBDatasourceTasks(VIEW_FULL_SERVICE_HISTORY_QUERY,true)));
		pipelinesList.add(createPipeline(AUTOPAY_SELECT_BANK_ACC_PIPELINE, RestDataPipeline(AUTO_PAY_bANK_DETAILS_REST_API,"BankDetailsUrl")));
		pipelinesList.add(createPipeline(BUDGET_BILLING_PIPELINE, RestDataPipeline(BUDGET_BILLING_REST_API,"BBUrl")));
		
	    pipelinesList.add(createPipeline("UsageOverviewPipeline", getDBDatasourceTasks(USAGE_OVERVIEW_24HOURS_QUERY, false)));
	    
	    /*
	     * Service Order Pipelines
	     * */
	    pipelinesList.add(createPipeline(SERVICE_ORDER_TYPE_PIPELINE, getDBDatasourceTasks(SERVICE_ORDER_TYPE_QUERY,false)));
	    pipelinesList.add(createPipeline(SERVICE_ORDER_CODE_PIPELINE, getDBDatasourceTasks(SERVICE_ORDER_CODE_QUERY,true)));

		return pipelinesList;
	}
	
	protected ITaskType<?, ?>[] RestDataPipeline(String defaultRestUrl,String restUrl) {
        DatasourceTaskType task1;
       task1 = new DatasourceTaskType.Builder().name("Read data from web api")
                .datasourceType(RestDatasource.class).datasourcePropertiesType(RestDatasourceProperties.class)
               .requestParameterProperty(RestDatasourceProperties.EProperty.urlPath.name(),restUrl,defaultRestUrl)
                .property(RestDatasourceProperties.EProperty.method.name(), EHttpRequestMethod.GET)
                //.property(RestDatasourceProperties.EProperty.tokenConfigName.name(), "c1v_test")
                .build();
             
              DatasinkTaskType task3 = taskBuilderHelper().getOutputDataSinkTask();

         return new ITaskType<?, ?>[] { task1, task3 };
     }

	private ITaskType<?, ?>[] getExcelTasks(String fileName) {
		return fileTaskBuilderHelper().getExcelTasks(DATA_PATH + "/excel/" + fileName + ".xlsx", false);
	}
	
	/*public ITaskType<?, ?>[] getESWaterSystemDataViewTasks() {
		return esTaskBuilderHelper().getElasticsearchTasks(DEFAULT_ES_LOCAL_CLUSTER, DEFAULT_ES_LOCAL_INDEX, DEFAULT_ES_LOCAL_TYPE,
				new String[] { "pwsid", "pwsName", "addressLine1", "cityName" }, "<span style=\"background-color:yellow\">", "</span>", true);
	}*/
	
	public ITaskType<?, ?>[] getCustomerInfoTasks(String query, boolean enableCriteria) {
		ICriteria<?> likeCriteria = new StringCriteria("business_partner_number", EStringOperator.Equals, "1200249011");
        ICriteria<?> nameCriteria= new StringCriteria("connection_contract_number",EStringOperator.Equals,"220002218724");
        
        AndCriteria ac1 = new AndCriteria();
        ac1.addCriteria(likeCriteria);
        ac1.addCriteria(nameCriteria);
		DatasourceTaskType.Builder taskBuilder = new DatasourceTaskType.Builder() .name("Read data")
				.datasourceType(RelationalDBDatasource.class)
				.defaultCriteria(ac1)
				//.datasourceType(CSR1VRelationalDBDatasource.class)
				.datasourcePropertiesType(RelationalDBDatasourceProperties.class)
				.property(RelationalDBDatasourceProperties.EProperty.sqlDatasourceName.name(), DEFAULT_SQL_DATASOURCE_NAME)
				.property(RelationalDBDatasourceProperties.EProperty.sqlQuery.name(), query);
		
		if(enableCriteria) {
			taskBuilder.requestParameterProperty(RelationalDBDatasourceProperties.EProperty.criteria.name(), "criteria");
		}
		DatasourceTaskType task1 = taskBuilder.build();
		

		DatasinkTaskType task3 = taskBuilderHelper().getOutputDataSinkTask();

		return new ITaskType<?, ?>[] { task1, task3 };
	}
	
	public ITaskType<?, ?>[] getDBDatasourceTasks(String query, boolean withCriteria) {
		ITaskType<?, ?>[] task1 = dbTaskBuilderHelper().getDBDatasourceTasks(DEFAULT_SQL_DATASOURCE_NAME, query, withCriteria);
		return task1;
	}
	
	public ITaskType<?, ?>[] getBillSummaryDBDatasourceTasks(String query, boolean enableCriteria) {
		
		DatasourceTaskType task1 =getDataSourceTask(query,enableCriteria);
		
//		TransformerTaskType task2=new TransformerTaskType.Builder().name("Transform Past Due Date  Data")
//				.transformerType(BillSummaryTransformer.class)
//				.transformerPropertiesType(TransformerProperties.class)
//				.build();
		
		DatasinkTaskType task3 = taskBuilderHelper().getOutputDataSinkTask();

		return new ITaskType<?, ?>[] { task1, task3 };
	}
	public DatasourceTaskType getDataSourceTask(String query,boolean enableCriteria) {
		DatasourceTaskType.Builder taskBuilder = new DatasourceTaskType.Builder() .name("Read data")
				.datasourceType(RelationalDBDatasource.class)
				//.datasourceType(CSR1VRelationalDBDatasource.class)
				.datasourcePropertiesType(RelationalDBDatasourceProperties.class)
				.property(RelationalDBDatasourceProperties.EProperty.sqlDatasourceName.name(), DEFAULT_SQL_DATASOURCE_NAME)
				.property(RelationalDBDatasourceProperties.EProperty.sqlQuery.name(), query);
		if(enableCriteria) {
			taskBuilder.requestParameterProperty(RelationalDBDatasourceProperties.EProperty.criteria.name(), "criteria");
		}
		DatasourceTaskType task1 = taskBuilder.build();
		return task1;
	}
	
	@Override
    protected String[] getPipelinesToRun() {
        return new String[] { getSystemPipelineId(PIPELINE_NAME_EXCEL_BASED_MUTLI_MSO_CREATOR),getId("") };
    }
	
	@Override
    protected IVariables getInputVariables(String pipelineName, int index) {
        if (index == 0) {
            IVariables inputVariables = new Variables();
            inputVariables.add("msoDirectoryPath", "/csr1/mso/customMSO.xlsx");
            return inputVariables;
        }
        return null;
	}

}
