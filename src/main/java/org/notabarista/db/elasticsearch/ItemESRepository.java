package org.notabarista.db.elasticsearch;

import org.notabarista.domain.elasticsearch.ItemES;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ItemESRepository extends ElasticsearchRepository<ItemES, String> {

    Page<ItemES> findByName(String name, Pageable pageable);

    ItemES findByItemId(String itemId);

    void deleteByItemId(String id);

}
