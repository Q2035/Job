package top.hellooooo.job.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Q
 * @Date 2021-01-12 08:58
 * @Description
 */
@Log4j2
public class JwtUtils {

    // secret key
    private static SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 有效期 半小时
    private static Long EXPIRE_TIME = 1000 * 30 * 60L;

    /**
     * 生成JWT，将username放入claim
     *
     * @return
     */
    public static String generateJWT(Map<String, String> claimsMap) {
        return Jwts.builder()
                .setClaims(claimsMap)
                .setIssuedAt(new Date())
                // 如果在setClaims之前无效
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .setSubject("auth")
                .signWith(secretKey)
                .compact();
    }


    public static Object getClaim(String jwt, String key) {
        if (StringUtils.isEmpty(jwt)) {
            return "";
        }
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            return claims.get(key);
        } catch (SignatureException e) {
            log.info("JWT Parse Error. key:" + key);
            return "";
        } catch (ExpiredJwtException e) {
            log.info("JWT Expired. key:" + key);
            return "";
        }

    }

    public static boolean validateJwt(String jwt) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwt);
            Date expiration = claimsJws.getBody()
                    .getExpiration();
            // 已过期
            if (expiration.getTime() < System.currentTimeMillis()) {
                return false;
            }
            //OK, we can trust this JWT
            return true;
        } catch (Exception e) {
            //don't trust the JWT!
            return false;
        }
    }
}