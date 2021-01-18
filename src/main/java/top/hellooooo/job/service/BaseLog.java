package top.hellooooo.job.service;

import java.nio.charset.StandardCharsets;

/**
 * @Author Q
 * @Date 2021-01-18 19:51
 * @Description
 */
public class BaseLog {

    public static final Integer MAX_MESSAGE_LENGTH = 194;

    public String log(String head, String msg, String... args) {
        String res = replaceBraces(msg, args);
        if (res.length() > MAX_MESSAGE_LENGTH) {
            res = res.substring(0, MAX_MESSAGE_LENGTH);
        }
        return head + " " + res;
    }

    protected String replaceBraces(String origin, String... args) {
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
