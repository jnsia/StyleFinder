package com.d111.backend.repository.s3;

import com.d111.backend.entity.multipart.S3File;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class S3Repository {

    private final EntityManager em;

    /**
     * 로컬 DB에 파일에 대한 메타 데이터 정보 저장
     * @param s3File
     */
    public void upload(S3File s3File){
        em.persist(s3File);
    }
}