package com.test.communicator.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import com.test.communicator.CommunicatorInterface;
import com.test.exception.CustomException;

public class HTTPCommunicator implements CommunicatorInterface {

	/*
	 * This method is used to send a HTTPRequest .
	 * It converts the response body to string and returns it .
	 * It also handles different cases like BAD REQUEST , REDIRECT ,etc .
	 * It return filename where the HTML content is stored . 
	 */
	@Override
	public String sendMessage(String link) throws Exception {
		
		String requestLink = link ;
		HttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(requestLink);
		String responseString = null ;
		
		boolean redirect ;
		do{
			redirect = false ;
			try {
				URI uri = new URI(requestLink);
				HttpResponse response = httpClient.execute(httpGet);
				int statusCode = response.getStatusLine().getStatusCode();
				
				if(statusCode == HttpStatus.SC_OK){
					responseString = processResponse(uri,response);
					/*HttpEntity entity = response.getEntity();
					responseString = EntityUtils.toString(entity);*/
				}else if(statusCode==HttpStatus.SC_BAD_REQUEST){
					String message = "BAD REQUEST : " + uri.toString() ;
					throw new CustomException(message);
				}
				
			} catch (Exception exception) {
				String exceptionMessage = "Exception while sending message : "+exception ;
				throw new CustomException(exceptionMessage);
				
			}
			System.out.println("");
		}while(redirect);
		
		
		return responseString ;
	}

	/**
	 * This method writes the HTML content to a file .
	 * Filename is same as URI.
	 * These files are stored in a /tmp folder .
	 * 
	 * @param uri
	 * @param response
	 * @return {@link String}
	 * @throws Exception
	 */
	private String processResponse(URI uri ,HttpResponse response) throws  Exception {
		String uriString = uri.toString().replaceAll("/", "-");
		
		String fileName = "tmp/"+uriString+".html";
		InputStream is = response.getEntity().getContent();
		FileOutputStream fos = new FileOutputStream(new File(fileName),false);
		
		int read = 0;
		byte[] buffer = new byte[32768];
		while( (read = is.read(buffer)) > 0) {
		  fos.write(buffer, 0, read);
		}

		fos.close();
		is.close();
		return fileName ;
	}
	
	

}
