package com.abhi.docman.repo;

import com.abhi.docman.model.DocumentAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentAccessLogRepo extends JpaRepository<DocumentAccessLog, UUID> {
    @Query("SELECT dal FROM DocumentAccessLog dal WHERE dal.document.id = ?1")
    Optional<DocumentAccessLog> getAccessLogByDocumentId(UUID documentId);

    List<DocumentAccessLog> findAllByDocumentId(UUID documentId);

}
