package com.zhike.note.service.impl;


import com.zhike.note.dao.INoteThingLogDao;
import com.zhike.note.exception.ServiceRollbackException;
import com.zhike.note.pojo.NoteThingLog;
import com.zhike.note.service.INoteThingLogService;
import com.zhike.note.util.code.EventCode;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.rowset.serial.SerialException;

//  笔记小记日志业务的实现层
public class NoteThingLogServiceImpl implements INoteThingLogService {

    @Autowired
    private INoteThingLogDao noteThingLogDao;  //笔记小记日志的数据库接口

    /**
     * @param log        日记对象
     * @param isRollback 是否需要回滚
     * @throws SerialException
     */
    @Override
    public void addOneLog(NoteThingLog log, boolean isRollback) throws SerialException {

        int count = 0;
        try {
            count = noteThingLogDao.insert(log);
        } catch (Exception e) {
            e.printStackTrace();
            if (isRollback){
                throw new ServiceRollbackException(log.getDesc() + "失败！", EventCode.INSERT_EXCEPTION);
            }else {
                throw new SerialException(log.getDesc() + "失败！", EventCode.INSERT_EXCEPTION);
            }

        }

        if (count != 1){
            if(isRollback){
                throw new ServiceRollbackException("修改小记失败", EventCode.INSERT_ERROR);
            } else {
                throw new SerialException(log.getDesc() + "失败！", EventCode.INSERT_EXCEPTION);
            }

        }
    }
}
