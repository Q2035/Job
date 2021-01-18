package top.hellooooo.job.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hellooooo.job.mapper.FileMapper;
import top.hellooooo.job.pojo.FileDTO;
import top.hellooooo.job.service.FileService;

import java.util.List;

/**
 * @Author Q
 * @Date 2021-01-17 11:19
 * @Description
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileMapper fileMapper;

    /**
     * 进行文件保存
     *
     * @param fileDTO
     */
    @Override
    public void save(FileDTO fileDTO) {
        //根据 数据库的 文件标识来查询 当前视频 是否存在
        List<FileDTO> fileDTOS = fileMapper.getFileDTO(fileDTO.getFileKey());
        //如果存在就话就修改
        if (fileDTOS.size() != 0) {
            //根据key来修改
            fileMapper.updateFileDTO(fileDTO);
        } else {
            //不存在就添加
            fileMapper.insertFileDTO(fileDTO);
        }
    }

    /**
     * 检测数据库中是否存在该文件
     *
     * @param key
     * @return
     */
    @Override
    public List<FileDTO> check(String key) {
        List<FileDTO> dtos = fileMapper.getFileDTO(key);
        return dtos;
    }

}
