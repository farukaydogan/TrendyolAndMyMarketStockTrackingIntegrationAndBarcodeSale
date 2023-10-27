package com.fta.stock.tracking.service;

import com.fta.stock.tracking.model.Role;
import com.fta.stock.tracking.model.Settings;
import com.fta.stock.tracking.model.User;
import com.fta.stock.tracking.repository.SettingsRepository;
import com.fta.stock.tracking.repository.UserRepository;
import com.fta.stock.tracking.requests.UpdateSettingsRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {


    private UserRepository userRepository;
    private SettingsRepository settingsRepository;

    public Optional<User> getUserById(Integer userId){
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }
    public User saveUser(User user){

        return userRepository.save(user);
    }

    @Transactional
    public User createUserWithSettings(User user, Settings settings) {
        settings = settingsRepository.save(settings); // Ensure settings is attached to the persistence context
        user.setSettings(settings);
        settings.setUser(user);
        return userRepository.save(user);
    }

    public User findUserByEmailAndRole(String email, Role role){
        User user=  userRepository.findByEmail(email).orElseThrow();
        if (user.getRole()==Role.USER){
            return  null;
        }
        else {
            return  user;
        }
    }

}