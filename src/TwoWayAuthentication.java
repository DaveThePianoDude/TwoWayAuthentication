
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.net.MalformedURLException;  
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;  
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import mil.dia.midb.mdal.gmi.controller.ejb.GMIControllerInterface;
import mil.dia.midb.mdal.gmi.search.ejb.GMISearchInterface;
import mil.dia.midb.mdal.gmi.support.ejb.GMISupportInterface;
import mil.dia.midb.mdal.gmi.util.MDALServiceLocator;



public class TwoWayAuthentication {

	public static void main(String[] args) throws Exception {
		
		//callSocketDirectly();
		//callSocketViaFactory();  
		
		System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
		System.setProperty("javax.net.ssl.keyStore", "C:/temp/gms.p12");
		System.setProperty("javax.net.ssl.keyStorePassword","midbstore");
		
		System.setProperty("javax.net.ssl.trustStore", "C:/Progra~1/Java/jdk1.7.0_79/jre/lib/security/cacerts");
		System.setProperty("javax.net.ssl.trustStorePassword","changeit");
		
		System.setProperty("https.protocols", "TLSv3");

        MDALServiceLocator ejbLocator = new MDALServiceLocator("96.127.81.134", "443", "gms", "Password1234567");
        GMISearchInterface mySearchInterface = ejbLocator.getGMISearchInterface();
        
        //mySearchInterface.getCurrentTime(1);
        
        String uid = mySearchInterface.getUniqueId();
        
        System.out.println("Unique ID ="+uid);
        
        //int limit = mySearchInterface.getAreaLimit();
        
        //System.out.println("area limit="+limit);
        
        //System.out.println("Time="+timeOfDay);
        
		//Context InitialContext = getInitialContext();
		//System.out.println("Obtained Initial Context: " + InitialContext != null);
		
		//InitialContext.lookup("ejb:MDAL/GMISearch/GMISearch!mil.dia.midb.mdal.gmi.search.ejb.GMISearchInterface");
		
		//System.out.println("EJB exists." + ejb != null);
		
		System.out.println("Done looking up EJB");
	}

	private static void callSocketDirectly() throws NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
		
		try {  
        	 String baseUrl = "https://ec2-96-127-81-134.us-gov-west-1.compute.amazonaws.com";
        	 String midb = baseUrl + "/i3/midb/adal/v1/rest/currentServer";
        	
             URL url = new URL(midb);  
             HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();  
   
             File pKeyFile = new File("C:\\temp\\osgi.p12");
             String pKeyPassword = "midbstore";
             KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
             
             KeyStore keyStore = KeyStore.getInstance("PKCS12");
             InputStream keyInput = new FileInputStream (pKeyFile);
             keyStore.load(keyInput,  pKeyPassword.toCharArray());
             
             keyInput.close();
             keyManagerFactory.init(keyStore,  pKeyPassword.toCharArray());
             SSLContext context = SSLContext.getInstance("TLS");
             context.init(keyManagerFactory.getKeyManagers(), null,  new SecureRandom());
             
             SSLSocketFactory sockFact = context.getSocketFactory();
             conn.setSSLSocketFactory(sockFact);
            
             InputStream inputstream = conn.getInputStream();
             InputStreamReader inputstreamreader = new InputStreamReader(inputstream);  
             BufferedReader bufferedreader = new BufferedReader(inputstreamreader);  
             String string = null;  
             while ((string = bufferedreader.readLine()) != null) {  
               System.out.println("Received " + string);  
             }  
        } catch (MalformedURLException e) {  
             e.printStackTrace();  
        } catch (IOException e) {  
             e.printStackTrace();  
        }
	}  
	
	private static void callSocketViaFactory() throws NoSuchAlgorithmException,
			KeyStoreException, CertificateException, IOException,
			UnrecoverableKeyException, KeyManagementException,
			MalformedURLException {
		// Create a factory.
		TlsSocketFactory factory = new TlsSocketFactory((byte) 0);
		
   	 	String baseUrl = "https://ec2-96-127-81-134.us-gov-west-1.compute.amazonaws.com";
   	 	String midb = baseUrl + "/i3/midb/adal/v1/rest/currentServer";
   	
   	 	// Create a connection.
        URL url = new URL(midb);  
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();  
		   
        // Have the connection use the factory to get a response.
		conn.setSSLSocketFactory(factory);    
        InputStream inputstream = conn.getInputStream();
        
        // Read back the returned input stream.
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);  
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);  
        String string = null;  
        while ((string = bufferedreader.readLine()) != null) {  
          System.out.println("Received " + string);  
        }
	}

	protected static InitialContext getInitialContext() throws NamingException, NoSuchAlgorithmException {
        
		java.util.Properties properties = new java.util.Properties();
        
		properties.put(Context.PROVIDER_URL, "ec2-96-127-81-134.us-gov-west-1.compute.amazonaws.com:443");
        properties.put(Context.SECURITY_PRINCIPAL, "gms");
        properties.put(Context.SECURITY_CREDENTIALS, "Password1234567");    
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        properties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");  
        properties.put("jboss.naming.client.ejb.context", Boolean.TRUE);

        return new InitialContext(properties);	
	}
	
}
