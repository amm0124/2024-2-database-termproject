package database.termproject.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import database.termproject.domain.member.entity.Member;
import database.termproject.global.security.UserDetailsImpl;
import database.termproject.global.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Description("인증 시도 메서드")
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        log.info("email - password 기반으로 인증을 시도 합니다 : CustomUsernamePasswordAuthenticationFilter");
        log.info("URL : " + request.getRequestURI() );

        ObjectMapper loginRequestMapper = new ObjectMapper();
        String email = null;
        String password = null;

        try {
            BufferedReader reader = request.getReader();
            Map<String, String> credentials = loginRequestMapper.readValue(reader, Map.class);
            email = credentials.get("email");
            password = credentials.get("password");
            log.info("유저 정보를 출력합니다. email : "+ email + "password : " + password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
        log.info("CustomUsernamePasswordAuthenticationFilter : authentication token 생성 완료");
        return this.authenticationManager.authenticate(authToken);


    }

    @Override
    @Description("로그인 성공 시, accessToken과 refreshToken 발급")
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        log.info("로그인 성공 -> 토큰 발급");
        UserDetailsImpl customUserDetails = (UserDetailsImpl) authentication.getPrincipal();
        Member member = customUserDetails.getMember();

        String accessToken = jwtUtil.generateAccessToken(member);

        log.info("usename password 기반 로그인 성공. access token 발급.");
        response.setHeader("access", accessToken);
        response.setStatus(HttpStatus.OK.value());

        response.setContentType("application/json");  // Content-Type 설정

        PrintWriter out = response.getWriter();
        out.println("access : " + accessToken);
        out.close();
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.info("usename password 기반 로그인 실패. ");
        response.setStatus(401);
    }
}
