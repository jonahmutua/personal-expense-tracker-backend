package com.jonah.service.user;

import com.jonah.model.AppUser;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface UserService {
    AppUser findByUsername(String username);
    AppUser saveUser(AppUser user);
    Optional<AppUser> findUserById( Long userId );
}
