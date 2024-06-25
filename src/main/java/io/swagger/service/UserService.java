package io.swagger.service;

import io.swagger.Security.JwtTokenProvider;
import io.swagger.model.entity.Role;
import io.swagger.model.entity.User;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

   // @Autowired
    private UserRepository userRepository;
   // @Autowired
    PasswordEncoder passwordEncoder;
  //  @Autowired
    private JwtTokenProvider jwtTokenProvider;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public List<User> getUsers() {

        return userRepository.findAll();
    }

    public User getUser(UUID userID) {
        return userRepository.getOne(userID);
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setTransactionLimit(500.0);
            user.setDayLimit(2000.0);
            user.setCurrentDayLimit(2000.0);
            userRepository.save(user);
            return jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
        } else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Email is already in use");
        }

       // return userRepository.save(user);
    }


    public void deleteUser(UUID userID) {
        userRepository.deleteById(userID);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
