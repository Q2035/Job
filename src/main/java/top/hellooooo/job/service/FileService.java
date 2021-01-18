package top.hellooooo.job.service;

import top.hellooooo.job.pojo.FileDTO;

import java.util.List;

/**
 * @Author Q
 * @Date 2021-01-18 12:27
 * @Description
 */
public interface FileService {

    /**
     * 根据提供的key查看数据库中是否存在插入信息
     * @param key
     * @return
     */
    List<FileDTO> check(String key);

    /**
     * 将文件信息插入数据库
     * @param fileDTO
     */
    void save(FileDTO fileDTO);
}
