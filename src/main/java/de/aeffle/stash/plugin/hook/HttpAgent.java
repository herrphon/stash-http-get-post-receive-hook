package de.aeffle.stash.plugin.hook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;

import com.atlassian.extras.common.log.Logger;

public class HttpAgent {
	private static final Logger.Log log = Logger
			.getInstance(HttpGetPostReceiveHook.class);
	private String urlString;
	private HttpURLConnection httpURLConnection;
	private String user;
	private String pass;
	private Boolean useAuth;

	public HttpAgent(HttpLocation httpLocation) {
		urlString = httpLocation.getUrl();

		useAuth = httpLocation.getUseAuth();

	    user = httpLocation.getUser();
	    pass = httpLocation.getPass();
	    
		log.info("Http request with URL: " + urlString);
	}

	public void setConnection(HttpURLConnection httpURLConnection) {
		this.httpURLConnection = httpURLConnection;
	}
	
	private void authenticate(String user, String pass) {
		log.info("Authentication was enabled with user: " + user);
		
		// build the auth string
		String authString = user + ":" + pass;
		String authStringEnc = new String(Base64.encodeBase64(authString.getBytes()));
		
		httpURLConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
	}

	
	public void doPageRequest() {
		try {
			if (httpURLConnection == null) {
				URL url = new URL(urlString);
				httpURLConnection = createHttpURLConnection(url);
			}
			
			httpURLConnection.setReadTimeout(5000);
			httpURLConnection.setInstanceFollowRedirects(true);
			HttpURLConnection.setFollowRedirects(true);
			
			if (useAuth == true) {
				authenticate(user, pass);
			}
			
			httpURLConnection.connect();
			checkResponse();
			log.debug("HTTP response:\n" + getPageContent());
			
		} catch (MalformedURLException e) {
			log.error("Malformed URL:" + e);
		} catch (IOException e) {
			log.error("Some IO exception occured", e);
		} catch (Exception e) {
			log.error("Something else went wrong: ", e);
		}
	}

	private HttpURLConnection createHttpURLConnection(URL url) throws IOException{
	    return (HttpURLConnection)url.openConnection();

	}
	
	private String getPageContent() throws IOException {
		// Get HTTP Response Body
		BufferedReader buffer = new BufferedReader(new InputStreamReader(
				httpURLConnection.getInputStream()));
		String inputLine;
		StringBuffer body = new StringBuffer();

		while ((inputLine = buffer.readLine()) != null) {
			body.append(inputLine + "\n");
		}
		// close everything
		buffer.close();

		return body.toString();
	}

	private void checkResponse() throws IOException {
		// Get HTTP Response Code
		int responseCode = httpURLConnection.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) {
			log.error("Problem with the HTTP connection with response code: "
					+ responseCode);
		}
	}
}
