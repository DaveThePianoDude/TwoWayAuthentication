import mil.dia.midb.mdal.gmi.search.ejb.GMISearchInterface;
import mil.dia.midb.mdal.gmi.util.MDALServiceLocator;

import javax.xml.ws.WebServiceRef;

import java.io.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import productionservice.midb.*;
import midb.gmi.util.UserInformation;
import _9._1._168._192._80.midbproductionservice.*;

public class TwoWayAuthentication {
  
	@WebServiceRef(wsdlLocation="https://ec2-96-127-81-134.us-gov-west-1.compute.amazonaws.com/axis/services/MIDBProductionService?wsdl")
	static MIDBProductionService service;
	
	    public static void main(String[] args)
	    {
            //System.setProperty("javax.net.ssl.keyStorePassword","midbstore");
            //System.setProperty("javax.net.ssl.trustStore", 
	        //        "C:/Progra~1/Java/jdk1.8.0_45/jre/lib/security/cacerts");
            //System.setProperty("javax.net.ssl.trustStorePassword","changeit");
            //System.setProperty("javax.net.debug", "true");
            //System.setProperty("https.protocols", "TLSv3");
	    	            
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
	    	
	    	HttpClient client = new DefaultHttpClient();
	    	client.getParams().setParameter("http.useragent", "Web Service Test Client");
	         
	        String baseUrl = "https://ec2-96-127-81-134.us-gov-west-1.compute.amazonaws.com";
	        String facRnd = baseUrl + "/i3/midb/mdal/v1/rest/Facility?Facility.FacSk=10035000000960";
	        
	     	//request.addHeader("User-Agent", USER_AGENT);
	        
	        HttpGet request = new HttpGet(facRnd);
	        
	     	HttpResponse response = client.execute(request);

	     	System.out.println("Response Code : " 
	                     + response.getStatusLine().getStatusCode());
	    }
	    
	    public void doTest(String[] args) {
	        try {
	        	
	        	 MDALServiceLocator locator = 
		           new MDALServiceLocator("ec2-96-127-81-134.us-gov-west-1.compute.amazonaws.com", "443", 
		          "psantos", "Passwordpassword4!");
		           GMISearchInterface searchIface = locator.getGMISearchInterface();
		          System.out.println("MIDB time of day:" + searchIface.getCurrentTime(1));
		           
		        //ResponseUpdateComposite responder = new ResponseUpdateComposite();     
		        //responder.setUPDATEDCOMPOSITES(updateComposites);
	        	
	            COMPOSITES updateComposites = new COMPOSITES();
	            UserInformation userInfo = new UserInformation();
	            userInfo.setRESPROD("DJ");
	            userInfo.setSYSSOURCE("TEST");
	            
	            RequestUpdateComposite requestor = new RequestUpdateComposite();
	            requestor.setUPDATECOMPOSITES(updateComposites);
	            requestor.setUSERINFO(userInfo);
	            
	            
	            
	            //System.out.println("Retrieving the port from the following service: " + service);	            
	            //service.requestUpdateComposite(updateComposites, null, null, userInfo);
	            
	            //System.out.println(response);
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	    }
	                                         
}
