package com.amwater.csr1v.base.rest.sap.api;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.amwater.csr1v.base.rest.sap.exception.SAPRequestException;

public class CSR1VHttpUtils {

	
	public static String getRequestBody(HttpServletRequest request) throws SAPRequestException {
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
	        SAPRequestException sapException = new SAPRequestException(ex.getMessage());
	        throw sapException;
	    } finally {

	    }

	    System.out.println(sb.toString());
	    return sb.toString();
	}
}
