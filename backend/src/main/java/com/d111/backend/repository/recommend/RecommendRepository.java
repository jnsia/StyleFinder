package com.d111.backend.repository.recommend;

import com.d111.backend.entity.recommend.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendRepository extends JpaRepository<Recommend, Long> {

}