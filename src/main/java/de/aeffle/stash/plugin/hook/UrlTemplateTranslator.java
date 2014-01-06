package de.aeffle.stash.plugin.hook;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;

import com.atlassian.stash.hook.repository.RepositoryHookContext;
import com.atlassian.stash.repository.RefChange;
import com.atlassian.stash.repository.Repository;
import com.atlassian.stash.user.StashAuthenticationContext;
import com.atlassian.stash.user.StashUser;

public class UrlTemplateTranslator {
	private Map<String, String> translationMap;

	public UrlTemplateTranslator(
			StashAuthenticationContext stashAuthenticationContext,
			RepositoryHookContext repositoryHookContext,
			Collection<RefChange> refChanges) {
		translationMap = new HashMap<String, String>();
		
		if (stashAuthenticationContext != null) { 
			addStashAuthenticationContext(stashAuthenticationContext);
		}
		
		if (repositoryHookContext != null) {
			addRepositoryHookContext(repositoryHookContext);
		}
		
		if (refChanges != null) {
			addRefChanges(refChanges);
		}
	}

	public void addRepositoryHookContext(RepositoryHookContext repositoryHookContext) {
		Repository repository = repositoryHookContext.getRepository();
		addTranslation("repository.id", repository.getId().toString());
		addTranslation("repository.name", repository.getName());
		addTranslation("repository.slug", repository.getSlug());
		addTranslation("project.name", repository.getProject().getName());
		addTranslation("project.key", repository.getProject().getKey());
	}

	public void addStashAuthenticationContext(StashAuthenticationContext stashAuthenticationContext) {
		StashUser user = stashAuthenticationContext.getCurrentUser();
		addTranslation("user.name", user.getName());
		addTranslation("user.displayName", user.getDisplayName());
		addTranslation("user.email", user.getEmailAddress());
	}
	
	public void addRefChanges(Collection<RefChange> refChanges) {
		Iterator<RefChange> iterator = refChanges.iterator();
		while (iterator.hasNext()) {
			RefChange refChange = iterator.next();
			refChange.getRefId();

			addTranslation("somthing",  "with a value");
		}
	}
	
	private void addTranslation(String key, String value) {
		translationMap.put(key, value);
	}

	public void transform(HttpLocation httpLocation) {
		StrSubstitutor substitutor = new StrSubstitutor(translationMap);
		String urlTemplate = httpLocation.getUrlTemplateString();
		String url = substitutor.replace(urlTemplate);
		httpLocation.setUrl(url);
	}

}
