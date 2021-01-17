package top.hellooooo.job.controller.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.hellooooo.job.controller.BaseController;
import top.hellooooo.job.pojo.Clazz;
import top.hellooooo.job.pojo.CommonResult;
import top.hellooooo.job.pojo.User;
import top.hellooooo.job.service.UserService;
import top.hellooooo.job.util.ConstantString;
import top.hellooooo.job.util.JwtUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author Q
 * @Date 2021-01-17 12:58
 * @Description
 */
@Controller
@RequestMapping("/manager/clazz")
public class ClazzManagerController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping({"/", "", "/index"})
    public String index(Model model,
                        HttpServletRequest request) {
        Cookie cookie = getCookie(ConstantString.tokenName, request);
        Integer userId = Integer.valueOf((String) JwtUtils.getClaim(cookie.getValue(), ConstantString.tokenUserId));
        List<Clazz> clazzs = userService.getClazzsByAdminId(userId);
        model.addAttribute("clazzs", clazzs);
        return "manager/clazzInfo";
    }

    @ResponseBody
    @PostMapping("/create")
    public CommonResult create(@RequestBody Clazz clazz,
                               HttpServletRequest request) {
        if (clazz == null || StringUtils.isEmpty(clazz.getClazzName())) {
            return CommonResult.fail("Empty clazz name");
        }
        Cookie cookie = getCookie(ConstantString.tokenName, request);
        Integer userId = Integer.valueOf((String) JwtUtils.getClaim(cookie.getValue(), ConstantString.tokenUserId));
        userService.insertClazz(clazz, userId);
        return CommonResult.ok("Create Clazz Successfully");
    }

    @ResponseBody
    @PostMapping("/import")
    public CommonResult importStudent(@RequestParam("clazzId") Integer clazzId,
                                      HttpServletRequest request) {

        return CommonResult.ok("Import Successfully");
    }
}