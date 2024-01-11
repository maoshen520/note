package com.yun.note.dao;

import com.mybatisflex.core.BaseMapper;
import com.yun.note.pojo.Thing;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

//关于小记的数据库接口
@Mapper
@Repository
public interface IThingDao extends BaseMapper<Thing> {
}
