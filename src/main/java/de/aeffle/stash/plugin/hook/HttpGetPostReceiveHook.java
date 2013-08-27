package de.aeffle.stash.plugin.hook;

import com.atlassian.stash.hook.repository.*;
import com.atlassian.stash.repository.*;
import com.atlassian.stash.setting.*;

import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import org.apache.commons.codec.binary.Base64;

public class HttpGetPostReceiveHook implements AsyncPostReceiveRepositoryHook, RepositorySettingsValidator {
	/**
	 * Connects to a configured URL to notify of all changes.
	 */
	@Override
	public void postReceive(RepositoryHookContext context, Collection<RefChange> refChanges) {
		String  url      = context.getSettings().getString("url");
		String  user     = context.getSettings().getString("user");
		String  pass     = context.getSettings().getString("pass");
		Boolean use_auth = context.getSettings().getBoolean("use_auth");
		
		if (url != null) {
			try {
				URL the_url = new URL(url);
				URLConnection connection = (URLConnection) the_url.openConnection();

				if ( use_auth == true ) {
                    // build the auth string
                    String authString = user + ":" + pass;
                    String authStringEnc = new String(Base64.encodeBase64(authString.getBytes()));
                    connection.setRequestProperty("Authorization", "Basic " + authStringEnc);
				}
				
				// get the stream  and close it again
				connection.getInputStream().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void validate(Settings settings, SettingsValidationErrors errors,
			Repository repository) {
		if (settings.getString("url", "").isEmpty()) {
			errors.addFieldError("url", "Url field is blank, please supply one");
		}
	}
}