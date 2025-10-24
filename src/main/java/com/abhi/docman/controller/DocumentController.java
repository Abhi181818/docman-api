package com.abhi.docman.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.abhi.docman.model.Document;
import com.abhi.docman.model.User;
import com.abhi.docman.repo.UserRepo;
import com.abhi.docman.service.DocumentService;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private  UserRepo userRepo;

    @Autowired
    private DocumentService documentService;

    //upload files
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

//    all files for the user
    @GetMapping("/getAllFile")
    public  ResponseEntity<List<Document>> getAllFiles(Principal principal){
        User user=getUserFromPrincipal(principal);
         if(user==null) {
            return ResponseEntity.status(401).build();
         }
         List<Document> documents=documentService.getAllDocumentsForUser(user);
         return ResponseEntity.ok(documents);
    }

    //get file by fileName
    @GetMapping("/getFile/{fileName}")
    public ResponseEntity<byte[]> getFile(@PathVariable String fileName, Principal principal) {
        User user = getUserFromPrincipal(principal);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        byte[] fileData = documentService.getFileData(fileName);
        if (fileData == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fileData);
    }

      private User getUserFromPrincipal(Principal principal) {
        return userRepo.findByEmail(principal.getName());
    }
}