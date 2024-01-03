package com.zhike.note.service.impl;

import cn.hutool.core.lang.Validator;
import com.mybatisflex.core.query.QueryWrapper;
import com.zhike.note.dao.INoteThingLogDao;
import com.zhike.note.dao.IThingDao;
import com.zhike.note.exception.ServiceException;
import com.zhike.note.exception.ServiceRollbackException;
import com.zhike.note.pojo.NoteThingLog;
import com.zhike.note.pojo.Thing;

import com.zhike.note.service.INoteThingLogService;
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

//    @Autowired
//    private INoteThingLogDao noteThingLogDao;  //笔记小记日志的数据库接口
    @Autowired
    private INoteThingLogService noteThingLogService;  //笔记小记的业务实现层

    /**
     * 获取用户正常的小记
     * @param search  查询关键词（标题含有或者标签含有）
     * @param filter  过滤【null:默认， 0：只查未完成， 1：只查已完成】
     * @param userId
     * @return 小记对象集合
     * @throws ServiceException 业务异常
     */
    @Override
    public List<Thing> getUserNormalThing(String search, Integer filter, int userId) throws ServiceException {

        // WHERE `u_id` = ? AND `status` = 1 ORDER BY `finished`, `top` desc, `update_time` desc
        QueryWrapper wrapper = QueryWrapper.create()
                .select(THING.ID, THING.TITLE, THING.TOP, THING.TAGS, THING.UPDATE_TIME, THING.FINISHED)
                .where(THING.USER_ID.eq(userId))
                .and(THING.STATUS.eq(1));

        //  是否有关键字查询
        if (Validator.isNotEmpty((search))){
            wrapper.and(
                    THING.TITLE.like(search)
                    .or(THING.TAGS.like(search))
            );
        }

        //是否有过来条件(finished字段)
        if (filter != null && (filter == 0 || filter == 1)) {
            wrapper.and(THING.FINISHED.eq(filter));
        }

        // 排序
        wrapper.orderBy(THING.FINISHED.asc(), THING.TOP.desc(), THING.UPDATE_TIME.desc());

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

        noteThingLogService.addOneLog(log,true);

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

        noteThingLogService.addOneLog(log,true);
    }

    /**
     * 新增小记
     *
     * @param thing 小记信息（标题、是否置顶、标签、内容、用户id、是否完成、时间、最后更新时间）
     * @throws ServiceException //业务异常
     */
    @Override
    public void newCreateThing(Thing thing) throws ServiceException {
        //新增小记
        int count = 0;
        try {
            count = thingDao.insert(thing);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("新增小记失败", EventCode.THING_CREATE_EXCEPTION);
        }
        if (count != 1) {
            throw new ServiceRollbackException("新增小记失败",EventCode.THING_CREATE_FAILED);
        }

        //新增小记日志记录（新增业务）
        NoteThingLog log = NoteThingLog.builder()
                .time(thing.getUpdateTime())
                .event(EventCode.THING_CREATE_SUCCESS)
                .desc("新增小记")
                .thingId(thing.getId())
                .userId(thing.getUserId())
                .build();

        noteThingLogService.addOneLog(log,true);
    }

    /**
     * 获取编辑的小记信息
     *
     * @param thingId 小记编号
     * @param userId  用户编号
     * @throws ServiceException 业务异常
     */
    @Override
    public Thing getEditThing(int thingId, int userId) throws ServiceException {
        //封装查询的条件
        //判断小记是否存在
        //将查到小记返回出去

        QueryWrapper wrapper = QueryWrapper.create()
                .select(THING.TITLE, THING.TOP, THING.TAGS, THING.CONTENT, THING.USER_ID)
                .where(THING.ID.eq(thingId))
                .and(THING.USER_ID.eq(userId))
                .and(THING.STATUS.eq(1));

        Thing thing = null;
        try {
            thing = thingDao.selectOneByQuery(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("查询小记异常", EventCode.SELECT_EXCEPTION);
        }
        if(thing == null){
            throw new ServiceException("小记不存在，请刷新后再试", EventCode.SELECT_NONE);
        }

        return thing;
    }

    /**
     * 修改小记
     * @param thing 小记信息（标题、是否置顶、标签、内容、用户id、是否完成、最后更新时间, id）
     * @throws ServiceException
     */
    @Override
    public void updateThing(Thing thing) throws ServiceException {

        //修改小记的条件
        QueryWrapper wrapper = QueryWrapper.create()
                .where(THING.ID.eq(thing.getId()))
                .and(THING.USER_ID.eq(thing.getUserId()))
                .and(THING.STATUS.eq(1));

        Thing updateColumn = Thing.builder()
                .title(thing.getTitle())
                .tags(thing.getTags())
                .content(thing.getContent())
                .finished(thing.getFinished())
                .top(thing.getTop())
                .updateTime(thing.getUpdateTime())
                .build();

        int count = 0;
        try {
            count = thingDao.updateByQuery(updateColumn, wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("修改失败", EventCode.UPDATE_EXCEPTION);
        }

        if(count != 1){
            throw new ServiceRollbackException("修改失败", EventCode.UPDATE_ERROR);
        }

        //添加修改小记日记
        NoteThingLog log = NoteThingLog.builder()
                .time(thing.getUpdateTime())
                .event(EventCode.THING_UPDATE_SUCCESS)
                .desc("修改小记")
                .thingId(thing.getId())
                .userId(thing.getUserId())
                .build();

        noteThingLogService.addOneLog(log,true);

    }

}
