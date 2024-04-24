package com.meli.be_java_hisp_w26_g09.controller;

import com.meli.be_java_hisp_w26_g09.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/{userId}/followed/list")
    public ResponseEntity<?> getFollowedList(@PathVariable Integer userId,
                                             @RequestParam(required = false) String order) {
        if (order == null || order.isEmpty())
            return ResponseEntity.ok(userService.getFollowedById(userId));

        return ResponseEntity.ok(userService.getFollowedByIdOrdered(userId, order));
    }
}
