package com.ecom;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;


    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getAllUsers (){

        return new ResponseEntity<>(userService.fetchAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<User> getUser (@PathVariable Long id){
//        User user = userService.fetchUser(id);
//        if (user == null)
//            return ResponseEntity.notFound().build();
//        return new ResponseEntity<>(userService.fetchUser(id),HttpStatus.FOUND);

            return userService.fetchUser(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(()-> ResponseEntity.notFound().build());

    }
    @PostMapping ("/api/users")
    public ResponseEntity<String> createUser (@RequestBody User user){
        userService.addUser(user);
        return new ResponseEntity<>("User added successfully", HttpStatus.CREATED);
    }

    @PutMapping ("/api/users/{id}")
    public ResponseEntity<String> updateUser (@PathVariable Long id,
                                            @RequestBody User updatedUser){
        boolean updated = userService.updateUser(id,updatedUser);
        if (updated)
            return ResponseEntity.ok("User Updated Successfully!");
        return ResponseEntity.notFound().build();
    }

}
