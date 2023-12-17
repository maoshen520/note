package com.zhike.note.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.zhike.note.dao.INoteThingLogDao;
import com.zhike.note.dao.IThingDao;
import com.zhike.note.exception.ServiceException;
import com.zhike.note.exception.ServiceRollbackException;
import com.zhike.note.pojo.NoteThingLog;
import com.zhike.note.pojo.Thing;

import com.zhike.note.service.IThingService;

import com.zhike.note.util.code.EventCode;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.zhike.note.pojo.table.Tables.THING;

import java.util.Date;
import java.util.List;


/*
* 小记业务的实现层
*
* */
@Component
@Service
@Transactional(rollbackFor = {ServiceRollbackException.class})   //只要下面的方法抛出错误，回滚事务
public class ThingServiceImpl implements IThingService {

    @Autowired
    private IThingDao thingDao;  //小记的数据库接口

    @Autowired
    private INoteThingLogDao noteThingLogDao;  //笔记小记日志的数据库接口

    /**
     * 获取用户正常的小记
     *
     * @param userId
     * @return 小记对象集合
     * @throws ServiceException 业务异常
     */
    @Override
    public List<Thing> getUserNormalThing(int userId) throws ServiceException {

        // WHERE `u_id` = ? AND `status` = 1 ORDER BY `finished`, `top` desc, `update_time` desc
        QueryWrapper wrapper = QueryWrapper.create()
                .select(THING.ID, THING.TITLE, THING.TOP, THING.TAGS, THING.UPDATE_TIME, THING.FINISHED)
                .where(THING.USER_ID.eq(userId))
                .and(THING.STATUS.eq(1))
                .orderBy(THING.FINISHED.asc(), THING.TOP.desc(), THING.UPDATE_TIME.desc());

        //根据条件查询用户小记
        try {
            return thingDao.selectListByQuery(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("小记列表服务器异常", EventCode.SELECT_EXCEPTION);
        }
    }

    /**
     * 置顶小记（取消置顶小记）
     *
     * @param isTop   是否置顶小记
     * @param thingId 小记编号
     * @param userId  用户编号
     * @throws ServiceException
     */
    @Override
    public void topThing(boolean isTop, int thingId, int userId) throws ServiceException {

        String desc = "置顶小记";  //事件描述
        String eventSuccess = EventCode.THING_tOP_SUCCESS;
        String eventFailed = EventCode.THING_tOP_FAILED;
        int beforeTop = 0;  //修改之前的top 字段值
        int afterTop = 1;  //修改之后的top 字段值
        if(!isTop){
            desc = "取消置顶";
            eventSuccess = EventCode.THING_CANCET_tOP_SUCCESS;
            eventFailed = EventCode.THING_CANCET_tOP_FAILED;
            beforeTop = 1;
            afterTop = 0;
        }

        //封装修改的条件  WHERE `id` = ? AND `u_id` = ? AND `status` = 1 AND `top` = 1/0
        QueryWrapper wrapper = QueryWrapper.create()
                .where(THING.ID.eq(thingId))
                .and(THING.USER_ID.eq(userId))
                .and(THING.STATUS.eq(1))
                .and(THING.TOP.eq(beforeTop));

        //封装修改的字段 top
        Thing thing  = Thing.builder().top(afterTop).build();

        //根据 wrapper条件来修改小记的 top 字段
        int count = 0;
        try {
            count = thingDao.updateByQuery(thing, wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(desc + "小记服务异常", EventCode.UPDATE_EXCEPTION);
        }

        if (count != 1){
            throw new ServiceRollbackException(desc + "小记服务异常", eventFailed);
        }

        Date localTime = new Date();  //时间
        //新增小记日志记录（置顶业务）
        NoteThingLog log = NoteThingLog.builder()
                .time(localTime)
                .event(eventSuccess)
                .desc(desc)
                .thingId(thingId)
                .userId(userId)
                .build();

        try {
            count = noteThingLogDao.insert(log);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceRollbackException(desc + "小记服务异常", EventCode.INSERT_EXCEPTION);
        }

        if (count != 1){
            throw new ServiceRollbackException(desc + "小记服务异常", EventCode.INSERT_ERROR);
        }



    }

    /**
     *   删除小记或彻底删除小记
     *
     * @param complete     是否彻底删除
     * @param thingId      小记编号
     * @param userId       用户编号
     * @param isRecycleBin 是否是回收站中的操作
     * @throws ServiceException
     */
    @Override
    public void deleteThingById(boolean complete, int thingId, int userId, boolean isRecycleBin) throws ServiceException {

        // 默认为正常删除操作，并不是彻底删除，也不是回收站中的删除
        String desc = "删除小记";
        String eventSuccess = EventCode.THING_DELETE_SUCCESS;
        int beForeStatus = 1;  //之前的状态
        int afterStatus = 0;  // 删除之后的状态

        if (complete){  //彻底删除
            afterStatus = -1;
            desc = "彻底删除小记";
            eventSuccess = EventCode.THING_COMPLETE_DELETE_SUCCESS;
            if(isRecycleBin){  //回收站中只能是彻底删除（因为都是已删除的）
                beForeStatus = 0;
            }
        }

        //封装修改条件  WHERE `id` = ? AND `u_id` = ? AND `status` = ?(1/0)
        QueryWrapper wrapper = QueryWrapper.create()
                .where(THING.ID.eq(thingId))
                .and(THING.USER_ID.eq(userId))
                .and(THING.STATUS.eq(beForeStatus));

        Date localTime = new Date();

        //要修改的哪些字段  status
        Thing thing = Thing.builder()
                .status(afterStatus)
                .updateTime(localTime)
                .build();

        int count = 0;
        try {
            count = thingDao.updateByQuery(thing, wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(desc + "失败", EventCode.UPDATE_EXCEPTION);
        }
        if(count != 1){
            throw new ServiceRollbackException(desc + "失败", EventCode.UPDATE_ERROR);
        }

        //新增小记日志记录（删除业务）
        NoteThingLog log = NoteThingLog.builder()
                .time(localTime)
                .event(eventSuccess)
                .desc(desc)
                .thingId(thingId)
                .userId(userId)
                .build();

        try {
            count = noteThingLogDao.insert(log);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceRollbackException(desc + "失败", EventCode.INSERT_EXCEPTION);
        }

        if (count != 1){
            throw new ServiceRollbackException(desc + "失败", EventCode.INSERT_ERROR);
        }
    }

}
