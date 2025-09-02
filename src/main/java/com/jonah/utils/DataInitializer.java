package com.jonah.utils;

import com.jonah.model.AppUser;
import com.jonah.model.Role;
import com.jonah.repository.UserRepository;
import com.jonah.service.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, UserService userService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if(userService.findByUsername( "admin") == null ){
            AppUser adminUser = new AppUser();
            adminUser.setFullName( "Admin User" );
            adminUser.setUsername( "admin" );
            adminUser.setPassword( passwordEncoder.encode( "admin123") );
            adminUser.setRole(Role.ADMIN);

            userService.saveUser( adminUser );
            System.out.println("Admin user created with username: 'admin' " +
                    " and password: 'admin123" );
        }

    }
}
