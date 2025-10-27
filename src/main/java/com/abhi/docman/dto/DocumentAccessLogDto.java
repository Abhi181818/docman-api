package com.abhi.docman.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class DocumentAccessLogDto {
    public UUID id;
    public LocalDateTime accessedAt;
    public String city;
    public String country;
    public String userAgent;
    public String viewerEmail;
    public String viewerIp;
    public UUID documentId;
    public UUID shareId;
}

