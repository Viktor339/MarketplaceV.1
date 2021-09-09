package com.boot.controller;

import com.boot.pojo.ChangeRequest;
import com.boot.pojo.CreateRequest;
import com.boot.pojo.DeleteRequest;
import com.boot.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name="Admin controller", description="Allows admin to create/delete/change item")
public class AdminController {
    private static  final boolean FORCE_UPDATE = true;

    @Autowired
    private AdminService adminService;


    @Operation(summary = "Create item", description = "Allows admin to change item")
    @PostMapping()
    public ResponseEntity<?> createItem(@RequestBody CreateRequest createRequest) {

        return adminService.createItem(createRequest);
    }

    @Operation(summary="Change item controller", description="Allows admin to change item")
    @PutMapping()
    public ResponseEntity<?> changeItem(@RequestBody ChangeRequest changeRequest, boolean forceUpdate) {
        return adminService.changeItem(changeRequest, forceUpdate);
    }

    @Operation(summary="Delete item controller", description="Allows admin to delete item")
    @DeleteMapping()
    public ResponseEntity<?> deleteItem(@RequestBody DeleteRequest deleteRequest, boolean forceUpdate) {
        return adminService.deleteItem(deleteRequest, forceUpdate);
    }

    @Operation(summary="Change item controller with \"force update\"", description="Allows admin to change items even if they are in the user's basket")
    @PutMapping("/change/force")
    public ResponseEntity<?> changeForce(@RequestBody ChangeRequest changeRequest) {
        return changeItem(changeRequest, FORCE_UPDATE);
    }

    @Operation(summary="Delete item controller with \"force update\"", description="Allows admin to delete items even if they are in the user's basket")
    @DeleteMapping("/delete/force")
    public ResponseEntity<?> deleteForce(@RequestBody DeleteRequest deleteRequest) {
        return deleteItem(deleteRequest, FORCE_UPDATE);

    }

}
