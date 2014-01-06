package ut.de.aeffle.stash.plugin.hook;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.junit.Test;

import ut.de.aeffle.stash.plugin.hook.helper.RepositoryHookContextMockFactory;
import de.aeffle.stash.plugin.hook.HttpAgent;
import de.aeffle.stash.plugin.hook.HttpLocation;

public class HttpAgentTest {

	
	private HttpLocation getHttpLocationMockWithAuth() {
		RepositoryHookContextMockFactory contextFactory = new RepositoryHookContextMockFactory();
		contextFactory.prepareIntSetting("version", 2);
		contextFactory.prepareBooleanSetting("useAuth", 1, true);
		contextFactory.prepareStringSetting("user", 1, "john.doe");
		contextFactory.prepareStringSetting("pass", 1, "secret");

		return contextFactory.getFirstHttpLocation();
	}

	private HttpLocation getHttpLocationMockWithoutAuth() {
		RepositoryHookContextMockFactory contextFactory = new RepositoryHookContextMockFactory();
		contextFactory.prepareIntSetting("version", 2);
		contextFactory.prepareBooleanSetting("useAuth", 1, false);

		return contextFactory.getFirstHttpLocation();
	}
	
	
	@Test
	public void testDoPageRequestWithoutAuth() {
		HttpLocation httpLocation = getHttpLocationMockWithoutAuth();
		HttpAgent httpAgent = new HttpAgent(httpLocation);


		try {
			HttpURLConnection connection = mock(HttpURLConnection.class);
			when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
			
			httpAgent.setConnection(connection);
			httpAgent.doPageRequest();

			verify(connection, times(1)).connect();
			verify(connection, never()).setRequestProperty(anyString(), anyString());
			
		} catch (IOException e) {
			fail("IOException");
		}
	}
	
	@Test
	public void testDoPageRequestWithAuth() {
		HttpLocation httpLocation = getHttpLocationMockWithAuth();
		HttpAgent httpAgent = new HttpAgent(httpLocation);


		try {
			HttpURLConnection connection = mock(HttpURLConnection.class);
			when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
			
			httpAgent.setConnection(connection);
			httpAgent.doPageRequest();

			verify(connection, times(1)).connect();
			verify(connection, times(1)).setRequestProperty("Authorization", "Basic am9obi5kb2U6c2VjcmV0");
			
		} catch (IOException e) {
			fail("IOException");
		}
	}

	@Test
	public void testDoBadPageRequestWithAuth() {
		HttpLocation httpLocation = getHttpLocationMockWithAuth();
		HttpAgent httpAgent = new HttpAgent(httpLocation);


		try {
			HttpURLConnection connection = mock(HttpURLConnection.class);
			when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
			
			httpAgent.setConnection(connection);
			httpAgent.doPageRequest();

			verify(connection, times(1)).connect();
			verify(connection, times(1)).setRequestProperty("Authorization", "Basic am9obi5kb2U6c2VjcmV0");
			
		} catch (IOException e) {
			fail("IOException");
		}
	}


}
