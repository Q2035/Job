package top.hellooooo.job.mapper;

import top.hellooooo.job.pojo.FileDTO;

import java.util.List;

/**
 * @Author Q
 * @Date 2021-01-18 12:25
 * @Description
 */
public interface FileMapper {

    /**
     * 将上传文件信息插入数据库
     * @param fileDTO
     */
    void insertFileDTO(FileDTO fileDTO);

    /**
     * 更新上传文件信息
     * @param fileDTO
     */
    void updateFileDTO(FileDTO fileDTO);

    /**
     * 根据文件标识查看数据库中是否存在该文件
     * @param fileKey
     */
    List<FileDTO> getFileDTO(String fileKey);
}
