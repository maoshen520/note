package com.yun.note.dao;

import com.mybatisflex.core.BaseMapper;
import com.yun.note.pojo.NoteThingLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

//关于笔记小记的数据库接口

//注解？为何要写
@Mapper
@Repository
public interface INoteThingLogDao extends BaseMapper<NoteThingLog> {
}
