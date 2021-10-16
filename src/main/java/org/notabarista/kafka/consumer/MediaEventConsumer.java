package org.notabarista.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.notabarista.controller.dto.ItemDTO;
import org.notabarista.exception.AbstractNotabaristaException;
import org.notabarista.kafka.MediaEvent;
import org.notabarista.service.ItemService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;

@Component
@Log4j2
public class MediaEventConsumer {

    private final ItemService itemService;
    private final ObjectMapper objectMapper;

    public MediaEventConsumer(ItemService itemService, ObjectMapper objectMapper) {
        this.itemService = itemService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = {"${spring.kafka.template.default-topic}"})
    public void onMessage(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException, AbstractNotabaristaException {
        log.info("ConsumerRecord : {}", consumerRecord);
        MediaEvent mediaEvent = objectMapper.readValue(consumerRecord.value(), MediaEvent.class);
        processMediaEvent(mediaEvent);
    }

    private void processMediaEvent(MediaEvent mediaEvent) throws AbstractNotabaristaException {
        if (mediaEvent != null && StringUtils.isNotBlank(mediaEvent.getItemID()) &&
                !CollectionUtils.isEmpty(mediaEvent.getMediaURLs())) {
            ItemDTO item = itemService.findById(mediaEvent.getItemID());
            if (item != null) {
                if (item.getMediaUrls() == null) {
                    item.setMediaUrls(new HashSet<>());
                }

                switch (mediaEvent.getMediaEventType()) {
                    case ADD:
                        item.getMediaUrls().addAll(mediaEvent.getMediaURLs());
                        break;
                    case DELETE:
                        item.getMediaUrls().removeAll(mediaEvent.getMediaURLs());
                        break;
                    default:
                        break;
                }

                itemService.update(item, mediaEvent.getUserID());
            } else {
                log.error("Item with id '{}' not found.", mediaEvent.getItemID());
            }
        }
    }

}
