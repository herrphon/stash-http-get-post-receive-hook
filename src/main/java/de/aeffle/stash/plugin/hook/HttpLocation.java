package de.aeffle.stash.plugin.hook;

import java.util.ArrayList;
import java.util.Collection;

import com.atlassian.stash.hook.repository.RepositoryHookContext;

public class HttpLocation {
	private final RepositoryHookContext context;

	public static Collection<HttpLocation> getAllFromContext(RepositoryHookContext context) {
		Collection<HttpLocation> httpGetLocations = new ArrayList<HttpLocation>();
		
		HttpLocation first = new HttpLocation(context); 
		
		httpGetLocations.add(first);
		return httpGetLocations;
	}
	
	public HttpLocation(RepositoryHookContext context) {
		this.context = context;
	}

	public String getUrlString() {
		return getConfigString("url");
	}

	public String getUser() {
		return getConfigString("user");
	}

	public String getPass() {
		return getConfigString("pass");
	}

	private String getConfigString(String name) {
		return context.getSettings().getString(name);
	}

	public Boolean getUseAuth() {
		Boolean useAuth = context.getSettings().getBoolean("use_auth");
		if (useAuth == null) {
			useAuth = Boolean.FALSE;
		}
		return useAuth;
	}
}