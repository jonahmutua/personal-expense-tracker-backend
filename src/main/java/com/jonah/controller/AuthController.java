package com.jonah.controller;

import com.jonah.dto.AppUserDto;
import com.jonah.dto.AuthDto;
import com.jonah.dto.AuthResponseDto;
import com.jonah.model.AppUser;
import com.jonah.service.auth.AuthService;
import com.jonah.service.user.UserService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> registerUser(@RequestBody AppUserDto appUserDto){

        AuthResponseDto response = authService.registerUser( appUserDto );

        if( "success".equals( response.getMessage() )){
            return ResponseEntity.ok( response );
        }
        return ResponseEntity.badRequest().body( response );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthDto authDto){
        AuthResponseDto response = authService.loginUser( authDto );

        if( "success".equals( response.getMessage() )){
            return ResponseEntity.ok( response );
        }

        return ResponseEntity.badRequest().body( response );
    }
}
