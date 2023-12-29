package com.zhike.note.service;

import com.zhike.note.exception.ServiceException;
import com.zhike.note.pojo.Thing;

import java.util.List;


//业务接口：小记的业务   --去到impl实现业务
public interface IThingService {
    /**
     * 获取用户正常的小记
     * @param userId
     * @return 小记对象集合
     * @throws ServiceException  业务异常
     */
    List<Thing> getUserNormalThing(int userId) throws ServiceException;

    /**
     * 置顶小记（取消置顶小记）
     *
     * @param isTop  是否置顶小记
     * @param thingId  小记编号
     * @param userId  用户编号
     * @throws ServiceException  业务异常
     */
    void topThing(boolean isTop, int thingId, int userId) throws ServiceException;

    /**
     * 删除小记（删除或彻底删除小记）
     *
     * @param complete  是否彻底删除
     * @param thingId  小记编号
     * @param userId  用户编号
     * @param isRecycleBin  是否是回收站中的操作
     * @throws ServiceException  业务异常
     */
    void deleteThingById(boolean complete, int thingId, int userId, boolean isRecycleBin) throws ServiceException;

    /**
     * 新增小记
     * @param thing  小记信息（标题、是否置顶、标签、内容、用户id、是否完成、时间、最后更新时间）
     * @throws ServiceException  //业务异常
     */
    void newCreateThing(Thing thing) throws ServiceException;

    /**
     * 获取编辑的小记信息
     *
     * @param thingId  小记编号
     * @param userId  用户编号
     * @throws ServiceException  业务异常
     */
    Thing getEditThing(int thingId, int userId) throws ServiceException;
}
