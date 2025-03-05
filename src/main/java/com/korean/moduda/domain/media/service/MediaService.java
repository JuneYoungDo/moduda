package com.korean.moduda.domain.media.service;

import com.korean.moduda.domain.lecture.Lecture;
import com.korean.moduda.domain.lecture.repository.LectureRepository;
import com.korean.moduda.domain.media.Media;
import com.korean.moduda.domain.media.MediaType;
import com.korean.moduda.domain.media.dto.SaveMediaFileResponse;
import com.korean.moduda.domain.media.repository.MediaRepository;
import com.korean.moduda.domain.member.Member;
import com.korean.moduda.global.util.S3Service;
import jakarta.transaction.Transactional;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class MediaService {

    private final MediaRepository mediaRepository;
    private final LectureRepository lectureRepository;
    private final S3Service s3Service;

    public void save(Media media) {
        mediaRepository.save(media);
    }

    public SaveMediaFileResponse saveAudioFile(Member member, Long lectureId, MultipartFile file)
        throws IOException {
        Lecture lecture = lectureRepository.findById(lectureId).orElse(null);
        String url = s3Service.uploadMedia(member.getId(), file, MediaType.AUDIO);
        Media media = Media.builder()
            .mediaType(MediaType.AUDIO)
            .url(url)
            .member(member)
            .lecture(lecture)
            .build();

        save(media);
        return new SaveMediaFileResponse(media.getUrl());
    }

    public SaveMediaFileResponse saveVideoFile(Member member, Long lectureId, MultipartFile file)
        throws IOException {
        Lecture lecture = lectureRepository.findById(lectureId).orElse(null);
        String url = s3Service.uploadMedia(member.getId(), file, MediaType.VIDEO);
        Media media = Media.builder()
            .mediaType(MediaType.VIDEO)
            .url(url)
            .member(member)
            .lecture(lecture)
            .build();

        save(media);
        return new SaveMediaFileResponse(media.getUrl());
    }
}
