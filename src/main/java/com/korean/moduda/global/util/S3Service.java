package com.korean.moduda.global.util;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.korean.moduda.domain.media.MediaType;
import java.io.IOException;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {
    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.url}")
    private String url;

    public S3Service(@Value("${cloud.aws.credentials.accessKey}") String accessKey,
        @Value("${cloud.aws.credentials.secretKey}") String secretKey,
        @Value("${cloud.aws.s3.bucket}") String bucket,
        @Value("${cloud.aws.region.static}") String region,
        @Value("${cloud.aws.s3.url}") String url) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucket = bucket;
        this.region = region;
        this.url = url;

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(region)
            .build();
    }

    public String uploadMedia(Long memberId, MultipartFile file, MediaType mediaType)
        throws IOException {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        s3Client = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(region)
            .build();
        String fileName = memberId + "/" + LocalDate.now() + "_" + file.getOriginalFilename();

        String filePath = "";
        if (mediaType.equals(MediaType.AUDIO)) {
            filePath = "/audio";
        } else if (mediaType.equals(MediaType.VIDEO)) {
            filePath = "/video";
        }

        ObjectMetadata metadata = new ObjectMetadata();
        s3Client.putObject(
            new PutObjectRequest(bucket + filePath, fileName, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return url + filePath + "/" + fileName;
    }

    public void deleteFile(String uploadFilePath) {
        try {
            s3Client.deleteObject(bucket, uploadFilePath);
        } catch (AmazonClientException e) {
            throw new RuntimeException("파일 삭제 실패: " + e.getMessage(), e);
        }
    }
}
