package de.aeffle.stash.plugin.hook;

import com.atlassian.stash.hook.repository.RepositoryHookContext;

public class StashConfig {
	private final RepositoryHookContext context;

	public StashConfig(RepositoryHookContext context) {
		this.context = context;
	}

	public String getUrl() {
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