package by.vadim.netflix.hystrix.controllers;

import by.vadim.netflix.hystrix.services.BookStoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReadingController {

    @Autowired
    private BookStoreClient bookStoreClient;

    @RequestMapping("/to-read")
    public String toRead() {
        return bookStoreClient.readingList();
    }
}
