package com.abhi.docman.controller;

import com.abhi.docman.dto.DocumentShareDto;
import com.abhi.docman.repo.DocumentRepo;
import com.abhi.docman.repo.DocumentShareRepo;
import com.abhi.docman.service.DocumentService;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/document-shares")
public class DocumentShareController {

    @Autowired
    private DocumentRepo documentRepo;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentShareRepo documentShareRepo;

    private DocumentShareDto toDto(com.abhi.docman.model.DocumentShare e) {
        DocumentShareDto d = new DocumentShareDto();
        d.id = e.getId();
        d.documentId = e.getDocument() != null ? e.getDocument().getId() : null;
        d.createdAt = e.getCreatedAt();
        return d;
    }

    @PostMapping("/embed-tracker")
    public void embedTracker(@RequestParam String trackingId, HttpServletResponse response) {
        try {
            com.abhi.docman.model.Document doc = documentRepo.findByTrackingId(trackingId);
            if (doc == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid trackingId");
                return;
            }
            byte[] original = documentService.getFileData(doc.getStoragePath());
            if (original == null || original.length == 0) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found in storage");
                return;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfReader reader = new PdfReader(new ByteArrayInputStream(original));
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(reader, writer);
            Document iTextDoc = new Document(pdfDoc);

            String trackingUrl = "http://localhost:8080/api/tracking/pdf-tracking?trackingId=" + trackingId;
            PdfAction js = PdfAction.createJavaScript("try{app.launchURL('" + trackingUrl + "', false);}catch(e){}");
            pdfDoc.getCatalog().setOpenAction(js);

            iTextDoc.close();

            byte[] bytes = outputStream.toByteArray();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=tracked-" + doc.getOriginalFileName());
            response.setContentLength(bytes.length);
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/document/{documentId}")
    public ResponseEntity<List<DocumentShareDto>> getSharesByDocumentId(@PathVariable UUID documentId) {
        List<com.abhi.docman.model.DocumentShare> shares = documentShareRepo.findAllByDocumentId(documentId);
        List<DocumentShareDto> dtos = shares.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/tracking/{trackingId}")
    public ResponseEntity<List<DocumentShareDto>> getSharesByTrackingId(@PathVariable String trackingId) {
        var doc = documentRepo.findByTrackingId(trackingId);
        if (doc == null) {
            return ResponseEntity.notFound().build();
        }
        List<com.abhi.docman.model.DocumentShare> shares = documentShareRepo.findAllByDocumentId(doc.getId());
        List<DocumentShareDto> dtos = shares.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
