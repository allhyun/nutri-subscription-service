package project3.nutrisubscriptionservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project3.nutrisubscriptionservice.security.CustomAuthFilter;

@Configuration
@EnableWebSecurity
public class NutriWebSecurityConfig  {
    @Autowired
    CustomAuthFilter customAuthFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder();}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .cors(Customizer.withDefaults())
                .csrf(CsrfConfigurer::disable)
                .formLogin( form -> form  // 로그인 페이지 지정
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/",true)
                        .permitAll()
                ) // 폼 로그인 사용
                 // 로그인 페이지는 모든 사용자에게 허용
                .logout( auth -> auth
                        .logoutUrl("/user/logout")
                        .logoutSuccessHandler((((request, response, authentication) -> {
                            response.setStatus(200);
                        }))))
                .authorizeHttpRequests(authorize -> authorize
                     .requestMatchers("/user/**").permitAll()
                     .anyRequest().authenticated()//위에 나온 주소 말고 , 나머지 주소는 로그인이 필요하다.

        );
        http.addFilterAfter(customAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
