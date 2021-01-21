package top.hellooooo.job.controller.manager;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.hellooooo.job.controller.BaseController;
import top.hellooooo.job.pojo.*;
import top.hellooooo.job.service.JobService;
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
@Log4j2
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

    /**
     * 为指定班级重命名
     *
     * @param clazzId
     * @param clazzName
     * @return
     */
    @ResponseBody
    @PostMapping("/update/name")
    public CommonResult update(Integer clazzId,
                               String clazzName) {
        try {
            Clazz clazzByClazzId = userService.getClazzByClazzId(clazzId);
            clazzByClazzId.setClazzName(clazzName);
            userService.updateClazz(clazzByClazzId);
        } catch (RuntimeException e) {
            return CommonResult.fail("Something Error");
        }
        return CommonResult.ok("Update Class Name Successfully");
    }

    /**
     * 查看指定班级信息
     *
     * @param clazzId
     * @return
     */
    @ResponseBody
    @PostMapping("/info")
    public CommonResult<User> info(@RequestParam("clazzId") Integer clazzId) {
        List<User> users = userService.getUsers(clazzId);
        return CommonResult.ok(users, "Successfully Get");
    }

    /**
     * 新建班级信息
     *
     * @param clazz
     * @param request
     * @return
     */
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

    /**
     * 为指定班级导入学生
     *
     * @param multipartFile
     * @param clazzId
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/import")
    public CommonResult importStudent(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("clazzId") Integer clazzId,
            HttpServletRequest request) {
        String originalFilename = multipartFile.getOriginalFilename();
        if (!originalFilename.endsWith(fileType)) {
            return CommonResult.fail("Not the support file type: txt");
        }
        Cookie cookie = getCookie(ConstantString.tokenName, request);
        String username = (String) JwtUtils.getClaim(cookie.getValue(), ConstantString.tokenUsername);
        Integer userId = Integer.valueOf((String) JwtUtils.getClaim(cookie.getValue(), ConstantString.tokenUserId));
        long size = multipartFile.getSize();
        if (size < _1MB) {
            try (
                    InputStream inputStream = multipartFile.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            ) {
                Integer insertUser = insertUser(bufferedReader, clazzId);
                log.info("Id:{} insert count:{} into class:{}", userId, insertUser, clazzId);
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
            try (
                    InputStream inputStream = new FileInputStream(file);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            ) {
                multipartFile.transferTo(file);
                Integer integer = insertUser(bufferedReader, clazzId);
                log.info("Id:{} insert count:{} into class:{}", userId, integer, clazzId);
                UserActionInfo userActionInfo = new UserActionInfo(null, userId, username, null);
                logService.importUser(userActionInfo, "Date:{} username:{} import user for class {}", simpleDateFormat.format(new Date()), username, String.valueOf(clazzId));
            } catch (IOException e) {
                return CommonResult.fail("Fail to Upload, please retry later.");
            }
        }
        return CommonResult.ok("Import Successfully");
    }

    /**
     * 根据给定的输入流解析后插入数据库
     *
     * @param bufferedReader
     * @param clazzId
     * @return
     * @throws IOException
     */
    private Integer insertUser(BufferedReader bufferedReader, Integer clazzId) throws IOException {
        Integer countOfStudent = 0;
        List<User> userList = new ArrayList<>(128);
        String inp = bufferedReader.readLine();
        while (inp != null) {
            if (StringUtils.isEmpty(inp)) {
                continue;
            }
            // 处理输入字符串后构建User对象
            // TODO: 2021-01-19  创建User对象
            // uesername realname
            User user = new User();
            String[] split = inp.split("\t");
            if (split.length != 2) {
                throw new RuntimeException("Not the support style");
            }
            user.setUsername(split[0].trim());
            user.setPassword(split[0].trim());
            user.setRealName(split[1].trim());
            userList.add(user);
            if (userList.size() > 100) {
                userService.batchInsertUserWithClazzId(userList, clazzId);
                userList.clear();
            }
            countOfStudent++;
            inp = bufferedReader.readLine();
        }
        if (!userList.isEmpty()) {
            userService.batchInsertUserWithClazzId(userList, clazzId);
        }
        // 更新班级人数信息
        Clazz clazzByClazzId = userService.getClazzByClazzId(clazzId);
        clazzByClazzId.setStudentCount(clazzByClazzId.getStudentCount() + countOfStudent);
        userService.updateClazz(clazzByClazzId);
        return countOfStudent;
    }

}