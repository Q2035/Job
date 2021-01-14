package top.hellooooo.job;

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

    }
}
