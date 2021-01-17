package top.hellooooo.job.controller;

import top.hellooooo.job.util.ConstantString;
import top.hellooooo.job.util.JwtUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author Q
 * @Date 2021-01-17 15:22
 * @Description
 */
public abstract class BaseController {
    protected Cookie getCookie(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }

}
