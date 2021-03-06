package top.hellooooo.job.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.hellooooo.job.pojo.CommonResult;
import top.hellooooo.job.pojo.FileDTO;
import top.hellooooo.job.pojo.UserActionInfo;
import top.hellooooo.job.service.FileService;
import top.hellooooo.job.service.LogService;
import top.hellooooo.job.util.ConstantString;
import top.hellooooo.job.util.JwtUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author Q
 * @Date 2021-01-18 12:59
 * @Description
 */
@Controller
@RequestMapping("/file")
@Log4j2
public class FileController extends BaseController {

    @Autowired
    private FileService fileService;

    @Autowired
    private SimpleDateFormat simpleDateFormat;

    @Autowired
    private LogService logService;

    /**
     * 文件上传路径
     */
    @Value("${file.basepath}")
    private String basePath;

    @RequestMapping("/show")
    public String show() {
        return "/user/file";
    }

    /**
     * 上传
     *
     * @param file
     * @param suffix
     * @param shardIndex
     * @param shardSize
     * @param shardTotal
     * @param size
     * @param key
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @PostMapping("/upload")
    @ResponseBody
    public CommonResult upload(MultipartFile file,
                               String suffix,
                               Long shardIndex,
                               Long shardSize,
                               Long shardTotal,
                               Long size,
                               String key,
                               HttpServletRequest request
    ) throws IOException, InterruptedException {
        Integer userId = 0;
        // 只有第一次传输文件时才会打印日志
        if (shardIndex == 1) {
            Cookie cookie = getCookie(ConstantString.tokenName, request);
            if (cookie != null) {
                try {
                    userId = Integer.valueOf((String) JwtUtils.getClaim(cookie.getValue(), ConstantString.tokenUserId));
                    String username = (String) JwtUtils.getClaim(cookie.getValue(), ConstantString.tokenUsername);
                    UserActionInfo userActionInfo = new UserActionInfo(null, userId, username, null);
                    logService.upload(userActionInfo, "Date:{} username:{} upload file {}", simpleDateFormat.format(new Date()), username, key);
                } catch (Exception e) {
                    log.warn("Error getting name from jwt.");
                }
                log.info("User ID:{} Begin to upload file {}", userId, key);
            }
        } else {
            // 不管是不是第一次提交，都需要用户ID
            Cookie cookie = getCookie(ConstantString.tokenName, request);
            userId = Integer.valueOf((String) JwtUtils.getClaim(cookie.getValue(), ConstantString.tokenUserId));
        }
        // 获取文件的扩展名
        // 设置新的名字
        // scbwTp88b6WY4MYWQguWY.zip
        String fileName = new StringBuffer().append(key).append(".").append(suffix).toString();
        // 分片的名字
        // scbwTp88b6WY4MYWQguWY.zip.1
        String localfilename = new StringBuffer(fileName)
                .append(".")
                .append(shardIndex)
                .toString();

        // 以绝对路径保存重名命后的图片
        File targetFile = new File(basePath, localfilename);
        // 上传这个文件
        file.transferTo(targetFile);
        //数据库持久化这个数据
        FileDTO fileDTO = new FileDTO(null, userId, basePath + localfilename, localfilename, suffix, size, System.currentTimeMillis(), System.currentTimeMillis(), shardIndex, shardSize, shardTotal, key);
        // 插入到数据库中
        fileService.save(fileDTO);
        // 判断当前是不是最后一个分页 如果不是就继续等待其他分页 合并分页
        if (shardIndex.equals(shardTotal)) {
            fileDTO.setPath(basePath + fileName);
            this.merge(fileDTO);
            return CommonResult.ok("Upload Successfully");
        }
        return CommonResult.ok(shardIndex + " Upload Successfully");
    }

    /**
     * @param listFiles
     * @param key
     * @param shardSize
     * @param size
     * @return -1 代表存在上传完的文件 0 代表全部需要重新传输 其他数字代表下一个需要传输的分片shardIndex
     */
    private Integer getExistedMaxShardFile(File[] listFiles, String key, Long shardSize, Long size) {
        Integer maxShardFile = 1;
        if (listFiles != null) {
            for (File listFile : listFiles) {
                // 如果以文件key开头，则说明是未传输完成的分片文件
                if (listFile.getName().startsWith(key)) {
                    String name = listFile.getName();
                    int lastIndexOf = name.lastIndexOf('.');
                    if (lastIndexOf < name.length()) {
                        name = name.substring(lastIndexOf + 1);
                        Integer parseNumber = 0;
                        try {
                            parseNumber = NumberUtils.parseNumber(name, Integer.class) + 1;
                            // 如果解析出来的数字没问题，那么比较分片大小与文件大小查看文件是否为合法分片
                            if (listFile.length() == shardSize) {
                                if (maxShardFile < parseNumber) {
                                    maxShardFile = parseNumber;
                                }
                            } else {
                                listFile.delete();
                            }
                        } catch (NumberFormatException e) {
                            // 只要解析出来不是数字并且文件大小一致则说明文件已经上传成功并成功Merge
                            if (listFile.length() == size) {
                                log.info("Fast upload successfully {}", key);
                                return -1;
                            }
                            // 不是则说明有问题，应该删除
                            listFile.delete();
                        }
                    }
                }
            }
        }
        return maxShardFile;
    }

    @RequestMapping("/check")
    @ResponseBody
    public CommonResult check(String suffix,
                              Long shardSize,
                              Long shardTotal,
                              Long size,
                              String key,
                              HttpServletRequest request) {
        File baseFileParent = new File(basePath);
        Integer maxShardFile;
        maxShardFile = getExistedMaxShardFile(baseFileParent.listFiles(), key, shardSize, size);
        // 与数据库中的数据进行比对
        if (maxShardFile > 1) {
            List<FileDTO> check = fileService.check(key);
            FileDTO fileDTO = check.get(0);
            if (fileDTO.getShardIndex().intValue() + 1 == maxShardFile) {
                return CommonResult.fail(maxShardFile, "Incomplete file detected, waiting...");
            }
            return CommonResult.fail(1, "Something error, need to re-upload");
        }
        switch (maxShardFile) {
            case -1:
                Cookie cookie = getCookie(ConstantString.tokenName, request);
                Integer userId = Integer.valueOf((String) JwtUtils.getClaim(cookie.getValue(), ConstantString.tokenUserId));
                String username = (String) JwtUtils.getClaim(cookie.getValue(), ConstantString.tokenUsername);
                UserActionInfo userActionInfo = new UserActionInfo(null, userId, username, null);
                logService.upload(userActionInfo, "Date:{} username:{} upload file {}", simpleDateFormat.format(new Date()), username, key);

                FileDTO fileDTO = new FileDTO(null, userId, basePath + key + suffix, key, suffix, size, System.currentTimeMillis(), System.currentTimeMillis(), shardTotal, shardSize, shardTotal, key);
                // 插入到数据库中
                fileService.save(fileDTO);

                return CommonResult.ok("Fast upload successfully");
            case 0:
                break;
            default:
                // 下一个需要传输的分片为maxShardFile
                return CommonResult.fail(maxShardFile, "Prepare to continue upload...");
        }
        //如果这个key存在的话 那么就获取上一个分片去继续上传
        return CommonResult.fail(maxShardFile, "Prepare to upload..");
    }


    /**
     * @author fengxinglie
     * 合并分页
     */
    private void merge(FileDTO fileDTO) throws InterruptedException {
        //合并分片开始
        log.info("Begin merge the file {}", fileDTO.getFileKey());
        // 获取到的路径 没有.1 .2 这样的东西
        String path = fileDTO.getPath();
        // 截取视频所在的路径
        path = path.replace(basePath, "");
        Long shardTotal = fileDTO.getShardTotal();
        // TODO: 2021-01-18 使用作业指定的名字格式
        File newFile = new File(basePath + path);
        byte[] byt = new byte[10 * 1024 * 1024];
        int len;
        // 分片文件
        FileInputStream fileInputStream = null;
        try (
                // 文件追加写入
                FileOutputStream outputStream = new FileOutputStream(newFile, true);

        ) {
            for (int i = 0; i < shardTotal; i++) {
                // 读取第i个分片
                //  course\6sfSqfOwzmik4A4icMYuUe.mp4.1
                fileInputStream = new FileInputStream(basePath + path + "." + (i + 1));
                while ((len = fileInputStream.read(byt)) != -1) {
                    outputStream.write(byt, 0, len);
                }
            }
        } catch (IOException e) {
            log.error("Merge Error", e);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (Exception e) {
                log.error("IOException", e);
            }
        }
        log.info("Merge Successfully");
        //告诉java虚拟机去回收垃圾 至于什么时候回收  这个取决于 虚拟机的决定
        System.gc();
        //等待100毫秒 等待垃圾回收去 回收完垃圾
        Thread.sleep(100);
        log.info("Begin delete the shard file");
        for (int i = 0; i < shardTotal; i++) {
            String filePath = basePath + path + "." + (i + 1);
            File file = new File(filePath);
            boolean result = file.delete();
        }
        log.info("Finish Delete");
    }

}