package com.yun.note.pojo;


import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

//最近使用的文件--实体类
@Table("z_file_recently_use")  //table注解  设置实体类与表名的映射关系
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileRecentlyUse {
    private Integer id;  //编号
    private String title; //标题
    @Column("update_time")
    private Date updateTime; // 最后修改时间
    @Column("u_id")
    private Integer userId;  //小记所属用户编号
    private Integer type;  //类别【1：笔记， 2：小记】
}
