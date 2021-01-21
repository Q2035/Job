package top.hellooooo.job.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.hellooooo.job.pojo.AccountStatus;
import top.hellooooo.job.pojo.Role;
import top.hellooooo.job.pojo.User;
import top.hellooooo.job.pojo.UserActionInfo;
import top.hellooooo.job.service.JobService;
import top.hellooooo.job.service.LogService;
import top.hellooooo.job.service.UserService;
import top.hellooooo.job.util.ConstantString;
import top.hellooooo.job.util.JwtUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@Log4j2
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private SimpleDateFormat simpleDateFormat;

    @Autowired
    private JobService jobService;

    @Autowired
    private LogService logService;

    @Value("${url.token}")
    private String urlToken;


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
                Integer userId = Integer.valueOf((String)JwtUtils.getClaim(cookie.getValue(), ConstantString.tokenUserId));
                return getUserIndexURI(Role.fromRoleName(Integer.valueOf(JWTRole)), model, userId);
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
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        return "/login";
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
    public String authentication(Model model,
                                 @RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 HttpServletResponse response,
                                 HttpServletRequest request) {
        User user = userService.getUserByUsername(username);
        Date now = new Date();
        if (user.getAccountStatus() != AccountStatus.ACCOUNT_NORMAL) {
            return "redirect:/user/index?msg=Account Freeze";
        }
        if (user.getPassword().equals(password)) {
            Map<String, String> map = new HashMap<>();
            // 存放一些可能用到的信息
            map.put(ConstantString.tokenUsername, username);
            Role role = user.getRole();
            map.put(ConstantString.tokenRole, String.valueOf(role.getRole()));
            map.put(ConstantString.tokenUserId, String.valueOf(user.getId()));
            map.put(ConstantString.tokenUserRealName, user.getRealName());
            String jwt = JwtUtils.generateJWT(map);
            Cookie cookie = new Cookie(urlToken, jwt);
            // 可在同一应用服务器内共享
            cookie.setPath("/");
            String device = request.getHeader(ConstantString.deviceInfo);
            // 打印日志
            String loginState = "Date:{} id:{} realName:{} login from IP:{} Device:{}";
            log.info(loginState,
                    simpleDateFormat.format(now), user.getId(), user.getRealName(), request.getRemoteAddr(), device);
            UserActionInfo userActionInfo = new UserActionInfo(null, user.getId(), user.getUsername(), null);
            logService.login(userActionInfo, loginState, simpleDateFormat.format(now), String.valueOf(user.getId()), user.getRealName(), request.getRemoteAddr(), device);
            response.addCookie(cookie);
            // 根据用户角色信息返回相应的页面
            return getUserIndexURI(role, model, user.getId());
        } else {
            return "redirect:/user/index?msg=Wrong Password";
        }
    }

    private String getUserIndexURI(Role role,
                                   Model model,
                                   Integer userId) {
        if (role == null) {
            return "/login";
        }
        switch (role) {
            case ADMIN:
                return "/admin/main";
            case STUDENT:
                // TODO: 2021-01-21 需要增加未提交的作业和已提交的作业
                return "/user/main";
            case CLAZZ_ADMIN:
                model.addAttribute("jobs", jobService.getJobsByCreatorId(userId));
                return "/manager/main";
            default:
                return "/login";
        }
    }
}