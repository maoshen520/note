package com.zhike.note.service;

import com.zhike.note.pojo.NoteThingLog;

import javax.sql.rowset.serial.SerialException;

//  小记日志业务
public interface INoteThingLogService {


    /**
     * 新增一个笔记小记日志记录
     * @param log   日记对象
     * @param isRollback   是否需要回滚
     * @throws SerialException   回滚异常/ 回滚业务异常
     */
    void addOneLog(NoteThingLog log, boolean isRollback) throws SerialException;


}
