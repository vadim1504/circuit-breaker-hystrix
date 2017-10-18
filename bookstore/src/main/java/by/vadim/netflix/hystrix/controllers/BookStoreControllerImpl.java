package by.vadim.netflix.hystrix.controllers;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookStoreControllerImpl implements BookStoreController{

    @Override
    public String readingList() {
        return "Spring in Action (Manning), Cloud Native Java (O'Reilly), Learning Spring Boot (Packet)";
    }
}
