package com.d111.backend.controller.user;

import com.d111.backend.dto.user.request.SignInRequestDTO;
import com.d111.backend.dto.user.request.SignUpRequestDTO;
import com.d111.backend.dto.user.request.TokenReissueRequestDTO;
import com.d111.backend.dto.user.request.UpdateUserInfoRequestDTO;
import com.d111.backend.dto.user.response.*;
import com.d111.backend.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "User API")
@RequestMapping("/api/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공")
    })
    @PostMapping(value = "/signUp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> signUp(@RequestPart(value = "signUpRequest") SignUpRequestDTO signUpRequestDTO,
                          @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        return userService.signUp(signUpRequestDTO, profileImage);
    }

    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = SignInResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "가입되지 않은 회원", content = @Content)
    })
    @PostMapping(value = "/signIn")
    ResponseEntity<SignInResponseDTO> signIn(@RequestBody SignInRequestDTO signInRequest) {
        return userService.signIn(signInRequest);
    }

    @Operation(summary = "액세스 토큰 재발급", description = "리프레시 토큰을 이용하여 액세스 토큰을 재발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재발급 성공", content = @Content(schema = @Schema(implementation = TokenReissueResponseDTO.class)))
    })
    @PostMapping(value = "/token")
    ResponseEntity<TokenReissueResponseDTO> tokenReissue(@RequestBody TokenReissueRequestDTO tokenReissueRequest) {
        return userService.tokenReissue(tokenReissueRequest);
    }

    @Operation(summary = "유저 정보 수정", description = "유저 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "유저 정보 수정 성공", content = @Content(schema = @Schema(implementation = UpdateUserInfoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "입력값이 적절하지 않음"),
            @ApiResponse(responseCode = "404", description = "유저 정보 없음"),
            @ApiResponse(responseCode = "500", description = "프로필 이미지 업로드 실패")
    })
    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> updateUserInfo(@RequestPart(value = "updateUserInfoRequest") UpdateUserInfoRequestDTO updateUserInfoRequestDTO,
                          @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        return userService.updateUserInfo(updateUserInfoRequestDTO, profileImage);
    }

    @Operation(summary = "유저 정보 삭제", description = "유저 정보를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "203", description = "유저 정보 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "삭제하려는 유저와 토큰을 발급한 유저가 일치하지 않음")
    })
    @DeleteMapping(value = "/remove")
    ResponseEntity<String> removeUserInfo() {
        return userService.removeUserInfo();
    }


    // 유저 조회
    @Operation(summary = "유저 정보 조회", description = "개별 유저를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재발급 성공", content = @Content(schema = @Schema(implementation = GetUserResponse.class)))
    })
    @GetMapping("/profile")
    public ResponseEntity<GetUserResponse> userProfile() {
        return userService.getUser();
    }

    @Operation(summary = "유저 취향 분석", description = "유저의 취향을 분석합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "분석 성공", content = @Content(schema = @Schema(implementation = AnalysisFavorResponseDTO.class)))
    })
    @GetMapping("/favor")
    public ResponseEntity<AnalysisFavorResponseDTO> analysisFavor() {
        return userService.analysisFavor();
    }

}
