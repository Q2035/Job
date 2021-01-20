package top.hellooooo.job;

import org.junit.jupiter.api.Test;
import top.hellooooo.job.pojo.Clazz;
import top.hellooooo.job.pojo.JobInfo;
import top.hellooooo.job.pojo.User;
import top.hellooooo.job.util.JobFilenameParser;

import java.io.File;

/**
 * @Author Q
 * @Date 2021-01-18 20:25
 * @Description
 */
public class TestFile {
    @Test
    public void test1(){
        long start = System.currentTimeMillis();
        File file = new File("D:\\Private\\samsung_image_1.zip");
        File file1 = new File("D:\\Private\\samsung_image.zip");
        file.renameTo(file1);
        System.out.println(System.currentTimeMillis() - start);

    }

    @Test
    public void testJobFileParser(){
        JobInfo jobInfo = new JobInfo();
        jobInfo.setJobFilenameInfo("$$classname $username $realname 计网");
        User user = new User();
        user.setUsername("180604124");
        user.setRealName("Q");
        Clazz clazz = new Clazz();
        clazz.setClazzName("A18CS");
        System.out.println(JobFilenameParser.parseJobFilename(jobInfo, user, clazz, " "));
    }
}
