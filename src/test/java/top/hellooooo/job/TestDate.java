package top.hellooooo.job;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author Q
 * @Date 2021-01-14 19:42
 * @Description
 */
public class TestDate {
    public static void main(String[] args) {
        Date now = new Date();
        System.out.println(new Date(now.getTime() + 1000 * 30 * 60));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println(simpleDateFormat.format(now));

        TestDate testDate = new TestDate();
        String s = testDate.replaceBraces("hello{}isme{}", "nihao", "wohao","tahao");
        System.out.println(s);
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
