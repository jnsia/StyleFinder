package com.d111.backend.entity.multipart;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 파일 업로드를 위한 엔티티
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "file")
public class S3File {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String origFileName; // 원본 파일 명

    @Column(nullable = false)
    private String storedName; // 실제 저장되는 파일 명

    @Column(nullable = false)
    private String filePath; // 파일 저장 경로

    @Builder // 빌더 패턴
    public S3File(String origFileName, String storedName, String filePath){
        this.origFileName = origFileName;
        this.storedName = storedName;
        this.filePath = filePath;
    }
}