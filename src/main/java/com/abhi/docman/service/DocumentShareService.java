package com.abhi.docman.service;

import com.abhi.docman.model.Document;
import com.abhi.docman.model.DocumentShare;
import com.abhi.docman.model.DocumentAccessLog;
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
    @Autowired
    private GeoIpService geoIpService;

    public void shareDocument(String trackingId) {
        try {
            Document file=documentRepo.findByTrackingId(trackingId);
            if(file==null){
                throw new RuntimeException("Invalid tracking ID");
            }
            DocumentShare documentShare=new DocumentShare();
            documentShare.setDocument(file);
            documentShare = documentShareRepo.save(documentShare);

            DocumentAccessLog accessLog = new DocumentAccessLog();
            accessLog.setDocument(file);
            accessLog.setDocumentShare(documentShare);
            documentAccessLogRepo.save(accessLog);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void shareDocument(String trackingId, String viewerIp, String userAgent) {
        try {
            Document file = documentRepo.findByTrackingId(trackingId);
            if (file == null) {
                throw new RuntimeException("Invalid tracking ID");
            }
            DocumentShare documentShare = new DocumentShare();
            documentShare.setDocument(file);
            documentShare = documentShareRepo.save(documentShare);

            DocumentAccessLog accessLog = new DocumentAccessLog();
            accessLog.setDocument(file);
            accessLog.setDocumentShare(documentShare);
            accessLog.setViewerIp(viewerIp);
            accessLog.setUserAgent(userAgent);

            GeoIpService.Location loc = geoIpService.lookup(viewerIp);
            if (loc != null) {
                accessLog.setCountry(loc.country);
                accessLog.setCity(loc.city);
            }
            documentAccessLogRepo.save(accessLog);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
