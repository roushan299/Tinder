package com.tinder.tinderservice.util;

import com.tinder.tinderservice.service.IUserImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class S3StorageService {

    private final S3Client s3Client;
    private final IUserImageService userImageService;

    @Value("${S3_BUCKET_NAME}")
    private String bucketName;

    public S3StorageService(S3Client s3Client, IUserImageService userImageService) {
        this.s3Client = s3Client;
        this.userImageService = userImageService;
    }

    public String uploadUserImage(Long userId, String uuid, MultipartFile file) throws IOException {
        String folder = uuid;
        String key = folder+"/"+file.getOriginalFilename();
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

        //Generate file URL
        String fileUrl = "https://"+bucketName+".s3.amazonaws.com/"+key;

        //save in DB
        userImageService.saveImageUrl(userId, fileUrl);
        return  fileUrl;
    }

}
