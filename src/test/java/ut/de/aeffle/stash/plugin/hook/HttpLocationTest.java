package ut.de.aeffle.stash.plugin.hook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ut.de.aeffle.stash.plugin.hook.helper.RepositoryHookContextMockFactory;
import de.aeffle.stash.plugin.hook.HttpLocation;

public class HttpLocationTest { 
	private RepositoryHookContextMockFactory contextFactory = new RepositoryHookContextMockFactory();

    @Before
    public void beforeTestClearSettings() {
    	contextFactory.clear();
    }

 
	@Test
	public void testGetUrl1() {
		contextFactory.prepareStringSetting("url", 1, "http://aeffle.de");
		
		assertEquals("http://aeffle.de", contextFactory.getFirstHttpLocation().getUrl());
	}

	@Test
	public void testGetUser1() {
		contextFactory.prepareStringSetting("user", 1, "john.doe");
		
		assertEquals("john.doe", contextFactory.getFirstHttpLocation().getUser());
	}

	@Test
	public void testGetPass1() {
		contextFactory.prepareStringSetting("pass", 1, "secret");
		
		assertEquals("secret", contextFactory.getFirstHttpLocation().getPass());
	}

	@Test
	public void testGetUseAuthNullV1() {
		contextFactory.prepareBooleanSetting("use_auth", 1, null);
		
		assertEquals(false, contextFactory.getFirstHttpLocation().getUseAuth());
	}
	
	@Test
	public void testGetUseAuthFalseV1() {
		contextFactory.prepareBooleanSetting("use_auth", 1, false);
		
		assertEquals(false, contextFactory.getFirstHttpLocation().getUseAuth());
	}

	@Test
	public void testGetUseAuthTrueV1() {
		contextFactory.prepareBooleanSetting("use_auth", 1, true);
		
		assertEquals(true, contextFactory.getFirstHttpLocation().getUseAuth());
	}

	@Test
	public void testGetUseAuthTrueV2() {
		contextFactory.prepareIntSetting("version", 2);
		contextFactory.prepareBooleanSetting("useAuth", 1, true);
		
		assertEquals(true, contextFactory.getFirstHttpLocation().getUseAuth());
	}
	
	@Test
	public void testgetNumberOfHttpLocationsNull() {
		contextFactory.prepareIntSetting("locationCount", null);
			
		ArrayList<HttpLocation> httpLocations = contextFactory.getHttpLocations();

		assertEquals(1, httpLocations.size());
   }
	
	@Test
	public void testgetNumberOfHttpLocations1() {
		contextFactory.prepareIntSetting("locationCount", 1);
			
		ArrayList<HttpLocation> httpLocations = contextFactory.getHttpLocations();

		assertEquals(1, httpLocations.size());
   }
	
	@Test
	public void testgetNumberOfHttpLocations3() {
		contextFactory.prepareIntSetting("locationCount", 3);
		
		ArrayList<HttpLocation> httpLocations = contextFactory.getHttpLocations();

		assertEquals(3, httpLocations.size());
   }


	@Test
	public void testGetUrl2() {
		contextFactory.prepareIntSetting("locationCount",  2);
		contextFactory.prepareStringSetting("url", 2, "http://aeffle.de");
		
		ArrayList<HttpLocation> httpLocations = contextFactory.getHttpLocations();
		assertTrue("LocationCount shouldn't be smaller than 2.", httpLocations.size() >= 2);
		
		HttpLocation httpLocation = contextFactory.getHttpLocation(2);		
		assertEquals("http://aeffle.de", httpLocation.getUrl());
	}
	
	@Test
	public void testGetUser2() {
		contextFactory.prepareIntSetting("locationCount",  2);
		contextFactory.prepareStringSetting("user", 2, "john.doe");
		
		ArrayList<HttpLocation> httpLocations = contextFactory.getHttpLocations();
		assertTrue("LocationCount shouldn't be smaller than 2.", httpLocations.size() >= 2);
		
		HttpLocation httpLocation = contextFactory.getHttpLocation(2);		
		
		assertEquals("john.doe", httpLocation.getUser());
	}
	
	@Test
	public void testGetPass2() {
		contextFactory.prepareIntSetting("locationCount",  2);
		contextFactory.prepareStringSetting("pass", 2, "secret");
		
		ArrayList<HttpLocation> httpLocations = contextFactory.getHttpLocations();
		assertTrue("LocationCount shouldn't be smaller than 2.", httpLocations.size() >= 2);
		
		HttpLocation httpLocation = contextFactory.getHttpLocation(2);		
		
		assertEquals("secret", httpLocation.getPass());
	}
	
	@Test
	public void testManyLocations() {
		contextFactory.prepareIntSetting("version", 2);
		contextFactory.prepareIntSetting("locationCount",  3);
		
		contextFactory.prepareStringSetting("url", 1, "http://aeffle.de/1");
		contextFactory.prepareStringSetting("url", 2, "http://aeffle.de/2");
		contextFactory.prepareStringSetting("url", 3, "http://aeffle.de/3");

		contextFactory.prepareBooleanSetting("useAuth", 1, true);
		contextFactory.prepareBooleanSetting("useAuth", 2, true);
		contextFactory.prepareBooleanSetting("useAuth", 3, true);

		contextFactory.prepareStringSetting("user", 1, "john.doe1");
		contextFactory.prepareStringSetting("user", 2, "john.doe2");
		contextFactory.prepareStringSetting("user", 3, "john.doe3");

		contextFactory.prepareStringSetting("pass", 1, "secret1");
		contextFactory.prepareStringSetting("pass", 2, "secret2");
		contextFactory.prepareStringSetting("pass", 3, "secret3");
		
		ArrayList<HttpLocation> httpLocations = contextFactory.getHttpLocations();
		
		assertTrue("LocationCount shouldn't be smaller than 3.", httpLocations.size() >= 3);
		
		HttpLocation httpLocation1 = contextFactory.getHttpLocation(1);		
		HttpLocation httpLocation2 = contextFactory.getHttpLocation(2);
		HttpLocation httpLocation3 = contextFactory.getHttpLocation(3);
		
		assertEquals("http://aeffle.de/1", httpLocation1.getUrl());
		assertEquals("http://aeffle.de/2", httpLocation2.getUrl());
		assertEquals("http://aeffle.de/3", httpLocation3.getUrl());
		
		assertEquals(true, httpLocation1.getUseAuth());
		assertEquals(true, httpLocation2.getUseAuth());
		assertEquals(true, httpLocation3.getUseAuth());
		
		assertEquals("john.doe1", httpLocation1.getUser());
		assertEquals("john.doe2", httpLocation2.getUser());
		assertEquals("john.doe3", httpLocation3.getUser());
		
		assertEquals("secret1", httpLocation1.getPass());
		assertEquals("secret2", httpLocation2.getPass());
		assertEquals("secret3", httpLocation3.getPass());
	}
}