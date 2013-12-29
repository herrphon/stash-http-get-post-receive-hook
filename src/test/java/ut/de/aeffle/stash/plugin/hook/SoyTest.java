package ut.de.aeffle.stash.plugin.hook;

import java.util.HashMap;
import java.util.Map;

import com.atlassian.soy.renderer.SoyException;
import com.atlassian.soy.renderer.SoyTemplateRenderer;

import static org.junit.Assert.*;

import org.junit.*;


public class SoyTest { 
    protected final SoyTemplateRenderer soyTemplateRenderer;

    public SoyTest(SoyTemplateRenderer soyTemplateRenderer) {
        this.soyTemplateRenderer = soyTemplateRenderer;
    }	


	@Test
	public void testGetUseAuth() {
		
		String completeModuleKey = "";
		String templateName = "";
		Map<String, Object> data = new HashMap<String, Object>();
		
		try {
			soyTemplateRenderer.render(completeModuleKey, templateName, data);
		} catch (SoyException e) {
			e.printStackTrace();
			
			assertTrue("SoyTemplateRenderer failed.", false);
		}
		assertEquals(Boolean.TRUE, true);
	}

}
