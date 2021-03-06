package org.notabarista.db;

import org.notabarista.domain.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {

    List<Item> findByName(String name);

    List<Item> findBySeller_Name(String sellerName);

    List<Item> findAllBy(TextCriteria criteria, Pageable pageable);

}
