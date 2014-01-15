package ut.de.aeffle.stash.plugin.hook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ut.de.aeffle.stash.plugin.hook.helper.RepositoryHookContextMockFactory;
import de.aeffle.stash.plugin.hook.HttpLocation;

public class HttpLocationTest { 
	private RepositoryHookContextMockFactory repositoryHookContextFactory = new RepositoryHookContextMockFactory();

    @Before
    public void beforeTestClearSettings() {
    	repositoryHookContextFactory.clear();
    }

 
	@Test
	public void testGetUrl1() {
		repositoryHookContextFactory.setUrl( 1, "http://aeffle.de");
		
		assertEquals("http://aeffle.de", repositoryHookContextFactory.getFirstHttpLocation().getUrl());
	}

	@Test
	public void testGetUser1() {
		repositoryHookContextFactory.setUser(1, "john.doe");
		
		assertEquals("john.doe", repositoryHookContextFactory.getFirstHttpLocation().getUser());
	}

	@Test
	public void testGetPass1() {
		repositoryHookContextFactory.setPass(1, "secret");
		
		assertEquals("secret", repositoryHookContextFactory.getFirstHttpLocation().getPass());
	}

	@Test
	public void testGetUseAuthNullV1() {
		repositoryHookContextFactory.setOldUseAuth(null);
		
		assertEquals(false, repositoryHookContextFactory.getFirstHttpLocation().getUseAuth());
	}
	
	@Test
	public void testGetUseAuthFalseV1() {
		repositoryHookContextFactory.setOldUseAuth(false);
		
		assertEquals(false, repositoryHookContextFactory.getFirstHttpLocation().getUseAuth());
	}

	@Test
	public void testGetUseAuthTrueV1() {
		repositoryHookContextFactory.setOldUseAuth(true);
		
		HttpLocation firstHttpLocation = repositoryHookContextFactory.getFirstHttpLocation();
		boolean hasUseAuth = firstHttpLocation.getUseAuth();
		assertEquals(true, hasUseAuth);
	}

	@Test
	public void testGetUseAuthTrueV2() {
		repositoryHookContextFactory.setVersion("2");
		repositoryHookContextFactory.setUseAuth(1, true);
		
		HttpLocation firstHttpLocation = repositoryHookContextFactory.getFirstHttpLocation();
		boolean hasUseAuth = firstHttpLocation.getUseAuth();
		assertEquals(true, hasUseAuth);
	}
	
	@Test
	public void testgetNumberOfHttpLocationsNull() {
		repositoryHookContextFactory.setLocationCount(null);
			
		ArrayList<HttpLocation> httpLocations = repositoryHookContextFactory.getHttpLocations();

		assertEquals(1, httpLocations.size());
   }
	
	@Test
	public void testgetNumberOfHttpLocations1() {
		repositoryHookContextFactory.setLocationCount("1");
			
		ArrayList<HttpLocation> httpLocations = repositoryHookContextFactory.getHttpLocations();

		assertEquals(1, httpLocations.size());
   }
	
	@Test
	public void testgetNumberOfHttpLocations3() {
		repositoryHookContextFactory.setLocationCount("3");
		
		ArrayList<HttpLocation> httpLocations = repositoryHookContextFactory.getHttpLocations();

		assertEquals(3, httpLocations.size());
   }


	@Test
	public void testGetUrl2() {
		repositoryHookContextFactory.setLocationCount("2");
		repositoryHookContextFactory.setUrl(2, "http://aeffle.de");
		
		ArrayList<HttpLocation> httpLocations = repositoryHookContextFactory.getHttpLocations();
		assertTrue("LocationCount shouldn't be smaller than 2.", httpLocations.size() >= 2);
		
		HttpLocation httpLocation = repositoryHookContextFactory.getHttpLocation(2);		
		assertEquals("http://aeffle.de", httpLocation.getUrl());
	}
	
	@Test
	public void testGetUser2() {
		repositoryHookContextFactory.setLocationCount("2");
		repositoryHookContextFactory.setUser(2, "john.doe");
		
		ArrayList<HttpLocation> httpLocations = repositoryHookContextFactory.getHttpLocations();
		assertTrue("LocationCount shouldn't be smaller than 2.", httpLocations.size() >= 2);
		
		HttpLocation httpLocation = repositoryHookContextFactory.getHttpLocation(2);		
		
		assertEquals("john.doe", httpLocation.getUser());
	}
	
	@Test
	public void testGetPass2() {
		repositoryHookContextFactory.setLocationCount("2");
		repositoryHookContextFactory.setPass(2, "secret");
		
		ArrayList<HttpLocation> httpLocations = repositoryHookContextFactory.getHttpLocations();
		assertTrue("LocationCount shouldn't be smaller than 2.", httpLocations.size() >= 2);
		
		HttpLocation httpLocation = repositoryHookContextFactory.getHttpLocation(2);		
		
		assertEquals("secret", httpLocation.getPass());
	}
	
	@Test
	public void testManyLocations() {
		repositoryHookContextFactory.setVersion("2");
		repositoryHookContextFactory.setLocationCount("3");
		
		repositoryHookContextFactory.setUrl( 1, "http://aeffle.de/1");
		repositoryHookContextFactory.setUrl( 2, "http://aeffle.de/2");
		repositoryHookContextFactory.setUrl( 3, "http://aeffle.de/3");

		repositoryHookContextFactory.setUseAuth(1, true);
		repositoryHookContextFactory.setUseAuth(2, true);
		repositoryHookContextFactory.setUseAuth(3, true);

		repositoryHookContextFactory.setUser(1, "john.doe1");
		repositoryHookContextFactory.setUser(2, "john.doe2");
		repositoryHookContextFactory.setUser(3, "john.doe3");

		repositoryHookContextFactory.setPass(1, "secret1");
		repositoryHookContextFactory.setPass(2, "secret2");
		repositoryHookContextFactory.setPass(3, "secret3");
		
		ArrayList<HttpLocation> httpLocations = repositoryHookContextFactory.getHttpLocations();
		
		assertTrue("LocationCount shouldn't be smaller than 3.", httpLocations.size() >= 3);
		
		HttpLocation httpLocation1 = repositoryHookContextFactory.getHttpLocation(1);		
		HttpLocation httpLocation2 = repositoryHookContextFactory.getHttpLocation(2);
		HttpLocation httpLocation3 = repositoryHookContextFactory.getHttpLocation(3);
		
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