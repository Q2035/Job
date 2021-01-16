package top.hellooooo.job.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import top.hellooooo.job.pojo.Role;
import top.hellooooo.job.pojo.User;
import top.hellooooo.job.service.UserService;
import top.hellooooo.job.util.ConstantString;
import top.hellooooo.job.util.JwtUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Q
 * @Date 10/01/2021 20:45
 * @Description
 */
@Controller
// @PropertySource("url.properties")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${url.token}")
    private String urlToken;


    @ResponseBody
    @GetMapping("hello")
    public String hello() {
        return "hello";
    }

    /**
     * 登录页面跳转
     *
     * @return
     */
    @GetMapping({"/login", "/", "", "/index"})
    public String loginPage(Model model,
                            HttpServletRequest request) {
        Cookie cookie = getCookie(urlToken, request);
        if (cookie != null) {
            // 登录成功，根据角色信息进行跳转
            String JWTRole = (String) JwtUtils.getClaim(cookie.getValue(), ConstantString.tokenRole);
            if (!StringUtils.isEmpty(JWTRole)) {
                return getUserIndexURI(Role.fromRoleName(Integer.valueOf(JWTRole)));
            }
        }
        String msg = request.getParameter(ConstantString.resultMsg);
        if (!StringUtils.isEmpty(msg)) {
            model.addAttribute(ConstantString.resultMsg, msg);
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        Cookie cookie = getCookie(urlToken, request);
        if (cookie != null) {
            cookie.setValue("");
            response.addCookie(cookie);
        }
        return "/login";
    }

    public boolean validateAuthFromCookie(HttpServletRequest request, String urlToken) {
        Cookie cookie = getCookie(urlToken, request);
        if (cookie != null) {
            if (!StringUtils.isEmpty(cookie.getValue())) {
                return JwtUtils.validateJwt(cookie.getValue());
            }
        }
        return false;
    }
    public Cookie getCookie(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }


    @GetMapping("/main")
    public String mainPage() {
        return "user/main";
    }

    /**
     * @param username 一般是学号
     * @param password
     * @return
     */
    @PostMapping("/authenticate")
    public String authentication(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 HttpServletResponse response) {
        User user = userService.getUserByUsername(username);
        if (user.getPassword().equals(password)) {
            Map<String, String> map = new HashMap<>();
            // 存放一些可能用到的信息
            map.put(ConstantString.tokenUsername, username);
            Role role = user.getRole();
            map.put(ConstantString.tokenRole, String.valueOf(role.getRole()));
            String jwt = JwtUtils.generateJWT(map);
            Cookie cookie = new Cookie(urlToken, jwt);
            response.addCookie(cookie);
            // 根据用户角色信息返回相应的页面
            return "redirect:" + getUserIndexURI(role);
        } else {
            return "redirect:/user/index?msg=Wrong Password";
        }
    }

    private String getUserIndexURI(Role role) {
        if (role == null) {
            return "/login";
        }
        switch (role) {
            case ADMIN:
                return "/admin/main";
            case STUDENT:
                return "/user/main";
            case CLAZZ_ADMIN:
                return "/manager/main";
            default:
                return "/login";
        }
    }
}