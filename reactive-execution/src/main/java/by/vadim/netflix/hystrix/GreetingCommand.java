package by.vadim.netflix.hystrix;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rx.schedulers.Schedulers;

public class GreetingCommand extends HystrixObservableCommand<String> {

    private final String name;

    public GreetingCommand(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("GreetingGroup"));
        this.name = name;
    }

    @Override
    protected Observable<String> construct() {
        return Observable.create((Observable.OnSubscribe<String>) observer -> {
            try {
                if (!observer.isUnsubscribed()){

                    if (name == null){
                        throw new IllegalArgumentException("name cannot be null");
                    }

                    observer.onNext("Hello " + name + "!");
                    observer.onCompleted();
                }
            } catch (Exception e){
                observer.onError(e);
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    protected Observable<String> resumeWithFallback() {
        return Observable.create((Observable.OnSubscribe<String>) observer -> {
            if (!observer.isUnsubscribed()){
                observer.onNext("Hello Guest!");
                observer.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }
}