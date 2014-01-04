package ut.de.aeffle.stash.plugin.hook;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.atlassian.stash.hook.repository.RepositoryHookContext;
import com.atlassian.stash.repository.Repository;
import com.atlassian.stash.setting.Settings;

import de.aeffle.stash.plugin.hook.HttpLocation;

public class HttpLocationTest { 
	private ContextFactory contextFactory;


    class ContextFactory {
    	private Repository repository;
    	private Settings settings;
    	
	    public ContextFactory() {
	    	repository = mock(Repository.class);
	    	settings = mock(Settings.class);
	    }
	    
	    public ContextFactory clear() {
	    	repository = mock(Repository.class);
	    	settings = mock(Settings.class);
	    	return this;
	    }
		 
		public void addStringSetting(String key, String value) {
			when(settings.getString(key)).thenReturn(value); 
		}
		
		public void addBooleanSetting(String key, Boolean value) {
			when(settings.getBoolean(key)).thenReturn(value);
		}

		public RepositoryHookContext getContext() {
		    return new RepositoryHookContext(repository, settings);
		}
	}
	
    @Before
    public void clearSettings() {
    	contextFactory = new ContextFactory();
    }

	@Test
	public void testGetUrl() {
		contextFactory.addStringSetting("url", "http://aeffle.de");
		RepositoryHookContext context = contextFactory.getContext();
		Collection<HttpLocation> httpLocations = HttpLocation.getAllFromContext(context);

		HttpLocation httpLocation = httpLocations.iterator().next();
		assertEquals("http://aeffle.de", httpLocation.getUrl());
	}

	@Test
	public void testGetUser() {
		contextFactory.addStringSetting("user", "john.doe");
		
		RepositoryHookContext context = contextFactory.getContext();
		Collection<HttpLocation> httpLocations = HttpLocation.getAllFromContext(context);

		HttpLocation httpLocation = httpLocations.iterator().next();
		
		assertEquals("john.doe", httpLocation.getUser());
	}

	@Test
	public void testGetPass() {
		contextFactory.addStringSetting("pass", "secret");
		
		RepositoryHookContext context = contextFactory.getContext();
		Collection<HttpLocation> httpLocations = HttpLocation.getAllFromContext(context);

		HttpLocation httpLocation = httpLocations.iterator().next();
		
		assertEquals("secret", httpLocation.getPass());
	}

	@Test
	public void testGetUseAuthNull() {
		contextFactory.addBooleanSetting("use_auth", null);
		
		RepositoryHookContext context = contextFactory.getContext();
		Collection<HttpLocation> httpLocations = HttpLocation.getAllFromContext(context);

		HttpLocation httpLocation = httpLocations.iterator().next();

		assertEquals(Boolean.FALSE, httpLocation.getUseAuth());
	}
	
	@Test
	public void testGetUseAuthFalse() {
		contextFactory.addBooleanSetting("use_auth", Boolean.FALSE);
		
		RepositoryHookContext context = contextFactory.getContext();
		Collection<HttpLocation> httpLocations = HttpLocation.getAllFromContext(context);

		HttpLocation httpLocation = httpLocations.iterator().next();
		
		assertEquals(Boolean.FALSE, httpLocation.getUseAuth());
	}

	@Test
	public void testGetUseAuthTrue() {
		contextFactory.addBooleanSetting("use_auth", Boolean.TRUE);
		
		RepositoryHookContext context = contextFactory.getContext();
		Collection<HttpLocation> httpLocations = HttpLocation.getAllFromContext(context);

		HttpLocation httpLocation = httpLocations.iterator().next();
			
		assertEquals(Boolean.TRUE, httpLocation.getUseAuth());
	}

}
