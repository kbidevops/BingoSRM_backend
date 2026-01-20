package kr.go.smplatform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "kr.go.smplatform")
public class ItsmApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItsmApplication.class, args);
    }
}
