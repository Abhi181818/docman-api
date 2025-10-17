package com.abhi.docman.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.abhi.docman.model.Document;
import com.abhi.docman.model.User;
import com.abhi.docman.repo.DocumentRepo;

public class DocumentService {


    @Autowired
    private DocumentRepo documentRepository;
    @Autowired
    private MinioService minioService;
    @Autowired
    private TrackingService trackingService;

    public Document uploadDocument(MultipartFile file,User owner) throws Exception {
        
        String trackingId = trackingService.generateTrackingId();
        String fileName = minioService.uploadFile(file, trackingId);
        
        Document document = new Document();
        document.setFileName(fileName);
        document.setOriginalFileName(file.getOriginalFilename());
        document.setFileType(file.getContentType());
        document.setFileSize(file.getSize());
        document.setStoragePath(fileName);
        document.setTrackingId(trackingId);
        document.setOwner(owner);
        
        return documentRepository.save(document);
    }
}
