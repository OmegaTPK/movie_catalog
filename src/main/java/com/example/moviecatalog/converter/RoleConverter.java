package com.example.moviecatalog.converter;

import com.example.moviecatalog.dto.RoleDto;
import com.example.moviecatalog.entity.RoleEntity;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter {

    public RoleDto convert(RoleEntity roleEntity) {
        return new RoleDto(roleEntity.getId(),
                roleEntity.getName());
    }
}
