package de.aeffle.stash.plugin.hook;

import java.util.ArrayList;
import java.util.Collection;

import com.atlassian.stash.hook.repository.RepositoryHookContext;

public class HttpLocation {
	private final RepositoryHookContext context;
	private final String urlTemplate;
	private String url;
	private final String user;
	private final String pass;

	public static Collection<HttpLocation> getAllFromContext(RepositoryHookContext context) {
		Collection<HttpLocation> httpGetLocations = new ArrayList<HttpLocation>();
		
		HttpLocation first = new HttpLocation(context); 
		
		httpGetLocations.add(first);
		return httpGetLocations;
	}
	
	private HttpLocation(RepositoryHookContext context) {
		this.context = context;
		
		urlTemplate = getConfigString("url");
		url = urlTemplate;
		user = getConfigString("user");
		pass = getConfigString("pass");
	}

	private String getConfigString(String name) {
		return context.getSettings().getString(name);
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
		Boolean useAuth = context.getSettings().getBoolean("use_auth");
		if (useAuth == null) {
			useAuth = Boolean.FALSE;
		}
		return useAuth;
	}
	
	public String toString() {
		return "user: " + user + " - pass: " + pass + " - url: " + url;
	}
}