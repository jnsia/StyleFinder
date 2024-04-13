package com.d111.backend.service.coordi;

import com.d111.backend.dto.coordi.request.CoordiCreateRequest;
import com.d111.backend.dto.coordi.response.CoordiCreateResponse;
import com.d111.backend.dto.coordi.response.CoordiReadResponse;
import org.springframework.http.ResponseEntity;

public interface MongoCoordiService {

    ResponseEntity<CoordiCreateResponse> create(CoordiCreateRequest coordiCreateRequest);

    ResponseEntity<CoordiReadResponse> read(String id);



}
