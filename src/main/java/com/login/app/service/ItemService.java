package com.login.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.login.app.model.Item;
import com.login.app.repository.ItemRepository;

@Service
public class ItemService {

    private ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ResponseEntity<Item> addItem(Item item) {
        
        Optional<Item> itemToCheck = itemRepository.findByItemName(item.getItemName());
        
        if(itemToCheck.isPresent() == true){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(itemRepository.save(item));
    }

    public ResponseEntity<Item> getItem(Long id) {
        Optional<Item> itemToGet = itemRepository.findById(id);

        if(itemToGet.isPresent() == false){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(itemToGet.get());
    }

    public List<Item> getItems() {
        return itemRepository.findAll();
    }

}
