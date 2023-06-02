package com.website.courses.config;
import com.website.courses.exceptions.NotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ServletException e) {
            switch (e.getCause().getClass().getSimpleName())
            {
                case "NullPointerException" -> response.setStatus(HttpStatus.BAD_REQUEST.value());
                case "NotFoundException"-> response.setStatus(HttpStatus.NOT_FOUND.value());
                default -> response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
            response.getWriter().write(e.getMessage());
        }
    }

}