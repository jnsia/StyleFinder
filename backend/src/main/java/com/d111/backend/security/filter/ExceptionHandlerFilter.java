package com.d111.backend.security.filter;

import com.d111.backend.exception.user.CustomJWTException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomJWTException exception) {
            setErrorResponse(response, exception);
        }
    }

    public void setErrorResponse(HttpServletResponse response,
                                 Throwable throwable) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json; charset=UTF-8");

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("message", throwable.getMessage());

        objectMapper.writeValue(response.getWriter(), responseData);
    }

}
