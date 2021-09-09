package com.boot.controller;


import com.boot.entity.User;
import com.boot.pojo.AddRequest;
import com.boot.pojo.SearchRequest;
import com.boot.repository.UserRepository;
import com.boot.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
@Tag(name="Item controller", description="Allows users and administrators to search/buy/add to basket items ")
public class ItemController {


    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    @Operation(summary="Search item controller", description="Allows you to search items")
    @GetMapping()
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> searchItem(@RequestBody SearchRequest searchRequest) {
        return itemService.searchItem(searchRequest);
    }


    @Operation(summary="Add item controller", description="Allows you to add item to the users basket")
    @PutMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addItem(@RequestBody AddRequest addRequest,@AuthenticationPrincipal UserDetails user) {
        User loggedInUserFromDB = userRepository.findAllByUsername(user.getUsername());
        return itemService.addItem(addRequest, loggedInUserFromDB);
    }


    @Operation(summary="Buy item controller", description="Allows the user to buy items")
    @PutMapping("/buy")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> buyItem(@AuthenticationPrincipal UserDetails user) {
        User loggedInUserFromDB = userRepository.findAllByUsername(user.getUsername());
        return itemService.buyItem(loggedInUserFromDB);
    }

}













