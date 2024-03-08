package com.example.todo.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

public class SecurityUtils {
    public static String getTokenFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
