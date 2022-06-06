package com.example.moviecatalog.dao;

import com.example.moviecatalog.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<RoleEntity, Long> {
}
