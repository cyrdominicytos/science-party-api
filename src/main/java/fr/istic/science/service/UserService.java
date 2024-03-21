package fr.istic.science.service;

import fr.istic.science.exception.ResourceNotFoundException;
import fr.istic.science.model.User;
import fr.istic.science.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
    // Other CRUD operations for User
}
