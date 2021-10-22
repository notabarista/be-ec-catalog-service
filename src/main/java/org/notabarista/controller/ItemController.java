package org.notabarista.controller;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.StopWatch;
import org.notabarista.controller.dto.ItemDTO;
import org.notabarista.entity.response.Response;
import org.notabarista.entity.response.ResponseStatus;
import org.notabarista.exception.AbstractNotabaristaException;
import org.notabarista.service.ItemService;
import org.notabarista.util.NABConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Log4j2
@RestController
@CrossOrigin
@RequestMapping("/item")
@Validated
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/findAll")
    public ResponseEntity<Response<ItemDTO>> findAll(Pageable pageable) throws AbstractNotabaristaException {
        StopWatch watch = new StopWatch();
        watch.start();

        Page<ItemDTO> itemPage = this.itemService.findAll(pageable);

        watch.stop();

        return new ResponseEntity<>(new Response<>(ResponseStatus.SUCCESS, watch.getTime(), itemPage.getContent(),
                itemPage.getTotalElements(), itemPage.getPageable().getPageNumber(), itemPage.getTotalPages(),
                itemPage.getNumberOfElements(), ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<ItemDTO>> findById(@PathVariable @NotBlank String id) throws AbstractNotabaristaException {
        StopWatch watch = new StopWatch();
        watch.start();

        return getItemResponseEntity(itemService.findById(id), HttpStatus.NOT_FOUND, watch);
    }

    @PostMapping
    public ResponseEntity<Response<ItemDTO>> insertItem(@NotNull @Valid @RequestBody ItemDTO item, @RequestHeader(NABConstants.UID_HEADER_NAME) String userId) throws AbstractNotabaristaException {
        StopWatch watch = new StopWatch();
        watch.start();

        return getItemResponseEntity(itemService.insert(item, userId), HttpStatus.BAD_REQUEST, watch);
    }

    @PatchMapping
    public ResponseEntity<Response<ItemDTO>> updateItem(@NotNull @Valid @RequestBody ItemDTO item, @RequestHeader(NABConstants.UID_HEADER_NAME) String userId) throws AbstractNotabaristaException {
        StopWatch watch = new StopWatch();
        watch.start();

        return getItemResponseEntity(itemService.update(item, userId), HttpStatus.BAD_REQUEST, watch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteById(@PathVariable @NotBlank String id) throws AbstractNotabaristaException {
        this.itemService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResponseEntity<Response<ItemDTO>> getItemResponseEntity(ItemDTO newItem, HttpStatus httpStatus, StopWatch watch) {
        if (newItem != null) {
            watch.stop();
            List<ItemDTO> results = List.of(newItem);
            return new ResponseEntity<>(Response.<ItemDTO>builder()
                                                .status(ResponseStatus.SUCCESS)
                                                .data(results)
                                                .page(1)
                                                .size(results.size())
                                                .totalPageNumber(0)
                                                .total(results.size())
                                                .executionTime(watch.getTime())
                                                .build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(httpStatus);
        }
    }

}
