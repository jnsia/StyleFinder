package com.d111.backend.controller.feed;

import com.d111.backend.dto.feed.request.FeedCoordiCreateRequest;
import com.d111.backend.dto.feed.request.FeedUpdateRequest;
import com.d111.backend.dto.feed.request.FittingRequest;
import com.d111.backend.dto.feed.response.FeedDeleteResponse;
import com.d111.backend.dto.feed.response.FeedListReadResponse;
import com.d111.backend.dto.feed.response.FeedReadResponse;
import com.d111.backend.dto.feed.response.dto.FeedCoordiResponseDTO;
import com.d111.backend.entity.user.User;
import com.d111.backend.exception.user.EmailNotFoundException;
import com.d111.backend.repository.user.UserRepository;
import com.d111.backend.service.feed.FeedService;
import com.d111.backend.util.JWTUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Feed", description = "Feed API")
@RequestMapping("/api/feed")
@RestController
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;
    private final UserRepository userRepository;

    // 피드 전체 조회
    @GetMapping
    public ResponseEntity<FeedListReadResponse> readFeedList(@RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 8);
        return feedService.readList(pageable);
    }

    // 피드 및 코디 생성
    @PostMapping(value = "/create")
    public ResponseEntity<?> createFeedCoordi(@RequestBody FeedCoordiCreateRequest request) {
        return feedService.create(request.getFeedCreateRequest(), request.getCoordiCreateRequest());
    }

    // 피드 상세 조회
    @GetMapping("/{feedId}")
    public ResponseEntity<FeedReadResponse> readFeed(@PathVariable Long feedId){
        return feedService.read(feedId);
    }

    // 피드 삭제
    @DeleteMapping("/{feedId}")
    public ResponseEntity<FeedDeleteResponse> deleteFeed(@PathVariable Long feedId){
        return feedService.delete(feedId);
    }

    // 피드 좋아요
    @PostMapping("/like/{feedId}")
    public ResponseEntity<?> feedLikes(@PathVariable Long feedId) {
        return feedService.feedLikes(feedId);
    }

    // 피드 수정
    @PutMapping(value = "/update/{feedId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateFeed(@PathVariable Long feedId,
                                        @RequestPart(value = "feedUpdateRequest") FeedUpdateRequest feedUpdateRequest) {
        return feedService.update(feedId, feedUpdateRequest);
    }

    // 피드 인기순 조회
    @GetMapping("/popularity")
    public ResponseEntity<FeedListReadResponse> readPopularFeedList(@RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 8);
        return feedService.readPopularList(pageable);
    }


    // 피드 서치
//    @GetMapping("/search")
//    public ResponseEntity<FeedListReadResponse> searchByTitle(@RequestParam(value = "title") String title, Pageable pageable) {
//        return feedService.searchByTitle(title, pageable);
//    }

    
    // 내가 쓴 피드 조회
    @GetMapping("/myfeed")
    public ResponseEntity<FeedListReadResponse> searchMyFeed(@RequestParam(value = "page", defaultValue = "0") int page) {
        Optional<User> currentUserId = getCurrentUserId();
        Pageable pageable = PageRequest.of(page, 8);
        return feedService.searchMyFeed(currentUserId, pageable);
    }

    @GetMapping("/search")
    public ResponseEntity<FeedListReadResponse> searchByTitle(@RequestParam(value = "title") String title,
                                                              @RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 8);
        return feedService.searchByTitle(title, pageable);
    }

    @PostMapping(value = "/{feedId}/fitting")
    public ResponseEntity<FeedCoordiResponseDTO> fitting(@PathVariable Long feedId){
        return feedService.fitting(feedId);
    }

    private Optional<User> getCurrentUserId() {
        // 현재 로그인한 유저 정보 받아오기
        String userid = JWTUtil.findEmailByToken();
        Optional<User> currentUser = userRepository.findByEmail(userid);

        if (currentUser.isEmpty()) {
            throw new EmailNotFoundException("사용자를 찾을 수 없습니다.");
        }
        return currentUser;
    }
}
