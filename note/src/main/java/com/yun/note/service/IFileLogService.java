package com.yun.note.service;

import com.yun.note.exception.ServiceException;
import com.yun.note.pojo.FileLog;

import java.util.List;

//  小记日志业务
public interface IFileLogService {


    /**
     * 新增一个笔记小记日志记录
     * @param log   日记对象
     * @param isRollback   是否需要回滚
     * @throws ServiceException  回滚异常/ 回滚业务异常
     */
    void addOneLog(FileLog log, boolean isRollback) throws ServiceException;


    /**
     * 新增多个笔记小记日志记录
     * @param logs   日记对象集合
     * @param isRollback   是否需要回滚
     * @throws ServiceException  回滚异常/ 回滚业务异常
     */
    void addMoreLog(List<FileLog> logs, boolean isRollback) throws ServiceException;


}
