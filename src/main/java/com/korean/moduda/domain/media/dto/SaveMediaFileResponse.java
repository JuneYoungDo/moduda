package com.korean.moduda.domain.media.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaveMediaFileResponse {
    private Long mediaId;
    private String fileUrl;
}
