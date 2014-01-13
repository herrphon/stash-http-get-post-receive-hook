package ut.de.aeffle.stash.plugin.hook;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.atlassian.stash.repository.Repository;
import com.atlassian.stash.setting.Settings;
import com.atlassian.stash.setting.SettingsValidationErrors;
import com.atlassian.stash.user.StashAuthenticationContext;

import de.aeffle.stash.plugin.hook.HttpGetPostReceiveHook;


public class HttpGetPostReceiveHookTest {
	
	private HttpGetPostReceiveHook httpGetPostReceiveHook;
	
	private Settings settings;
	private SettingsValidationErrors errors;
	private Repository repository;

	@Before
	public void beforeTestCreateCleanPlayground() {
		StashAuthenticationContext stashAuthenticationContext = mock(StashAuthenticationContext.class);
		httpGetPostReceiveHook = new HttpGetPostReceiveHook(stashAuthenticationContext);
		
		settings = mock(Settings.class);
		errors = mock(SettingsValidationErrors.class);
		repository = mock(Repository.class);		
	}

	private void setLocationCount(String count) {
		when(settings.getString("locationCount", "1")).thenReturn(count);
	}
	
	private void setUrl(int id, String url) {
		String urlName = ( id > 1 ? "url" + id : "url" );
		when(settings.getString(urlName, "")).thenReturn(url);
	}
	
	@Test
	public void testValidateWithEmptyUrl() {
		setLocationCount("1");
		setUrl(1, "");

		httpGetPostReceiveHook.validate(settings, errors, repository);
		
		verify(errors, times(1)).addFieldError(eq("url"), anyString());
	}

	
	@Test
	public void testValidateWithInvalidUrl() {
		setLocationCount("1");
		setUrl(1, "not a valid url");
		
		httpGetPostReceiveHook.validate(settings, errors, repository);

		verify(errors, atLeastOnce()).addFieldError(eq("url"), anyString());
	}
	
	@Test
	public void testValidateWithValidUrl() {
		setLocationCount("1");
		setUrl(1, "http://www.testing.com/valid.url");
		
		httpGetPostReceiveHook.validate(settings, errors, repository);

		verify(errors, never()).addFieldError(anyString(), anyString());
	}
	
	@Test
	public void testValidateWithEmptyUrl2() {
		setLocationCount("2");
		setUrl(1, "");
		setUrl(2, "");
		
		httpGetPostReceiveHook.validate(settings, errors, repository);
		
		verify(errors, times(1)).addFieldError(eq("url2"), anyString());
	}

	
	@Test
	public void testValidateWithInvalidUrl2() {
		setLocationCount("2");
		setUrl(1, "");
		setUrl(2, "not a valid url");
		
		httpGetPostReceiveHook.validate(settings, errors, repository);

		verify(errors, atLeastOnce()).addFieldError(eq("url2"), anyString());
	}

	@Test
	public void testValidateWith2ValidUrl() {
		setLocationCount("2");
		setUrl(1, "http://www.testing.com/valid.url");
		setUrl(2, "http://www.testing.com/valid.url");
		
		httpGetPostReceiveHook.validate(settings, errors, repository);

		verify(errors, never()).addFieldError(anyString(), anyString());
	}
	
	@Test
	public void testValidateWithEmptyUrl3() {
		setLocationCount("3");
		setUrl(1, "");
		setUrl(2, "");
		setUrl(3, "");
		
		httpGetPostReceiveHook.validate(settings, errors, repository);
		
		verify(errors, times(1)).addFieldError(eq("url3"), anyString());
	}

	
	@Test
	public void testValidateWithInvalidUrl3() {
		setLocationCount("3");
		setUrl(1, "");
		setUrl(2, "");
		setUrl(3, "not a valid url");
		
		httpGetPostReceiveHook.validate(settings, errors, repository);

		verify(errors, atLeastOnce()).addFieldError(eq("url3"), anyString());
	}

	@Test
	public void testValidateWith3ValidUrls() {
		setLocationCount("3");
		setUrl(1, "http://www.testing.com/valid.url");
		setUrl(2, "http://www.testing.com/valid.url");
		setUrl(3, "http://www.testing.com/valid.url");
		
		httpGetPostReceiveHook.validate(settings, errors, repository);

		verify(errors, never()).addFieldError(anyString(), anyString());
	}
	
	@Test
	public void testValidateWithInvalidCount() {
		setLocationCount("-1");
		
		httpGetPostReceiveHook.validate(settings, errors, repository);

		verify(errors, times(1)).addFieldError(eq("locationCount"), anyString());
	}
	
	@Test
	public void testValidateWithInvalidCount2() {
		setLocationCount("11");
		for (int i = 1; i <= 10; i++) {
			setUrl(i, "");
		}
		
		httpGetPostReceiveHook.validate(settings, errors, repository);

		verify(errors, times(1)).addFieldError(eq("locationCount"), anyString());
	}
}
