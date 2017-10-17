package by.vadim.netflix.hystrix;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Future;

import static org.junit.Assert.*;

public class GreetingCommandTest {

    @Test
    public void testGreeting() throws Exception {
        Assert.assertEquals("Hello World!", new GreetingCommand("World").execute());
        GreetingCommand command = new GreetingCommand("John");
        Assert.assertEquals("Hello John!", command.execute());
        assertFalse(command.isResponseFromFallback());
    }

    @Test
    public void testGreetingFallBack() throws Exception {
        try {
            GreetingCommand command = new GreetingCommand(null);
            Assert.assertEquals("Hello Guest!", command.execute());
            assertTrue(command.isResponseFromFallback());
        } catch (Exception e){
            fail("we should not get an exception as we return a fallback");
        }
    }

    @Test
    public void testAsynchronousGreeting1() throws Exception {
        GreetingCommand command1 = new GreetingCommand("World");
        GreetingCommand command2 = new GreetingCommand("John");

        Future fWorld = new GreetingCommand("World").queue();
        Future fJohn = new GreetingCommand("John").queue();

        assertEquals("Hello World!", fWorld.get());
        assertEquals("Hello John!", fJohn.get());

        assertFalse(command1.isResponseFromFallback());
        assertFalse(command2.isResponseFromFallback());
    }

    @Test
    public void testAsynchronousGreetingFallBack() throws Exception {
        try {
            GreetingCommand command = new GreetingCommand(null);
            Future fNull = command.queue();
            assertEquals("Hello Guest!", fNull.get());
            assertTrue(command.isResponseFromFallback());
        } catch (Exception e){
            fail("we should not get an exception as we return a fallback");
        }
    }
}