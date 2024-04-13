package com.d111.backend.repository.closet;

import com.d111.backend.entity.closet.Closet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClosetRepository extends JpaRepository<Closet, Long> {

}
