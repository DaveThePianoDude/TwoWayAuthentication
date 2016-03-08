
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

public class TwoWayAuthentication {

	public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
		
		try {  
        	 String baseUrl = "https://go-west-young-man.compute.amazonaws.com";
        	 String midb = baseUrl + "/i4/your/adal/v1/rest/currentServer";
        	
             URL url = new URL(midb);  
             HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();  
   
             File pKeyFile = new File("C:\\temp\\sgi.p12");
             String pKeyPassword = "ask-dave";
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
}
