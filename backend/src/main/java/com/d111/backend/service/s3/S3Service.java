package com.d111.backend.service.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.d111.backend.entity.multipart.S3File;
import com.d111.backend.repository.s3.S3Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class S3Service {

    private final AmazonS3Client amazonS3Client;
    private final S3Repository s3Repository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket; // 버킷 이름

    @Transactional
    public void saveUploadFile(MultipartFile multipartFile) {
        try{
            // 파일 메타 데이터 설정
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentEncoding(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getSize());

            // 파일명 수정
            String originalFileName = multipartFile.getOriginalFilename();
            int index = originalFileName.lastIndexOf(".");
            String ext = originalFileName.substring(index + 1);

            String storeFileName = UUID.randomUUID() + "." + ext;
            String key = "testFolder/" + storeFileName;

            // ObjectRequest 생성
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucket,
                    key,
                    multipartFile.getInputStream(),
                    objectMetadata
            );

            // S3에 오브젝트 저장
            amazonS3Client.putObject(putObjectRequest);

            // 파일 엔티티 생성 및 저장
            String fileUrl = amazonS3Client.getUrl(bucket, key).toString();
            S3File s3File = S3File.builder()
                    .origFileName(originalFileName)
                    .storedName(storeFileName)
                    .filePath(fileUrl)
                    .build();
            s3Repository.upload(s3File);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}