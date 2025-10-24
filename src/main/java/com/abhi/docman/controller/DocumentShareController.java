package com.abhi.docman.controller;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageDataFactory;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/document-shares")
public class DocumentShareController {

    @PostMapping("/embed-tracker")
    public void embedTracker(@RequestParam("file") MultipartFile file, @RequestParam String trackingId, HttpServletResponse response) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfReader reader = new PdfReader(file.getInputStream());
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(reader, writer);
        Document document = new Document(pdfDoc);
        String trackingUrl = "http://localhost:8080/pdf-tracker?trackingId=" + trackingId;

        Image trackingImage = new Image(ImageDataFactory.create(trackingUrl))
                .setAutoScale(false)
                .setWidth(1)
                .setHeight(1)
                .setFixedPosition(1, 1);
        document.add(trackingImage);
        document.close();

        response.setContentType(file.getContentType());
        response.setHeader("Content-Disposition", "attachment; filename=tracked-" + file.getOriginalFilename());
        response.getOutputStream().write(outputStream.toByteArray());
        response.getOutputStream().flush();
    }

}

