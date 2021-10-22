package org.notabarista.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.notabarista.controller.dto.ItemDTO;
import org.notabarista.controller.dto.ReviewDTO;
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

    List<ItemDTO> itemsToDto(List<Item> items);

    List<Item> dtoToItems(List<ItemDTO> items);

    ItemDTO itemToDTO(Item item);

    @Mappings({
            @Mapping(target="reviews", source="reviews")
    })
    ItemDTO itemToDTO(Item item, List<ReviewDTO> reviews);

    Item dtoToItem(ItemDTO itemDTO);

}
