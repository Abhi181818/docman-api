package com.abhi.docman.controller;

import com.abhi.docman.dto.DocumentAccessLogDto;
import com.abhi.docman.model.DocumentAccessLog;
import com.abhi.docman.repo.DocumentAccessLogRepo;
import com.abhi.docman.repo.DocumentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/document-access-logs")
public class DocumentAccessLogsController {

    @Autowired
    private DocumentAccessLogRepo documentAccessLogRepo;
    @Autowired
    private DocumentRepo documentRepo;

    private DocumentAccessLogDto toDto(DocumentAccessLog e) {
        DocumentAccessLogDto d = new DocumentAccessLogDto();
        d.id = e.getId();
        d.accessedAt = e.getAccessedAt();
        d.city = e.getCity();
        d.country = e.getCountry();
        d.userAgent = e.getUserAgent();
        d.viewerEmail = e.getViewerEmail();
        d.viewerIp = e.getViewerIp();
        d.documentId = e.getDocument() != null ? e.getDocument().getId() : null;
        d.shareId = e.getDocumentShare() != null ? e.getDocumentShare().getId() : null;
        return d;
    }

    @GetMapping("/document/{documentId}")
    public ResponseEntity<List<DocumentAccessLogDto>> getAccessLogsByDocumentId(@PathVariable UUID documentId) {
        List<DocumentAccessLog> logs = documentAccessLogRepo.findAllByDocumentId(documentId);
        List<DocumentAccessLogDto> dtos = logs.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/tracking/{trackingId}")
    public ResponseEntity<List<DocumentAccessLogDto>> getAccessLogsByTrackingId(@PathVariable String trackingId) {
        var doc = documentRepo.findByTrackingId(trackingId);
        if (doc == null) {
            return ResponseEntity.notFound().build();
        }
        List<DocumentAccessLog> logs = documentAccessLogRepo.findAllByDocumentId(doc.getId());
        List<DocumentAccessLogDto> dtos = logs.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
