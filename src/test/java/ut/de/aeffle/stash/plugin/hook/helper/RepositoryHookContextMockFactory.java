package ut.de.aeffle.stash.plugin.hook.helper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.mockito.Matchers;

import com.atlassian.stash.hook.repository.RepositoryHookContext;
import com.atlassian.stash.project.Project;
import com.atlassian.stash.repository.Repository;
import com.atlassian.stash.setting.Settings;

import de.aeffle.stash.plugin.hook.HttpLocation;

public class RepositoryHookContextMockFactory {
	private Settings settings;
	private Repository repository;
	private Project project;

	public RepositoryHookContextMockFactory() {
		clear();
	}

	public RepositoryHookContextMockFactory clear() {
		createNewMocks();
		loadDefaults();
		return this;
	}

	private void createNewMocks() {
		settings = mock(Settings.class);
		repository = mock(Repository.class);
		project = mock(Project.class);
	}

	private void loadDefaults() {
		setVersion("1");
		when(repository.getProject()).thenReturn(project);
	}

	public void setVersion(String version) {
		setStringSetting("version", 0, version, "1");
	}

	public void setLocationCount(String count) {
		setStringSetting("locationCount", 0, count, "1");
	}

	public void setUrl(int id, String url) {
		setStringSetting("url", id, url, "");
	}
	
	public void setUseAuth(int id, Boolean useAuth) {
		setBooleanSetting("useAuth", id, useAuth);
	}
	
	public void setOldUseAuth(Boolean useAuth) {
		setBooleanSetting("use_auth", 0, useAuth);
	}
	
	public void setUser(int id, String user) {
		setStringSetting("user", id, user, "");
	}
	
	public void setPass(int id, String pass) {
		setStringSetting("pass", id, pass, "");
	}
	
	

	private void setStringSetting(String parameter, int id, String value, String defaultValue) {
		String parameterName = (id > 1 ? parameter + id : parameter);
		
		if (value != null) {
			when(settings.getString(parameterName, defaultValue)).thenReturn(value);
		} else {
			when(settings.getString(parameterName, defaultValue)).thenReturn(defaultValue);
		}	
	}
	
	private void setBooleanSetting(String parameter, int id, Boolean value) {
		String parameterName = (id > 1 ? parameter + id : parameter);

		if (value != null) {
			when(settings.getBoolean(parameterName, false)).thenReturn(value);
		} else {
			when(settings.getBoolean(parameterName, false)).thenReturn(false);
		}
	}

	// getInt does not work....
	@SuppressWarnings("unused")
	private void prepareIntSetting(String parameter, int id, Integer value) {
		String parameterName = (id > 1 ? parameter + id : parameter);

		if (value != null) {
			when(settings.getInt(parameterName, 1)).thenReturn(value);
		} else {
			when(settings.getInt(parameterName, 1)).thenReturn(1);
		}
	}

	
	public RepositoryHookContext getContext() {
		return new RepositoryHookContext(repository, settings);
	}
	
    public ArrayList<HttpLocation> getHttpLocations() {
	    RepositoryHookContext context = getContext();
	    ArrayList<HttpLocation> httpLocations = HttpLocation.getAllFromContext(context);

	    return httpLocations;
    }
    

    public HttpLocation getFirstHttpLocation() {
	    return getHttpLocation(1);
    }

    public HttpLocation getHttpLocation(int id) {
    	int i = id - 1;
    	return getHttpLocations().get(i);
    }

	public void setRepositoryId(Integer i) {
		when(repository.getId()).thenReturn(i);
		
	}

	public void setRepositoryName(String name) {
		when(repository.getName()).thenReturn(name);
	}

	public void setRepositorySlug(String slug) {
		when(repository.getSlug()).thenReturn(slug);
	}

	public void setProjectKey(String key) {
		when(project.getKey()).thenReturn(key);
	}

	public void setProjectName(String name) {
		when(project.getName()).thenReturn(name);
	}

}
