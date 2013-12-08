package de.aeffle.stash.plugin.hook;

import com.atlassian.extras.common.log.Logger;
import com.atlassian.stash.hook.repository.*;
import com.atlassian.stash.repository.*;
import com.atlassian.stash.setting.*;
import com.atlassian.stash.user.StashAuthenticationContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HttpGetPostReceiveHook implements AsyncPostReceiveRepositoryHook,
		RepositorySettingsValidator {

	private static final Logger.Log log = Logger
			.getInstance(HttpGetPostReceiveHook.class);
	private final StashAuthenticationContext authenticationContext;

	public HttpGetPostReceiveHook(StashAuthenticationContext authenticationContext) {
		this.authenticationContext = authenticationContext;
		log.error("User: " + this.authenticationContext.getCurrentUser().getName());
	}
	
	
	/**
	 * Connects to a configured URL to notify of all changes.
	 */
	@Override
	public void postReceive(RepositoryHookContext context,
			Collection<RefChange> refChanges) {
		log.debug("Http Get Post Receive Hook started.");

		Collection<HttpLocation> httpLocations = HttpLocation.getAllFromContext(context);
		
		for(HttpLocation httpLocation: httpLocations){

			HttpAgent httpAgent = new HttpAgent(httpLocation);
			httpAgent.doPageRequest();
		}
		
	}

	@Override
	public void validate(Settings settings, SettingsValidationErrors errors,
			Repository repository) {
		String urlString = settings.getString("url", "");
		if (urlString.isEmpty()) {
			errors.addFieldError("url",
					"Url field is blank, please supply one.");
		} else {
			try {
				URL url = new URL(urlString);
				String protocol = url.getProtocol();
				List<String> validProtocols = new ArrayList<String>();
				validProtocols.add("http");
				validProtocols.add("https");

				if (!validProtocols.contains(protocol)) {
					errors.addFieldError("url",
							"Url did not contain a valid http(s) URL.");
				}
			} catch (MalformedURLException e) {
				errors.addFieldError("url", "Url was malformed.");
			}
		}

	}
}