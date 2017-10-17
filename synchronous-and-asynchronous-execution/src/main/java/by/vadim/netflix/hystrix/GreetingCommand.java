package by.vadim.netflix.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class GreetingCommand extends HystrixCommand<String> {

    private final String name;

    public GreetingCommand(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("GreetingGroup"));
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        if (this.name == null){
            throw new IllegalArgumentException("name sould not be 'null'.");
        }
        return "Hello " + this.name + "!";
    }

    @Override
    protected String getFallback() {
        return "Hello Guest!";
    }
}