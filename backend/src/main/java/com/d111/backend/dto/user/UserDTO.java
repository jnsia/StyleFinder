package com.d111.backend.dto.user;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserDTO extends User {

    public UserDTO(String email, String password, List<String> authorities) {
        super(
                email, password, authorities.stream().map(str -> new SimpleGrantedAuthority(str)).collect(Collectors.toList())
        );
    }

}
