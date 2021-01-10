package top.hellooooo.job.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Q
 * @Date 10/01/2021 20:45
 * @Description
 */
@Controller
@RequestMapping("/user")
public class UserController {
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
    @PostMapping("/authentication")
    public String authentication(@RequestParam("username")String username,
                                 @RequestParam("password")String password){

        return "user/main";
    }
}
