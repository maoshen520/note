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

//用户类--实体类
@Table("z_user")  //table注解  设置实体类与表名的映射关系
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id(keyType = KeyType.Auto)  //id注解
    private Integer id;  //用户编号
    private String email;
    private String password;
    private String nickname;
    @Column("head_pic")
    private String headPic;
    @Column(onInsertValue = "0")
    private Integer level;
    private Date time; // 注册时间
    @Column(onInsertValue = "1")
    private Integer status;

}
