package de.aeffle.stash.plugin.hook;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;

import com.atlassian.stash.hook.repository.RepositoryHookContext;
import com.atlassian.stash.repository.RefChange;
import com.atlassian.stash.repository.Repository;

public class UrlTemplateTranslator {
	private Map<String, String> translationMap;

	public UrlTemplateTranslator() {
		translationMap = new HashMap<String, String>();
	}

	public void addStashRepositoryHookContext(RepositoryHookContext context) {
		Repository repository = context.getRepository();
		addTranslation("repositoryId", repository.getId().toString());
		addTranslation("repository", repository.getName());
		addTranslation("projectName", repository.getProject().getName());
		addTranslation("projectKey", repository.getProject().getKey());
		addTranslation("slug", repository.getSlug());
	}

	public void addStashRefChanges(Collection<RefChange> refChanges) {
		Iterator<RefChange> iterator = refChanges.iterator();
		while (iterator.hasNext()) {
			RefChange refChange = iterator.next();
			refChange.getRefId();

			addTranslation("somthing",  "with a value");
		}
	}
	
	public void addTranslation(String key, String value) {
		translationMap.put(key, value);
	}

	public String getUrlFromTemplate(String template) {
		StrSubstitutor substitutor = new StrSubstitutor(translationMap);
		return substitutor.replace(template);
	}

}
