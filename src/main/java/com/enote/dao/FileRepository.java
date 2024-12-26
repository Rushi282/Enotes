package com.enote.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enote.entity.FileDetails;

public interface FileRepository extends JpaRepository<FileDetails, Integer> {

}
