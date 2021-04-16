package org.notabarista.db.event.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class GenericMongoAuditListener extends AbstractMongoEventListener<Object> {

    @Override
    public void onAfterSave(AfterSaveEvent<Object> event) {
        Object obj = event.getSource();
        log.info("Saved document: " + obj);
    }

    @Override
    public void onAfterDelete(AfterDeleteEvent<Object> event) {
        Object obj = event.getSource();
        log.info("Deleted document: " + obj);
    }

}
