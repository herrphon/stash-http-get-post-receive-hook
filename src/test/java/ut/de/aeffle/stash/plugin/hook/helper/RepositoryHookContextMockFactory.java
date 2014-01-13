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
		prepareIntSetting("version", null);
		when(repository.getProject()).thenReturn(project);
	}

	public void prepareStringSetting(String parameter, int id, String value) {
		String parameterName = (id > 1 ? parameter + id : parameter);
		prepareStringSetting(parameterName, value);
	}
	
	public void prepareLocationCount(String value) {
		String key = "locationCount";
		if (value != null) {
			when(settings.getString(key, "1")).thenReturn(value);
		} else {
			when(settings.getString(key, "1")).thenReturn("");
		}
	}

	public void prepareStringSetting(String key, String value) {
		if (value != null) {
			when(settings.getString(key, "")).thenReturn(value);
		} else {
			when(settings.getString(key, "")).thenReturn("");
		}
	}
	
	public void prepareVersion(String value) {
		String key = "version";
		if (value != null) {
			when(settings.getString(key, "1")).thenReturn(value);
		} else {
			when(settings.getString(key, "1")).thenReturn("1");
		}
	}

	public void prepareBooleanSetting(String parameter, int id, Boolean value) {
		String parameterName = (id > 1 ? parameter + id : parameter);
		prepareBooleanSetting(parameterName, value);
	}

	public void prepareBooleanSetting(String key, Boolean value) {
		if (value != null) {
			when(settings.getBoolean(key, false)).thenReturn(value);
		} else {
			when(settings.getBoolean(key, false)).thenReturn(false);
		}
	}

	public void prepareIntSetting(String parameter, int id, Integer value) {
		String parameterName = (id > 1 ? parameter + id : parameter);
		prepareIntSetting(parameterName, value);
	}

	public void prepareIntSetting(String key, Integer value) {
		if (value != null) {
			when(settings.getInt(key, 1)).thenReturn(value);
		} else {
			when(settings.getInt(key, 1)).thenReturn(1);
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
