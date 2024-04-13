package com.d111.backend.dto.closet.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClosetUploadResponseDTO {

    private List<String> category;

    private List<String> detail;

    private List<String> texture;

}
