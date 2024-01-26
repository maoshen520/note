package com.yun.note.dao;

import com.mybatisflex.core.BaseMapper;
import com.yun.note.pojo.FileDumpster;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


//关于文件回收站的数据库接口
@Mapper
@Repository
public interface IFileDumpsterDao extends BaseMapper<FileDumpster> {
}
