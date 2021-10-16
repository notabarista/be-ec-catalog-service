package org.notabarista.service.impl;

import lombok.extern.log4j.Log4j2;
import org.notabarista.controller.dto.ItemDTO;
import org.notabarista.controller.dto.ReviewDTO;
import org.notabarista.db.ItemRepository;
import org.notabarista.domain.Item;
import org.notabarista.domain.elasticsearch.ItemES;
import org.notabarista.entity.response.Response;
import org.notabarista.exception.AbstractNotabaristaException;
import org.notabarista.mappers.ItemMapper;
import org.notabarista.service.ItemESService;
import org.notabarista.service.ItemService;
import org.notabarista.service.ReviewService;
import org.notabarista.service.util.IBackendRequestService;
import org.notabarista.service.util.enums.MicroService;
import org.notabarista.util.NABConstants;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
public class ItemServiceImpl implements ItemService {

    private static final String MODIFIED_AT = "modifiedAt";

    private final IBackendRequestService backendRequestService;
    private final ItemRepository itemRepository;
    private final ItemESService itemESService;
    private final ItemMapper itemMapper;
    private final ReviewService reviewService;

    public ItemServiceImpl(IBackendRequestService backendRequestService, ItemRepository itemRepository, ItemESService itemESService, ItemMapper itemMapper, ReviewService reviewService) {
        this.backendRequestService = backendRequestService;
        this.itemRepository = itemRepository;
        this.itemESService = itemESService;
        this.itemMapper = itemMapper;
        this.reviewService = reviewService;
    }

    @Override
    public Page<ItemDTO> findAll(Pageable pageable) throws AbstractNotabaristaException {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, MODIFIED_AT));
        }
        log.info("Finding all items by page {}", pageable);

        Page<Item> items = itemRepository.findAll(pageable);
        List<ItemDTO> itemDTOS = itemMapper.itemsToDto(items.getContent());
        return new PageImpl<>(itemDTOS, items.getPageable(), items.getTotalElements());
    }

    @Override
    public ItemDTO findById(String id) {
        List<ReviewDTO> reviews = reviewService.findAll(id, PageRequest.of(0, 50)).getContent();
        return itemMapper.itemToDTO(itemRepository.findById(id).orElse(null), reviews);
    }

    @Override
    public ItemDTO insert(ItemDTO item, String userId) throws AbstractNotabaristaException {
        Map<String, Object> sellerInfo = getSellerInfo(item, Map.of(NABConstants.UID_HEADER_NAME, userId));

        setSellerInfo(item, sellerInfo);

        item.setCreatedBy(userId);

        Item savedItem = itemRepository.insert(itemMapper.dtoToItem(item));

        indexDocument(savedItem, false);

        return itemMapper.itemToDTO(savedItem);
    }

    @Override
    public ItemDTO update(ItemDTO item, String userId) throws AbstractNotabaristaException {
        Optional<Item> itemOptional = this.itemRepository.findById(item.getId());
        if (!itemOptional.isPresent()) {
            throw new AbstractNotabaristaException("Invalid item");
        }

        // update seller info in embedded document
        if (!itemOptional.get().getSeller().getUserIdentifier().equals(item.getSeller().getUserIdentifier())) {
            log.info("Updating seller info for item with id {}", item.getId());
            Map<String, Object> sellerInfo = getSellerInfo(item, Map.of(NABConstants.UID_HEADER_NAME, userId));
            setSellerInfo(item, sellerInfo);
        }

        item.setModifiedBy(userId);

        Item updatedItem = this.itemRepository.save(itemMapper.dtoToItem(item));

        indexDocument(updatedItem, true);

        return itemMapper.itemToDTO(updatedItem);
    }

    @Override
    public void deleteById(String id) {
        deleteDocument(id);
        this.itemRepository.deleteById(id);
    }

    private Map<String, Object> getSellerInfo(ItemDTO item, Map<String, String> customHeaders) throws AbstractNotabaristaException {
        Map<String, Object> sellerInfo = null;
            Response<Map<String, Object>> response = backendRequestService.executeGet(MicroService.USER_MANAGEMENT_SERVICE, "/user/findById/" + item.getSeller().getUserIdentifier(),
                    null, new ParameterizedTypeReference<Response<Map<String, Object>>>() {
                    }, customHeaders);
            if (response != null && !CollectionUtils.isEmpty(response.getData())) {
                sellerInfo = response.getData().get(0);
            }
        if (sellerInfo == null) {
            throw new AbstractNotabaristaException("Seller not found");
        }

        return sellerInfo;
    }

    private void setSellerInfo(ItemDTO item, Map<String, Object> sellerInfo) {
        item.getSeller().setName(sellerInfo.get("firstName") + " " + sellerInfo.get("lastName"));
    }

    private void indexDocument(Item item, boolean update) {
        ItemES itemES = itemMapper.itemToES(item);
        if (update) {
            // get ES document id before indexing updated document
            itemESService.findByItemId(item.getId());
        }

        itemESService.indexItem(itemES);
    }

    private void deleteDocument(String id) {
        itemESService.deleteByItemId(id);
    }

}
