package com.login.app.service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.login.app.controller.ItemController;
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

        Item savedItem = itemRepository.save(item);
        //generating product uri
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().
                                                    path("/{id}")
                                                    .buildAndExpand(savedItem.getId()).toUri();


        return ResponseEntity.created(location).body(savedItem);
    }

    public EntityModel<Item> getItem(Long id) {
        Optional<Item> itemToGet = itemRepository.findById(id);

        if(itemToGet.isPresent() == false){
            return null;
        }

        EntityModel<Item> itemModel = EntityModel.of(itemToGet.get());
        
        WebMvcLinkBuilder linkToAllItems = linkTo(methodOn(ItemController.class).getItems());
        itemModel.add(linkToAllItems.withRel("all-items"));
        
        return itemModel;
    }

    public List<Item> getItems() {
        return itemRepository.findAll();
    }

}
