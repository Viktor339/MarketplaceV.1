package com.boot.service;

import com.boot.entity.Basket;
import com.boot.entity.Item;
import com.boot.entity.User;
import com.boot.pojo.AddRequest;
import com.boot.pojo.MessageResponse;
import com.boot.pojo.SearchRequest;
import com.boot.repository.BasketRepository;
import com.boot.repository.ItemRepository;
import com.boot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ItemService {

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSender mailSender;


    public ResponseEntity<?> searchItem(SearchRequest searchRequest) {
        /**
         * /нет поиска по англ. алфавиту
         */
        String[] tagsRequest = searchRequest.getTags();
        String descriptionRequest = searchRequest.getDescription();

        //chech that request have tags or description
        if (tagsRequest == null & descriptionRequest == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Enter your search criteria"));
        } else {
            List<List<Item>> itemList = new ArrayList<>();
            Set<Item> uniqueSearchResults = new TreeSet<>(Comparator.comparing(item -> item.getName()));

            //search by tags
            if (tagsRequest != null) {
                for (String tags : tagsRequest) {
                    itemList.add(itemRepository.findByTagsContaining(tags));
                }
            }

            //search by description
            if (descriptionRequest != null) {
                List<String> eachWordInRequest = Stream.of(descriptionRequest.split(" "))
                        .map(String::toLowerCase)
                        .distinct().filter(word -> word.length() > 1)
                        .collect(Collectors.toList());
                for (String word : eachWordInRequest) {
                    itemList.add(itemRepository.findByDescriptionContaining(word));
                }
            }

            //add only unique results
            for (List<Item> list : itemList) {
                uniqueSearchResults.addAll(list);
            }
            return new ResponseEntity<>(uniqueSearchResults, HttpStatus.OK);
        }
    }


    public ResponseEntity<?> addItem(AddRequest addRequest, User user) {

        String itemName = addRequest.getItemName();
        Item addedItem = itemRepository.findAllByName(addRequest.getItemName());

        //check that the item exists and is not in the user's cart
        if (itemRepository.existsByName(itemName) & !basketRepository.existsByItemIdAndUserId(addedItem.getId(),user.getId())) {
            basketRepository.insertItem(addedItem.getId(),user.getId());
        } else if (!itemRepository.existsByName(itemName)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Entered item doesn't found"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("You can bue only one copy of product"));
        }
        return ResponseEntity.ok(new MessageResponse("Item added into basket"));
    }


    public ResponseEntity<?> buyItem(User user) {

        String userEmail = user.getEmail();

        //check that the users basket not empty
        List<Basket> userItemList = basketRepository.findItemByUserId(user.getId());
        if (userItemList.size() == 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("The basket is empty!"));
        }

        //get full information about item
        List<Item> namesOfUsersItems = userItemList.stream().map(item -> item.getItem()).collect(Collectors.toList());
        List<String> fullInformationAboutItem = new ArrayList<>();
        for (Item usersItem : namesOfUsersItems) {
            fullInformationAboutItem.add(itemRepository.findAllByName(usersItem.getName()).getName());
            fullInformationAboutItem.add(itemRepository.findAllByName(usersItem.getName()).getDescription());
        }

        // send inf about item on the users email
        String message = "the products you bought: " + fullInformationAboutItem;
        // fullInformationAboutItem.stream().forEach(item -> System.out.println(item));
        mailSender.send(userEmail, "your list of purchased items ", message);
        //clean the basket
        basketRepository.deleteAllByUserId(user.getId());
        return ResponseEntity.ok(new MessageResponse("Email with information about purchased sended on your email"));
    }
}
