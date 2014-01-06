package ut.de.aeffle.stash.plugin.hook.helper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.atlassian.stash.user.StashAuthenticationContext;
import com.atlassian.stash.user.StashUser;


public class StashAuthenticationContextMockFactory {

	private StashAuthenticationContext stashAuthenticationContext;
	private StashUser stashUser;

	public StashAuthenticationContextMockFactory() {
		clear();
	}

	public StashAuthenticationContextMockFactory clear() {
		createNewMocks();
		loadDefaults();
		return this;
	}

	private void createNewMocks() {
		stashAuthenticationContext = mock(StashAuthenticationContext.class);
		stashUser = mock(StashUser.class);
		
		when(stashAuthenticationContext.getCurrentUser()).thenReturn(stashUser);
	}

	private void loadDefaults() {
		when(stashUser.getDisplayName()).thenReturn("");
		when(stashUser.getEmailAddress()).thenReturn("");
		when(stashUser.getName()).thenReturn("");
	}

	public void setDisplayName(String name) {
		when(stashUser.getDisplayName()).thenReturn(name);
	}
	
	public void setEmailAddress(String email) {
		when(stashUser.getEmailAddress()).thenReturn(email);
	}
	
	public void setName(String name) {
		when(stashUser.getName()).thenReturn(name);
	}
	
	public StashAuthenticationContext getContext() {
		return stashAuthenticationContext;
	}
	
}
