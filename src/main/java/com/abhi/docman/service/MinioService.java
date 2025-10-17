package com.abhi.docman.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.PostConstruct;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    private final String bucketName = "documents";

    @PostConstruct
    public void init() throws Exception {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    public String uploadFile(MultipartFile file, String trackId) {
        String fName = trackId + "_" + file.getOriginalFilename();
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fName;
    }
    public byte[] getFile(String fileName) {
        byte[] data = null;
        try {
            data = minioClient.getObject(
                    GetObjectArgs.builder().bucket(bucketName).object(fileName).build()).readAllBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

}
