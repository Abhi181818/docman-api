package com.abhi.docman.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.minio.MinioClient;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    private final String bucketName = "documents";

}
