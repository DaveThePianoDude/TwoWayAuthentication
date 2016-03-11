
import java.io.*;
import java.net.*;
import java.rmi.server.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory; 
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;  
import java.io.InputStream;  

public class TlsSocketFactory extends SSLSocketFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private KeyManagerFactory keyManagerFactory = null;
	
    private File pKeyFile = null;
    
    private String pKeyPassword = "midbstore";
    
	private byte pattern;
	
    SSLContext sslContext = SSLContext.getInstance("TLS");
   
    public  TlsSocketFactory(byte pattern) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, UnrecoverableKeyException, KeyManagementException {
        this.pattern = pattern;
        
        System.out.println("Getting keystore...");
		
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.out.println("Truststore found: " + trustStore != null);
        
        System.out.println("Getting key manager factory.");
       
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        
        System.out.println("Key Manager factory found:" + keyManagerFactory != null);
        
        String pKeyPassword = "midbstore";
        
        System.out.println("Getting key store.");
        
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        
        System.out.println("Key store found:" + keyStore != null);
        
        System.out.println("Initializing key store...");
        pKeyFile = new File("C:\\temp\\osgi.p12");
        InputStream keyInput = new FileInputStream (pKeyFile);
        keyStore.load(keyInput,  pKeyPassword.toCharArray());
        keyInput.close();
        System.out.println("Key store initialized.");
        
        keyManagerFactory.init(keyStore,  pKeyPassword.toCharArray());

        sslContext.init(keyManagerFactory.getKeyManagers(), null,  new SecureRandom());   
        System.out.println("SSL context initialized.");
    }
    
    public int hashCode() {
        return (int) pattern;
    }

    public boolean equals(Object obj) {
        return (getClass() == obj.getClass() &&
                pattern == (( TlsSocketFactory) obj).pattern);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        
    	System.out.println("host="+host);
    	System.out.println("port="+port);
    	
    	return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }
    
    @Override
    public Socket createSocket() throws IOException {
        
    	System.out.println("creating socket with no params");
    	
        return sslContext.getSocketFactory().createSocket();
    }

	@Override
	public String[] getDefaultCipherSuites() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getSupportedCipherSuites() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Socket createSocket(String arg0, int arg1) throws IOException,
			UnknownHostException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Socket createSocket(InetAddress arg0, int arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Socket createSocket(String arg0, int arg1, InetAddress arg2, int arg3)
			throws IOException, UnknownHostException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Socket createSocket(InetAddress arg0, int arg1, InetAddress arg2,
			int arg3) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
   
}
