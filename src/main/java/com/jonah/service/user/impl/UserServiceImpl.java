package com.jonah.service.user.impl;

import com.jonah.model.AppUser;
import com.jonah.repository.UserRepository;
import com.jonah.service.user.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<AppUser> findUserById(Long userId) {
        return userRepository.findById( userId );
    }

    @Override
    public AppUser findByUsername(String username) {

        return userRepository.findByUsername(username).orElse( null );
    }

    @Override
    public AppUser saveUser(AppUser user) {
        return userRepository.save( user );
    }




}
