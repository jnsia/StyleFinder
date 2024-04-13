package com.d111.backend.serviceImpl.sample;

import com.d111.backend.dto.sample.response.SampleTestResponseDTO;
import com.d111.backend.entity.sample.Sample;
import com.d111.backend.repository.sample.SampleRepository;
import com.d111.backend.service.sample.SampleService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SampleServiceImpl implements SampleService {

    private final SampleRepository sampleRepository;

    @Override
    public ResponseEntity<String> createSample(String sampleName) {
        Sample sample = Sample.builder()
                .sampleColumn(sampleName)
                .build();

        sampleRepository.save(sample);

        return ResponseEntity.status(HttpStatus.CREATED).body("Good");
    }

    @Override
    public ResponseEntity<List<SampleTestResponseDTO>> getSamples() {
        List<Sample> samples = sampleRepository.findAll();

        List<SampleTestResponseDTO> sampleTestResponses = new ArrayList<>();

        for (Sample sample: samples) {
            sampleTestResponses.add(SampleTestResponseDTO.builder().sampleColumn(sample.getSampleColumn()).build());
        }

        return ResponseEntity.status(HttpStatus.OK).body(sampleTestResponses);
    }
}
