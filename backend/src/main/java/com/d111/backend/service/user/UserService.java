package com.d111.backend.service.user;

import com.d111.backend.dto.user.request.SignInRequestDTO;
import com.d111.backend.dto.user.request.SignUpRequestDTO;
import com.d111.backend.dto.user.request.TokenReissueRequestDTO;
import com.d111.backend.dto.user.request.UpdateUserInfoRequestDTO;
import com.d111.backend.dto.user.response.AnalysisFavorResponseDTO;
import com.d111.backend.dto.user.response.GetUserResponse;
import com.d111.backend.dto.user.response.SignInResponseDTO;
import com.d111.backend.dto.user.response.TokenReissueResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    ResponseEntity<String> signUp(SignUpRequestDTO signUpRequestDTO, MultipartFile profileImage);

    ResponseEntity<SignInResponseDTO> signIn(SignInRequestDTO signInRequestDTO);

    ResponseEntity<TokenReissueResponseDTO> tokenReissue(TokenReissueRequestDTO tokenReissueRequestDTO);

    ResponseEntity<String> updateUserInfo(UpdateUserInfoRequestDTO updateUserInfoRequestDTO, MultipartFile profileImage);

    ResponseEntity<String> removeUserInfo();

    ResponseEntity<GetUserResponse> getUser();

    ResponseEntity<AnalysisFavorResponseDTO> analysisFavor();

}
