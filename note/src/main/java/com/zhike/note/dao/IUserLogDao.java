package com.zhike.note.dao;

import com.mybatisflex.core.BaseMapper;
import com.zhike.note.pojo.UserLog;
//import org.mapstruct.Mapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

//关于用户日志的数据库接口

//注解？为何要写
@Mapper
@Repository
public interface IUserLogDao extends BaseMapper<UserLog> {
}
