//package com.example.ttcn2etest.service.auth;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.Base64;
//
//@Component
//@Slf4j
//public class JwtTokenProvider {
//    @Value("${jwt.jwtSecret}")
//    private String jwtSecret;
//    @Value("${jwt.jwtExpirationMs}")
//    private Long jwtExpirationMs;
//    @Value("${jwt.jwtRefreshExpirationMs}")
//    private Long refreshTokenDurationsMs;
//    private static final String AUTHORITIES_KEY = "XAUTHOR";
//
//    @PostConstruct
//    public void init(){
//        jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
//        log.info("jwt secret: {}",jwtSecret);
//    }
//
////    public String generateTokenWithAuthorities()
//
//
//}
