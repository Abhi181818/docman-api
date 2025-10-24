package com.abhi.docman.service;

import com.abhi.docman.model.Document;
import com.abhi.docman.model.DocumentAccessLog;
import com.abhi.docman.repo.DocumentAccessLogRepo;
import com.abhi.docman.repo.DocumentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TrackingService {
    @Autowired
    private DocumentAccessLogRepo documentAccessLogRepo;
    @Autowired
    private DocumentRepo documentRepo;

    public String generateTrackingId() {
        return UUID.randomUUID().toString();
    }

    public DocumentAccessLog getAccessLogByTrackingId(String trackingId) {
        Document doc = documentRepo.findByTrackingId(trackingId);
        if (doc == null) {
            return null;
        }
        return documentAccessLogRepo.getAccessLogByDocumentId(doc.getId()).orElse(null);
    }
}
