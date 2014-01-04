package de.aeffle.stash.plugin.hook;

import java.util.ArrayList;
import java.util.Collection;

import com.atlassian.stash.hook.repository.RepositoryHookContext;

public class HttpLocation {
	private static RepositoryHookContext context;
	private final String urlTemplate;
	private String url;
	private final String user;
	private final String pass;
	private final Boolean useAuth;

	public static Collection<HttpLocation> getAllFromContext(RepositoryHookContext context) {
		HttpLocation.context = context;
		Collection<HttpLocation> httpGetLocations = new ArrayList<HttpLocation>();
		
		HttpLocation first = new HttpLocation(1); 
		
		httpGetLocations.add(first);
		return httpGetLocations;
	}
	
	private HttpLocation(int id) {
		String urlString = ( id > 1 ? "url" + id : "url" );
		String useAuthString = ( id > 1 ? "use_auth" + id : "use_auth" );
		String userString = ( id > 1 ? "user" + id : "user" );
		String passString = ( id > 1 ? "pass" + id : "pass" );
		
		urlTemplate = getConfigString(urlString);
		url = urlTemplate;
		
		useAuth = getConfigBoolean(useAuthString);
		user = getConfigString(userString);
		pass = getConfigString(passString);
	}

	private static String getConfigString(String name) {
		return context.getSettings().getString(name);
	}

	public static Boolean getConfigBoolean(String name) {
		Boolean b = context.getSettings().getBoolean(name);
		if (b == null) {
			b = Boolean.FALSE;
		}
		return b;
	}
	
	private static int getNumberOfHttpLocations() {
		return context.getSettings().getInt("rowCount");
    }

	public String getUrlTemplateString() {
		return urlTemplate;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}



	public Boolean getUseAuth() {
		return useAuth;
	}
	
	public String toString() {
		return "user: " + user + " - pass: " + pass + " - url: " + url;
	}
}