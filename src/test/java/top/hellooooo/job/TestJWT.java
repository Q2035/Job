package top.hellooooo.job;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.hellooooo.job.util.JwtUtils;

import javax.crypto.SecretKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    @Test
    public void testJWTUtils(){
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("username", "zhangsan");
        String x1 = JwtUtils.generateJWT(stringStringHashMap);
        System.out.println(x1);
        System.out.println(JwtUtils.validateJwt(x1));
    }

    @Test
    public void testJwt(){
        String secret = "a1g2y47dg3dj59fjhhsd7cnewy73ja1g2y47dg3dj59fjhhsd7cnewy73ja1g2y47dg3dj59fjhhsd7cnewy73ja1g2y47dg3dj59fjhhsd7cnewy73ja1g2y47dg3dj59fjhhsd7cnewy73j";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("username", "zss");
        claims.put("age", 18);

        //生成token
        String token = Jwts.builder()
                .setClaims(claims)
                .setId("666")  //登录用户的id
                .setSubject("小马")  //登录用户的名称
                .setExpiration(new Date(System.currentTimeMillis() + 30*1000))//过期时间
                .setIssuedAt(new Date(System.currentTimeMillis()))//当前时间
                .signWith(SignatureAlgorithm.HS512, secret)//头部信息 第一个参数为加密方式为哈希512  第二个参数为加的盐为secret字符串
                .compact();

        System.out.println("token令牌是："+token);

        Claims claims1 = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        Date d1 = claims1.getIssuedAt();
        Date d2 = claims1.getExpiration();
        System.out.println("username参数值：" + claims1.get("username"));
        System.out.println("登录用户的id：" + claims1.getId());
        System.out.println("登录用户的名称：" + claims1.getSubject());
        System.out.println("令牌签发时间：" + sdf.format(d1));
        System.out.println("令牌过期时间：" + sdf.format(d2));
    }
}
