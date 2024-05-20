package com.xc.common.utils;

import com.xc.common.constants.JwtConstant;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecretKeyBuilder;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class JwtTokenUtils {
    /**
     * jwt过期时间(毫秒)
     */
    public static final long ACCESS_EXPIRE = 3000000L;    //50min

    public static final long ADVANCE_EXPIRE_TIME = 1800000L; //30min
    /**
     * 加密算法
     */
    private final static SecureDigestAlgorithm<SecretKey, SecretKey> ALGORITHM = Jwts.SIG.HS256;
    /**
     * 私钥 / 生成签名的时候使用的秘钥secret，一般可以从本地配置文件中读取，切记这个秘钥不能外露，只在服务端使用，在任何场景都不应该流露出去。
     * 一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
     * 应该大于等于 256位(长度32及以上的字符串)，并且是随机的字符串
     */

    private final static String SECRET = "xiechengfrgurbfuivbuievbuiefveifuvbeuifvbiuvrvuxbdwc";
    /**
     * 秘钥实例
     */
    public static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    /**
     * jwt签发者
     */
    private final static String JWT_ISS = "xc";
    /**
     * jwt主题
     */
    private final static String SUBJECT = "Peripherals";


    /**
     * 生成jwt
     * 使用Hs256算法, 私匙使用固定秘钥
     *
     * @param claims 设置的信息
     * @return
     */
    public static String createJWT(HashMap<String, Object> claims) {
            /*
            这些是一组预定义的声明，它们 不是强制性的，而是推荐的 ，以 提供一组有用的、可互操作的声明 。
            iss: jwt签发者
            sub: jwt所面向的用户
            aud: 接收jwt的一方
            exp: jwt的过期时间，这个过期时间必须要大于签发时间
            nbf: 定义在什么时间之前，该jwt都是不可用的.
            iat: jwt的签发时间
            jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击
             */
        // 令牌id
        String uuid = UUID.randomUUID().toString();
        Date exprireDate = Date.from(Instant.now().plusMillis(ACCESS_EXPIRE));

        return Jwts.builder()
                // 设置头部信息header
                .header()
                .add("typ", "JWT")
                .add("alg", "HS256")
                .and()
                // 设置自定义负载信息payload
                .claim("userId", claims.get(JwtConstant.USER_ID))
                // 令牌ID
                .id(uuid)
                // 过期日期
                .expiration(exprireDate)
                // 签发时间
                .issuedAt(new Date())
                // 主题
                .subject(SUBJECT)
                // 签发者
                .issuer(JWT_ISS)
                // 签名
                .signWith(KEY, ALGORITHM)
                .compact();
    }

    /**
     * 解析token
     *
     * @param token token
     * @return Jws<Claims>
     */
    public static Jws<Claims> parseJwt(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token);
    }

    public static JwsHeader parseHeader(String token) {
        return parseJwt(token).getHeader();
    }

    public static Claims parsePayload(String token) {
        return parseJwt(token).getPayload();

    }

    /**
     * 检查token是否快要过期
     * @param token
     * @return
     */
    public static boolean isTokenExpired(String token){
        Date expiration = parseJwt(token).getPayload().getExpiration(); //过期时间
        if(expiration.before(new Date(System.currentTimeMillis()+ADVANCE_EXPIRE_TIME))){
            return true;
        }
        return false;  //true 为过期
    }

    /**
     * 检查token是否已经过期
     * @param token
     * @return
     */
    public static boolean isTokenAreadyExpired(String token){
        Date expiration = parseJwt(token).getPayload().getExpiration(); //过期时间
        if(expiration.before(new Date())){
            return true;
        }
        return false;  //true 为过期
    }
    /**
     * 获取可用的token
     *返回一个新token
     * @param userId
     * @return
     */
    public static String getGoodToken(Long userId){
            HashMap<String, Object> map = new HashMap<>();
            map.put(JwtConstant.USER_ID,userId);
            String newToken = createJWT(map);
            return newToken;
    }
}
