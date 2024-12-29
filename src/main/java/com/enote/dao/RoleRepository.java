package com.enote.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enote.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
