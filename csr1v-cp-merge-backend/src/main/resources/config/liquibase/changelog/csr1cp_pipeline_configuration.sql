CREATE TABLE public.csr1vcp_pipeline_configuration(mso_name varchar(250),pileline_mso_configuration text,PRIMARY KEY (mso_name));

INSERT INTO public.csr1vcp_pipeline_configuration(mso_name,pileline_mso_configuration) VALUES ('customer','{ "mso": "customer", "criteriaParameters": {"bano" : "business_partner_number","cano":"connection_contract_number"},
   "criteriaClause" : "And","attributes":[   {"name":"businessPartnerNumber", "type":"string",  "mappingattribute":"businessPartnerNumber",     "resolver":"customerPipelineResolver"   },   {"name":"fullname", "type":"string",   "mappingattribute":"fullName",     "resolver":"customerPipelineResolver"   },   {"name":"premiseId", "type":"string",   "mappingattribute":"premiseId",     "resolver":"customerPipelineResolver"   },   {"name":"premiseType", "type":"string",   "mappingattribute":"premiseType",     "resolver":"customerPipelineResolver"   },   {"name":"premiseAddress", "type":"string",   "mappingattribute":"premiseAddress",     "resolver":"customerPipelineResolver"   },   {"name":"premiseCity", "type":"string",   "mappingattribute":"premiseCity",     "resolver":"customerPipelineResolver"   },   {"name":"premiseState", "type":"string",   "mappingattribute":"premiseState",     "resolver":"customerPipelineResolver"   },   {"name":"premiseCountry", "type":"string",   "mappingattribute":"premiseCountry",     "resolver":"customerPipelineResolver"   },   {"name":"premiseZip", "type":"string",   "mappingattribute":"premiseCountry",     "resolver":"customerPipelineResolver"   },   {"name":"emailAddress", "type":"string",   "mappingattribute":"emailAddress",     "resolver":"customerPipelineResolver"   },   {"name":"mobileNumber", "type":"string",   "mappingattribute":"mobileNumber",     "resolver":"customerPipelineResolver"   },   {"name":"customerPeriod", "type":"string",   "mappingattribute":"customerPeriod",     "resolver":"customerPipelineResolver"   },   {"name":"waterService", "type":"string",   "mappingattribute":"waterService",     "resolver":"customerPipelineResolver"   },   {"name":"sewerService", "type":"string",   "mappingattribute":"sewerService",     "resolver":"customerPipelineResolver"   },   {"name":"fireService", "type":"string",   "mappingattribute":"fireService",     "resolver":"customerPipelineResolver"   },{"name":"meterLocation", "type":"string",   "mappingattribute":"meterLocation",     "resolver":"customerPipelineResolver"   }, {"name":"meterSize", "type":"string",   "mappingattribute":"meterSize",     "resolver":"customerPipelineResolver"   },{"name":"installationDate", "type":"string",   "mappingattribute":"installationDate",     "resolver":"customerPipelineResolver"   },{"name":"serialNumber", "type":"string",   "mappingattribute":"serialNumber",     "resolver":"customerPipelineResolver"   },   {"name":"customerStatus", "type":"string",  "mappingattribute":"customerStatus",     "resolver":"customerApiResolver"   },   {"name":"cashOnly", "type":"string",  "mappingattribute":"cashOnly",     "resolver":"customerApiResolver"   },   {"name":"creditWorthiness", "type":"string",  "mappingattribute":"creditWorthiness",     "resolver":"customerApiResolver"   },   {"name":"collectiveenrollStatus", "type":"string",  "mappingattribute":"collectiveenrollStatus",     "resolver":"customerApiResolver"   },   {"name":"budgetenrollStatus", "type":"string",  "mappingattribute":"budgetenrollStatus",     "resolver":"customerApiResolver"   },   {"name":"paperlessStatements", "type":"string",  "mappingattribute":"paperlessStatements",     "resolver":"customerApiResolver"   },   {"name":"riskLevelExplanation", "type":"string",  "mappingattribute":"riskLevelExplanation",     "resolver":"customerApiResolver"   },   {"name":"premiseType", "type":"string",  "mappingattribute":"premiseType",     "resolver":"customerApiResolver"   },   {"name":"taxJurisdictionCode", "type":"string",  "mappingattribute":"taxJurisdictionCode",     "resolver":"customerApiResolver"   },   {"name":"customerStatus", "type":"string",  "mappingattribute":"customerStatus",     "resolver":"customerApiResolver"   } ], "resolvers":[   {"name":"customerApiResolver", "type":"sapapi",   "url":"https://api-dev.amwaternp.com/api/sap-ecc-gateway-apis/v1/account/account-credit-score/"   },   {"name":"customerPipelineResolver", "type":"pipeline",   "url":"com::amwater::CSR1VCPBACKEND::sidebarpipeline"   }    ] }');

INSERT INTO public.csr1vcp_pipeline_configuration(mso_name,pileline_mso_configuration) VALUES ('billing','{  "mso": "billing",  "criteriaParameters": {"bano" : "business_partner_number","cano":"connection_contract_number","billmonth":"bill_month"},
   "criteriaClause" : "And","attributes":[    {"name":"businessPartnerNumber", "type":"string",  "mappingattribute":"businessPartnerNumber",     "resolver":"customerPipelineResolver"   },   {"name":"connectionContractNumber", "type":"string",  "mappingattribute":"connectionContractNumber",     "resolver":"customerPipelineResolver"   },   {"name":"totalDueAmount", "type":"string",  "mappingattribute":"currentBill",     "resolver":"customerPipelineResolver"   },   {"name":"consumption", "type":"string",  "mappingattribute":"currentUsage",     "resolver":"customerPipelineResolver"   },   {"name":"prevBalance", "type":"string",   "mappingattribute":"prevMonthBalance",     "resolver":"customerPipelineResolver"   },   {"name":"prevUsage", "type":"string",   "mappingattribute":"prevMonthUsage",     "resolver":"customerPipelineResolver"   },   {"name":"prevYearBalance", "type":"string",   "mappingattribute":"prevYearBalance",     "resolver":"customerPipelineResolver"   },   {"name":"prevYearConsumption", "type":"string",   "mappingattribute":"prevYearUsage",     "resolver":"customerPipelineResolver"   },   {"name":"yearAvg", "type":"string",   "mappingattribute":"yearAvg",     "resolver":"customerPipelineResolver"   },   {"name":"yearAvgUsage", "type":"string",   "mappingattribute":"yearAvgUsage",     "resolver":"customerPipelineResolver"   }  ],  "resolvers":[    {"name":"customerPipelineResolver", "type":"pipeline", 	"url":"com::amwater::CSR1VCPBACKEND::UsageQuickViewPipeline"}] }');

INSERT INTO public.csr1vcp_pipeline_configuration(mso_name,pileline_mso_configuration) VALUES ('servicehistory','{  "mso": "servicehistory", "criteriaParameters": {"bano" : "business_partner_number","cano":"connection_contract_number"},
   "criteriaClause" : "And", "attributes":[    {"name":"businessPartnerNumber", "type":"string",  "mappingattribute":"businessPartnerNumber",     "resolver":"customerPipelineResolver"   },   {"name":"connectionContractNumber", "type":"string",  "mappingattribute":"connectionContractNumber",     "resolver":"customerPipelineResolver"   },   {"name":"serviceOrder", "type":"string",  "mappingattribute":"serviceOrderNumber",     "resolver":"customerPipelineResolver"   },   {"name":"soCode", "type":"string",  "mappingattribute":"soCode",     "resolver":"customerPipelineResolver"   },   {"name":"description", "type":"string",   "mappingattribute":"description",     "resolver":"customerPipelineResolver"   },   {"name":"createdDate", "type":"string",   "mappingattribute":"createdDate",     "resolver":"customerPipelineResolver"   },   {"name":"status", "type":"string",   "mappingattribute":"status",     "resolver":"customerPipelineResolver"   },   {"name":"serialNumber", "type":"string",   "mappingattribute":"serialNumber",     "resolver":"customerPipelineResolver"   }  ],  "resolvers":[    {"name":"customerPipelineResolver", "type":"pipeline",     "url":"com::amwater::CSR1VCPBACKEND::viewfullservicehistorypipeline"}] }');