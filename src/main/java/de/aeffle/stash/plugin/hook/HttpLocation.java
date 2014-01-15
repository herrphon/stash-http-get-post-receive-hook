package de.aeffle.stash.plugin.hook;

import java.util.ArrayList;

import com.atlassian.stash.hook.repository.RepositoryHookContext;

public class HttpLocation {
	private static RepositoryHookContext context;
	private final String urlTemplate;
	private String url;
	private final String user;
	private final String pass;
	private final Boolean useAuth;

	public static ArrayList<HttpLocation> getAllFromContext(RepositoryHookContext context) {
		HttpLocation.context = context;
		
		ArrayList<HttpLocation> httpGetLocations = new ArrayList<HttpLocation>();
		
		for (int id = 1; id <= getNumberOfHttpLocations(context); id++) { 
			httpGetLocations.add(new HttpLocation(id));
		}
		
		return httpGetLocations;
	}
	
	private HttpLocation(int id) {
		String urlString = ( id > 1 ? "url" + id : "url" );
		
		String useAuthString = ( id > 1 ? "use_auth" + id : "use_auth" );
		if (getVersionNumber() > 1) {
			useAuthString = ( id > 1 ? "useAuth" + id : "useAuth" );
		}
		
		String userString = ( id > 1 ? "user" + id : "user" );
		String passString = ( id > 1 ? "pass" + id : "pass" );
		
		urlTemplate = getConfigString(urlString);
		url = urlTemplate;
		
		useAuth = getConfigBoolean(useAuthString);
		user = getConfigString(userString);
		pass = getConfigString(passString);
	}

	private int getVersionNumber() {
		int version;
		try {
			String versionString = context.getSettings().getString("version", "1");
			version = Integer.parseInt(versionString); 
		} catch (Exception e) {
			version = 1;
		}
		return version;
	}

	private String getConfigString(String name) {
		return context.getSettings().getString(name, "");
	}

	private boolean getConfigBoolean(String name) {
		return context.getSettings().getBoolean(name, false);
	}
	
	private static int getNumberOfHttpLocations(RepositoryHookContext context) {
		 int count;
		 try {
			 count = Integer.parseInt(context.getSettings().getString("locationCount", "1"));
		 } catch (Exception e) {
			 count = 1;
		 }
		 
		 return (count > 0 ? count : 1);
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