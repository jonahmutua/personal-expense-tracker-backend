package com.jonah.service.user.impl;

import com.jonah.model.AppUser;
import com.jonah.repository.UserRepository;
import com.jonah.service.user.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;

    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<AppUser> getALlUsers() {
        return userRepository.findAll();
    }
}
