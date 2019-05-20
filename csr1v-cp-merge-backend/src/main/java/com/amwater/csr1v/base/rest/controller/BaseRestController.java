package com.amwater.csr1v.base.rest.controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.springframework.context.annotation.Profile;

import com.amwater.csr1v.base.rest.sap.exception.SAPRequestException;
import com.amwater.csr1v.constants.ICSR1VPipelineConstants;
import com.apporchid.cloudseer.common.config.TokenUtils;
import com.apporchid.common.rest.AbstractResource;
import com.apporchid.common.utils.ContextHelper;
import com.apporchid.common.utils.HttpClientUtils;
import com.apporchid.common.utils.JsonUtils;
import com.apporchid.domain.cloudseer.common.OAuthClientConfiguration;
import com.apporchid.jpa.cpool.SqlDatasourceHelper;
import com.apporchid.service.cloudseer.configuration.OAuthClientConfigurationService;
import com.fasterxml.jackson.core.JsonProcessingException;

@Profile("!solution-dev")
public class BaseRestController extends AbstractResource {
	
	@Inject
	private OAuthClientConfigurationService oAuthClientConfigurationService;

	protected String executeFunction(String query){
		Connection connection = null;
		String fee = "";
		SqlDatasourceHelper sqlDatasourceHelper = ContextHelper.getBean(SqlDatasourceHelper.class);
		DataSource ds = sqlDatasourceHelper.getDatasourceByName(ICSR1VPipelineConstants.DEFAULT_SQL_DATASOURCE_NAME);
		try {
			connection = ds.getConnection();
			CallableStatement callableStatement = connection.prepareCall(query);
			callableStatement.registerOutParameter(1, java.sql.Types.DOUBLE);
			callableStatement.execute();
			fee = String.valueOf(callableStatement.getDouble(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fee;
		
	}
	
	protected List<Map<String, Object>> executeQuery(String sqlQuery) {
		ResultSet resultSet = null;
		Connection connection = null;
		Statement statement = null;
		List<Map<String, Object>> resultMap = new ArrayList<>();
		try {
			SqlDatasourceHelper sqlDatasourceHelper = ContextHelper.getBean(SqlDatasourceHelper.class);
			DataSource ds = sqlDatasourceHelper.getDatasourceByName(ICSR1VPipelineConstants.DEFAULT_SQL_DATASOURCE_NAME);
			connection = ds.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlQuery);

			if (resultSet != null) {
				List<String> columnList = new ArrayList<>();
				ResultSetMetaData metaDataRS = resultSet.getMetaData();

				if (metaDataRS != null) {
					IntStream.range(1, metaDataRS.getColumnCount() + 1).forEach(i -> {
						try {
							columnList.add(metaDataRS.getColumnName(i));
						} catch (SQLException e) {
							e.printStackTrace();
						}
					});
				}
				final ResultSet resultSetTemp = resultSet;
				while (resultSet.next()) {
					Map<String, Object> record = new HashMap<>();

					columnList.forEach(column -> {
						try {
							resultSetTemp.getString(column);
							if(!resultSetTemp.wasNull()) {
								String col = column.toLowerCase().replace("_", "");
								record.put(col, resultSetTemp.getString(column));
							}
							
						} catch (SQLException e) {
							e.printStackTrace();
						}
					});
					resultMap.add(record);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(resultSet, connection, statement);
		}
		return resultMap;
	}
	
	protected List<String> executeQueryForList(String sqlQuery) {
		ResultSet resultSet = null;
		Connection connection = null;
		Statement statement = null;
		List<String> resultMap = new ArrayList<>();
		try {
			SqlDatasourceHelper sqlDatasourceHelper = ContextHelper.getBean(SqlDatasourceHelper.class);
			DataSource ds = sqlDatasourceHelper.getDatasourceByName(ICSR1VPipelineConstants.DEFAULT_SQL_DATASOURCE_NAME);
			connection = ds.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlQuery);

			if (resultSet != null) {
				while (resultSet.next()) {
					resultSet.getString(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(resultSet, connection, statement);
		}
		return resultMap;
	}
	public void close(ResultSet resultSet, Connection connection, Statement statement) {
		DbUtils.closeQuietly(resultSet);
		DbUtils.closeQuietly(statement);
		try {
			DbUtils.close(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Parse request body to get input json passed from UI
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public String getRequestBody(HttpServletRequest request) throws IOException {
	    StringBuffer sb = new StringBuffer();
	    BufferedReader bufferedReader = null;
	    String content = "";

	    try {
		        bufferedReader =  request.getReader() ; //new BufferedReader(new InputStreamReader(inputStream));
		        char[] charBuffer = new char[128];
		        int bytesRead;
		        while ( (bytesRead = bufferedReader.read(charBuffer)) != -1 ) {
		            sb.append(charBuffer, 0, bytesRead);
		        }

	    } catch (IOException ex) {
	        ex.printStackTrace();
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }

	    System.out.println(sb.toString());
	    return sb.toString();
	}
	
	/**
	 * @return 
	 * @throws SAPRequestException 
	 * @throws Exception 
	 * @throws JSONException 
	 * @throws JsonProcessingException 
	 *  
	 */
	public BasicHeader getSecureHeaderWithToken(String tokenConfigName) throws SAPRequestException  {
		
		if (oAuthClientConfigurationService == null) {
			oAuthClientConfigurationService = ContextHelper.getBean(OAuthClientConfigurationService.class);
		}
		//getOAuthToken
		OAuthClientConfiguration oAuth2Configuration = oAuthClientConfigurationService
				.getOAuthClientConfiguration(tokenConfigName);
		
		try {
				Map<String, Object> tokenMap = TokenUtils.getOAuthClientTokenByName(tokenConfigName,"exact_time");
		
				String accessToken = tokenMap.get(oAuth2Configuration.getTokenName()).toString();
				Map<String, Object> configMap;
				configMap = JsonUtils.jsonToMap(oAuth2Configuration.getAddlProperties());
				String headerName = (String) configMap.get("header-name");
				String headerTokenName = (String) configMap.get("header-token-name");
				if (StringUtils.isNotBlank(headerTokenName)) {
					return new BasicHeader(headerName, headerTokenName + "=" + accessToken);
				} else {
					return new BasicHeader(headerName, accessToken);
				}		
			} catch (JsonProcessingException | JSONException e) {
				throw new SAPRequestException(e.getMessage());
			} catch (Exception e) {
					// TODO Auto-generated catch block
				throw new SAPRequestException(e.getMessage());
			}
	}
	
	/***
	 * POST Http request to SAP
	 * @return 
	 */
	public HttpResponse doPostToSAP(String uri, String parameters,BasicHeader authHeader) {
		try {
			HttpResponse httpResponse = HttpClientUtils.post(uri, parameters,new Header[] { authHeader });
			return httpResponse;
		} catch (IOException e) {
			throw new RuntimeException("Error " + e.getLocalizedMessage() + " occurred while reading from the url ->"
					+ uri,
					e);

		}
		
	}
	
	/***
	 * GET Http request to SAP
	 * @return 
	 */
	public HttpResponse getRequestToSAP(String uri, String contentType,BasicHeader authHeader) {
		try {
			HttpResponse httpResponse = HttpClientUtils.get(
					uri, "",
					contentType, null, new Header[] { authHeader });
			return httpResponse;
		} catch (IOException e) {
			throw new RuntimeException("Error " + e.getLocalizedMessage() + " occurred while reading from the url ->"
					+ uri,
					e);

		}
		
	}
	
	

}
