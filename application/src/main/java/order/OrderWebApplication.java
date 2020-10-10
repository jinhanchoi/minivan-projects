package order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {"order","orderservice"})
@EntityScan(basePackages = {"order","orderservice"})
@SpringBootApplication
public class OrderWebApplication {
    public static void main(String ... args){
        SpringApplication.run(OrderWebApplication.class,args);
    }
}
