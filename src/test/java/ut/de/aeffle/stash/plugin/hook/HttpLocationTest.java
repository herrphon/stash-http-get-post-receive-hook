package ut.de.aeffle.stash.plugin.hook;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.atlassian.stash.hook.repository.RepositoryHookContext;
import com.atlassian.stash.setting.Settings;

import de.aeffle.stash.plugin.hook.HttpLocation;

public class HttpLocationTest { 
	
	private HttpLocation httpLocation;
	private Settings settings;

	@Before
	public void setUp() throws Exception {
		RepositoryHookContext contextMock = mock(RepositoryHookContext.class);
		this.settings = mock(Settings.class);
		when(contextMock.getSettings()).thenReturn(settings);
		when(settings.getString("url")).thenReturn("http://aeffle.de");
		when(settings.getString("user")).thenReturn("john.doe");
		when(settings.getString("pass")).thenReturn("secret");
		
		Collection<HttpLocation> httpLocations = HttpLocation.getAllFromContext(contextMock);
		
		for (HttpLocation httpLocation: httpLocations) {
			this.httpLocation = httpLocation;
		}
	}

	@Test
	public void testGetUrl() {
		assertEquals("http://aeffle.de", httpLocation.getUrl());
	}

	@Test
	public void testGetUser() {
		assertEquals("john.doe", httpLocation.getUser());
	}

	@Test
	public void testGetPass() {
		assertEquals("secret", httpLocation.getPass());
	}

	@Test
	public void testGetUseAuth() {
		when(settings.getBoolean("use_auth")).thenReturn(null);
		assertEquals(Boolean.FALSE, httpLocation.getUseAuth());
		
		when(settings.getBoolean("use_auth")).thenReturn(Boolean.FALSE);
		assertEquals(Boolean.FALSE, httpLocation.getUseAuth());

		when(settings.getBoolean("use_auth")).thenReturn(Boolean.TRUE);
		assertEquals(Boolean.TRUE, httpLocation.getUseAuth());
	}

}
