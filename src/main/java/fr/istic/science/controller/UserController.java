package fr.istic.science.controller;

import fr.istic.science.exception.ResourceNotFoundException;
import fr.istic.science.model.User;
import fr.istic.science.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        try{
            User user = userService.getUserById(userId);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'utilisateur avec l'identifiant "+userId+" n'existe pas !");
        }
    }

    @GetMapping("")
    public ResponseEntity<Object> getUsers() {
        List<User> users = userService.getUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
    // Other CRUD endpoints for User
}

