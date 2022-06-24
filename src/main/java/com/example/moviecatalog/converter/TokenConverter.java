package com.example.moviecatalog.converter;

import com.example.moviecatalog.dto.TokenDto;
import org.springframework.stereotype.Component;

@Component
public class TokenConverter {

    public TokenDto convert(String token) {
        return new TokenDto(token);
    }

}
