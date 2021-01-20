package top.hellooooo.job.util;


import org.springframework.stereotype.Component;
import top.hellooooo.job.pojo.Clazz;
import top.hellooooo.job.pojo.JobInfo;
import top.hellooooo.job.pojo.User;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Q
 * @Date 10/01/2021 19:20
 * @Description 解析作业名
 */
@Component
public class JobFilenameParser {

    private static final String replace_1 = "$username";
    private static final String replace_2 = "$realname";
    private static final String replace_3 = "$classname";

    public static String parseJobFilename(JobInfo jobInfo, User user, Clazz clazz, String separator) {
        StringBuilder result = new StringBuilder();
        // "$classname $username $realname"
        String[] split = jobInfo.getJobFilenameInfo().split(" ");
        if (split.length == 0) {
            return jobInfo.getJobFilenameInfo();
        } else {
            for (String s : split) {
                switch (s) {
                    case replace_1:
                        result.append(user.getUsername());
                        break;
                    case replace_2:
                        result.append(user.getRealName());
                        break;
                    case replace_3:
                        result.append(clazz.getClazzName());
                        break;
                    default:
                        result.append(s);
                }
                result.append(separator);
            }
            // 将最后一个分隔符除去
            result.substring(0, result.length() - separator.length());
        }

        return result.toString();
    }
}
