package com.d111.backend.service.closet;

import com.d111.backend.dto.closet.response.ClosetListReadResponseDTO;
import com.d111.backend.dto.closet.response.ClosetUploadResponseDTO;
import com.d111.backend.dto.recommend.response.ClothResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ClosetService {

    ResponseEntity<ClosetUploadResponseDTO> uploadCloset(String part, MultipartFile clothImage) throws Exception;

    ResponseEntity<List<ClosetListReadResponseDTO>> getClosets(String part);

    ResponseEntity<String> deleteCloset(Long closetId);

    ResponseEntity<ClothResponseDTO> getCloset(Long closetId);
}
