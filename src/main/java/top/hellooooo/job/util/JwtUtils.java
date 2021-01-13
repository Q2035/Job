// package top.hellooooo.job.util;
//
// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import lombok.extern.log4j.Log4j2;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.util.StringUtils;
// import top.hellooooo.job.pojo.User;
//
// import java.util.Date;
// import java.util.HashMap;
// import java.util.Map;
//
// /**
//  * @Author Q
//  * @Date 2021-01-12 08:58
//  * @Description
//  */
// @Log4j2
// public class JwtUtils {
//
//     private static final String CLAIM_KEY_USERNAME = "sub";
//
//     private static final String CLAIM_KEY_CREATED = "created";
//
//     // 使用username + "secret"作为密钥
//
//     private String SECRET_PREFIX = "_secret";
//
//     @Value("${jwt.expiration}")
//     private Long expiration;
//
//     /**
//      * 根据负责生成JWT的token
//      */
//     private String generateToken(Map<String, Object> claims, String username) {
//         String secret = username + SECRET_PREFIX;
//         return Jwts.builder()
//                 .setClaims(claims)
//                 .setExpiration(generateExpirationDate())
//                 .signWith(SignatureAlgorithm.HS512, secret)
//                 .compact();
//     }
//
//     /**
//      * 从token中获取JWT中的负载
//      */
//     private Claims getClaimsFromToken(String token) {
//         Claims claims = null;
//         try {
//             claims = Jwts.parser()
//                     .setSigningKey(secret)
//                     .parseClaimsJws(token)
//                     .getBody();
//         } catch (Exception e) {
//             log.info("JWT格式验证失败:{}", token);
//         }
//         return claims;
//     }
//
//     /**
//      * 生成token的过期时间
//      */
//     private Date generateExpirationDate() {
//         return new Date(System.currentTimeMillis() + expiration * 1000);
//     }
//
//     /**
//      * 从token中获取登录用户名
//      */
//     public String getUserNameFromToken(String token) {
//         String username;
//         try {
//             Claims claims = getClaimsFromToken(token);
//             username = claims.getSubject();
//         } catch (Exception e) {
//             username = null;
//         }
//         return username;
//     }
//
//     /**
//      * 验证token是否还有效
//      *
//      * @param token       客户端传入的token
//      * @param user 从数据库中查询出来的用户信息
//      */
//     public boolean validateToken(String token, User user) {
//         String username = getUserNameFromToken(token);
//         return username.equals(user.getUsername()) && !isTokenExpired(token);
//     }
//
//     /**
//      * 判断token是否已经失效
//      */
//     private boolean isTokenExpired(String token) {
//         Date expiredDate = getExpiredDateFromToken(token);
//         return expiredDate.before(new Date());
//     }
//
//     /**
//      * 从token中获取过期时间
//      */
//     private Date getExpiredDateFromToken(String token) {
//         Claims claims = getClaimsFromToken(token);
//         return claims.getExpiration();
//     }
//
//     /**
//      * 根据用户信息生成token
//      */
//     public String generateToken(User userDetails) {
//         Map<String, Object> claims = new HashMap<>();
//         claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
//         claims.put(CLAIM_KEY_CREATED, new Date());
//         return generateToken(claims);
//     }
//
//
// }
