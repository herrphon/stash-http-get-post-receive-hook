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
	private final StashAuthenticationContext stashAuthenticationContext;

	public HttpGetPostReceiveHook(
			StashAuthenticationContext authenticationContext) {
		this.stashAuthenticationContext = authenticationContext;
	}

	/**
	 * Connects to a configured URL to notify of all changes.
	 */
	@Override
	public void postReceive(RepositoryHookContext repositoryHookContext,
			Collection<RefChange> refChanges) {
		log.debug("Http Get Post Receive Hook started.");
		log.debug("User: " + this.stashAuthenticationContext.getCurrentUser().getName());

		Collection<HttpLocation> httpLocations = HttpLocation.getAllFromContext(repositoryHookContext);
		
		int size = httpLocations.size();
		log.debug("Number of HttpLocations: " + size);
		
		for (HttpLocation httpLocation : httpLocations) {
			UrlTemplateTranslator translator = new UrlTemplateTranslator(stashAuthenticationContext, repositoryHookContext, refChanges);

			translator.transform(httpLocation);

			HttpAgent httpAgent = new HttpAgent(httpLocation);
			httpAgent.doPageRequest();
		}

	}

	@Override
	public void validate(Settings settings, SettingsValidationErrors errors,
			Repository repository) {

		int locationCount;
		try {
			locationCount = Integer.parseInt(settings.getString("locationCount", "1"));
		} catch (Exception e) {
			locationCount = 1;
		}

		if (locationCountIsSmallerThanOne(locationCount)
				|| locationCountIsLargerThanLimit(locationCount, 10)) {
			errors.addFieldError("locationCount",
					"Location has to be in the range from 1 to 10.");
		} else {
			for (int i = 1; i <= locationCount; i++) {
				validateUrl(i, settings, errors);
			}
		}
	}

	private void validateUrl(int id, Settings settings,
			SettingsValidationErrors errors) {
		String urlName = (id > 1 ? "url" + id : "url");
		String urlString = settings.getString(urlName, "");

		if (urlString.isEmpty()) {
			errors.addFieldError(urlName,
					"Url field is blank, please supply one.");
		} else {
			try {
				URL url = new URL(urlString);
				String protocol = url.getProtocol();
				List<String> validProtocols = new ArrayList<String>();
				validProtocols.add("http");
				validProtocols.add("https");

				if (!validProtocols.contains(protocol)) {
					errors.addFieldError(urlName,
							"Url did not contain a valid http(s) URL.");
				}
			} catch (MalformedURLException e) {
				errors.addFieldError(urlName, "Url was malformed.");
			}
		}
	}

	private boolean locationCountIsSmallerThanOne(int locationCount) {
		if (locationCount < 1) {
			return true;
		}
		return false;
	}

	private boolean locationCountIsLargerThanLimit(int locationCount, int limit) {
		if (locationCount > limit) {
			return true;
		}
		return false;
	}
}