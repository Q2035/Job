package top.hellooooo.job.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @Author Q
 * @Date 2021-01-12 08:57
 * @Description
 */
@Component
@Log4j2
public class TokenFilter extends OncePerRequestFilter {
    /**
     * 尝试从URI、Cookie、Header中提取JWT
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求参数中获取
        String requestURI = request.getRequestURI();
        String authStr = request.getParameter("Authentication");
        if (!StringUtils.isEmpty(authStr)) {
            return;
        }
        // 从Cookie中获取


        filterChain.doFilter(request, response);
        log.info("RequestURI is {}", requestURI);


        StringBuffer requestURL = request.getRequestURL();
        log.info("RequestURL is {}", requestURL);

    }
}
