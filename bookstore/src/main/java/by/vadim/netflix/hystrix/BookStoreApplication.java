package by.vadim.netflix.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@SpringBootApplication
public class BookStoreApplication {

    @RequestMapping(value = "/recommended")
    public String readingList(){
        return "Spring in Action (Manning), Cloud Native Java (O'Reilly), Learning Spring Boot (Packet)";
    }

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }
}
