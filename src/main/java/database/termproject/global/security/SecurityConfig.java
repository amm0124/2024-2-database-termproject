package database.termproject.global.security;

import database.termproject.domain.member.service.MemberService;
import database.termproject.global.security.filter.CustomUsernamePasswordAuthenticationFilter;
import database.termproject.global.security.filter.JwtAuthenticationFilter;
import database.termproject.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = false) // 인가 처리에 대한 annotation
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled",havingValue = "true")
    public WebSecurityCustomizer webSecurityCustomizer() {
  /*      return web -> web.ignoring()
                .requestMatchers("/error","/favicon.ico" );*/

        return web -> web.ignoring()
                .requestMatchers("/error", "/favicon.ico");
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    @Description("AuthenticationProvider를 위한 Spring bean")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Description("순서 : jwt -> login -> logout")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //기본 로그인 해제
        http.formLogin(AbstractHttpConfigurer::disable);

        //기본 베이직 로그인 해제
        http.httpBasic(AbstractHttpConfigurer::disable);

        //csrf 해제
        //http.csrf(AbstractHttpConfigurer::disable);
        http.csrf(csrf -> csrf.disable());

        http.headers(frameOptions -> frameOptions.disable());

        //cors 해제
        http.cors(AbstractHttpConfigurer::disable);

        //기본 로그아웃 해제
        http.logout(AbstractHttpConfigurer::disable);

        //세션 stateless
        http.sessionManagement(AbstractHttpConfigurer::disable);


        // 인증 필요한 경로
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(
                        "/api/v1/member/signup",
                        "api/login",
                        "/h2-console/**").permitAll() //.hasRole("ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/posting/**", "/api/v1/comment/**").authenticated()
                .anyRequest().permitAll()
        );


        CustomUsernamePasswordAuthenticationFilter customUsernameFilter =
                new CustomUsernamePasswordAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtUtil);
        customUsernameFilter.setFilterProcessesUrl("/api/login");
        http.addFilterAt(customUsernameFilter, UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(jwtAuthenticationFilter, CustomUsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
