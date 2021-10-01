package org.notabarista.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.notabarista.db.elasticsearch.ItemESRepository;
import org.notabarista.domain.Item;
import org.notabarista.domain.elasticsearch.ItemES;
import org.notabarista.mappers.ItemMapper;
import org.notabarista.service.IItemESService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@Service
@Log4j2
@AllArgsConstructor
public class ItemESService implements IItemESService {

    private final ItemESRepository itemESRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ItemMapper itemMapper;

    @Override
    public Page<Item> findByName(String name, Pageable pageable) {
        Page<ItemES> byName = itemESRepository.findByName(name, pageable);
        Page<Item> convertedPage = byName.map(itemMapper::esToItem);

        return convertedPage;
    }

    @Override
    public Page<Item> findBySearchTerm(String term, Pageable pageable) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(multiMatchQuery(term)
                        .field("name")
                        .field("description")
                        .field("labels.key")
                        .field("labels.value")
                        .fuzziness(Fuzziness.ONE)
                        .type(MultiMatchQueryBuilder.Type.BEST_FIELDS))
                .build();
        SearchHits<ItemES> searchHits = elasticsearchOperations.search(searchQuery, ItemES.class);
        List<Item> items = searchHits.getSearchHits().stream().map(i -> itemMapper.esToItem(i.getContent())).collect(Collectors.toList());

        return new PageImpl<>(items, pageable, items.size());
    }

    @Override
    public ItemES findByItemId(String itemId) {
        return itemESRepository.findByItemId(itemId);
    }

    @Override
    public ItemES indexItem(ItemES item) {
        return itemESRepository.save(item);
    }

    @Override
    public void deleteByItemId(String id) {
        itemESRepository.deleteByItemId(id);
    }

}
