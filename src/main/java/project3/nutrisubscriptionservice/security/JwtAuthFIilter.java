package project3.nutrisubscriptionservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthFIilter extends OncePerRequestFilter {
    @Autowired
    TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getBearerToken(request);

        try{
            if(token != null && ! token.equalsIgnoreCase("null")) { //token.equalsIgnoreCase("null") null값인지 아닌지 구분해냄
                String userId = tokenProvider.validateAndGetUserId(token);

                // 1. 사용자 정보를 담는 공간? 토큰 생성
                Authentication authentication = new UsernamePasswordAuthenticationToken(String.valueOf(userId), null, AuthorityUtils.NO_AUTHORITIES);

                // 2. SecurityContextHolder 에 authentication 정보 set
                // SecurityContextHolder : 클라이언트의 요청 -> 응답 사이에 일시적으로 auth 정보를 저장할 수 있는 공간
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception e){
            log.error("auth error ", e.getMessage());
        }

        //무조건 실행 시키도록
        filterChain.doFilter(request,response);
    }

    //토큰을 헤더에서 가져오는 작업 + TokenProvider 에 있는 메소드 사용해서 payload 값(userId) 추출
    //포스트맨에서 토큰확인시
    public String getBearerToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        //"Baerer sldkfjdngdfns,mgndf"

        //StringUtils.hasText(param) : param 이 null인지 아닌지, 길이가 0보다 큰지
        //startsWith("~"): ~로 시작하는 걸로 시작하는지 검증하기위해
        if(StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
            //substring(숫자) : 앞에 몇글자를 떼어낼것인가
            //즉 토큰값만 가져오게

        }

        return null;
    }
}