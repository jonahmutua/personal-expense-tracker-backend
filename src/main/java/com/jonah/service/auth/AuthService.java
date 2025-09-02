package com.jonah.service.auth;

import com.jonah.dto.AppUserDto;
import com.jonah.dto.AuthDto;
import com.jonah.dto.AuthResponseDto;
import com.jonah.model.AppUser;

public interface AuthService {
    public AuthResponseDto registerUser(AppUserDto appUserDto);
    public AuthResponseDto loginUser(AuthDto authDto);
}
