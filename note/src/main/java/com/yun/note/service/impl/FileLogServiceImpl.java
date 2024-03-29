package com.yun.note.service.impl;


import com.yun.note.exception.ServiceException;
import com.yun.note.exception.ServiceRollbackException;
import com.yun.note.service.IFileLogService;
import com.yun.note.util.code.EventCode;
import com.yun.note.dao.IFileLogDao;
import com.yun.note.pojo.FileLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


//  文件日志业务的实现层
@Service
public class FileLogServiceImpl implements IFileLogService {

    @Autowired
    private IFileLogDao fileLogDao;  //笔记小记日志的数据库接口


    /**
     * 新增一个笔记小记日志记录
     *
     * @param log        日记对象
     * @param isRollback 是否需要回滚
     * @throws ServiceException 回滚异常/ 回滚业务异常
     */
    @Override
    public void addOneLog(FileLog log, boolean isRollback) throws ServiceException {
        int count = 0;
        try {
            count = fileLogDao.insert(log);
        } catch (Exception e) {
            e.printStackTrace();
            if (isRollback){
                throw new ServiceRollbackException(log.getDesc() + "失败！", EventCode.LOG_CREATE_EXCEPTION);
            }else {
                throw new ServiceException(log.getDesc() + "失败！", EventCode.LOG_CREATE_EXCEPTION);
            }

        }

        if (count != 1){
            if(isRollback){
                throw new ServiceRollbackException(log.getDesc() + "失败", EventCode.LOG_CREATE_ERROR);
            } else {
                throw new ServiceException(log.getDesc() + "失败！", EventCode.LOG_CREATE_ERROR);
            }

        }
    }

    /**
     * 新增多个笔记小记日志记录
     *
     * @param logs       日记对象集合
     * @param isRollback 是否需要回滚
     * @throws ServiceException 回滚异常/ 回滚业务异常
     */
    @Override
    public void addMoreLog(List<FileLog> logs, boolean isRollback) throws ServiceException {
        int count = 0;
        try {
            count = fileLogDao.insertBatch(logs);
        } catch (Exception e) {
            e.printStackTrace();
            if (isRollback){
                throw new ServiceRollbackException("删除失败！", EventCode.LOG_CREATE_EXCEPTION);
            }else {
                throw new ServiceException("删除失败！", EventCode.LOG_CREATE_EXCEPTION);
            }

        }

        if (count != logs.size()){
            if(isRollback){
                throw new ServiceRollbackException("删除失败！", EventCode.LOG_CREATE_ERROR);
            } else {
                throw new ServiceException("删除失败！", EventCode.LOG_CREATE_ERROR);
            }

        }
    }
}
