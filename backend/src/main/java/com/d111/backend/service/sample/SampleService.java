package com.d111.backend.service.sample;

import com.d111.backend.dto.sample.response.SampleTestResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SampleService {

    ResponseEntity<List<SampleTestResponseDTO>> getSamples();

    ResponseEntity<String> createSample(String sampleName);

}
