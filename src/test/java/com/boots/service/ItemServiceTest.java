package com.boots.service;

import com.boots.entity.Basket;
import com.boots.entity.Item;
import com.boots.entity.User;
import com.boots.pojo.AddRequest;
import com.boots.pojo.MessageResponse;
import com.boots.pojo.SearchRequest;
import com.boots.repository.BasketRepository;
import com.boots.repository.ItemRepository;
import com.boots.repository.UserRepository;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@SpringBootTest
public class ItemServiceTest{
    @MockBean
    private BasketRepository basketRepository;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ItemService itemService;

    @MockBean
    private MailSender mailSender;

    private static User user = new User();
    private static AddRequest addRequest = new AddRequest();
    private static List<Basket> userItemList = new ArrayList<>();

    @BeforeClass
    public static void setUp() {
        user.setEmail("user.user@gmail.com");
        user.setPassword("user");
        user.setUsername("user");
        user.setId(123L);
        addRequest.setItemName("ItemName");
    }

    @Test
    public void testSearchItem_WhenDescriptionAndTagsIsNull_MustReturnHttpStatusBadRequest() {
        SearchRequest searchRequest = new SearchRequest(null, null);

        when(this.itemRepository.findByDescriptionContaining((String) any())).thenReturn(new ArrayList<Item>());
        when(this.itemRepository.findByTagsContaining((String) any())).thenReturn(new ArrayList<Item>());

        ResponseEntity<?> actualSearchItemResult = this.itemService.searchItem(searchRequest);
        assertTrue(actualSearchItemResult.hasBody());
        assertTrue(actualSearchItemResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.BAD_REQUEST, actualSearchItemResult.getStatusCode());
        assertEquals("Error: Enter your search criteria",
                ((MessageResponse) actualSearchItemResult.getBody()).getMessage());
    }

    @Test
    public void testSearchItem_WhenDescriptionIsNull_MustReturnHttpStatusBadRequest() {
        SearchRequest searchRequest = new SearchRequest(new String[]{"Tags"}, null);

        when(this.itemRepository.findByDescriptionContaining((String) any())).thenReturn(new ArrayList<Item>());
        when(this.itemRepository.findByTagsContaining((String) any())).thenReturn(new ArrayList<Item>());

        ResponseEntity<?> actualSearchItemResult = this.itemService.searchItem(searchRequest);
        assertTrue(((Collection<Object>) actualSearchItemResult.getBody()).isEmpty());
        assertEquals("<200 OK OK,[],[]>", actualSearchItemResult.toString());
        assertTrue(actualSearchItemResult.hasBody());
        assertEquals(HttpStatus.OK, actualSearchItemResult.getStatusCode());
        assertTrue(actualSearchItemResult.getHeaders().isEmpty());
        verify(this.itemRepository).findByTagsContaining((String) any());
    }

    @Test
    public void testSearchItem_WhenTagsIsNull_MustReturnHttpStatusBadRequest() {
        SearchRequest searchRequest = new SearchRequest(null, "Description");

        when(this.itemRepository.findByDescriptionContaining((String) any())).thenReturn(new ArrayList<Item>());
        when(this.itemRepository.findByTagsContaining((String) any())).thenReturn(new ArrayList<Item>());

        ResponseEntity<?> actualSearchItemResult = this.itemService.searchItem(searchRequest);
        assertTrue(((Collection<Object>) actualSearchItemResult.getBody()).isEmpty());
        assertEquals("<200 OK OK,[],[]>", actualSearchItemResult.toString());
        assertTrue(actualSearchItemResult.hasBody());
        assertEquals(HttpStatus.OK, actualSearchItemResult.getStatusCode());
        assertTrue(actualSearchItemResult.getHeaders().isEmpty());
        verify(this.itemRepository, atLeast(1)).findByDescriptionContaining((String) any());
    }


    @Test
    @WithMockUser(username = "user", password = "user")
    public void testAddItem_MustReturnHttpStatusOK() {
        when(itemRepository.existsByName((String) any())).thenReturn(true);
        when(basketRepository.existsByItem((String) any())).thenReturn(false);
        when(this.userRepository.findAllByUsername((String) any())).thenReturn(user);
        when(itemRepository.existsByName((String) any())).thenReturn(true);
        when(basketRepository.existsByItem((String) any())).thenReturn(false);

        ResponseEntity<?> actualAddItemResult = this.itemService.addItem(addRequest);

        assertTrue(actualAddItemResult.hasBody());
        assertEquals(HttpStatus.OK, actualAddItemResult.getStatusCode());
        assertTrue(actualAddItemResult.getHeaders().isEmpty());
        assertEquals("Item added into basket",
                ((MessageResponse) actualAddItemResult.getBody()).getMessage());
    }


    @Test
    @WithMockUser(username = "user", password = "user")
    public void testAddItem_WhenTheProductDoesNotExist_MustReturnHttpStatusBadRequest() {
        when(itemRepository.existsByName((String) any())).thenReturn(true);
        when(basketRepository.existsByItem((String) any())).thenReturn(false);
        when(this.userRepository.findAllByUsername((String) any())).thenReturn(user);
        when(itemRepository.existsByName((String) any())).thenReturn(false);
        when(basketRepository.existsByItem((String) any())).thenReturn(true);

        ResponseEntity<?> actualAddItemResult = this.itemService.addItem(addRequest);

        assertTrue(actualAddItemResult.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, actualAddItemResult.getStatusCode());
        assertTrue(actualAddItemResult.getHeaders().isEmpty());
        assertEquals("Entered item doesn't found",
                ((MessageResponse) actualAddItemResult.getBody()).getMessage());
    }

    @Test
    @WithMockUser(username = "user", password = "user")
    public void testAddItem_WhenTheProductIsAlreadyInTheBasket_MustReturnHttpStatusBadRequest() {
        when(this.userRepository.findAllByUsername((String) any())).thenReturn(user);
        when(itemRepository.existsByName((String) any())).thenReturn(true);
        when(basketRepository.existsByItemAndUserId((String) any(),(Long) any())).thenReturn(true);

        ResponseEntity<?> actualAddItemResult = this.itemService.addItem(addRequest);

        assertTrue(actualAddItemResult.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, actualAddItemResult.getStatusCode());
        assertTrue(actualAddItemResult.getHeaders().isEmpty());
        assertEquals("You can bue only one copy of product",
                ((MessageResponse) actualAddItemResult.getBody()).getMessage());
    }

    @Test
    @WithMockUser(username = "user", password = "user")
    public void testBuyItem_WhenTheBasketIsEmpty_MustReturnHttpStatusBadRequest() {
        when(userRepository.findAllByUsername((String) any())).thenReturn(user);
        when(basketRepository.findItemByUserId((Long) any())).thenReturn(userItemList);

        ResponseEntity<?> actualBuyItemResult = this.itemService.buyItem();

        assertTrue(actualBuyItemResult.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, actualBuyItemResult.getStatusCode());
        assertTrue(actualBuyItemResult.getHeaders().isEmpty());
        assertEquals("The basket is empty!",
                ((MessageResponse) actualBuyItemResult.getBody()).getMessage());
    }

    @Test
    @WithMockUser(username = "user", password = "user")
    public void testBuyItem_MustReturnHttpStatusOK() {
        List<Basket> userItemList = Arrays.asList(new Basket());

        when(userRepository.findAllByUsername((String) any())).thenReturn(user);
        when(itemRepository.findAllByName((String) any())).thenReturn(new Item());
        when(basketRepository.findItemByUserId((Long) any())).thenReturn(userItemList);

        ResponseEntity<?> actualBuyItemResult = this.itemService.buyItem();

        verify(mailSender, times(1)).send((String) any(), (String) any(), (String) any());
        verify(this.basketRepository, times(1)).deleteAllByUserId((Long) any());
        assertTrue(actualBuyItemResult.hasBody());
        assertEquals(HttpStatus.OK, actualBuyItemResult.getStatusCode());
        assertTrue(actualBuyItemResult.getHeaders().isEmpty());
        assertEquals("Email with information about purchased sended on your email",
                ((MessageResponse) actualBuyItemResult.getBody()).getMessage());
    }
}

