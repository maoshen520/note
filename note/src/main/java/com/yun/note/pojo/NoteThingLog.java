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
@Table("z_note_thing_log")  //设置实体类与表名的映射关系
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteThingLog {
    @Id(keyType = KeyType.Auto) //id注解
    private Integer id;  //日志编号
    private Date time; // 时间
    private String event;  //  事件码
    private String desc;  //描述
    @Column("u_id")
    private Integer userId;   //日志所对应的用户编号
    @Column("n_id")
    private Integer noteId;   //日志所对应的笔记编号
    @Column("t_id")
    private Integer thingId;   //日志所对应的小记编号
}
