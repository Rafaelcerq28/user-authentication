package com.login.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.login.app.model.Item;
import com.login.app.service.ItemService;

@RestController
public class ItemController {

    
    private ItemService itemService;
    
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    //add itemm
    @PostMapping("/item")
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        System.out.println(item.toString());
        return itemService.addItem(item);
    }

    //get item by id
    @GetMapping("/item/{id}")
    public ResponseEntity<Item> getItem(@PathVariable(value="id") Long id){
        return itemService.getItem(id);
    }
    
    //get all items
    @GetMapping("/item")
    public List<Item> getItems(){
        return itemService.getItems();
    }

    //delete item

    //update item



}
