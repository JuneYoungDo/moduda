package com.korean.moduda.domain.lecture.controller;

import com.korean.moduda.domain.lecture.service.LectureService;
import com.korean.moduda.domain.media.dto.SaveMediaFileResponse;
import com.korean.moduda.domain.media.service.MediaService;
import com.korean.moduda.domain.member.Member;
import com.korean.moduda.global.security.CurrentMember;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/lectures")
@RequiredArgsConstructor
public class PostLectureController {

    private final LectureService lectureService;
    private final MediaService mediaService;

    /**
     * 강의 완료하기
     *
     * @param member
     * @param lectureId
     * @return
     */
    @PostMapping("/{lectureId}/complete")
    public ResponseEntity<Void> completeLecture(@CurrentMember Member member,
        @PathVariable Long lectureId) {
        lectureService.completeLecture(member, lectureId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{lectureId}/audio")
    public ResponseEntity<SaveMediaFileResponse> uploadAudio(@CurrentMember Member member,
        @PathVariable Long lectureId, @RequestPart(name = "file")
        MultipartFile file) throws IOException {
        return new ResponseEntity(mediaService.saveAudioFile(member, lectureId, file),
            HttpStatus.OK);
    }

    @PostMapping("/{lectureId}/video")
    public ResponseEntity<SaveMediaFileResponse> uploadVideo(@CurrentMember Member member,
        @PathVariable Long lectureId, @RequestPart(name = "file")
        MultipartFile file) throws IOException {
        return new ResponseEntity(mediaService.saveVideoFile(member, lectureId, file),
            HttpStatus.OK);
    }
}
