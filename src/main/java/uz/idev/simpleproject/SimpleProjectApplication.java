package uz.idev.simpleproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class SimpleProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimpleProjectApplication.class, args);
    }
}
