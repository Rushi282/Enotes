package com.enote.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enote.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
