package com.yun.note.pojo;


import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

//笔记小记日志类---实体类
@Table("z_file_log")  //设置实体类与表名的映射关系
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileLog {
    @Id(keyType = KeyType.Auto) //id注解
    private Integer id;  //日志编号
    private Date time; // 时间
    private String event;  //  事件码
    private String desc;  //描述
    @Column("u_id")
    private Integer userId;   //日志所对应的用户编号
    @Column("f_id")
    private Integer fileId;   //文件编号
    @Column("f_type")
    private Integer fileType;   //文件类型


    public FileLog(Date time, Integer userId, Integer fileId, Integer fileType, String event, String desc) {
        this.time = time;
        this.userId = userId;
        this.fileId = fileId;
        this.fileType = fileType;
        this.event = event;
        this.desc = desc;
    }
}


