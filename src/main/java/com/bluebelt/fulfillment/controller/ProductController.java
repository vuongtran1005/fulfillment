package com.bluebelt.fulfillment.controller;

import com.bluebelt.fulfillment.exception.AppException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @GetMapping("/api/v1/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAll() {
        return "Admin";
    }

    @GetMapping("/api/v1/user")
    @PreAuthorize("hasRole('USER')")
    public String getAll1() {
        return "User";
    }

}
