package com.boots.service;

import com.boots.entity.Basket;
import com.boots.entity.Item;
import com.boots.entity.User;
import com.boots.pojo.ChangeRequest;
import com.boots.pojo.CreateRequest;
import com.boots.pojo.DeleteRequest;
import com.boots.pojo.MessageResponse;
import com.boots.repository.BasketRepository;
import com.boots.repository.ItemRepository;
import com.boots.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AdminService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    BasketRepository basketRepository;

    @Autowired
    MailSender mailSender;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<?> createItem(@RequestBody CreateRequest createRequest) {
        //create item with unique name
        if (itemRepository.existsByTags(createRequest.getName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Product with this name has already been created"));
        } else
            itemRepository.addNewItem(createRequest.getName(), createRequest.getDescription(), createRequest.getTags());
        return ResponseEntity.ok(new MessageResponse("Added new item"));
    }


    public ResponseEntity<?> changeItem(@RequestBody ChangeRequest changeRequest, boolean forceUpdate) {
        //The item we want to change must not be in the cart.
        //throw bad request if the item is in the user's cart
        if (basketRepository.existsByItem(changeRequest.getItemName()) & !forceUpdate) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: This product is in the buyer's cart"));
        }

        if (itemRepository.existsByName(changeRequest.getItemName())) {
            //get item
            Item itemBeforeChange = itemRepository.findAllByName(changeRequest.getItemName());

            String nameOfTheItemToBeChanged = changeRequest.getItemName();
            String newName = changeRequest.getNewItemName();
            String newTags = changeRequest.getNewItemTags();
            String newDescription = changeRequest.getNewItemDescription();

            //if we do not change some criteria, we get them from the old element
            if (newName == null) {
                newName = changeRequest.getItemName();
            }
            if (newTags == null) {
                newTags = itemBeforeChange.getTags();
            }
            if (newDescription == null) {
                newDescription = itemBeforeChange.getDescription();
            }

            itemRepository.changeItem(nameOfTheItemToBeChanged,
                    newName,
                    newDescription,
                    newTags);

            //send message if flag is true to force update
            if (forceUpdate) {
                Basket userBasket = basketRepository.findBasketByItem(changeRequest.getItemName());
                User user = userRepository.findUserById(userBasket.getUserId());
                basketRepository.forceUpdateItem(nameOfTheItemToBeChanged, newName);
                String message = "your list of purchased items has been changed by administrator";
                mailSender.send(user.getEmail(), "please check your chart", message);
            }

        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : Item with that name was not found"));
        }

        return ResponseEntity.ok(new MessageResponse("Item changed"));
    }


    public ResponseEntity<?> deleteItem(@RequestBody DeleteRequest deleteRequest, boolean forceUpdate) {

        //throw bad request if the item is in the user's cart
        if (basketRepository.existsByItem(deleteRequest.getItemName()) & !forceUpdate) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: This product is in the buyer's cart"));
        }

        if (itemRepository.existsByName(deleteRequest.getItemName()) & forceUpdate) {
            itemRepository.deleteByName(deleteRequest.getItemName());
            //send message if flag is true to force update
            if (forceUpdate) {
                Basket userBasket = basketRepository.findBasketByItem(deleteRequest.getItemName());
                User user = userRepository.findUserById(userBasket.getUserId());
                basketRepository.deleteByItem(deleteRequest.getItemName());
                String message = "your list of purchased items has been changed by administrator";
                mailSender.send(user.getEmail(), "please check your chart", message);
            }

        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : Item with that name was not found"));
        }
        return ResponseEntity.ok(new MessageResponse("Item deleted"));
    }
}
