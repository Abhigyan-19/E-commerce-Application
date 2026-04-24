package com.ecom.controller;
import com.ecom.model.User;
import com.ecom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers (){

        return new ResponseEntity<>(userService.fetchAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser (@PathVariable Long id){
//        User user = userService.fetchUser(id);
//        if (user == null)
//            return ResponseEntity.notFound().build();
//        return new ResponseEntity<>(userService.fetchUser(id),HttpStatus.FOUND);

            return userService.fetchUser(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(()-> ResponseEntity.notFound().build());

    }
    @PostMapping
    public ResponseEntity<String> createUser (@RequestBody User user){
        userService.addUser(user);
        return new ResponseEntity<>("User added successfully", HttpStatus.CREATED);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<String> updateUser (@PathVariable Long id,
                                            @RequestBody User updatedUser){
        boolean updated = userService.updateUser(id,updatedUser);
        if (updated)
            return ResponseEntity.ok("User Updated Successfully!");
        return ResponseEntity.notFound().build();
    }

}
