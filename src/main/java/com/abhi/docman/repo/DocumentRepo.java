package com.abhi.docman.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abhi.docman.model.Document;

public interface DocumentRepo extends JpaRepository<Document,UUID> {

}
