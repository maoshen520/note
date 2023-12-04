package com.zhike.note.pojo;


import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

//用户类日志类---实体类
@Table("z_user_log")  //设置实体类与表名的映射关系
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLog {
    @Id(keyType = KeyType.Auto) //id注解
    private Integer id;  //用户编号
    private String desc;  //描述
    private Date time; // 注册时间
    private String event;  //  事件码
    @Column("user_id")
    private Integer userId;   //日志所对应的用户编号

}
