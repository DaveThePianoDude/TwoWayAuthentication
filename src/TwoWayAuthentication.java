
import javax.xml.ws.WebServiceRef;

import java.io.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.impl.nio.client.HttpAsyncClients; 
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.*;
import org.apache.http.concurrent.FutureCallback;

//import org.json.*;

import mil.dia.midb.mdal.gmi.controller.ejb.GMIControllerInterface;
import mil.dia.midb.mdal.gmi.search.ejb.GMISearchInterface;
import mil.dia.midb.mdal.gmi.search.util.QueryHelper;
import mil.dia.midb.mdal.gmi.support.ejb.GMISupportInterface;
import mil.dia.midb.mdal.gmi.util.GeneralFailureException;
import mil.dia.midb.mdal.gmi.util.MDALServiceLocator;

public class TwoWayAuthentication {
  
	
	    public static void main(String[] args)
	    {
/*	    	JSONObject obj = new JSONObject("{\"name\":\"Alice\",\"age\":20,\"address\":{\"streetAddress\":\"100WallStreet\",\"city\":\"NewYork\"},\"phoneNumber\":[{\"type\":\"home\",\"number\":\"212-333-1111\"},{\"type\":\"fax\",\"number\":\"646-444-2222\"}]}");   	
	    	String name = obj.getString("name");    	
	    	JSONArray phone = obj.getJSONArray("phoneNumber");
	    	
	    	for (int i = 0; i < phone.length(); i++)
	    	{
	    	    String type = phone.getJSONObject(i).getString("type");
	    	    System.out.println(type);
	    	}
	    	
	    	for (int i = 0; i < phone.length(); i++)
	    	{
	    	    JSONObject ob = phone.getJSONObject(i);
	    	    System.out.println(ob);
	    	    
	    	    Object[] keys = ob.keySet().toArray();
	    	    
	    	    for (int j = 0; j < keys.length; j++)
	    	    {
	    	    	String key = (String)keys[j];
	    	    	System.out.println("key = " + key);
	    	    	System.out.println("value = " + ob.get(key));
	    	    }
	    	}*/
	    	
	    	//System.out.println(arr.toString());
	    	 	
            System.setProperty("javax.net.ssl.keyStore","C:/Progra~1/Java/jdk1.8.0_45/jre/lib/security/osgi.p12");                  
            System.setProperty("javax.net.ssl.keyStorePassword","midbstore");
            System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
            
            System.setProperty("javax.net.ssl.trustStore","C:/Progra~1/Java/jdk1.8.0_45/jre/lib/security/cacerts");
            System.setProperty("javax.net.ssl.trustStorePassword","changeit");
            
            System.setProperty("javax.net.debug", "true");
            System.setProperty("https.protocols", "TLSv3");
            System.setProperty("javax.net.debug", "ssl");
	    	            
	    	try {
	    		TwoWayAuthentication client = new TwoWayAuthentication();
	            client.doTest(args);
	    		//client.sendRESTRequest();
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	    }    
	    
	    @SuppressWarnings("deprecation")
		public void sendRESTRequest() throws ClientProtocolException, IOException {
	    	
	    	/*HttpClient client = new DefaultHttpClient();
	    	client.getParams().setParameter("http.useragent", "Web Service Test Client");
	        */ 
	        String baseUrl = "http://127.0.0.1:9200";
	        
	        CloseableHttpAsyncClient httpclient = null; 
	        
            httpclient = HttpAsyncClients.createDefault(); 
            httpclient.start(); 
            HttpGet request = new HttpGet(baseUrl);
	        
                
	     	//request.addHeader("User-Agent", USER_AGENT);
	        
	        //HttpGet request = new HttpGet(baseUrl);
	        
	     	//HttpResponse response = client.execute(request);

	     	//System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
	    }
	    
	    public void doTest(String[] args) {
	        try {
/*	        	
	        	for (int i = 0; i < 10; i++)
	        		sendRESTRequest();*/
	        	        	        	
	        	 MDALServiceLocator locator = 
		           new MDALServiceLocator("ec2-96-127-81-134.us-gov-west-1.compute.amazonaws.com", "443", 
		          "psantos", "Passwordpassword4!");
		           GMISearchInterface searchIface = locator.getGMISearchInterface();
		          System.out.println("MIDB time of day:" + searchIface.getCurrentTime(1));
		          
		        //ResponseUpdateComposite responder = new ResponseUpdateComposite();     
		        //responder.setUPDATEDCOMPOSITES(updateComposites);
	        	
	           /* COMPOSITES updateComposites = new COMPOSITES();
	            UserInformation userInfo = new UserInformation();
	            userInfo.setRESPROD("DJ");
	            userInfo.setSYSSOURCE("TEST");
	            
	            RequestUpdateComposite requestor = new RequestUpdateComposite();
	            requestor.setUPDATECOMPOSITES(updateComposites);
	            requestor.setUSERINFO(userInfo);
	                        */
	            //System.out.println("Retrieving the port from the following service: " + service);	            
	            //service.requestUpdateComposite(updateComposites, null, null, userInfo);
	            
	            //System.out.println(response);
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	    }
	                                         
}
