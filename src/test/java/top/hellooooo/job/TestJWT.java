package top.hellooooo.job;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.crypto.SecretKey;
import java.util.HashMap;

/**
 * @Author Q
 * @Date 2021-01-13 17:57
 * @Description
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TestJWT {
    @Test
    public void generateJWT(){
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", "Liangzai");
        hashMap.put("value", "123");
        String jwts = Jwts
                .builder()
                .setClaims(hashMap)
                .setSubject("Joe")
                .signWith(secretKey)
                .compact();

        System.out.println(jwts);

        Claims body = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwts)
                .getBody();
        System.out.println(body.get("name"));
        System.out.println(body.get("value"));

        System.out.println(
                Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(jwts)
                        .getBody()
                        .getSubject()
                        .equals("Joe")
        );
    }
}
