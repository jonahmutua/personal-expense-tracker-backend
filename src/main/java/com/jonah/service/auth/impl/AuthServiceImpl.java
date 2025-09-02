package com.jonah.service.auth.impl;

import com.jonah.dto.AppUserDto;
import com.jonah.dto.AuthDto;
import com.jonah.dto.AuthResponseDto;
import com.jonah.model.AppUser;
import com.jonah.model.Role;
import com.jonah.security.JwtUtil;
import com.jonah.service.auth.AuthService;
import com.jonah.service.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserService userService, AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponseDto registerUser(AppUserDto appUserDto) {
        if( userService.findByUsername( appUserDto.getUsername()) != null){
            return new AuthResponseDto(null, "Username is already taken.");
        }
        AppUser appUser = new AppUser();
        // TODO: Impliment AppUser Mapper -> More elegant code
        appUser.setUsername( appUserDto.getUsername());
        appUser.setFullName( appUserDto.getFullName());
        appUser.setPassword( passwordEncoder.encode( appUserDto.getPassword()));
        appUser.setRole(Role.USER); // default Role = user

        userService.saveUser(appUser);

        AuthDto authDto = new AuthDto();
        authDto.setUsername( appUserDto.getUsername() );
        authDto.setPassword( appUserDto.getPassword() );

        return loginUser( authDto ); // Automatically login in user.
    }

    @Override
    public AuthResponseDto loginUser(AuthDto authDto) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken( authDto.getUsername(),
                            authDto.getPassword()) );

            final String token = jwtUtil.generateToken( authDto.getUsername() );

            return new AuthResponseDto( token, "success");
        }catch (BadCredentialsException e){
            return  new AuthResponseDto(null, "Error: Invalid username or password");
        }

    }
}
