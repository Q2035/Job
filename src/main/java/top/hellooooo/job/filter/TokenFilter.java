package top.hellooooo.job.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import top.hellooooo.job.exception.AuthenticateException;
import top.hellooooo.job.util.JwtUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Q
 * @Date 2021-01-12 08:57
 * @Description
 */
@Component
@Log4j2
@PropertySource("url.properties")
@ConfigurationProperties(prefix = "url")
public class TokenFilter extends OncePerRequestFilter {

    @Value("${url.token}")
    private String token;

    @Value("#{'${url.ignoring}'.split(',')}")
    private String[] ignoring;

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
            boolean hasToken = tryGetAndParseToken(request, response, filterChain);
            if (!hasToken) {
                // response.sendError(HttpServletResponse.SC_FORBIDDEN, "NO Auth");
                response.sendRedirect("/user/index");
            }
        } catch (AuthenticateException e) {
            // 登录信息无效则返回登录界面
            // response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Re login");
            response.sendRedirect("/user/index");
            return;
        }

    }

    private boolean tryGetAndParseToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException, AuthenticateException {
        boolean hasToken = false;

        // 从Header中获取
        String header = request.getHeader(token);
        if (!StringUtils.isEmpty(header)) {
            hasToken = true;
            if (JwtUtils.validateJwt(header)) {
                filterChain.doFilter(request, response);
            } else {
                throw new AuthenticateException("Credentials expire, please try to login in again.");
            }
        }

        // 从请求路径获取Token信息
        String authToken = request.getParameter(token);
        if (!StringUtils.isEmpty(authToken)) {
            hasToken = true;
            // 校验通过
            if (JwtUtils.validateJwt(authToken)) {
                filterChain.doFilter(request, response);
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
                    hasToken = true;
                    if (JwtUtils.validateJwt(cookie.getValue())) {
                        filterChain.doFilter(request, response);
                    } else {
                        throw new AuthenticateException("Credentials expire, please try to login in again.");
                    }
                }
            }
        }

        return hasToken;
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
