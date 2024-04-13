package com.d111.backend.serviceImpl.feed;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.d111.backend.dto.coordi.request.CoordiCreateRequest;
import com.d111.backend.dto.coordi.response.dto.CoordiContainer;
import com.d111.backend.dto.feed.request.FeedCreateRequest;
import com.d111.backend.dto.feed.request.FeedUpdateRequest;
import com.d111.backend.dto.feed.request.FittingRequest;
import com.d111.backend.dto.feed.response.*;
import com.d111.backend.dto.feed.response.dto.*;
import com.d111.backend.dto.recommend.response.ClothResponseDTO;
import com.d111.backend.entity.comment.Comment;
import com.d111.backend.entity.coordi.Coordi;
import com.d111.backend.entity.feed.Feed;
import com.d111.backend.entity.likes.Likes;
import com.d111.backend.entity.user.User;
import com.d111.backend.exception.feed.CoordiNotFoundException;
import com.d111.backend.exception.feed.FeedImageIOException;
import com.d111.backend.exception.feed.FeedNotFoundException;
import com.d111.backend.exception.user.EmailNotFoundException;
import com.d111.backend.exception.user.UnauthorizedAccessException;
import com.d111.backend.repository.Likes.LikesRepository;
import com.d111.backend.repository.comment.CommentRepository;
import com.d111.backend.repository.feed.FeedRepository;
import com.d111.backend.repository.mongo.MongoCoordiRepository;
import com.d111.backend.repository.s3.S3Repository;
import com.d111.backend.repository.user.UserRepository;
import com.d111.backend.service.feed.FeedService;
import com.d111.backend.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.d111.backend.dto.coordi.response.dto.CoordiContainer.createMongoContainer;

@Service
@Log4j2
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final MongoCoordiRepository mongoCoordiRepository;
    private final S3Repository s3Repository;
    private final AmazonS3Client amazonS3Client;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket; // 버킷 이름


    // 피드 및 코디 생성
    @Override
    @Transactional
    public ResponseEntity<FeedCreateResponse> create(FeedCreateRequest feedCreateRequest,
                                                     CoordiCreateRequest coordiCreateRequest
                                                    ) {

        // Coordi 생성
        Coordi coordi = Coordi.createCoordi(coordiCreateRequest);
        Coordi savedCoordi = mongoCoordiRepository.save(coordi);
        String coordiId = savedCoordi.get_id(); // mongoDB에서 _id 값 가져옴

        String userid = JWTUtil.findEmailByToken();
        Optional<User> currentUser = userRepository.findByEmail(userid);
        Long userId = currentUser.get().getId();

        LocalDate now = LocalDate.now(ZoneId.of("UTC"));

        Feed feed = Feed.createFeed(feedCreateRequest, coordiId);

        if(feed.getOriginWriter() == null){
            feed.setOriginWriter(userId);
        }

        feed.setFeedCreatedDate(now);
        feed.setFeedUpdatedDate(now);
        feed.setUserId(currentUser.get());
        feed.setOuterCloth(feedCreateRequest.getOuterCloth());
        feed.setDress(feedCreateRequest.getDress());
        feed.setUpperBody(feedCreateRequest.getUpperBody());
        feed.setLowerBody(feedCreateRequest.getLowerBody());


        feedRepository.save(feed);

        FeedCreateResponse response = FeedCreateResponse.createFeedCreateResponse(
                "success",
                true
        );
        return ResponseEntity.ok(response);
    }

    // 피드 전체 조회
    @Override
    public ResponseEntity<FeedListReadResponse> readList(Pageable pageable) {
        Page<Feed> feedList = feedRepository.findAllByOrderByIdDesc(pageable);

        if (feedList.isEmpty()) {
            throw new FeedNotFoundException("피드를 찾을 수 없습니다.");
        }

        List<FeedListReadResponseDTO> feedListReadResponseDTOList = new ArrayList<>();

        // 각 피드의 이미지를 가져와서 리스트에 추가
        for (Feed feed : feedList) {
            log.info(feed.getFeedCreatedDate());
            Coordi coordi = mongoCoordiRepository.findById(feed.getCoordiId())
                    .orElseThrow(() -> new CoordiNotFoundException("코디를 찾을 수 없습니다."));

            String outerImage = feed.getOuterCloth();
            byte[] outerThumbnail = getThumbnailOrNull(bucket, outerImage);

            String upperImage = feed.getUpperBody();
            byte[] upperThumbnail = getThumbnailOrNull(bucket, upperImage);

            String dressImage = feed.getDress();
            byte[] dressThumbnail = getThumbnailOrNull(bucket, dressImage);

            String lowerImage = feed.getLowerBody();
            byte[] lowerThumbnail = getThumbnailOrNull(bucket, lowerImage);

            CoordiContainer coordiContainer = createMongoContainer(feed.getCoordiId(), mongoCoordiRepository);

            User user = feed.getUserId();
            FeedListUserDTO feedListUserDTO = FeedListUserDTO.builder()
                    .nickname(user.getNickname())
                    .profileImage(getFeedThumbnailFromS3(bucket, user.getProfileImage()))
                    .likeCategories(Arrays.asList(user.getLikeCategories().split(",")))
                    .dislikeCategories(Arrays.asList(user.getDislikeCategories().split(",")))
                    .introduce(user.getIntroduce())
                    .instagram(user.getInstagram())
                    .youtube(user.getYoutube())
                    .build();

            feedListReadResponseDTOList.add(
                    FeedListReadResponseDTO.builder()
                            .user(feedListUserDTO)
                            .feedId(feed.getId())
                            .feedTitle(feed.getFeedTitle())
                            .feedLikes(feed.getFeedLikes())
                            .outerCloth(outerThumbnail)
                            .dress(dressThumbnail)
                            .upperBody(upperThumbnail)
                            .lowerBody(lowerThumbnail)
                            .coordiContainer(coordiContainer)
                            .build()
            );
        }

        // 응답 생성
        FeedListReadResponse response = FeedListReadResponse.builder()
                .message("Success")
                .data(feedListReadResponseDTOList)
                .totalPage(feedList.getTotalPages())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 피드 상세 조회
    @Override
    public ResponseEntity<FeedReadResponse> read(Long feedId) {
        String userid = JWTUtil.findEmailByToken();

        User currentUser = userRepository.findByEmail(userid)
                .orElseThrow(() -> new UnauthorizedAccessException("로그인 해주세요."));

        Optional<Feed> optionalFeed = feedRepository.findById(feedId);
        Feed feed = optionalFeed.orElseThrow(() -> new FeedNotFoundException("피드를 찾을 수 없습니다."));

        Coordi coordi = mongoCoordiRepository.findById(feed.getCoordiId())
                .orElseThrow(() -> new CoordiNotFoundException("코디를 찾을 수 없습니다."));

        String outerImage = feed.getOuterCloth();
        byte[] outerThumbnail = getThumbnailOrNull(bucket, outerImage);

        String upperImage = feed.getUpperBody();
        byte[] upperThumbnail = getThumbnailOrNull(bucket, upperImage);

        String dressImage = feed.getDress();
        byte[] dressThumbnail = getThumbnailOrNull(bucket, dressImage);

        String lowerImage = feed.getLowerBody();
        byte[] lowerThumbnail = getThumbnailOrNull(bucket, lowerImage);

        CoordiContainer coordiContainer = createMongoContainer(feed.getCoordiId(), mongoCoordiRepository);

        List<Comment> comments = commentRepository.findAllByFeedId(feed);

        byte[] userProfileImage = getFeedThumbnailFromS3(bucket, feed.getUserId().getProfileImage());

        Optional<Likes> existingLike = likesRepository.findByFeedIdAndUserId(feedId, currentUser.getId());

        Boolean isLiked = existingLike.isPresent();

        FeedUserDTO feedUserDTO = FeedUserDTO.builder()
                .nickname(feed.getUserId().getNickname())
                .profileImage(userProfileImage)
                .isLiked(isLiked)
                .build();

        List<FeedCommentDTO> feedCommentDTOList = new ArrayList<>();

        for (Comment comment: comments) {
            User user = comment.getUserId();

            byte[] commenterProfileImage = getFeedThumbnailFromS3(bucket, user.getProfileImage());

            FeedCommentDTO commentInfo = FeedCommentDTO.builder()
                    .nickname(user.getNickname())
                    .profileImage(commenterProfileImage)
                    .content(comment.getContent())
                    .commentCreatedDate(comment.getCreatedDate())
                    .commentUpdatedDate(comment.getUpdatedDate())
                    .build();

            feedCommentDTOList.add(commentInfo);
        }

        FeedReadResponseDTO feedReadResponseDTO = FeedReadResponseDTO.builder()
                .id(feed.getId())
                .user(feedUserDTO)
                .feedTitle(feed.getFeedTitle())
                .feedContent(feed.getFeedContent())
                .feedLikes(feed.getFeedLikes())
                .originWriter(feed.getOriginWriter())
                .outerCloth(outerThumbnail)
                .dress(dressThumbnail)
                .upperBody(upperThumbnail)
                .lowerBody(lowerThumbnail)
                .coordiContainer(coordiContainer)
                .feedCreatedDate(feed.getFeedCreatedDate())
                .feedUpdatedDate(feed.getFeedUpdatedDate())
                .comments(feedCommentDTOList)
                .build();


        FeedReadResponse response = FeedReadResponse.builder()
                .message("success")
                .data(feedReadResponseDTO)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // feedId로 개별 조회 후 삭제
    public ResponseEntity<FeedDeleteResponse> delete(Long feedId) {

        // 현재 로그인한 유저 정보 받아오기
        String userid = JWTUtil.findEmailByToken();
        User user = userRepository.findByEmail(userid)
                .orElseThrow(() -> new EmailNotFoundException("사용자를 찾을 수 없습니다."));

        Long userId = user.getId();
        Optional<Feed> optionalFeed = feedRepository.findById(feedId);

        if (optionalFeed.isEmpty()) {
            throw new FeedNotFoundException("피드를 찾을 수 없습니다.");
        }
        Feed feed = optionalFeed.get();

        if (!userId.equals(feed.getUserId().getId())) {
            throw new UnauthorizedAccessException("피드를 삭제할 권한이 없습니다.");
        }

        String coordiId = feed.getCoordiId();
        Coordi coordi = mongoCoordiRepository.findById(coordiId).orElseThrow(() -> new RuntimeException("coordi not found"));
        mongoCoordiRepository.delete(coordi);

        feedRepository.delete(feed);

        FeedDeleteResponse response = FeedDeleteResponse.createFeedDeleteResponse(
                "success",
                true
        );

        return ResponseEntity.ok(response);
    }

    // 피드 좋아요
    @Override
    public ResponseEntity<?> feedLikes(Long feedId) {
        Optional<Feed> optionalFeed = feedRepository.findById(feedId);

        if (optionalFeed.isEmpty()) {
            throw new FeedNotFoundException("피드를 찾을 수 없습니다.");
        }

        String userid = JWTUtil.findEmailByToken();
        Optional<User> currentUser = userRepository.findByEmail(userid);
        Long userId = currentUser.get().getId();

        Optional<Feed> feedOptional = feedRepository.findById(feedId);

        if (feedOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Feed feed = feedOptional.get();

        // 해당 사용자가 해당 피드에 이미 좋아요를 눌렀는지 확인
        Optional<Likes> existingLike = likesRepository.findByFeedIdAndUserId(feedId, userId);

        if (existingLike.isPresent()) {
            // 이미 좋아요를 누른 상태라면 좋아요 취소
            likesRepository.delete(existingLike.get());
            feed.setFeedLikes(feed.getFeedLikes() - 1);
            feedRepository.save(feed);


            return ResponseEntity.ok("좋아요 취소를 눌렀습니다.");

        } else {
            // 좋아요를 누르지 않았다면 좋아요
            Likes like = Likes.createLikes(feed, currentUser.get());
            likesRepository.save(like);
            feed.setFeedLikes(feed.getFeedLikes() + 1);
            feedRepository.save(feed);
        }

        return ResponseEntity.ok("피드 좋아요를 눌렀습니다.");
    }


    @Override
    public ResponseEntity<FeedUpdateResponse> update(Long feedId, FeedUpdateRequest feedUpdateRequest) {

        // 현재 로그인한 유저 정보 받아오기
        String userid = JWTUtil.findEmailByToken();
        Optional<User> currentUser = userRepository.findByEmail(userid);

        if (currentUser.isEmpty()) {
            throw new EmailNotFoundException("사용자를 찾을 수 없습니다.");
        }

        Long userId = currentUser.get().getId();

        Optional<Feed> optionalFeed = feedRepository.findById(feedId);

        if (optionalFeed.isEmpty()) {
            throw new FeedNotFoundException("피드를 찾을 수 없습니다.");
        }
        Feed feed = optionalFeed.get();
        Long feedUserId = feed.getUserId().getId();

        if (!userId.equals(feedUserId)) {
            throw new UnauthorizedAccessException("피드를 수정할 수 없습니다.");
        }

        // 제목에 대한 입력값이 없을 경우
        if (feedUpdateRequest.getFeedTitle().isBlank()) {
            throw new RuntimeException("제목을 입력해주세요.");
        }

        LocalDate now = LocalDate.now(ZoneId.of("UTC"));

        // 피드 제목 및 내용 업데이트
        feed.updateFeedTitle(feedUpdateRequest.getFeedTitle());
        feed.updateFeedContent(feedUpdateRequest.getFeedContent());
        feed.updateFeedUpdatedDate(now);

        feedRepository.save(feed);

        FeedUpdateResponse response = FeedUpdateResponse.createFeedUpdateResponse(
                "success",
                FeedUpdateResponseDTO.createFeedUpdateResponseDTO(feed));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<FeedListReadResponse> readPopularList(Pageable pageable) {
        Page<Feed> feedList = feedRepository.findAllByOrderByFeedLikesDesc(pageable);

        if (feedList.isEmpty()) {
            throw new FeedNotFoundException("피드를 찾을 수 없습니다.");
        }

        List<FeedListReadResponseDTO> feedListReadResponseDTOList = new ArrayList<>();

        // 각 피드의 이미지를 가져와서 리스트에 추가

        for (Feed feed : feedList) {
            Coordi coordi = mongoCoordiRepository.findById(feed.getCoordiId())
                    .orElseThrow(() -> new CoordiNotFoundException("코디를 찾을 수 없습니다."));

            String outerImage = feed.getOuterCloth();
            byte[] outerThumbnail = getThumbnailOrNull(bucket, outerImage);

            String upperImage = feed.getUpperBody();
            byte[] upperThumbnail = getThumbnailOrNull(bucket, upperImage);

            String dressImage = feed.getDress();
            byte[] dressThumbnail = getThumbnailOrNull(bucket, dressImage);

            String lowerImage = feed.getLowerBody();
            byte[] lowerThumbnail = getThumbnailOrNull(bucket, lowerImage);

            CoordiContainer coordiContainer = createMongoContainer(feed.getCoordiId(), mongoCoordiRepository);

            User user = feed.getUserId();
            FeedListUserDTO feedListUserDTO = FeedListUserDTO.builder()
                    .nickname(user.getNickname())
                    .profileImage(getFeedThumbnailFromS3(bucket, user.getProfileImage()))
                    .likeCategories(Arrays.asList(user.getLikeCategories().split(",")))
                    .dislikeCategories(Arrays.asList(user.getDislikeCategories().split(",")))
                    .introduce(user.getIntroduce())
                    .instagram(user.getInstagram())
                    .youtube(user.getYoutube())
                    .build();

            feedListReadResponseDTOList.add(
                    FeedListReadResponseDTO.builder()
                            .user(feedListUserDTO)
                            .feedId(feed.getId())
                            .feedTitle(feed.getFeedTitle())
                            .feedLikes(feed.getFeedLikes())
                            .outerCloth(outerThumbnail)
                            .dress(dressThumbnail)
                            .upperBody(upperThumbnail)
                            .lowerBody(lowerThumbnail)
                            .coordiContainer(coordiContainer)
                            .build()
            );
        }

        // 응답 생성
        FeedListReadResponse response = FeedListReadResponse.builder()
                .message("Success")
                .data(feedListReadResponseDTOList)
                .totalPage(feedList.getTotalPages())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<FeedListReadResponse> searchByTitle(String title, Pageable pageable) {
        // 검색어가 없으면 빈 문자열로 설정, trim으로 검색어 공백 제거
        title = (title == null) ? "" : title.trim();

        Page<Feed> feedList;

        // 검색어가 없는 경우 모든 피드 반환
        if (title.isEmpty()) {
            feedList = feedRepository.findAll(pageable);
        } else {
            // 제목에 검색어를 포함하는 피드를 반환
            feedList = feedRepository.findByfeedTitleContaining(title, pageable);
        }

        List<FeedListReadResponseDTO> feedListReadResponseDTOList = new ArrayList<>();

        // 각 피드의 이미지를 가져와서 리스트에 추가
        for (Feed feed : feedList) {
            // 피드 썸네일 읽어오기

            Coordi coordi = mongoCoordiRepository.findById(feed.getCoordiId())
                    .orElseThrow(() -> new CoordiNotFoundException("코디를 찾을 수 없습니다."));

            String outerImage = feed.getOuterCloth();
            byte[] outerThumbnail = getThumbnailOrNull(bucket, outerImage);

            String upperImage = feed.getUpperBody();
            byte[] upperThumbnail = getThumbnailOrNull(bucket, upperImage);

            String dressImage = feed.getDress();
            byte[] dressThumbnail = getThumbnailOrNull(bucket, dressImage);

            String lowerImage = feed.getLowerBody();
            byte[] lowerThumbnail = getThumbnailOrNull(bucket, lowerImage);

            CoordiContainer coordiContainer = createMongoContainer(feed.getCoordiId(), mongoCoordiRepository);
            User user = feed.getUserId();
            FeedListUserDTO feedListUserDTO = FeedListUserDTO.builder()
                    .nickname(user.getNickname())
                    .profileImage(getFeedThumbnailFromS3(bucket, user.getProfileImage()))
                    .likeCategories(Arrays.asList(user.getLikeCategories().split(",")))
                    .dislikeCategories(Arrays.asList(user.getDislikeCategories().split(",")))
                    .introduce(user.getIntroduce())
                    .instagram(user.getInstagram())
                    .youtube(user.getYoutube())
                    .build();

            feedListReadResponseDTOList.add(
                    FeedListReadResponseDTO.builder()
                            .user(feedListUserDTO)
                            .feedId(feed.getId())
                            .feedTitle(feed.getFeedTitle())
                            .feedLikes(feed.getFeedLikes())
                            .outerCloth(outerThumbnail)
                            .dress(dressThumbnail)
                            .upperBody(upperThumbnail)
                            .lowerBody(lowerThumbnail)
                            .coordiContainer(coordiContainer)
                            .build()
            );
        }

        // 응답 생성
        FeedListReadResponse response = FeedListReadResponse.builder()
                .message("Success")
                .data(feedListReadResponseDTOList)
                .totalPage(feedList.getTotalPages())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 내가 쓴 피드 조회
    @Override
    public ResponseEntity<FeedListReadResponse> searchMyFeed(Optional<User> userId, Pageable pageable) {

        Page<Feed> feedList = feedRepository.findAllByuserId(userId, pageable);

        List<FeedListReadResponseDTO> feedListReadResponseDTOList = new ArrayList<>();

        // 각 피드의 이미지를 가져와서 리스트에 추가
        for (Feed feed : feedList) {
            // 피드 썸네일 읽어오기

            Coordi coordi = mongoCoordiRepository.findById(feed.getCoordiId())
                    .orElseThrow(() -> new CoordiNotFoundException("코디를 찾을 수 없습니다."));

            String outerImage = feed.getOuterCloth();
            byte[] outerThumbnail = getThumbnailOrNull(bucket, outerImage);

            String upperImage = feed.getUpperBody();
            byte[] upperThumbnail = getThumbnailOrNull(bucket, upperImage);

            String dressImage = feed.getDress();
            byte[] dressThumbnail = getThumbnailOrNull(bucket, dressImage);

            String lowerImage = feed.getLowerBody();
            byte[] lowerThumbnail = getThumbnailOrNull(bucket, lowerImage);

            CoordiContainer coordiContainer = createMongoContainer(feed.getCoordiId(), mongoCoordiRepository);

            User user = feed.getUserId();
            FeedListUserDTO feedListUserDTO = FeedListUserDTO.builder()
                    .nickname(user.getNickname())
                    .profileImage(getFeedThumbnailFromS3(bucket, user.getProfileImage()))
                    .likeCategories(Arrays.asList(user.getLikeCategories().split(",")))
                    .dislikeCategories(Arrays.asList(user.getDislikeCategories().split(",")))
                    .introduce(user.getIntroduce())
                    .instagram(user.getInstagram())
                    .youtube(user.getYoutube())
                    .build();

            feedListReadResponseDTOList.add(
                    FeedListReadResponseDTO.builder()
                            .user(feedListUserDTO)
                            .feedId(feed.getId())
                            .feedTitle(feed.getFeedTitle())
                            .feedLikes(feed.getFeedLikes())
                            .outerCloth(outerThumbnail)
                            .dress(dressThumbnail)
                            .upperBody(upperThumbnail)
                            .lowerBody(lowerThumbnail)
                            .coordiContainer(coordiContainer)
                            .build()
            );
        }

        // 응답 생성
        FeedListReadResponse response = FeedListReadResponse.builder()
                .message("Success")
                .data(feedListReadResponseDTOList)
                .totalPage(feedList.getTotalPages())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<FeedCoordiResponseDTO> fitting(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedNotFoundException("피드를 찾을 수 없습니다."));

        Coordi coordi = mongoCoordiRepository.findById(feed.getCoordiId())
                .orElseThrow(() -> new CoordiNotFoundException("코디를 찾을 수 없습니다."));

        byte[] outerThumbnail = getThumbnailOrNull(bucket, feed.getOuterCloth());
        byte[] upperThumbnail = getThumbnailOrNull(bucket, feed.getUpperBody());
        byte[] dressThumbnail = getThumbnailOrNull(bucket, feed.getDress());
        byte[] lowerThumbnail = getThumbnailOrNull(bucket, feed.getLowerBody());

        CoordiContainer coordiContainer = createMongoContainer(feed.getCoordiId(), mongoCoordiRepository);

        ClothResponseDTO outerCloth = ClothResponseDTO.builder()
                .image(outerThumbnail)
                .imageUrl(feed.getOuterCloth())
                .style(coordi.getOuterCloth().getStyle())
                .category(coordi.getOuterCloth().getCategory())
                .color(coordi.getOuterCloth().getColor())
                .part("outerCloth")
                .build();

        ClothResponseDTO upperBody = ClothResponseDTO.builder()
                .image(upperThumbnail)
                .imageUrl(feed.getUpperBody())
                .style(coordi.getUpperBody().getStyle())
                .category(coordi.getUpperBody().getCategory())
                .color(coordi.getUpperBody().getColor())
                .part("upperBody")
                .build();

        ClothResponseDTO lowerBody = ClothResponseDTO.builder()
                .image(lowerThumbnail)
                .imageUrl(feed.getLowerBody())
                .style(coordi.getLowerBody().getStyle())
                .category(coordi.getLowerBody().getCategory())
                .color(coordi.getLowerBody().getColor())
                .part("lowerBody")
                .build();

        ClothResponseDTO dress = ClothResponseDTO.builder()
                .image(dressThumbnail)
                .imageUrl(feed.getDress())
                .style(coordi.getDress().getStyle())
                .category(coordi.getDress().getCategory())
                .color(coordi.getDress().getColor())
                .part("dress")
                .build();

        // 새로운 피드 생성
        FeedCoordiResponseDTO feedCoordiResponseDTO = FeedCoordiResponseDTO.builder()
                .outerCloth(outerCloth)
                .upperBody(upperBody)
                .lowerBody(lowerBody)
                .dress(dress)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(feedCoordiResponseDTO);
    }

    public byte[] getFeedThumbnailFromS3(String bucket, String storeFilePath) throws FeedImageIOException {
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, storeFilePath);
            S3Object s3Object = amazonS3Client.getObject(getObjectRequest);
            S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
            return IOUtils.toByteArray(s3ObjectInputStream);
        } catch (IOException exception) {
            throw new FeedImageIOException("피드 썸네일을 불러오지 못했습니다.");
        } catch (AmazonS3Exception exception) {
            log.info(storeFilePath);
            throw new FeedImageIOException("저장된 피드 썸네일이 없습니다.");
        } catch (Exception exception) {
            throw new FeedImageIOException("피드 썸네일을 불러오는 중 오류가 발생했습니다.");
        }
    }

    public byte[] getThumbnailOrNull(String bucket, String imageUrl) {
        if (imageUrl != null && !imageUrl.equals("null")) {
            return getFeedThumbnailFromS3(bucket, imageUrl);
        }
        return null;
    }
}