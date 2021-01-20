package top.hellooooo.job.controller.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.hellooooo.job.controller.BaseController;
import top.hellooooo.job.pojo.Clazz;
import top.hellooooo.job.pojo.CommonResult;
import top.hellooooo.job.pojo.JobInfo;
import top.hellooooo.job.pojo.User;
import top.hellooooo.job.service.UserService;
import top.hellooooo.job.util.ConstantString;
import top.hellooooo.job.util.JobFilenameParser;
import top.hellooooo.job.util.JwtUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Q
 * @Date 2021-01-20 20:16
 * @Description
 */
@Controller
@RequestMapping("/manager/job")
public class JobController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private SimpleDateFormat simpleDateFormat;

    @ResponseBody
    @PostMapping("/create")
    public CommonResult create(HttpServletRequest request) {
        String inpJSON = "";
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            StringBuilder stringBuilder = new StringBuilder();
            String inp = bufferedReader.readLine();
            while (inp != null) {
                stringBuilder.append(inp);
                inp = bufferedReader.readLine();
            }
            inpJSON = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map;
        try {
            map = objectMapper.readValue(inpJSON, new TypeReference<Map<String, Object>>() {
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return CommonResult.fail(e.getMessage());
        }

        Cookie cookie = getCookie(ConstantString.tokenName, request);
        Integer userId = Integer.valueOf((String) JwtUtils.getClaim(cookie.getValue(), ConstantString.tokenUserId));
        User user = userService.getUserByUserId(userId);
        List<Clazz> clazzse = new ArrayList<>();
        List<Integer> clazzIds = (List<Integer>) map.get("clazzIds");
        for (Integer clazzId : clazzIds) {
            clazzse.add(userService.getClazzByClazzId(clazzId));
        }
        JobInfo jobInfo = new JobInfo();
        try {
            Date now = new Date();
            jobInfo.setJobName(String.valueOf(map.get("jobName")));
            jobInfo.setCreateTime(now);
            jobInfo.setSeparators(String.valueOf(map.get("separator")));
            jobInfo.setJobFilenameInfo(String.valueOf(map.get("jobfilename")));
            jobInfo.setCreator(user);
            jobInfo.setClazzes(clazzse);
            jobInfo.setDeadline(simpleDateFormat.parse(String.valueOf(map.get("deadline"))));
        } catch (ParseException e) {
            return CommonResult.fail("Not the support time");
        } catch (Exception e) {
            return CommonResult.fail("Something error");
        }
        return CommonResult.ok("Create Clazz Successfully");
    }
}
