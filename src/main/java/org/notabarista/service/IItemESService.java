package org.notabarista.service;

import org.notabarista.domain.Item;
import org.notabarista.domain.elasticsearch.ItemES;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IItemESService {

    Page<Item> findByName(String name, Pageable pageable);

    Page<Item> findBySearchTerm(String term, Pageable pageable);

    ItemES findByItemId(String itemId);

    ItemES indexItem(ItemES item);

    void deleteByItemId(String id);

}
