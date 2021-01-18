package top.hellooooo.job;

import org.junit.jupiter.api.Test;

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
}
