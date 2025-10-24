package com.abhi.docman.repo;

import com.abhi.docman.model.DocumentShare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentShareRepo extends JpaRepository<DocumentShare, UUID> {
}
