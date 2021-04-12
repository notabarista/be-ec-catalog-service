package org.notabarista;

import org.notabarista.db.event.listener.GenericCascadeEventListener;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@TestConfiguration
public class DatabaseTestConfiguration {

    @Bean
    public GenericCascadeEventListener genericCascadeEventListener(MongoTemplate mongoTemplate) {
        return new GenericCascadeEventListener(mongoTemplate);
    }

}
