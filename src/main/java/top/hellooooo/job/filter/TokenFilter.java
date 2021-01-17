package top.hellooooo.job.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import top.hellooooo.job.exception.AuthenticateException;
import top.hellooooo.job.pojo.SecurityURI;
import top.hellooooo.job.service.UserService;
import top.hellooooo.job.util.ConstantString;
import top.hellooooo.job.util.JwtUtils;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author Q
 * @Date 2021-01-12 08:57
 * @Description
 */
@Component
@Log4j2
@PropertySource("classpath:url.properties")
@ConfigurationProperties(prefix = "url")
public class TokenFilter extends OncePerRequestFilter {

    @Value("${url.token}")
    private String token;

    @Value("#{'${url.ignoring}'.split(',')}")
    private String[] ignoring;

    @Autowired
    private UserService userService;

    private List<SecurityURI> securityURIs;

    @PostConstruct
    public void initSecurityURIs() {
        securityURIs = userService.getSecurityURI();
    }

    /**
     * 尝试从URI、Cookie、Header中提取JWT
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        for (String ignoringURL : ignoring) {
            if (ignoringURL.contains(requestURI)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        try {
            String jwt = tryGetAndParseToken(request, filterChain);
            if (StringUtils.isEmpty(jwt)) {
                response.sendRedirect("/user/index?msg=No Auth");
                return;
            }
            // 鉴权、拦截
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(token)) {
                        Object claim = JwtUtils.getClaim(cookie.getValue(), ConstantString.tokenRole);
                        if (!StringUtils.isEmpty(claim)) {
                            for (SecurityURI uri : securityURIs) {
                                if (uri.getUri().contains(request.getRequestURI())) {
                                    Integer role = uri.getRoles().getRole();
                                    // 角色等级高于当前角色才有访问权限
                                    try {
                                        if ((Integer.valueOf((String) claim)) >= role) {
                                            filterChain.doFilter(request, response);
                                        } else {
                                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Do not have permission!");
                                        }
                                        return;
                                    } catch (ClassCastException e) {
                                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token parse error.");
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"Forbidden");
            return;
        } catch (AuthenticateException e) {
            // 登录信息无效则返回登录界面
            response.sendRedirect("/user/index?msg=Re login");
            return;
        }
    }

    private String tryGetAndParseToken(HttpServletRequest request, FilterChain filterChain) throws IOException, ServletException, AuthenticateException {
        String jwt = "";
        // 从Header中获取
        String header = request.getHeader(token);
        if (!StringUtils.isEmpty(header)) {
            jwt = header;
            if (JwtUtils.validateJwt(header)) {
                return jwt;
            } else {
                throw new AuthenticateException("Credentials expire, please try to login in again.");
            }
        }

        // 从请求路径获取Token信息
        String authToken = request.getParameter(token);
        if (!StringUtils.isEmpty(authToken)) {
            jwt = authToken;
            // 校验通过
            if (JwtUtils.validateJwt(authToken)) {
                return jwt;
            } else {
                throw new AuthenticateException("Credentials expire, please try to login in again.");
            }
        }

        // 从Cookie中获取
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // 存在Token
                if (cookie.getName().equals(token)) {
                    if (StringUtils.isEmpty(cookie.getValue())) {
                        throw new AuthenticateException("Logout Successfully");
                    }
                    if (JwtUtils.validateJwt(cookie.getValue())) {
                        jwt = cookie.getValue();
                        // filterChain.doFilter(request, response);
                        return jwt;
                    } else {
                        throw new AuthenticateException("Credentials expire, please try to login in again.");
                    }
                }
            }
        }
        return jwt;
    }

    private boolean tryValidate(HttpServletResponse response, String authStr) throws IOException {
        // 校验通过
        if (JwtUtils.validateJwt(authStr)) {
            return true;
            // 失败
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendRedirect("/error/4xx.html");
            return false;
        }
    }
}
