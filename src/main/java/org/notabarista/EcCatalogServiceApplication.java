package org.notabarista;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableScheduling
@EnableCaching
@EnableAsync
@EnableDiscoveryClient
@EnableMongoAuditing
public class EcCatalogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcCatalogServiceApplication.class, args);
    }

}
