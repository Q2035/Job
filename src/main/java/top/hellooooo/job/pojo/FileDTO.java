package top.hellooooo.job.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {
    /**
    * id
    */
    private Integer id;

    /**
     * 上传者
     */
    private Integer userId;

    /**
    * 相对路径
    */
    private String path;

    /**
    * 文件名
    */
    private String name;

    /**
    * 后缀
    */
    private String suffix;

    /**
    * 大小|字节B
    */
    private Long size;


    /**
    * 创建时间
    */
    private Long createdAt;

    /**
    * 修改时间
    */
    private Long updatedAt;

    /**
    * 已上传分片
    */
    private Long shardIndex;

    /**
    * 分片大小|B
    */
    private Long shardSize;

    /**
    * 分片总数
    */
    private Long shardTotal;

    /**
    * 文件标识
    */
    private String fileKey;

}