package org.notabarista.service;

import org.notabarista.domain.Item;
import org.notabarista.exception.AbstractNotabaristaException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IItemService {

    Page<Item> findAll(Pageable pageable) throws AbstractNotabaristaException;

    Item findById(String id) throws AbstractNotabaristaException;

    Item insert(Item item, String userId) throws AbstractNotabaristaException;

    Item update(Item item, String userId) throws AbstractNotabaristaException;

    void deleteById(String id) throws AbstractNotabaristaException;

}
