package ut.de.aeffle.stash.plugin;

import org.junit.Test;
import de.aeffle.stash.plugin.MyPluginComponent;
import de.aeffle.stash.plugin.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}