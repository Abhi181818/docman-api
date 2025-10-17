package com.abhi.docman.service;

import java.util.UUID;

public class TrackingService {

    public String generateTrackingId() {
        return UUID.randomUUID().toString();
    }
}
