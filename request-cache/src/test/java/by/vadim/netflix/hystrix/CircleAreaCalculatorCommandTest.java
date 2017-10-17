package by.vadim.netflix.hystrix;

import junit.framework.TestCase;
import org.junit.Test;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import static junit.framework.TestCase.*;

public class CircleAreaCalculatorCommandTest {

    @Test
    public void testWithoutCacheHits() throws Exception {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            CircleAreaCalculatorCommand command1 = new CircleAreaCalculatorCommand(2.11);
            TestCase.assertEquals(13.986684653047117, command1.execute());
            TestCase.assertFalse(command1.isResponseFromCache());

            CircleAreaCalculatorCommand command2 = new CircleAreaCalculatorCommand(5.32);
            TestCase.assertEquals(88.91461191895976, command2.execute());
            TestCase.assertFalse(command2.isResponseFromCache());
        } finally {
            context.shutdown();
        }
    }

    @Test
    public void testWithCacheHits() throws Exception {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            CircleAreaCalculatorCommand command1 = new CircleAreaCalculatorCommand(13.13);
            CircleAreaCalculatorCommand command2 = new CircleAreaCalculatorCommand(13.13);

            TestCase.assertEquals(541.6008345416543, command1.execute());
            TestCase.assertFalse(command1.isResponseFromCache());

            TestCase.assertEquals(541.6008345416543, command2.execute());
            TestCase.assertTrue(command2.isResponseFromCache());
        } finally {
            context.shutdown();
        }

        // start a new request context
        context = HystrixRequestContext.initializeContext();
        try {
            CircleAreaCalculatorCommand command3 = new CircleAreaCalculatorCommand(13.13);
            TestCase.assertEquals(541.6008345416543, command3.execute());
            TestCase.assertFalse(command3.isResponseFromCache());
        } finally {
            context.shutdown();
        }
    }
}