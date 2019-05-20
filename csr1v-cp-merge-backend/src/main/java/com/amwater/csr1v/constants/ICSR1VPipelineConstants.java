package com.amwater.csr1v.constants;

public interface ICSR1VPipelineConstants extends ICSR1VCommonConstants {
	/*public static final String DEFAULT_ES_LOCAL_CLUSTER = "cloudseer-analytics";
	public static final String DEFAULT_ES_LOCAL_INDEX = "sample-indexes";
	public static final String DEFAULT_ES_LOCAL_TYPE = "watersystem";*/
	
	public static final String DEFAULT_ES_CLUSTER_NAME ="elasticsearch";
	public static final String DEFAULT_ES_INDEX_NAME ="newbusinesspartner3";
	public static final String DEFAULT_ES_TYPE_NAME ="profile";
	
	public static final String SEARCHRESULTS_PIPELINE_ID = "Search Results";
	
	public static final String DUMMY_PIPELINE_ID = "Dummy Id";
	
	public static final String DEFAULT_SQL_DATASOURCE_NAME = "csr1vcpbackendDB";

	//Pipeline Ids
	public static final String SERVICE_HISTORY_PIPELINE = "servicehistorypipeline";
	public static final String FULL_BILLING_HISTORY_PIPELINE = "fullbillinghistorypipeline";
	public static final String CONNECTION_CONTRACTS_PIPELINE = "connectioncontractspipeline";
	public static final String VIEW_FULL_SERVICE_HISTORY_PIPELINE = "viewfullservicehistorypipeline";
	public static final String AUTOPAY_SELECT_BANK_ACC_PIPELINE = "autopayselectbankaccountpipeline";
	public static final String BUDGET_BILLING_PIPELINE = "budgetbillingpipeline";
	public static final String AUTO_PAY_SUCCESS_PAGE_PIPELINE="autopaysuccesspagepipeline";
	public static final String AUTOPAY_CONTRACT_NUMBERS_PIPELINE = "autopaypipelineforca";
	public static final String AUTOPAY_LOCK_REASONS_PIPELINE = "autopaypipelineforca";
	
	// Service order pipelines
	
	public static final String SERVICE_ORDER_TYPE_PIPELINE = "serviceordertypepipeline";
	public static final String SERVICE_ORDER_CODE_PIPELINE = "serviceordercodepipeline";
	
	//SQL Queries
	public static final String AUTOPAY_FLAG_UPDATE ="insert into sapupdates.connection_contracts_flags(connection_contract_number,autopayflag) values(?,?) ON CONFLICT (connection_contract_number) DO UPDATE SET autopayflag = EXCLUDED.autopayflag";
	
	public static final String SAPAPI_RESPONSE_INSERT ="insert into sapupdates.SAPApiResponses(url,request,response) values(?,?,?)";
	
	public static final String CUSTOMER_LANDING_PAGE_QUERY ="select distinct on (premise_id) premise_id, business_partner_number,fullname,premise_type,premise_address,premise_city, cash_only, premise_state,premise_country,premise_zip,customer_period,move_in,move_out,security_pin,contract_acc_number,water_service,fire_service,sewer_service,connection_count,customer_satisfaction,satisfaction_index_fsr,satisfaction_index_fsr_reason,customer_status,premise_connection_status, account_status,disconnect_reason,disconnecton_status,credit_worthiness,risk_level_explanation,budgetEnroll_Status,collectiveEnroll_Status,installmentEnroll_Status,home_number,mobile_number,email_address,preferred_contact_method,paperless_statements,autopay,billprint_format,mailing_address,mailing_city,mailing_state,mailing_country,mailing_zip,water_hos_flag,fire_hos_flag,sewer_hos_flag,low_income_flag,autopayflag,auto_payment_preferred_due_data,tax_jurisdiction_code,meter_location,meter_size,installation_date,serial_number from app.customer_connection_contracts";
	
	public static final String BILL_BREAKDOWN_QUERY = "SELECT * FROM (SELECT  b.business_partner_number,b.connection_contract_number, b.premise_id,b.bill_id, Concat(c.premise_address, '  ', c.premise_city, ', ', c.premise_state, ' ', c.premise_zip) AS premise_address, b.premise_id AS for_account,To_char(b.due_date :: DATE, 'MM-dd-yyyy') AS due_date, Round(Cast(Coalesce(bs.prev_balance,0) AS NUMERIC),2) AS prior_balance,'0.00'AS protection_charages, b.bill_month,bs.prev_bill_month,concat(TO_CHAR(b.start_bill_period :: DATE, 'Month dd'),' -',TO_CHAR(b.end_bill_period :: DATE, 'Month dd'),' (',days_in_bill_cycle,' days',')') billing_period,To_char(payments.payment_Date :: DATE, 'mm/dd')  AS as_of_payment_date,COALESCE(bs.prev_bill_month, 'NA'::character varying) AS prev_bill_exists, Round(Coalesce(payments.payment_amount,0)::Numeric,2) AS payment_amount,Round(Cast(Coalesce(b.tax,0) As Numeric),2) AS tax, Round(Cast(Coalesce(b.balance_forward, 0) AS NUMERIC), 2) AS balance_forward, Round(Coalesce(b.water_service_charge, 0)::Numeric,2) AS water_service_charge,Round(Cast(( ( CASE WHEN b.water_service_charge IS NULL THEN 0  ELSE b.water_service_charge END ) / b.consumption )AS NUMERIC), 8)  AS rate_per_gallon, Coalesce(b.consumption, 0)*100 AS consumption,  0.00 AS water_usage_charge, Round(Cast(Coalesce(b.other_charges, 0)  + Coalesce(b.waste_water_service_charge, 0)  + Coalesce(b.fire_service_charge, 0) AS NUMERIC), 2) other_charges, Coalesce(b.final_balance, 0) AS total_due_amount, Round(Coalesce(bs.final_balance, 0)::numeric,2) AS final_balance, Round(Coalesce(bs.prev_balance, 0)::numeric,2) AS prev_balance, Coalesce(bs.prev_usage, 0) * 100 AS prev_usage, Round(Cast(Abs(Coalesce(bs.year_avg, 0)) AS NUMERIC), 2) AS year_avg, Round(Coalesce(bs.year_avg_usage, 0) * 100, 0) AS year_avg_usage, Round(Cast(Abs(Coalesce(bs.navg, 0)) AS NUMERIC), 2) navg, Round(Coalesce(bs.navg_usage, 0) * 100, 0)  AS navg_usage, Round(Coalesce(bs.prev_year_consumption, 0) * 100, 0)  AS prev_year_consumption, Round(Cast(Abs(Coalesce(bs.prev_year_balance, 0))AS NUMERIC), 2) AS prev_year_balance, b.statement_date  FROM   app.bills b         left outer join app.bills_summary_all bs ON b.business_partner_number = bs.business_partner_number AND b.connection_contract_number = bs.connection_contract_number  AND b.bill_month = bs.bill_month  left outer join app.connection_contracts c ON bs.business_partner_number = c.business_partner_number  AND bs.connection_contract_number = c.connection_contract_number     left outer join (                 select  s.business_partner_number , s.connection_contract_number, c.ebill_flag,c.budget_bill_flag , min(p.due_date) as due_date ,   p.payment_month,                 max(p.payment_date) as payment_date  , max(p.bill_id) as bill_id ,                 sum(p.payment_amount)  as payment_amount , sum(p.clearing_amount) as clearing_amount                    FROM    app.customers c  , app.payments  p , app.bills_summary_all s                 where  c.business_partner_number =  p.business_partner_number                 and p.clearing_amount is not null                  and s.business_partner_number = p.business_partner_number                 and s.connection_contract_number = p.connection_contract_number                 and s.bill_month= p.payment_month                 group by s.business_partner_number , s.connection_contract_number, p.payment_month, p.due_date,c.ebill_flag,c.budget_bill_flag              ) AS payments ON payments.business_partner_number = b.business_partner_number and payments.connection_contract_number = b.connection_contract_number and payments.payment_month=bs.bill_month) invoice order by invoice.statement_date desc";
	
	public static final String USAGE_QUICKVIEW_QUERY = "SELECT * FROM (SELECT  distinct(b.business_partner_number),b.connection_contract_number, b.premise_id,b.bill_id, Concat(c.premise_address, '  ', c.premise_city, ', ', c.premise_state, ' ', c.premise_zip) AS premise_address, b.premise_id AS for_account,To_char(b.due_date :: DATE, 'MM-dd-yyyy') AS due_date, Round(Cast(Coalesce(bs.prev_balance,0) AS NUMERIC),2) AS prior_balance,'0.00'AS protection_charages, b.bill_month,bs.prev_bill_month,concat(TO_CHAR(b.start_bill_period :: DATE, 'Month dd'),' -',TO_CHAR(b.end_bill_period :: DATE, 'Month dd'),' (',days_in_bill_cycle,' days',')') billing_period,To_char(payments.payment_Date :: DATE, 'mm/dd')  AS as_of_payment_date,COALESCE(bs.prev_bill_month, 'NA'::character varying) AS prev_bill_exists, Round(Coalesce(payments.payment_amount,0)::Numeric,2) AS payment_amount,Round(Cast(Coalesce(b.tax,0) As Numeric),2) AS tax, Round(Cast(Coalesce(b.balance_forward, 0) AS NUMERIC), 2) AS balance_forward, Round(Coalesce(b.water_service_charge, 0)::Numeric,2) AS water_service_charge,Round(Cast(( ( CASE WHEN b.water_service_charge IS NULL THEN 0  ELSE b.water_service_charge END ) / b.consumption )AS NUMERIC), 8)  AS rate_per_gallon, Coalesce(b.consumption, 0)*100 AS consumption,  0.00 AS water_usage_charge, Round(Cast(Coalesce(b.other_charges, 0)  + Coalesce(b.waste_water_service_charge, 0)  + Coalesce(b.fire_service_charge, 0) AS NUMERIC), 2) other_charges, Coalesce(b.final_balance, 0) AS total_due_amount, Round(Coalesce(bs.final_balance, 0)::numeric,2) AS final_balance, Round(Coalesce(bs.prev_balance, 0)::numeric,2) AS prev_balance, Coalesce(bs.prev_usage, 0) * 100 AS prev_usage, Round(Cast(Abs(Coalesce(bs.year_avg, 0)) AS NUMERIC), 2) AS year_avg, Round(Coalesce(bs.year_avg_usage, 0) * 100, 0) AS year_avg_usage, Round(Cast(Abs(Coalesce(bs.navg, 0)) AS NUMERIC), 2) navg, Round(Coalesce(bs.navg_usage, 0) * 100, 0)  AS navg_usage, Round(Coalesce(bs.prev_year_consumption, 0) * 100, 0)  AS prev_year_consumption, Round(Cast(Abs(Coalesce(bs.prev_year_balance, 0))AS NUMERIC), 2) AS prev_year_balance, b.statement_date  FROM   app.bills b         left outer join app.bills_summary_all bs ON b.business_partner_number = bs.business_partner_number AND b.connection_contract_number = bs.connection_contract_number  AND b.bill_month = bs.bill_month  left outer join app.connection_contracts c ON bs.business_partner_number = c.business_partner_number  AND bs.connection_contract_number = c.connection_contract_number     left outer join (                 select  s.business_partner_number , s.connection_contract_number, c.ebill_flag,c.budget_bill_flag , min(p.due_date) as due_date ,   p.payment_month,                 max(p.payment_date) as payment_date  , max(p.bill_id) as bill_id ,                 sum(p.payment_amount)  as payment_amount , sum(p.clearing_amount) as clearing_amount                    FROM    app.customers c  , app.payments  p , app.bills_summary_all s                 where  c.business_partner_number =  p.business_partner_number                 and p.clearing_amount is not null                  and s.business_partner_number = p.business_partner_number                 and s.connection_contract_number = p.connection_contract_number                 and s.bill_month= p.payment_month                 group by s.business_partner_number , s.connection_contract_number, p.payment_month, p.due_date,c.ebill_flag,c.budget_bill_flag              ) AS payments ON payments.business_partner_number = b.business_partner_number and payments.connection_contract_number = b.connection_contract_number and payments.payment_month=bs.bill_month) invoice order by invoice.statement_date desc";
	
	public static final String ACCOUNT_PREFERENCES_QUERY = "select distinct on (premise_id) premise_id, business_partner_number,fullname,water_service,fire_service,sewer_service, paperless_statements,billprint_format,water_hos_flag,fire_hos_flag,sewer_hos_flag from app.customer_connection_contracts";
	
	public static final String BILL_SUMMARY_QUERY = "select b.business_partner_number,b.statement_date,bs.connection_contract_number,b.bill_month,prevBill.bill_month AS prev_bill_month,Round(Cast(Coalesce(b.balance_forward, 0) AS NUMERIC), 2) AS pastDue,CASE WHEN b.balance_forward>0 THEN 'true' ELSE 'false' END AS pastDueFlag,COALESCE(prevBill.bill_month, 'NA'::character varying) AS prev_bill_exists,concat(to_char(to_date(cast(b.start_bill_period as text),'yyyy-mm-dd'), 'mm/dd/yyyy'),        ' - ', to_char(to_date(cast(b.end_bill_period as text),'yyyy-mm-dd'), 'mm/dd/yyyy')) curr_service_period,concat(to_char(to_date(cast(prevBill.start_bill_period as text),'yyyy-mm-dd'), 'mm/dd/yyyy'),        ' - ', to_char(to_date(cast(prevBill.end_bill_period as text),'yyyy-mm-dd'), 'mm/dd/yyyy')) prev_service_period,to_char(to_date(cast(b.due_date as text),'yyyy-mm-dd'), 'mm/dd/yyyy')as curr_bill_due_date,to_char(to_date(cast(prevBill.due_date as text),'yyyy-mm-dd'), 'mm/dd/yyyy')as prev_bill_due_date,Round(Cast(Coalesce(b.final_balance,0) AS Numeric),2) AS curr_bill_amnt,Round(Cast(Coalesce(prevBill.final_balance,0) AS Numeric),2) AS prev_bill_amnt from app.bills b,app.bills_summary_all bs left join app.bills prevBill on prevBill.bill_id = bs.prev_bill_id and prevBill.business_partner_number = bs.business_partner_number and prevBill.connection_contract_number = bs.connection_contract_number where b.business_partner_number = bs.business_partner_number and  b.connection_contract_number = bs.connection_contract_number and b.bill_month = bs.bill_month";
	
	public static final String CONSUMPTION_QUERY = "SELECT * from app.highbill_consumption_view_new";
	
	public static final String BILL_MONTHS_QUERY = "select bill_id,bill_month,concat(to_char(to_timestamp(to_char(substring(bill_month,0,3)::integer, '999'), 'MM'), 'Month'),' ',substring(bill_month,4,8)) as bill_month_value from app.bills order by statement_date desc";
	
	public static final String SERVICE_HISTORY_QUERY = "SELECT * FROM ( SELECT c.business_partner_number, c.connection_contract_number, work_order_number AS service_order, activity_type AS so_code, activity_description AS description, concat(to_char(to_timestamp(SUBSTR(created_date::text,6,2)::text,'MM'),'Mon'),' ',SUBSTR(created_date::text,9,2),',',SUBSTR(created_date::text,1,4)) AS created_date,  status FROM app.work_order wo, app.connection_contracts c WHERE c.premise_id = wo.premise_id UNION SELECT b.business_partner_number, b.connection_contract_number, so.work_order_number AS service_order, activity_type AS so_code,COALESCE(activity_description,'') AS description, concat(to_char(to_timestamp(SUBSTR(created_date::text,6,2)::text,'MM'),'Mon'),' ',SUBSTR(created_date::text,9,2),',',SUBSTR(created_date::text,1,4)) AS created_date, status FROM sapupdates.work_order so, app.connection_contracts b WHERE so.premise_id = b.premise_id and so.work_order_number not in ( select work_order_number from app.work_order w1 where w1.premise_id=so.premise_id) ) wo ORDER BY wo.created_date DESC";
	
	public static final String PREVIOUS_INVOICES_QUERY = "SELECT * from (select business_partner_number, connection_contract_number, concat(to_char(to_timestamp(substring(t.statement_date::text,6,2),'MM'),'Month'),' ',substring(t.statement_date::text,0,5)) invoice_date,concat(to_char(to_date(cast(start_bill_period as text),'yyyy-mm-dd'), 'mm/dd/yyyy'),        ' - ', to_char(to_date(cast(end_bill_period as text),'yyyy-mm-dd'), 'mm/dd/yyyy')) service_period,Round(COALESCE(t.final_balance,0)::Numeric,2) AS amountDue,t.statement_date, COALESCE(TO_CHAR(t.consumption*100,'99,999,999,999'),'0')as consumption,COALESCE(round(cast((t.balance_forward)as numeric),2),0.00) past_due, TO_CHAR(t.due_date :: DATE, 'MM/dd/yyyy')  as  dueDate, COALESCE(round(cast(( t.waste_water_service_charge + t.fire_service_charge + t.other_charges + t.tax)as numeric),2),0) charges, t.document_id As document from app.bills t) prevInvoice  order by prevInvoice.statement_date desc ";
	
	public static final String CONNECTION_CONTRACTS_QUERY = "select premise_address,premise_city,premise_state,premise_country,premise_zip,connection_contract_number,premise_id,water_active_flag as water_service,fire_active_flag as fire_service,waste_water_active_flag as sewer_service,connection_status,to_char(move_in_date,'mm/dd/yyyy') from app.connection_contracts";
	
	public static final String VIEW_FULL_SERVICE_HISTORY_QUERY = "SELECT * FROM ( SELECT c.business_partner_number, c.connection_contract_number, work_order_number AS service_order, activity_type AS so_code, activity_description AS description, concat(to_char(to_timestamp(SUBSTR(created_date::text,6,2)::text,'MM'),'Mon'),' ',SUBSTR(created_date::text,9,2),',',SUBSTR(created_date::text,1,4)) AS created_date,  status,serial_number FROM app.work_order wo, app.connection_contracts c WHERE c.premise_id = wo.premise_id UNION SELECT b.business_partner_number, b.connection_contract_number, so.work_order_number AS service_order, activity_type AS so_code,COALESCE(activity_description,'') AS description, concat(to_char(to_timestamp(SUBSTR(created_date::text,6,2)::text,'MM'),'Mon'),' ',SUBSTR(created_date::text,9,2),',',SUBSTR(created_date::text,1,4)) AS created_date, status, serial_number FROM sapupdates.work_order so, app.connection_contracts b WHERE so.premise_id = b.premise_id and so.work_order_number not in ( select work_order_number from app.work_order w1 where w1.premise_id=so.premise_id) ) wo ORDER BY wo.created_date DESC";

	public static final String USAGE_OVERVIEW_24HOURS_QUERY ="SELECT DISTINCT  mt.meter_id,mt.connection_contract_number,mt.business_partner_number,mr.reading_datetime,  extract(hour from mr.reading_datetime) hourly,extract(day from mr.reading_datetime) daywise,mr.consumption,mr.unit_of_measure,\r\n" + 
			 " cc.premise_id FROM app.meters mt JOIN app.meter_ami_reads_hourly mr ON mt.business_partner_number = mr.businesspartnernumber \r\n" + 
			"AND mt.meter_id = trim(leading '0' FROM mr.equipmentnumber) JOIN app.connection_contracts cc \r\n" + 
			"ON mt.business_partner_number = cc.business_partner_number AND mr.contractaccount = cast ( cc.connection_contract_number AS int8) \r\n" + 
			" WHERE  mt.business_partner_number = '1101874783' AND mr.reading_datetime BETWEEN (SELECT max(reading_datetime) - interval '23 HOUR' FROM   app.meter_ami_reads_hourly WHERE  businesspartnernumber= '1101874783') AND(SELECT max(reading_datetime) FROM   app.meter_ami_reads_hourly WHERE  businesspartnernumber= '1101874783') ORDER BY reading_datetime DESC,hourly";
	
	public static final String AUTOPAY_CONTRACT_NUMBERS_QUERY = "select business_partner_number as bpNumber,connection_contract_number as contractNumber from app.connection_contracts";
	
	public static final String USAGE_OVERVIEW_DAILY_QUERY ="SELECT DISTINCT mt.meter_id,mt.connection_contract_number,mt.business_partner_number,mr.reading_datetime,mr.consumption,mr.unit_of_measure,cc.premise_id FROM join app.meter_ami_reads_daily mr ON mt.business_partner_number = mr.businesspartnernumber AND mt.meter_id = Trim(leading '0' FROM mr.equipmentnumber) join app.connection_contracts cc ON mt.business_partner_number = cc.business_partner_number AND mr.contractaccount = Cast (cc.connection_contract_number AS INT8 ) WHERE  mt.business_partner_number = '1101874783' AND mr.reading_datetime >= '2019-01-24 06:00:00' AND mr.reading_datetime <= '2019-01-24 23:00:00' ORDER  BY reading_datetime DESC";
	
	public static final String USAGE_OVERVIEW_12MONTHS_QUERY = "";
	
	public static final String BUDGET_BILLING_REST_API = "http://localhost:8090/csr1vapi/budgetbill/eligibility/220012090879";
	public static final String AUTO_PAY_bANK_DETAILS_REST_API = "http://localhost:8090/csr1vapi/customer/creditWorthiness/1201319191/220012090879";
	public static final String LOCK_REASONS_AUTO_PAY = "select reason_cd as id , reason_descr as value from app.lock_type_reason_codes where lock_id = '02'";
	
	/*
	 * Service order queries
	 * */
	public static final String SERVICE_ORDER_TYPE_QUERY = "select distinct(wo_type) as id ,wo_description as value from app.work_order_type wo where wo_type !='34' and  wo_type !='31'";
	public static final String SERVICE_ORDER_CODE_QUERY = "select distinct mat_code as id ,mat_code_description as value from app.work_order_type";
	
	public static final String ES_INDEX_DATA_POPULATION_VIEW = "SELECT businesspartner_number, fullname, connection_count, customer_status, customer_satisfaction, satisfaction_index_fsr, satisfaction_index_fsr_reason, mailing_address, mailing_city, mailing_state, mailing_country, mailing_zip, account_status, paperless_statements, mobile_number,  credit_worthiness, email_address, preferred_contact_method, budgetenroll_status,  collectiveenroll_status, installmentenroll_status, autopay, security_pin,   contract_acc_number, premise_id, premise_address, premise_city, \r\n" + 
			"       premise_state, premise_country, premise_zip, billprint_format,    premise_connection_status, home_number, premise_type, customer_period,  move_in::text, move_out::text, water_service, fire_service,sewer_service  FROM app.customer_connection_contracts_es";





}