package org.notabarista.service;

import org.notabarista.controller.dto.ItemDTO;
import org.notabarista.exception.AbstractNotabaristaException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemService {

    Page<ItemDTO> findAll(Pageable pageable) throws AbstractNotabaristaException;

    ItemDTO findById(String id) throws AbstractNotabaristaException;

    ItemDTO insert(ItemDTO item, String userId) throws AbstractNotabaristaException;

    ItemDTO update(ItemDTO item, String userId) throws AbstractNotabaristaException;

    void deleteById(String id) throws AbstractNotabaristaException;

}
