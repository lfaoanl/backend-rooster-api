package nl.faanveldhuijsen.roosters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@SpringBootApplication
public class NoviBackendRoostersApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoviBackendRoostersApplication.class, args);
    }


}
