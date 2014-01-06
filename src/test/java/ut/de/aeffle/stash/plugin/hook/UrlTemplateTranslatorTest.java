package ut.de.aeffle.stash.plugin.hook;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import ut.de.aeffle.stash.plugin.hook.helper.RepositoryHookContextMockFactory;
import ut.de.aeffle.stash.plugin.hook.helper.StashAuthenticationContextMockFactory;

import com.atlassian.stash.hook.repository.RepositoryHookContext;
import com.atlassian.stash.repository.RefChange;
import com.atlassian.stash.user.StashAuthenticationContext;

import de.aeffle.stash.plugin.hook.HttpLocation;
import de.aeffle.stash.plugin.hook.UrlTemplateTranslator;


public class UrlTemplateTranslatorTest {
	
	private RepositoryHookContextMockFactory contextFactory = new RepositoryHookContextMockFactory();
	private StashAuthenticationContextMockFactory authenticationContextFactory = new StashAuthenticationContextMockFactory();

    @Before
    public void beforeTestClearSettings() {
    	contextFactory.clear();
    	authenticationContextFactory.clear();
	}
	

	@Test
	public void testTransformWithDisplayName() {
    	contextFactory.prepareStringSetting("url", "http://doe.com/${user.displayName}");
 		authenticationContextFactory.setDisplayName("John_Doe");
    	
		HttpLocation httpLocation = contextFactory.getFirstHttpLocation();
		
		StashAuthenticationContext stashAuthenticationContext = authenticationContextFactory.getContext();
		RepositoryHookContext repositoryHookContext = contextFactory.getContext();
		Collection<RefChange> refChanges = null;
		
		UrlTemplateTranslator translator = new UrlTemplateTranslator(stashAuthenticationContext, repositoryHookContext, refChanges);
		translator.addStashAuthenticationContext(stashAuthenticationContext);
		translator.transform(httpLocation);

		assertEquals("http://doe.com/John_Doe", httpLocation.getUrl());
	}
	
	@Test
	public void testTransformWithUserName() {
    	contextFactory.prepareStringSetting("url", "http://doe.com/${user.name}");
 		authenticationContextFactory.setName("john.doe");
    	
		HttpLocation httpLocation = contextFactory.getFirstHttpLocation();
		
		StashAuthenticationContext stashAuthenticationContext = authenticationContextFactory.getContext();
		RepositoryHookContext repositoryHookContext = contextFactory.getContext();
		Collection<RefChange> refChanges = null;
		
		UrlTemplateTranslator translator = new UrlTemplateTranslator(stashAuthenticationContext, repositoryHookContext, refChanges);
		translator.addStashAuthenticationContext(stashAuthenticationContext);
		translator.transform(httpLocation);

		assertEquals("http://doe.com/john.doe", httpLocation.getUrl());
	}
	
	@Test
	public void testTransformWithEmail() {
    	contextFactory.prepareStringSetting("url", "http://doe.com/${user.email}");
 		authenticationContextFactory.setEmailAddress("john@doe.de");
    	
		HttpLocation httpLocation = contextFactory.getFirstHttpLocation();
		
		StashAuthenticationContext stashAuthenticationContext = authenticationContextFactory.getContext();
		RepositoryHookContext repositoryHookContext = contextFactory.getContext();
		Collection<RefChange> refChanges = null;
		
		UrlTemplateTranslator translator = new UrlTemplateTranslator(stashAuthenticationContext, repositoryHookContext, refChanges);
		translator.addStashAuthenticationContext(stashAuthenticationContext);
		translator.transform(httpLocation);

		assertEquals("http://doe.com/john@doe.de", httpLocation.getUrl());
	}
	
	@Test
	public void testTransformWithRepository() {
    	contextFactory.prepareStringSetting("url", "http://doe.com/${repository.id}/${repository.name}/${repository.slug}");
 		contextFactory.setRepositoryId(1);
 		contextFactory.setRepositoryName("Test Repository");
 		contextFactory.setRepositorySlug("REPO");
    	
		HttpLocation httpLocation = contextFactory.getFirstHttpLocation();
		
		StashAuthenticationContext stashAuthenticationContext = authenticationContextFactory.getContext();
		RepositoryHookContext repositoryHookContext = contextFactory.getContext();
		Collection<RefChange> refChanges = null;
		
		UrlTemplateTranslator translator = new UrlTemplateTranslator(stashAuthenticationContext, repositoryHookContext, refChanges);
		translator.addStashAuthenticationContext(stashAuthenticationContext);
		translator.transform(httpLocation);

		assertEquals("http://doe.com/1/Test Repository/REPO", httpLocation.getUrl());
	}

	@Test
	public void testTransformWithProject() {
    	contextFactory.prepareStringSetting("url", "http://doe.com/${project.name}/${project.key}");
    	contextFactory.setProjectKey("PROJECT");
    	contextFactory.setProjectName("Test Project");
    	
		HttpLocation httpLocation = contextFactory.getFirstHttpLocation();
		
		StashAuthenticationContext stashAuthenticationContext = authenticationContextFactory.getContext();
		RepositoryHookContext repositoryHookContext = contextFactory.getContext();
		Collection<RefChange> refChanges = null;
		
		UrlTemplateTranslator translator = new UrlTemplateTranslator(stashAuthenticationContext, repositoryHookContext, refChanges);
		translator.addStashAuthenticationContext(stashAuthenticationContext);
		translator.transform(httpLocation);

		assertEquals("http://doe.com/Test Project/PROJECT", httpLocation.getUrl());
	}
	
}
