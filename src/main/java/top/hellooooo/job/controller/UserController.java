package top.hellooooo.job.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Q
 * @Date 10/01/2021 20:45
 * @Description
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @ResponseBody
    @GetMapping("hello")
    public String hello(){
        return "hello";
    }

    /**
     * 登录页面跳转
     * @return
     */
    @GetMapping({"/login", "/", "", "/index"})
    public String loginPage() {
        return "login";
    }

    /**
     *
     * @param username 一般是学号
     * @param password
     * @return
     */
    @PostMapping("/authenticate")
    public String authentication(@RequestParam("username")String username,
                                 @RequestParam("password")String password){

        return "user/main";
    }
}
