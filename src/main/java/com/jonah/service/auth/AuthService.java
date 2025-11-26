package com.jonah.service.auth;

import com.jonah.dto.request.AppUserDto;
import com.jonah.dto.request.AuthDto;
import com.jonah.dto.response.AuthResponseDto;

public interface AuthService {
    public AuthResponseDto registerUser(AppUserDto appUserDto);
    public AuthResponseDto loginUser(AuthDto authDto);
}
