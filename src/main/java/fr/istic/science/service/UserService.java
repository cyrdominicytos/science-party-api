package fr.istic.science.service;

import fr.istic.science.dto.UserDto;
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

    public User updateUser(Long userId, UserDto userDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setName(userDetails.getName());
        user.setSurname(userDetails.getSurname());
        user.setPseudo(userDetails.getPseudo());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());

        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
