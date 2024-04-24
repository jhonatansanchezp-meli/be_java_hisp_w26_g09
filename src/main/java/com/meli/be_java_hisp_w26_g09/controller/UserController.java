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
    public ResponseEntity<?> getFollowed(@PathVariable Integer userId){
        return ResponseEntity.ok(userService.getFollowedById(userId));
    }
    @GetMapping("/{userId}/followers/list")
    public ResponseEntity<?> getFollowers(@PathVariable Integer userId){
        return ResponseEntity.ok(userService.getFollowersById(userId));
    }


    @GetMapping("{userId}/followeds/list")
    public ResponseEntity<?> getFollowedSorted(@PathVariable Integer userId,
                                               @RequestParam String order){
        return ResponseEntity.ok(userService.getFollowedByIdOrdered(userId, order));
    }


}
