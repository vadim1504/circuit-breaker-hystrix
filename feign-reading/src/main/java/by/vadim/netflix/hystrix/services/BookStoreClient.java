package by.vadim.netflix.hystrix.services;

import by.vadim.netflix.hystrix.controllers.BookStoreController;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

@FeignClient(name = "recommended", url = "http://localhost:8090", fallback = BookStoreClient.BookStoreClientFallback.class)
public interface BookStoreClient extends BookStoreController {

    @Component
    class BookStoreClientFallback implements BookStoreClient{
        @Override
        public String readingList() {
            return "Cloud Native Java (O'Reilly)";
        }
    }
}
