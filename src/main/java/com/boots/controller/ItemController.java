package com.boots.controller;


import com.boots.pojo.AddRequest;
import com.boots.pojo.SearchRequest;
import com.boots.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
@Tag(name="Item controller", description="Allows users and administrators to search/buy/add to basket items ")
public class ItemController {


    @Autowired
    ItemService itemService;

    @Operation(summary="Search item controller", description="Allows you to search items")
    @PostMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> searchItem(@RequestBody SearchRequest searchRequest) {
        return itemService.searchItem(searchRequest);
    }


    @Operation(summary="Add item controller", description="Allows you to add item to the users basket")
    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addItem(@RequestBody AddRequest addRequest) {
        return itemService.addItem(addRequest);
    }


    @Operation(summary="Buy item controller", description="Allows the user to buy items")
    @GetMapping("/buy")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> buyItem() {
        return itemService.buyItem();
    }

}













