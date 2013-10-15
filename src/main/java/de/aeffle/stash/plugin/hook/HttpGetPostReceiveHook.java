package de.aeffle.stash.plugin.hook;

import com.atlassian.extras.common.log.Logger;
import com.atlassian.stash.hook.repository.*;
import com.atlassian.stash.repository.*;
import com.atlassian.stash.setting.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

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

        String urlString = context.getSettings().getString("url");
        String user = context.getSettings().getString("user");
        String pass = context.getSettings().getString("pass");
        Boolean useAuth = context.getSettings().getBoolean("use_auth");
        if (useAuth == null) {
            useAuth = Boolean.FALSE;
        }

        if (urlString != null) {
            log.info("The following URL was found: " + urlString);

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(5000);
                connection.setInstanceFollowRedirects(true); 
                HttpURLConnection.setFollowRedirects(true);

                if (useAuth == true) {
                    log.info("Authentication was enabled with user: " + user);
                    // build the auth string
                    String authString = user + ":" + pass;
                    String authStringEnc = new String(
                            Base64.encodeBase64(authString.getBytes()));
                    connection.setRequestProperty("Authorization", "Basic "
                            + authStringEnc);
                }

                connection.connect();

                // Get HTTP Response Code
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    log.error("Problem with the HTTP connection with response code: "
                            + responseCode);
                }

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

                log.debug("HTTP response:\n" + body.toString());
            } catch (MalformedURLException e) {
                log.error("Malformed URL:" + e);
            } 
            catch (IOException e) { 
                log.error("Some IO exception occured", e);
            }
            catch (Exception e) {
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