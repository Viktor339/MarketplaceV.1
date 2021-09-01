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
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private BasketRepository basketRepository;

    @MockBean
    private MailSender mailSender;

    @MockBean
    private UserRepository userRepository;

    private static CreateRequest createRequest = new CreateRequest();
    private static ChangeRequest changeRequest = new ChangeRequest();
    private static DeleteRequest deleteRequest = new DeleteRequest();
    private static Item item = new Item();
    private static User user = new User();
    private static Basket basket = new Basket();
    private static final boolean FORCE_UPDATE = true;


    @BeforeClass
    public static void setUp() {
        createRequest.setName("Name");
        createRequest.setTags("Tags");
        createRequest.setDescription("The characteristics of someone or something");

        changeRequest.setNewItemDescription("New Item Description");
        changeRequest.setNewItemTags("New Item Tags");
        changeRequest.setNewItemName("New Item Name");
        changeRequest.setItemName("Item Name");

        item.setName("Name");
        item.setTags("Tags");
        item.setDescription("The characteristics of someone or something");

        deleteRequest.setItemName("ItemName");


    }

    @Test
    public void testCreateItem_ShouldReturnHttpStatusOK() {
        when(itemRepository.existsByTags((String) any())).thenReturn(false);

        ResponseEntity<?> actualCreateItemResult = this.adminService.createItem(createRequest);
        assertTrue(actualCreateItemResult.hasBody());
        assertTrue(actualCreateItemResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualCreateItemResult.getStatusCode());
        assertEquals("Added new item",
                ((MessageResponse) actualCreateItemResult.getBody()).getMessage());
    }

    @Test
    public void testCreateItem_WhenProductWithThisNameHasAlreadyBeenCreated_ShouldReturnHttpStatusBadRequest() {
        when(itemRepository.existsByTags((String) any())).thenReturn(true);

        ResponseEntity<?> actualCreateItemResult = this.adminService.createItem(createRequest);
        assertEquals(HttpStatus.BAD_REQUEST, actualCreateItemResult.getStatusCode());

        assertEquals("Error: Product with this name has already been created",
                ((MessageResponse) actualCreateItemResult.getBody()).getMessage());
    }


    @Test
    public void testChangeItem_WhenTheProductInTheUsersBasketAndWithoutForceUpdate_ShouldReturnHttpStatusBadRequest() {
        when(this.basketRepository.existsByItem((String) any())).thenReturn(true);

        ResponseEntity<?> actualCreateItemResult = this.adminService.changeItem(changeRequest, !FORCE_UPDATE);
        assertEquals(HttpStatus.BAD_REQUEST, actualCreateItemResult.getStatusCode());
        assertEquals("Error: This product is in the buyer's cart",
                ((MessageResponse) actualCreateItemResult.getBody()).getMessage());
    }

    @Test
    public void testChangeItem_WithForceUpdate_ShouldReturnHttpStatusOK() {
        when(itemRepository.existsByName((String) any())).thenReturn(true);
        when(userRepository.findUserById((Long) any())).thenReturn(user);
        when(itemRepository.findAllByName((String) any())).thenReturn(item);
        Item itemBeforeChange = itemRepository.findAllByName(changeRequest.getItemName());
        assertNotNull(itemBeforeChange);

        when(basketRepository.findBasketByItem(changeRequest.getItemName())).thenReturn(basket);
        when(userRepository.findUserById((Long) any())).thenReturn(user);
        ResponseEntity<?> actualCreateItemResult = this.adminService.changeItem(changeRequest, FORCE_UPDATE);

        verify(itemRepository).changeItem(changeRequest.getItemName(),
                changeRequest.getNewItemName(),
                changeRequest.getNewItemDescription(),
                changeRequest.getNewItemTags());
        verify(basketRepository, times(1)).forceUpdateItem((String) any(), (String) any());
        verify(mailSender, times(1)).send((String) any(), (String) any(), (String) any());

        assertEquals(HttpStatus.OK, actualCreateItemResult.getStatusCode());
        assertEquals("Item changed",
                ((MessageResponse) actualCreateItemResult.getBody()).getMessage());
    }

    @Test
    public void testDeleteItem_WhenTheProductInTheUsersBasketAndWithoutForceUpdate_ShouldReturnHttpStatusBadRequest() {
        when(basketRepository.existsByItem((String) any())).thenReturn(true);

        ResponseEntity<?> actualDeleteItemResult = this.adminService.deleteItem(deleteRequest, !FORCE_UPDATE);
        assertEquals(HttpStatus.BAD_REQUEST, actualDeleteItemResult.getStatusCode());
        assertEquals("Error: This product is in the buyer's cart",
                ((MessageResponse) actualDeleteItemResult.getBody()).getMessage());
    }

    @Test
    public void testDeleteItem_WhenItemNotFound_ShouldReturnHttpStatusBadRequest() {
        when(itemRepository.existsByName((String) any())).thenReturn(false);

        ResponseEntity<?> actualDeleteItemResult = this.adminService.deleteItem(deleteRequest, !FORCE_UPDATE);
        verify(itemRepository, times(1)).existsByName((String) any());
        assertEquals(HttpStatus.BAD_REQUEST, actualDeleteItemResult.getStatusCode());
        assertEquals("Error : Item with that name was not found",
                ((MessageResponse) actualDeleteItemResult.getBody()).getMessage());
    }

    @Test
    public void testDeleteItem_ShouldReturnHttpStatusOK() {
        when(itemRepository.existsByName(deleteRequest.getItemName())).thenReturn(true);
        when(basketRepository.findBasketByItem(deleteRequest.getItemName())).thenReturn(basket);
        when(userRepository.findUserById((Long) any())).thenReturn(user);

        ResponseEntity<?> actualDeleteItemResult = this.adminService.deleteItem(deleteRequest, FORCE_UPDATE);
        verify(itemRepository, times(1)).existsByName((String) any());
        verify(basketRepository, times(1)).deleteByItem(deleteRequest.getItemName());
        verify(mailSender, times(1)).send((String) any(), (String) any(), (String) any());

        assertEquals(HttpStatus.OK, actualDeleteItemResult.getStatusCode());
        assertEquals("Item deleted",
                ((MessageResponse) actualDeleteItemResult.getBody()).getMessage());
    }
}