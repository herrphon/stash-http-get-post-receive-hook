package ut.de.aeffle.stash.plugin.hook;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.atlassian.stash.hook.repository.RepositoryHookContext;
import com.atlassian.stash.setting.Settings;

import de.aeffle.stash.plugin.hook.StashConfig;

public class StashConfigTest { 
	
	private StashConfig stashConfig;
	private Settings settings;

	@Before
	public void setUp() throws Exception {
		RepositoryHookContext contextMock = mock(RepositoryHookContext.class);
		stashConfig = new StashConfig(contextMock);

		settings = mock(Settings.class);
		
		when(contextMock.getSettings()).thenReturn(settings);
	}

	@Test
	public void testGetUrl() {
		when(settings.getString("url")).thenReturn("http://aeffle.de");
		assertEquals("http://aeffle.de", stashConfig.getUrl());
	}

	@Test
	public void testGetUser() {
		when(settings.getString("user")).thenReturn("john.doe");
		assertEquals("john.doe", stashConfig.getUser());
	}

	@Test
	public void testGetPass() {
		when(settings.getString("pass")).thenReturn("secret");
		assertEquals("secret", stashConfig.getPass());
	}

	@Test
	public void testGetUseAuth() {
		when(settings.getBoolean("use_auth")).thenReturn(null);
		assertEquals(Boolean.FALSE, stashConfig.getUseAuth());
		
		when(settings.getBoolean("use_auth")).thenReturn(Boolean.FALSE);
		assertEquals(Boolean.FALSE, stashConfig.getUseAuth());

		when(settings.getBoolean("use_auth")).thenReturn(Boolean.TRUE);
		assertEquals(Boolean.TRUE, stashConfig.getUseAuth());
	}

}
