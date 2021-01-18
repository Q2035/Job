package top.hellooooo.job.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.hellooooo.job.pojo.CommonResult;
import top.hellooooo.job.pojo.FileDTO;
import top.hellooooo.job.service.FileService;

import java.io.*;
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
public class FileController extends BaseController{

    @Autowired
    FileService fileService;

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
    @RequestMapping("/upload")
    @ResponseBody
    public String upload(MultipartFile file,
                         String suffix,
                         Long shardIndex,
                         Long shardSize,
                         Long shardTotal,
                         Long size,
                         String key
    ) throws IOException, InterruptedException {
        log.info("上传文件开始");
        //文件的名称
        String name = UUID.randomUUID().toString().replaceAll("-", "");
        // 获取文件的扩展名
        // 设置图片新的名字
        // 例 course\6sfSqfOwzmik4A4icMYuUe.mp4
        String fileName = new StringBuffer().append(key).append(".").append(suffix).toString();
        // 分片的名字
        String localfilename = new StringBuffer(fileName)
                .append(".")
                .append(shardIndex)
                .toString(); // course\6sfSqfOwzmik4A4icMYuUe.mp4.1

        // 以绝对路径保存重名命后的图片
        File targetFile = new File(basePath, localfilename);
        // 上传这个文件
        file.transferTo(targetFile);
        //数据库持久化这个数据
        FileDTO fileDTO = new FileDTO();
        fileDTO.setPath(basePath + localfilename);
        fileDTO.setSuffix(suffix);
        fileDTO.setName(name);
        fileDTO.setSize(size);
        fileDTO.setCreatedAt(System.currentTimeMillis());
        fileDTO.setUpdatedAt(System.currentTimeMillis());
        fileDTO.setShardIndex(shardIndex);
        fileDTO.setShardSize(shardSize);
        fileDTO.setShardTotal(shardTotal);
        fileDTO.setFileKey(key);
        // 插入到数据库中
        fileService.save(fileDTO);
        // 判断当前是不是最后一个分页 如果不是就继续等待其他分页 合并分页
        if (shardIndex.equals(shardTotal)) {
            fileDTO.setPath(basePath + fileName);
            this.merge(fileDTO);
        }
        return "上传成功";
    }

    @RequestMapping("/check")
    @ResponseBody
    public CommonResult check(String key) {
        List<FileDTO> check = fileService.check(key);
        //如果这个key存在的话 那么就获取上一个分片去继续上传
        if (check.size() != 0) {
            return CommonResult.ok(check.get(0), "查询成功");
        }
        return CommonResult.fail("查询失败,可以添加");
    }


    /**
     * @author fengxinglie
     * 合并分页
     */
    private void merge(FileDTO fileDTO) throws FileNotFoundException, InterruptedException {
        //合并分片开始
        log.info("分片合并开始");
        //获取到的路径 没有.1 .2 这样的东西
        String path = fileDTO.getPath();
        //截取视频所在的路径
        path = path.replace(basePath, "");
        Long shardTotal = fileDTO.getShardTotal();
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
            log.error("分片合并异常", e);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                log.info("IO流关闭");
            } catch (Exception e) {
                log.error("IO流关闭", e);
            }
        }
        log.info("分片结束");
        //告诉java虚拟机去回收垃圾 至于什么时候回收  这个取决于 虚拟机的决定
        System.gc();
        //等待100毫秒 等待垃圾回收去 回收完垃圾
        Thread.sleep(100);
        log.info("删除分片开始");
        for (int i = 0; i < shardTotal; i++) {
            String filePath = basePath + path + "." + (i + 1);
            File file = new File(filePath);
            boolean result = file.delete();
            log.info("删除{}，{}", filePath, result ? "成功" : "失败");
        }
        log.info("删除分片结束");
    }

}