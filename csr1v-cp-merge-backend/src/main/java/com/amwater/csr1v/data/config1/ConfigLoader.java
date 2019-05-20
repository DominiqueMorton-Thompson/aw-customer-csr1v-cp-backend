package com.amwater.csr1v.data.config1;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;

import com.amwater.csr1v.data.context.Context;
import com.apporchid.common.utils.ContextHelper;
import com.apporchid.jpa.cpool.SqlDatasourceHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConfigLoader {
	String dataType;
	
	public static String loadConfigs(String dataType,Context context) {
        ResultSet resultSet = null;
        Connection connection = null;
        Statement statement = null;
        String configJson =null;
        
        String configQuery =" select configuration from ao-csrcp2.data_configuration where datatype = '" + dataType +"'";
        try {
            SqlDatasourceHelper sqlDatasourceHelper = ContextHelper.getBean(SqlDatasourceHelper.class);
            DataSource ds = sqlDatasourceHelper.getDatasourceByName("ao-csrcp2");
            connection = ds.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(configQuery);

            if (resultSet != null) {
 
                final ResultSet resultSetTemp = resultSet;
                while (resultSet.next()) {
                	configJson =  resultSetTemp.getString("configuration");
                }
            }

			Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(configJson);// response will be the json String

//			context.setDataConfig(gson.fromJson(object, DataConfig.class));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(resultSet, connection, statement);
        }
        return null;
    }
	public static void close(ResultSet resultSet, Connection connection, Statement statement) {
		DbUtils.closeQuietly(resultSet);
		DbUtils.closeQuietly(statement);
		try {
			DbUtils.close(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
