package ut.de.aeffle.stash.plugin.hook;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.atlassian.stash.hook.repository.RepositoryHookContext;
import com.atlassian.stash.setting.Settings;
import com.atlassian.stash.user.StashAuthenticationContext;
import com.atlassian.stash.user.StashUser;

import de.aeffle.stash.plugin.hook.HttpLocation;
import de.aeffle.stash.plugin.hook.UrlTemplateTranslator;

public class UrlTemplateTranslatorTest {
	
	private HttpLocation httpLocation;
	private StashAuthenticationContext authenticationContext;
	
	@Before
	public void setUp() throws Exception {
		RepositoryHookContext contextMock = mock(RepositoryHookContext.class);
		Settings settings = mock(Settings.class);
		when(contextMock.getSettings()).thenReturn(settings);
		when(settings.getString("url")).thenReturn("http://doe.com/${userName}");
		
		Collection<HttpLocation> httpLocations = HttpLocation.getAllFromContext(contextMock);
		
		for (HttpLocation httpLocation: httpLocations) {
			this.httpLocation = httpLocation;
		}

		authenticationContext = mock(StashAuthenticationContext.class);
		StashUser user = mock(StashUser.class);
		when(authenticationContext.getCurrentUser()).thenReturn(user);
		when(user.getDisplayName()).thenReturn("John Doe");
		when(user.getEmailAddress()).thenReturn("john@doe.com");
		when(user.getName()).thenReturn("john.doe");
	}

	@Test
	public void translationTest() {
		UrlTemplateTranslator translator = new UrlTemplateTranslator();
		translator.addStashAuthenticationContext(authenticationContext);
		translator.transform(httpLocation);

		assertEquals("http://doe.com/john.doe", httpLocation.getUrl());
		
	}
}
