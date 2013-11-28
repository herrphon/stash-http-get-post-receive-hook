package de.aeffle.stash.plugin.hook;

import com.atlassian.extras.common.log.Logger;

import com.atlassian.stash.hook.repository.*;
import com.atlassian.stash.repository.*;
import com.atlassian.stash.setting.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HttpGetPostReceiveHook implements AsyncPostReceiveRepositoryHook,
		RepositorySettingsValidator {

	private static final Logger.Log log = Logger
			.getInstance(HttpGetPostReceiveHook.class);

	/**
	 * Connects to a configured URL to notify of all changes.
	 */
	@Override
	public void postReceive(RepositoryHookContext context,
			Collection<RefChange> refChanges) {
		log.debug("Post-receive hook started:");

		StashConfig stashConfig = new StashConfig(context);
		String url = stashConfig.getUrl();
		String user = stashConfig.getUser();
		String pass = stashConfig.getPass();
		Boolean useAuth = stashConfig.getUseAuth();

		if (url != null) {
			log.info("The following URL was found: " + url);

			try {
				HttpHandler httpHandler = new HttpHandler(url);

				if (useAuth == true) {
					httpHandler.authenticate(user, pass);
				}

				httpHandler.doPageRequest();

			} catch (MalformedURLException e) {
				log.error("Malformed URL:" + e);
			} catch (IOException e) {
				log.error("Some IO exception occured", e);
			} catch (Exception e) {
				log.error("Something else went wrong: ", e);
			}
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