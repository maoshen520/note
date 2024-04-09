package com.yun.note.dao;

import com.mybatisflex.core.BaseMapper;
import com.yun.note.pojo.FileDumpster;
import com.yun.note.pojo.FileRecentlyUse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


//关于最近使用文件的数据库接口
@Mapper
@Repository
public interface IFileRecentlyUseDao extends BaseMapper<FileRecentlyUse> {
}
