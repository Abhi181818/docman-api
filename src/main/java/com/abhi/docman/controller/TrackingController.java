package com.abhi.docman.controller;

import com.abhi.docman.model.DocumentAccessLog;
import com.abhi.docman.service.DocumentService;
import com.abhi.docman.service.DocumentShareService;
import com.abhi.docman.service.TrackingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/api/tracking")
public class TrackingController {
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentShareService documentShareService;

    @GetMapping("/access-log/{trackingId}")
    public ResponseEntity<DocumentAccessLog> getAccessLog(@PathVariable  String trackingId) {
        DocumentAccessLog log = trackingService.getAccessLogByTrackingId(trackingId);
        if (log == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(log);
    }

//    @PostMapping("/share/{trackingId}")
//    public ResponseEntity<String> trackFileShare(@PathVariable String trackingId) {
//        try {
//            documentShareService.shareDocument(trackingId);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseEntity.notFound().build();
//    }

    @GetMapping("/pdf-tracking")
    public void trackPdf(@RequestParam String trackingId,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        try {
            String xff = request.getHeader("X-Forwarded-For");
            String viewerIp = (xff != null && !xff.isBlank()) ? xff.split(",")[0].trim() : request.getRemoteAddr();
            String userAgent = request.getHeader("User-Agent");
            documentShareService.shareDocument(trackingId, viewerIp, userAgent);
        } catch (Exception ignored) {
        }

        String base64Image = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR4nGNgYAAAAAMAASsJTYQAAAAASUVORK5CYII=";
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        response.setContentType("image/png");
        response.setContentLength(imageBytes.length);
        response.getOutputStream().write(imageBytes);
    }
}
