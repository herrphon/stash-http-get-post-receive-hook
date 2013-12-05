package ut.de.aeffle.stash.plugin.hook;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.aeffle.stash.plugin.hook.UrlTemplateTranslator;

public class UrlTemplateTranslatorTest {
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void translationTest() {
		UrlTemplateTranslator translator = new UrlTemplateTranslator();
		
		translator.addTranslation("animal", "quick brown fox");
		translator.addTranslation("target", "lazy dog");
		String template = "The ${animal} jumped over the ${target}.";
		String expected = "The quick brown fox jumped over the lazy dog.";

		assertEquals(expected, translator.getUrlFromTemplate(template));
		
	}
}
