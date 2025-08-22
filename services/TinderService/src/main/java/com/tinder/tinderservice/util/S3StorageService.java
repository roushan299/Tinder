package com.tinder.tinderservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.IOException;

@Slf4j
@Service
public class S3StorageService {

    private final S3Client s3Client;

    @Value("${S3_BUCKET_NAME}")
    private String bucketName;

    public S3StorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadUserImage(String uuid, MultipartFile file) throws IOException {
        String folder = uuid;
        String key = folder+"/"+file.getOriginalFilename();

        //compress image before upload
        byte[] optimizeImage = ImageUtils.compressImage(file, 1024, 1024, 0.8f);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, software.amazon.awssdk.core.sync.RequestBody.fromBytes(optimizeImage));

        //Generate file URL
        String fileUrl = "https://"+bucketName+".s3.amazonaws.com/"+key;
        return  fileUrl;
    }

    public void deleteImage(String uuid, String fileName)throws IOException{
        String key = uuid+"/"+fileName;
        log.info("Deleting image from S3: bucket={}, key={}", bucketName, key);
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
        log.info("Image deleted successfully: key={}", key);
    }

}
