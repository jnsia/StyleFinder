package com.d111.backend.serviceImpl.user;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.d111.backend.dto.user.request.SignInRequestDTO;
import com.d111.backend.dto.user.request.SignUpRequestDTO;
import com.d111.backend.dto.user.request.TokenReissueRequestDTO;
import com.d111.backend.dto.user.request.UpdateUserInfoRequestDTO;
import com.d111.backend.dto.user.response.*;
import com.d111.backend.entity.closet.Closet;
import com.d111.backend.entity.coordi.ClothCategory;
import com.d111.backend.entity.coordi.ClothInfo;
import com.d111.backend.entity.coordi.Coordi;
import com.d111.backend.entity.feed.Feed;
import com.d111.backend.entity.multipart.S3File;
import com.d111.backend.entity.user.RefreshToken;
import com.d111.backend.entity.user.User;
import com.d111.backend.exception.feed.FeedImageIOException;
import com.d111.backend.exception.user.*;
import com.d111.backend.repository.feed.FeedRepository;
import com.d111.backend.repository.mongo.MongoCoordiRepository;
import com.d111.backend.repository.s3.S3Repository;
import com.d111.backend.repository.user.RefreshTokenRepository;
import com.d111.backend.repository.user.UserRepository;
import com.d111.backend.service.user.UserService;
import com.d111.backend.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final S3Repository s3Repository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AmazonS3Client amazonS3Client;
    private final MongoCoordiRepository mongoCoordiRepository;
    private final FeedRepository feedRepository;

    @Value("${DEFAULT_PROFILE_URL}")
    private String DEFAULT_PROFILE_URL;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket; // 버킷 이름

    private int accessTokenMinute = 60;
    private int refreshTokenMinute = 300;

    @Override
    @Transactional
    public ResponseEntity<String> signUp(SignUpRequestDTO signUpRequestDTO, MultipartFile profileImage) {
        Optional<User> user = userRepository.findByEmail(signUpRequestDTO.getEmail());

        user.ifPresent(findUser -> { throw new ExistedEmailException("이미 가입한 이메일입니다."); });

        // S3 bucket에 프로필 이미지 저장
        String storeFilePath;

        if (profileImage == null || profileImage.isEmpty()) {
            storeFilePath = DEFAULT_PROFILE_URL;
        } else {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentEncoding(profileImage.getContentType());
            objectMetadata.setContentLength(profileImage.getSize());

            String originalFileFullName = profileImage.getOriginalFilename();
            String originalFileName = originalFileFullName.substring(originalFileFullName.lastIndexOf(".") + 1);

            String storeFileName = UUID.randomUUID() + "." + originalFileName;
            storeFilePath = "PROFILE/" + storeFileName;

            try {
                PutObjectRequest putObjectRequest = new PutObjectRequest(
                        bucket, storeFilePath, profileImage.getInputStream(), objectMetadata
                );

                amazonS3Client.putObject(putObjectRequest);
            } catch (IOException e) {
                throw new ProfileImageIOException("프로필 이미지 저장에 실패하였습니다.");
            }

            S3File s3File = new S3File(originalFileFullName, storeFileName, storeFilePath);
            s3Repository.upload(s3File);
        }

        // List<String> -> String
        String likeCategories = signUpRequestDTO.getLikeCategories().stream()
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(","));
        String dislikeCategories = signUpRequestDTO.getDislikeCategories().stream()
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(","));

        User newUser = User.builder()
                .email(signUpRequestDTO.getEmail())
                .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
                .nickname(signUpRequestDTO.getNickname())
                .likeCategories(likeCategories)
                .dislikeCategories(dislikeCategories)
                .height(signUpRequestDTO.getHeight())
                .weight(signUpRequestDTO.getWeight())
                .profileImage(storeFilePath)
                .build();

        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    @Override
    public ResponseEntity<SignInResponseDTO> signIn(SignInRequestDTO signInRequestDTO) {
        // 이메일 유무 판단
        User user = userRepository.findByEmail(signInRequestDTO.getEmail())
                .orElseThrow(() -> new EmailNotFoundException("일치하는 이메일이 없습니다."));

        // 비밀번호 일치 여부 판단
        if (!passwordEncoder.matches(signInRequestDTO.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성
        String userEmail = signInRequestDTO.getEmail();

        String accessToken = JWTUtil.createToken(userEmail, accessTokenMinute);
        String refreshToken = JWTUtil.createToken(userEmail, refreshTokenMinute);

        refreshTokenRepository.save(RefreshToken.builder()
                        .email(userEmail)
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build());

        // 로그인 응답 정보 생성
        SignInResponseDTO signInResponseDTO = SignInResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(signInResponseDTO);
    }

    @Override
    public ResponseEntity<TokenReissueResponseDTO> tokenReissue(TokenReissueRequestDTO tokenReissueRequestDTO) {
        RefreshToken refreshToken =
                refreshTokenRepository.findById(tokenReissueRequestDTO.getRefreshToken())
                        .orElseThrow(() -> new RefreshTokenNotFoundException("다시 로그인이 필요합니다."));

        String accessToken = JWTUtil.createToken(refreshToken.getEmail(), accessTokenMinute);

        TokenReissueResponseDTO tokenReissueResponseDTO = TokenReissueResponseDTO.builder()
                .accessToken(accessToken)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(tokenReissueResponseDTO);
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateUserInfo(UpdateUserInfoRequestDTO updateUserInfoRequestDTO,
                                                                    MultipartFile profileImage) {
        String email = JWTUtil.findEmailByToken();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("유저 정보가 존재하지 않습니다."));

        // 프로필 이미지 변경 요청이 있을 경우
        if (profileImage != null && !profileImage.isEmpty()) {
            String storeFilePath;

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentEncoding(profileImage.getContentType());
            objectMetadata.setContentLength(profileImage.getSize());

            String originalFileFullName = profileImage.getOriginalFilename();
            String originalFileName = originalFileFullName.substring(originalFileFullName.lastIndexOf(".") + 1);

            String storeFileName = UUID.randomUUID() + "." + originalFileName;
            storeFilePath = "PROFILE/" + storeFileName;

            try {
                PutObjectRequest putObjectRequest = new PutObjectRequest(
                        bucket, storeFilePath, profileImage.getInputStream(), objectMetadata
                );

                amazonS3Client.putObject(putObjectRequest);

                user.updateProfileImage(storeFilePath);
            } catch (IOException e) {
                throw new ProfileImageIOException("프로필 이미지 저장에 실패하였습니다.");
            }

            S3File s3File = new S3File(originalFileFullName, storeFileName, storeFilePath);
            s3Repository.upload(s3File);
        }

        // 닉네임에 대한 입력값이 없을 경우
        if (updateUserInfoRequestDTO.getNickname().isBlank()) {
            throw new InvalidInputException("닉네임을 입력해주세요.");
        }

        user.updateNickname(updateUserInfoRequestDTO.getNickname());
        
        // List<String> -> String
        String likeCategories = updateUserInfoRequestDTO.getLikeCategories().stream()
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(","));
        String dislikeCategories = updateUserInfoRequestDTO.getDislikeCategories().stream()
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(","));


        user.updateLikeCategories(likeCategories);
        user.updateDislikeCategories(dislikeCategories);

        user.updateHeight(updateUserInfoRequestDTO.getHeight());
        user.updateWeight(updateUserInfoRequestDTO.getWeight());
        user.updateIntroduce(updateUserInfoRequestDTO.getIntroduce());
        user.updateInstagram(updateUserInfoRequestDTO.getInstagram());
        user.updateYoutube(updateUserInfoRequestDTO.getYoutube());

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("유저 정보가 수정되었습니다.");
    }

    @Override
    public ResponseEntity<String> removeUserInfo() {
        String email = JWTUtil.findEmailByToken();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomJWTException("삭제하려는 유저와 토큰을 발급한 유저가 일치하지 않습니다."));

        userRepository.delete(user);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("유저 정보가 삭제되었습니다.");
    }


    @Override
    @Transactional
    public ResponseEntity<GetUserResponse> getUser() {


        String userid = JWTUtil.findEmailByToken();
        User user = userRepository.findByEmail(userid)
                .orElseThrow(() -> new EmailNotFoundException("사용자를 찾을 수 없습니다."));

        String storeFilePath = user.getProfileImage();
        byte[] userProfileImage = getUserProfileImageFromS3(bucket, storeFilePath);

        List<String> likeCategories =
                (user.getLikeCategories().isEmpty() ?
                        new ArrayList() :
                        Arrays.asList(user.getLikeCategories().split(",")));
        List<String> dislikeCategories =
                (user.getDislikeCategories().isEmpty() ?
                        new ArrayList() :
                        Arrays.asList(user.getDislikeCategories().split(",")));

        GetUserResponseDTO getUserResponseDTO = GetUserResponseDTO.builder()
                .nickname(user.getNickname())
                .likeCategories(likeCategories)
                .dislikeCategories(dislikeCategories)
                .height(user.getHeight())
                .weight(user.getWeight())
                .profileImage(userProfileImage)
                .introduce(user.getIntroduce())
                .instagram(user.getInstagram())
                .youtube(user.getYoutube())
                .build();

        log.info(getUserResponseDTO.getLikeCategories());

        GetUserResponse response = GetUserResponse.creategetUserResponse(
                "Success",
                getUserResponseDTO
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<AnalysisFavorResponseDTO> analysisFavor() {
        String email = JWTUtil.findEmailByToken();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("유저 정보가 존재하지 않습니다."));

        Map<String, Integer> likeCategories = new HashMap<>();
        Map<String, Integer> feedCategories = new HashMap<>();

        List<String> coordiIdList = feedRepository.findCoordiIdsByUserId(user).stream()
                .map(Feed::getCoordiId)
                .collect(Collectors.toList());

        for (String coordiId : coordiIdList) {
            Coordi coordi = mongoCoordiRepository.findBy_id(coordiId);

            for (ClothInfo clothInfo : Arrays.asList(coordi.getOuterCloth(), coordi.getUpperBody(), coordi.getLowerBody(), coordi.getDress())) {
                if (clothInfo != null && clothInfo.getCategory() != null) {
                    // isValidCategory 메서드를 통해 Enum에 등록된 카테고리만 분석에 포함
                    if (isValidCategory(clothInfo.getCategory())) {
                        feedCategories.put(clothInfo.getCategory(), feedCategories.getOrDefault(clothInfo.getCategory(), 0) + 1);
                    }
                }
            }
        }

        for (String category : user.getLikeCategories().split(",")) {
            likeCategories.put(category, likeCategories.getOrDefault(category, 0) + 1);
        }

        Map<String, Integer> closetCategories = new HashMap<>();

        for (Closet closet : user.getClosets()) {
            String categories = closet.getCategories();

            for (String category : categories.split(",")) {
                closetCategories.put(category, closetCategories.getOrDefault(category, 0) + 1);
            }
        }

        Map<String, Integer> feedStyles = new HashMap<>();

        AnalysisFavorResponseDTO analysisFavorResponseDTO = AnalysisFavorResponseDTO.builder()
                .likeCategories(likeCategories)
                .closetCategories(closetCategories)
                .feedStyles(feedStyles)
                .feedCategories(feedCategories)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(analysisFavorResponseDTO);
    }



    public byte[] getUserProfileImageFromS3(String bucket, String storeFilePath) throws FeedImageIOException {
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, storeFilePath);
            S3Object s3Object = amazonS3Client.getObject(getObjectRequest);
            S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
            return IOUtils.toByteArray(s3ObjectInputStream);
        } catch (IOException exception) {
            throw new ProfileImageIOException("유저 프로필을 불러오지 못했습니다.");
        } catch (AmazonS3Exception exception) {
            throw new ProfileImageIOException("저장된 유저 프로필이 없습니다.");
        }
    }

    private List<String> findcoordiIdsByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        System.out.println(user + "유저");
        if (user.isPresent()) {
            List<Feed> feeds = feedRepository.findAllByuserId(user);
            return feeds.stream()
                    .map(Feed::getCoordiId)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList(); // 유저가 존재하지 않는 경우 빈 리스트 반환
        }
    }
    private boolean isValidCategory(String category) {
        for (ClothCategory clothCategory : ClothCategory.values()) {
            if (clothCategory.name().equals(category)) {
                return true;
            }
        }
        return false;
    }
}

