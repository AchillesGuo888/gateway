package com.yuanzhi.gateway.utils;



import com.yuanzhi.gateway.constant.UserTypeEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private final static String SECRET_KEY = "2024_CS5721"; // private key
  private final Map<String, Date> tokenBlacklist = new ConcurrentHashMap<>(); // black_list

  private final static int USER_EXPIRE = 600000;
  private final static int ADMINISTRATOR_EXPIRE = 3600000;

  // Create JWT Token
  public static String generateToken(String userId, byte userType, String jti) {
    int expireTime = 0;
    if (UserTypeEnum.USER.getCode().equals(userType)) {
      expireTime = USER_EXPIRE;
    } else {
      expireTime = ADMINISTRATOR_EXPIRE;
    }
    return Jwts.builder()
        .setSubject(userId)
        .setId(jti)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expireTime))
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
        .compact();
  }

  // Validate JWT Token
  public Claims validateToken(String token) {
    if (isTokenBlacklisted(token)) {
      throw new JwtException("Token is blacklisted");
    }
    try {
      return Jwts.parser()
          .setSigningKey(SECRET_KEY)
          .parseClaimsJws(token)
          .getBody();
    } catch (ExpiredJwtException e) {
      throw new JwtException("Token has expired");

    } catch (SignatureException e) {
      throw new JwtException("Invalid JWT signature.");

    } catch (Exception e) {
      throw new JwtException("JWT parsing error.");

    }

  }

  // put Token into blacklist
  public void blacklistToken(String token) {
    Date expiration = validateToken(token).getExpiration();
    tokenBlacklist.put(token, expiration);
  }

  // check Token in blacklist
  public boolean isTokenBlacklisted(String token) {
    return tokenBlacklist.containsKey(token) && tokenBlacklist.get(token).after(new Date());
  }

  // Periodically clear expired blacklist tokens
  @Scheduled(fixedRate = 3600000)
  public void cleanupBlacklist() {
    tokenBlacklist.entrySet().removeIf(entry -> entry.getValue().before(new Date()));
  }

  public String getUserIdFromToken(String token) {
    try {
      if (StringUtils.isBlank(token) && token.startsWith("Bearer ")) {
        return StringUtils.EMPTY;
      }
      token = token.substring(7);
      Claims claims = validateToken(token);
      return claims.getSubject(); // get subject from token(userId)
    } catch (SignatureException e) {

      return StringUtils.EMPTY;
    } catch (Exception e) {

      return StringUtils.EMPTY;
    }
  }
}

