package com.abhi.docman.repo;

import java.util.List;
import java.util.UUID;

import com.abhi.docman.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.abhi.docman.model.Document;

public interface DocumentRepo extends JpaRepository<Document,UUID> {

    List<Document> findByOwner(User user);

}
