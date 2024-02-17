package project3.nutrisubscriptionservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthFilter extends OncePerRequestFilter {
    @Override
    protected void  doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException{
        try{
            HttpSession session = request.getSession();
            log.warn("session id {}", session.getId());
            Object userId = session.getAttribute("UserId");
            if(userId != null) {

                Authentication authentication = new UsernamePasswordAuthenticationToken(String.valueOf(userId),null, AuthorityUtils.NO_AUTHORITIES);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch(Exception e){

            log.error("filter error {}", e.getMessage());
        }

        filterChain.doFilter(request,response);
    }
}