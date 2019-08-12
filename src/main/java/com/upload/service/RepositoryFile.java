package com.upload.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upload.entity.MetaData;

/**
 *
 * @author cibarra
 */

public interface RepositoryFile extends JpaRepository<MetaData, Long> {
}