package top.hellooooo.job.controller.manager;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.hellooooo.job.controller.BaseController;
import top.hellooooo.job.pojo.Clazz;
import top.hellooooo.job.pojo.CommonResult;
import top.hellooooo.job.pojo.User;
import top.hellooooo.job.pojo.UserActionInfo;
import top.hellooooo.job.service.LogService;
import top.hellooooo.job.service.UserService;
import top.hellooooo.job.util.ConstantString;
import top.hellooooo.job.util.JwtUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    @Autowired
    private SimpleDateFormat simpleDateFormat;

    @Autowired
    private LogService logService;

    private final String fileType = "txt";

    private final int _1MB = 1024 * 1024;

    @Value("${file.basepath}")
    private String basePath;


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
    @PostMapping("/info")
    public CommonResult<User> info(@RequestParam("clazzId") Integer clazzId) {
        List<User> users = userService.getUsers(clazzId);
        return CommonResult.ok(users, "Successfully Get");
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
                                      HttpServletRequest request,
                                      MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        if (!originalFilename.endsWith(fileType)) {
            return CommonResult.fail("Not the support file type: txt");
        }
        Cookie cookie = getCookie(ConstantString.tokenName, request);
        String username = (String) JwtUtils.getClaim(cookie.getValue(), ConstantString.tokenUsername);
        Integer userId = Integer.valueOf((String) JwtUtils.getClaim(cookie.getValue(), ConstantString.tokenUserId));
        long size = multipartFile.getSize();
        if (size < _1MB) {
            try (InputStream inputStream = multipartFile.getInputStream();
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));) {
                insertUser(bufferedReader, clazzId);
                UserActionInfo userActionInfo = new UserActionInfo(null, userId, username, null);
                logService.importUser(userActionInfo, "Date:{} username:{} import user for class {}", simpleDateFormat.format(new Date()), username, String.valueOf(clazzId));
            } catch (IOException e) {
                return CommonResult.fail("IO Exception");
            }

        // 较大的文件，需要先进行保存
        } else {
            // 随机生成文件名
            String filename = UUID.randomUUID().toString();
            File file = new File(basePath, filename);
            try {
                multipartFile.transferTo(file);

            } catch (IOException e) {
                return CommonResult.fail("Fail to Upload, please retry later.");
            }
        }
        return CommonResult.ok("Import Successfully");
    }

    private void insertUser(BufferedReader bufferedReader, Integer clazzId) throws IOException {
        List<User> userList = new ArrayList<>(128);
        String inp = bufferedReader.readLine();
        while (inp != null) {
            // 处理输入字符串后构建User对象
            // TODO: 2021-01-19  创建User对象
            // uesername realname nickname
            User user = new User();
            userList.add(user);
            if (userList.size() > 100) {
                userService.batchInsertUserWithClazzId(userList, clazzId);
                userList.clear();
            }
        }
        if (!userList.isEmpty()) {
            userService.batchInsertUserWithClazzId(userList, clazzId);
        }
    }

}