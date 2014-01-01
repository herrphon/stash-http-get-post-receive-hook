package de.aeffle.stash.plugin.hook;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.atlassian.extras.common.log.Logger;
import com.atlassian.stash.hook.repository.AsyncPostReceiveRepositoryHook;
import com.atlassian.stash.hook.repository.RepositoryHookContext;
import com.atlassian.stash.repository.RefChange;
import com.atlassian.stash.repository.Repository;
import com.atlassian.stash.setting.RepositorySettingsValidator;
import com.atlassian.stash.setting.Settings;
import com.atlassian.stash.setting.SettingsValidationErrors;
import com.atlassian.stash.user.StashAuthenticationContext;

public class HttpGetPostReceiveHook implements AsyncPostReceiveRepositoryHook,
		RepositorySettingsValidator {

	private static final Logger.Log log = Logger
			.getInstance(HttpGetPostReceiveHook.class);
	private final StashAuthenticationContext authenticationContext;

	public HttpGetPostReceiveHook(StashAuthenticationContext authenticationContext) {
		this.authenticationContext = authenticationContext;
	}
	
	
	/**
	 * Connects to a configured URL to notify of all changes.
	 */
	@Override
	public void postReceive(RepositoryHookContext context,
			Collection<RefChange> refChanges) {
		log.debug("Http Get Post Receive Hook started.");
		log.debug("User: " + this.authenticationContext.getCurrentUser().getName());
		
		Collection<HttpLocation> httpLocations = HttpLocation.getAllFromContext(context);
		
		for(HttpLocation httpLocation: httpLocations){
			UrlTemplateTranslator urlTemplateTranslator = new UrlTemplateTranslator();
			urlTemplateTranslator.addStashAuthenticationContext(authenticationContext);
			urlTemplateTranslator.transform(httpLocation);
			
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
		
		String rowCount = settings.getString("rowCount", "1");
		if (rowCountIsSmallerThanOne(rowCount)) {
			errors.addFieldError("rowCount", "Row cannot be smaller than 1.");
		}
		if (rowCountIsLargerThanLimit(rowCount, 10)) {
			errors.addFieldError("rowCount", "Row should be less than 10.");
		}
	}
	
	private boolean rowCountIsSmallerThanOne(String rowCountString) {
		int rowCount = Integer.parseInt(rowCountString);

		if (rowCount < 1) {
			return true;
		}
		return false;
	}
	
	private boolean rowCountIsLargerThanLimit(String rowCountString, int limit) {
		int rowCount = Integer.parseInt(rowCountString);		

		if (rowCount > limit) {
			return true;
		}
		return false;
	}
}