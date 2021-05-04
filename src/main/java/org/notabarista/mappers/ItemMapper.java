package org.notabarista.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.notabarista.domain.Item;
import org.notabarista.domain.elasticsearch.ItemES;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target="itemId", source="item.id")
    })
    ItemES itemToES(Item item);

    @Mappings({
            @Mapping(target="id", source="itemES.itemId")
    })
    Item esToItem(ItemES itemES);

    List<ItemES> itemsToEs(List<Item> items);

    List<Item> esToItems(List<ItemES> items);

}
