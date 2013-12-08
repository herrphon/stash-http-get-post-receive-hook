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
	private HttpURLConnection connection;
	private String urlString;
	private String user;
	private String pass;
	private Boolean use_auth;

	public HttpAgent(HttpLocation httpLocation) {
		
		urlString = httpLocation.getUrlString();
	    user = httpLocation.getUser();
	    pass = httpLocation.getPass();
	    use_auth = httpLocation.getUseAuth();
	    
		log.info("The following URL was found: " + urlString);

	}

	
	public void authenticate(String user, String pass) {
		log.info("Authentication was enabled with user: " + user);
		// build the auth string
		String authString = user + ":" + pass;
		String authStringEnc = new String(Base64.encodeBase64(authString
				.getBytes()));
		connection
				.setRequestProperty("Authorization", "Basic " + authStringEnc);
	}

	
	public void doPageRequest() {
		try {
			URL url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();
			connection.setReadTimeout(5000);
			connection.setInstanceFollowRedirects(true);
			HttpURLConnection.setFollowRedirects(true);
			
			if (use_auth == true) {
				authenticate(user, pass);
			}
			
			connection.connect();
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

	private String getPageContent() throws IOException {
		// Get HTTP Response Body
		BufferedReader buffer = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
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
		int responseCode = connection.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) {
			log.error("Problem with the HTTP connection with response code: "
					+ responseCode);
		}
	}
}
