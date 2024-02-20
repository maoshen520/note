package com.yun.note.service;

import com.yun.note.exception.ServiceException;
import com.yun.note.pojo.FileDumpster;

import java.util.List;


//关于文件的业务接口
public interface IFileService {


    /**
     * 批量删除文件
     *
     * @param complete  是否彻底删除
     * @param u_id  用户编号
     * @param dumpster  是否是回收站中的操作
     * @param files  需要删除的文件对象集合（{id，type}）
     * @throws ServiceException  业务异常
     */
    void deleteBatch(boolean complete, boolean dumpster, int u_id, List<FileDumpster> files) throws ServiceException;

    /**
     * 回收站单个恢复
     * @param u_id  用户id
     * @param id  小记或笔记id
     * @param type  是小记还是笔记
     * @throws ServiceException
     */
    void restoreOne(int u_id, int id, int type) throws ServiceException;


}
