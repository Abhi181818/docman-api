package com.abhi.docman.service;

import com.abhi.docman.model.Document;
import com.abhi.docman.model.DocumentShare;
import com.abhi.docman.repo.DocumentAccessLogRepo;
import com.abhi.docman.repo.DocumentRepo;
import com.abhi.docman.repo.DocumentShareRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentShareService {
    @Autowired
    private DocumentRepo documentRepo;
    @Autowired
    private DocumentShareRepo documentShareRepo;
    @Autowired
    private DocumentAccessLogRepo documentAccessLogRepo;

    public void shareDocument(String trackingId) {
        try {
            Document file=documentRepo.findByTrackingId(trackingId);
            if(file==null){
                throw new RuntimeException("Invalid tracking ID");
            }
            // file valid, insert in db document_shares and document_access_logs
            DocumentShare documentShare=new DocumentShare();
            documentShare.setDocument(file);
            documentShareRepo.save(documentShare);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
