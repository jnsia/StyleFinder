package com.d111.backend.controller.s3;

import com.d111.backend.service.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("s3File") MultipartFile multipartFile){
        s3Service.saveUploadFile(multipartFile);
        return ResponseEntity.ok().body("파일 업로드가 완료되었습니다.");
    }
}
