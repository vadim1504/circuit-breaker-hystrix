package by.vadim.netflix.hystrix;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class GreetingCommandTest {

    private static final Logger log = LoggerFactory.getLogger(GreetingCommandTest.class);

    // blocking
    @Test
    public void testBlockingReactiveGreeting() throws Exception {
        GreetingCommand command1 = new GreetingCommand("World");
        GreetingCommand command2 = new GreetingCommand("John");

        assertEquals("Hello World!", command1.observe().toBlocking().single());
        assertEquals("Hello John!", command2.observe().toBlocking().single());

        assertFalse(command1.isResponseFromFallback());
        assertFalse(command2.isResponseFromFallback());
    }

    // non-blocking
    @Test
    public void testNonBlockingReactiveGreeting() throws Exception {
        GreetingCommand command1 = new GreetingCommand("World");
        GreetingCommand command2 = new GreetingCommand("John");

        Observable<String> oWorld = command1.observe();
        Observable<String> oJohn = command2.observe();

        TestScheduler testScheduler = Schedulers.test();
        TestSubscriber<String> worldSubscriber = new TestSubscriber<>();
        TestSubscriber<String> johnSubscriber = new TestSubscriber<>();

        oWorld.subscribe(worldSubscriber);
        oJohn.subscribe(johnSubscriber);
        testScheduler.advanceTimeBy(100L, TimeUnit.SECONDS);

        assertEquals("Hello World!", worldSubscriber.getOnNextEvents().get(0));
        assertEquals("Hello John!", johnSubscriber.getOnNextEvents().get(0));

        assertFalse(command1.isResponseFromFallback());
        assertFalse(command2.isResponseFromFallback());
    }

    @Test
    public void executeNonBlockingReactiveGreeting() {
        GreetingCommand command1 = new GreetingCommand("World");
        GreetingCommand command2 = new GreetingCommand("John");

        Observable<String> oWorld = command1.observe();
        Observable<String> oJohn = command2.observe();

        // non-blocking
        // - this is a verbose anonymous inner-class approach and doesn't do assertions
        oWorld.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                // nothing needed here
            }

            @Override
            public void onError(Throwable e) {
                log.error("", e);
            }

            @Override
            public void onNext(String greeting) {
                log.debug(greeting);
            }
        });

        // non-blocking
        // - also verbose anonymous inner-class
        // - ignore errors and onCompleted signal
        oJohn.subscribe(log::debug);
        oJohn.subscribe(log::debug);
        oJohn.subscribe(greeting -> log.debug(greeting));
    }

    @Test
    public void testBlockingReactiveGreetingFallBack() throws Exception {
        try {
            GreetingCommand command = new GreetingCommand(null);
            assertEquals("Hello Guest!", command.observe().toBlocking().single());
            assertTrue(command.isResponseFromFallback());
        } catch (Exception e){
            fail("we should not get an exception as we return a fallback");
        }
    }

    @Test
    public void testNonBlockingReactiveGreetingFallBack() throws Exception {
        try {
            GreetingCommand command = new GreetingCommand(null);
            Observable<String> oNull = command.observe();

            TestScheduler testScheduler = Schedulers.test();
            TestSubscriber<String> nullSubscriber = new TestSubscriber<>();

            oNull.subscribe(nullSubscriber);
            testScheduler.advanceTimeBy(100L, TimeUnit.MILLISECONDS);

            assertEquals("Hello Guest!", nullSubscriber.getOnNextEvents().get(0));
            assertTrue(command.isResponseFromFallback());
        } catch (Exception e){
            fail("we should not get an exception as we return a fallback");
        }
    }
}