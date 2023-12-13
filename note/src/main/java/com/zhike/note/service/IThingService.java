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

}
