package org.notabarista.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.notabarista.db.ItemRepository;
import org.notabarista.domain.Item;
import org.notabarista.entity.response.Response;
import org.notabarista.exception.AbstractNotabaristaException;
import org.notabarista.service.IItemService;
import org.notabarista.service.util.IBackendRequestService;
import org.notabarista.service.util.enums.MicroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ItemService implements IItemService {

    private static final String MODIFIED_AT = "modifiedAt";

    @Autowired
    private IBackendRequestService backendRequestService;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Page<Item> findAll(Pageable pageable) throws AbstractNotabaristaException {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, MODIFIED_AT));
        }
        log.info("Finding all items by page {}", pageable);
        Page<Item> page = itemRepository.findAll(pageable);

        return page;
    }

    @Override
    public Item findById(String id) {
        return this.itemRepository.findById(id).orElse(null);
    }

    @Override
    public Item insert(Item item, String userId) throws AbstractNotabaristaException {
        Map<String, Object> sellerInfo = getSellerInfo(item);

        setSellerInfo(item, sellerInfo);

        item.setCreatedBy(userId);

        Item savedItem = this.itemRepository.insert(item);
        return savedItem;
    }

    @Override
    public Item update(Item item, String userId) throws AbstractNotabaristaException {
        Optional<Item> itemOptional = this.itemRepository.findById(item.getId());
        if (!itemOptional.isPresent()) {
            throw new AbstractNotabaristaException("Invalid item");
        }

        // update seller info in embedded document
        if (!itemOptional.get().getSeller().getUserIdentifier().equals(item.getSeller().getUserIdentifier())) {
            log.info("Updating seller info for item with id {}", item.getId());
            Map<String, Object> sellerInfo = getSellerInfo(item);
            setSellerInfo(item, sellerInfo);
        }

        item.setModifiedBy(userId);

        Item updatedItem = this.itemRepository.save(item);

        return updatedItem;
    }

    @Override
    public void deleteById(String id) {
        this.itemRepository.deleteById(id);
    }

    private Map<String, Object> getSellerInfo(Item item) throws AbstractNotabaristaException {
        Map<String, Object> sellerInfo = null;
            Response<Map<String, Object>> response = backendRequestService.executeGet(MicroService.USER_MANAGEMENT_SERVICE, "/user/findById/" + item.getSeller().getUserIdentifier(),
                    null, new ParameterizedTypeReference<Response<Map<String, Object>>>() {
                    });
            if (response != null && !CollectionUtils.isEmpty(response.getData())) {
                sellerInfo = response.getData().get(0);
            }
        if (sellerInfo == null) {
            throw new AbstractNotabaristaException("Seller not found");
        }

        return sellerInfo;
    }

    private void setSellerInfo(Item item, Map<String, Object> sellerInfo) {
        item.getSeller().setName(sellerInfo.get("firstName") + " " + sellerInfo.get("lastName"));
    }

}
