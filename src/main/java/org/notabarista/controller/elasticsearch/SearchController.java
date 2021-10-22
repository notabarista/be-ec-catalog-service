package org.notabarista.controller.elasticsearch;

import lombok.extern.log4j.Log4j2;
import org.notabarista.domain.Item;
import org.notabarista.exception.AbstractNotabaristaException;
import org.notabarista.service.ItemESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@Log4j2
@RestController
@CrossOrigin
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private ItemESService itemESService;

    @GetMapping("/term/{term}")
    public ResponseEntity<Page<Item>> findBySearchTerm(@PathVariable @NotBlank String term, Pageable pageable) throws AbstractNotabaristaException {
        Page<Item> itemPage = this.itemESService.findBySearchTerm(term, pageable);
        return new ResponseEntity<>(itemPage, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Page<Item>> findByName(@PathVariable @NotBlank String name, Pageable pageable) throws AbstractNotabaristaException {
        Page<Item> itemPage = this.itemESService.findByName(name, pageable);
        return new ResponseEntity<>(itemPage, HttpStatus.OK);
    }

}
