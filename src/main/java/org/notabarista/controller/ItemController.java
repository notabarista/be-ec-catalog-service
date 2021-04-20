package org.notabarista.controller;

import lombok.extern.log4j.Log4j2;
import org.notabarista.domain.Item;
import org.notabarista.exception.AbstractNotabaristaException;
import org.notabarista.service.IItemService;
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

@Log4j2
@RestController
@CrossOrigin
@RequestMapping("/item")
@Validated
public class ItemController {

    @Autowired
    private IItemService itemService;

    @GetMapping("/findAll")
    public ResponseEntity<Page<Item>> findAll(Pageable pageable) throws AbstractNotabaristaException {
        Page<Item> itemPage = this.itemService.findAll(pageable);
        return new ResponseEntity<>(itemPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> findById(@PathVariable @NotBlank String id) throws AbstractNotabaristaException {
        Item item = this.itemService.findById(id);
        if (item != null) {
            return new ResponseEntity<>(item, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Item> insertItem(@NotNull @Valid @RequestBody Item item, @RequestHeader("uid") String userId) throws AbstractNotabaristaException {
        Item newItem = this.itemService.insert(item, userId);
        if (newItem != null) {
            return new ResponseEntity<>(newItem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping
    public ResponseEntity<Item> updateItem(@NotNull @Valid @RequestBody Item item, @RequestHeader("uid") String userId) throws AbstractNotabaristaException {
        Item newItem = this.itemService.update(item, userId);
        if (newItem != null) {
            return new ResponseEntity<>(newItem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Item> deleteById(@PathVariable @NotBlank String id) throws AbstractNotabaristaException {
        this.itemService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
