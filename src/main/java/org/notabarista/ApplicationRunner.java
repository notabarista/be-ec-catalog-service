package org.notabarista;

import org.notabarista.db.ItemRepository;
import org.notabarista.domain.Item;
import org.notabarista.domain.Label;
import org.notabarista.domain.Seller;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Order(1)
@Log4j2
public class ApplicationRunner implements CommandLineRunner {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public void run(String... args) {
//        loadInitialData();

        log.info("Application started...");
    }

    private void loadInitialData() {
        itemRepository.deleteAll();
        itemRepository.insert(Item.builder()
                .name("Black Coffee")
                .description("..it is black.")
                .code(UUID.randomUUID().toString())
                .isActive(true)
                .rating(4.5)
                .seller(Seller.builder()
                        .userIdentifier(UUID.randomUUID().toString())
                        .name("The best seller")
                        .build())
                .labels(List.of(
                        Label.builder().key("Roasting").value("Espresso").build(),
                        Label.builder().key("Roaster").value("Gardeli").order(1).build(),
                        Label.builder().value("Recommended by NAB").build()
                ))
                .build());
    }

}
