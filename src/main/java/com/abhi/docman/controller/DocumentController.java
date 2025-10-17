package com.abhi.docman.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.abhi.docman.model.Document;
import com.abhi.docman.model.User;
import com.abhi.docman.repo.UserRepo;
import com.abhi.docman.service.DocumentService;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private  UserRepo userRepo;

    @Autowired
    private DocumentService documentService;

    DocumentController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/upload")
    public ResponseEntity<Document> uploadFile(@RequestParam("file") MultipartFile document, Principal principal) throws Exception {

        User user=getUserFromPrincipal(principal);
         if(user==null) {
            return ResponseEntity.status(401).build();
         }
        Document uploadedDocument = documentService.uploadDocument(document, user);
         if(uploadedDocument==null) {
            return ResponseEntity.status(500).build();
         }
        return ResponseEntity.ok(uploadedDocument);
    }

      private User getUserFromPrincipal(Principal principal) {
        return userRepo.findByEmail(principal.getName());
    }
}