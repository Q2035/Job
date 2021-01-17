package top.hellooooo.job.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hellooooo.job.mapper.LogMapper;
import top.hellooooo.job.pojo.UserActionInfo;
import top.hellooooo.job.service.LogService;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

/**
 * @Author Q
 * @Date 2021-01-17 11:19
 * @Description
 */
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private SimpleDateFormat simpleDateFormat;

    @Autowired
    private LogMapper logMapper;

    @Override
    public void info(UserActionInfo userActionInfo, String msg, String... args) {
        userActionInfo.setMessage("info " + replaceBraces(msg, args).substring(0, 195));
        logMapper.info(userActionInfo);
    }

    @Override
    public void login(UserActionInfo userActionInfo, String msg, String... args) {
        userActionInfo.setMessage("login " + replaceBraces(msg, args).substring(0, 195));
        logMapper.info(userActionInfo);
    }

    @Override
    public void logout(UserActionInfo userActionInfo, String msg, String... args) {
        userActionInfo.setMessage("logout " + replaceBraces(msg, args).substring(0, 195));
        logMapper.info(userActionInfo);
    }

    private String replaceBraces(String origin, String... args) {
        int argIndex = 0;
        StringBuilder stringBuilder = new StringBuilder();
        byte[] originBytes = origin.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < originBytes.length - 1; i++) {
            // 出现花括号则进行替换
            if (originBytes[i] == '{' && originBytes[i + 1] == '}') {
                stringBuilder.append(args[argIndex]);
                i++;
                argIndex++;
                if (argIndex == args.length) {
                    break;
                }
                continue;
            }
            stringBuilder.append((char) originBytes[i]);
        }
        return stringBuilder.toString();
    }
}
