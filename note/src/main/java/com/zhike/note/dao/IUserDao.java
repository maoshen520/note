package com.zhike.note.dao;

import com.mybatisflex.core.BaseMapper;
import com.zhike.note.pojo.User;
//import org.mapstruct.Mapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

//关于用户的数据库接口

//注解？为何要写
@Mapper
@Repository
public interface IUserDao extends BaseMapper<User> {
}
