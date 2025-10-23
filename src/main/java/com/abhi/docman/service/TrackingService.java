package com.abhi.docman.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TrackingService {

    public String generateTrackingId() {
        return UUID.randomUUID().toString();
    }
}
