package com.jonah.service.user.impl;

import com.jonah.model.AppUser;
import com.jonah.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser appUser = userRepository.findByUsername( username ).orElseThrow(
                ()->new UsernameNotFoundException("User not found."));

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(
                "ROLE_" + appUser.getRole().name() );

        return new User(appUser.getUsername(),
                appUser.getPassword(),
                Collections.singleton( grantedAuthority ) );
    }
}
