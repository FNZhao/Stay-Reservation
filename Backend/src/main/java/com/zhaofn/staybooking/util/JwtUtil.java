package com.zhaofn.staybooking.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

@Component//写成@Service也可以，但是一般service是需要连db需要发network request的
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String subject) {//subject就是username
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 864000000))//单位是ms，10天后过期
                .signWith(SignatureAlgorithm.HS256, secret)//secret长度最大256个bit
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    public Boolean validateToken(String token) {
        return extractExpiration(token).after(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

}
