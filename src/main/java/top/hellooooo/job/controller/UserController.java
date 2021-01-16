package top.hellooooo.job.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import top.hellooooo.job.pojo.User;
import top.hellooooo.job.service.UserService;
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
@PropertySource("url.properties")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${url.token}")
    private String urlToken;

    @ResponseBody
    @GetMapping("hello")
    public String hello(){
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
        if (validateAuthFromCookie(request, urlToken)) {
            return "user/main";
        }
        String msg = request.getParameter("msg");
        if (!StringUtils.isEmpty(msg)) {
            model.addAttribute("msg", msg);
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(urlToken)) {
                    cookie.setValue("");
                    response.addCookie(cookie);
                }
            }
        }
        return "/login";
    }

    public boolean validateAuthFromCookie(HttpServletRequest request, String urlToken) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(urlToken)) {
                    if (!StringUtils.isEmpty(cookie.getValue())) {
                        if (JwtUtils.validateJwt(cookie.getValue())) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        }
        return false;
    }

    @GetMapping("/main")
    public String mainPage(){
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
            map.put("username", username);
            String jwt = JwtUtils.generateJWT(map);
            Cookie cookie = new Cookie(urlToken, jwt);
            response.addCookie(cookie);
            return "user/main";
        } else {
            return "redirect:/user/index?msg=Wrong Password";
        }
    }
}
