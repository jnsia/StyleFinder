package com.d111.backend.security;

import com.d111.backend.dto.user.UserDTO;
import com.d111.backend.entity.user.User;
import com.d111.backend.exception.user.EmailNotFoundException;
import com.d111.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("loadUserByUsername: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("가입되지 않은 이메일입니다."));

        List<String> authorities = Collections.singletonList("USER");

        return new UserDTO(user.getEmail(), user.getPassword(), authorities);
    }
}
