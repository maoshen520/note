package com.yun.note.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.yun.note.dao.INoteDao;
import com.yun.note.exception.ServiceException;
import com.yun.note.exception.ServiceRollbackException;
import com.yun.note.pojo.table.Tables;
import com.yun.note.service.IFileLogService;
import com.yun.note.util.code.EventCode;
import com.yun.note.pojo.Note;
import com.yun.note.pojo.FileLog;
import com.yun.note.service.INoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 笔记的业务实现类
 */

@Service
public class NoteServiceImpl implements INoteService {

    @Autowired
    private INoteDao noteDao;   //笔记的数据库接口
    @Autowired
    private IFileLogService fileLogService;  //笔记小记日志的数据库接口




    /**
     * 获取用户正常的笔记
     * @param userId  用户id
     * @return  笔记列表
     * @throws ServiceException   业务异常
     */
    @Override
    public List<Note> getUserNormalNotes(int userId) throws ServiceException {

        //查询字段：编号，标题，内容，是否置顶，最后操作时间
        //排序规则： 置顶靠前，最后操作时间距离当前时间最近靠前
        QueryWrapper wrapper = QueryWrapper.create()
                .select(Tables.NOTE.ID, Tables.NOTE.TITLE, Tables.NOTE.BODY, Tables.NOTE.TOP, Tables.NOTE.UPDATE_TIME)
                .and(Tables.NOTE.STATUS.eq(1))
                .and(Tables.NOTE.USER_ID.eq(userId))
                .orderBy(Tables.NOTE.TOP.desc(), Tables.NOTE.UPDATE_TIME.desc());

        //查询用户的笔记列表
        List<Note> notes = null;
        try {
            notes = noteDao.selectListByQuery(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("获取笔记失败", EventCode.SELECT_EXCEPTION);
        }

        return notes;
    }

    /**
     * 置顶笔记（取消置顶笔记）
     *
     * @param isTop  是否置顶笔记
     * @param noteId 笔记编号
     * @param userId 用户编号
     * @throws ServiceException 业务异常
     */
    @Override
    public void topNote(boolean isTop, int noteId, int userId) throws ServiceException {
        String desc = "置顶笔记";  //事件描述
        String eventSuccess = EventCode.NOTE_tOP_SUCCESS;
        String eventFailed = EventCode.NOTE_tOP_FAILED;
        int beforeTop = 0;  //修改之前的top 字段值
        int afterTop = 1;  //修改之后的top 字段值
        if(!isTop){
            desc = "取消置顶笔记";
            eventSuccess = EventCode.NOTE_CANCET_tOP_SUCCESS;
            eventFailed = EventCode.NOTE_CANCET_tOP_FAILED;
            beforeTop = 1;
            afterTop = 0;
        }

        //封装修改的条件  WHERE `id` = ? AND `u_id` = ? AND `status` = 1 AND `top` = 1/0
        QueryWrapper wrapper = QueryWrapper.create()
                .where(Tables.NOTE.ID.eq(noteId))
                .and(Tables.NOTE.USER_ID.eq(userId))
                .and(Tables.NOTE.STATUS.eq(1))
                .and(Tables.NOTE.TOP.eq(beforeTop));

        //封装修改的字段 top
        Note note  = Note.builder().top(afterTop).build();

        //根据 wrapper条件来修改笔记的 top 字段
        int count = 0;
        try {
            count = noteDao.updateByQuery(note, wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(desc + "服务异常", EventCode.UPDATE_EXCEPTION);
        }

        if (count != 1){
            throw new ServiceRollbackException(desc + "服务异常", eventFailed);
        }

        Date localTime = new Date();  //时间
        //新增笔记日志记录（置顶业务）
//        FileLog log = FileLog.builder()
//                .time(localTime)
//                .event(eventSuccess)
//                .desc(desc)
//                .noteId(noteId)
//                .userId(userId)
//                .build();
        FileLog log = new FileLog(
                localTime,
                userId,
                noteId,
                1,
                eventSuccess,
                desc
        );
        fileLogService.addOneLog(log,true);
    }

    /**
     * 删除笔记（删除或彻底删除笔记）
     *
     * @param complete     是否彻底删除
     * @param noteId       笔记编号
     * @param userId       用户编号
     * @param isRecycleBin 是否是回收站中的操作
     * @throws ServiceException 业务异常
     */
    @Override
    public void deleteNoteById(boolean complete, int noteId, int userId, boolean isRecycleBin) throws ServiceException {
        // 默认为正常删除操作，并不是彻底删除，也不是回收站中的删除
        String desc = "删除笔记";
        String eventSuccess = EventCode.NOTE_DELETE_SUCCESS;
        int beForeStatus = 1;  //之前的状态
        int afterStatus = 0;  // 删除之后的状态

        if (complete){  //彻底删除
            afterStatus = -1;
            desc = "彻底删除笔记";
            eventSuccess = EventCode.NOTE_COMPLETE_DELETE_SUCCESS;
            if(isRecycleBin){  //回收站中只能是彻底删除（因为都是已删除的）
                beForeStatus = 0;
            }
        }

        //封装修改条件  WHERE `id` = ? AND `u_id` = ? AND `status` = ?(1/0)
        QueryWrapper wrapper = QueryWrapper.create()
                .where(Tables.NOTE.ID.eq(noteId))
                .and(Tables.NOTE.USER_ID.eq(userId))
                .and(Tables.NOTE.STATUS.eq(beForeStatus));

        Date localTime = new Date();

        //要修改的哪些字段  status
        Note note = Note.builder()
                .status(afterStatus)
                .updateTime(localTime)
                .build();

        int count = 0;
        try {
            count = noteDao.updateByQuery(note, wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(desc + "失败", EventCode.UPDATE_EXCEPTION);
        }
        if(count != 1){
            throw new ServiceRollbackException(desc + "失败", EventCode.UPDATE_ERROR);
        }

        //新增小记日志记录（删除业务）
//        FileLog log = FileLog.builder()
//                .time(localTime)
//                .event(eventSuccess)
//                .desc(desc)
//                .noteId(noteId)
//                .userId(userId)
//                .build();
        FileLog log = new FileLog(
                localTime,
                userId,
                noteId,
                1,
                eventSuccess,
                desc
        );

        fileLogService.addOneLog(log,true);
    }

    /**
     * 创建笔记（并且初始化）
     *
     * @param userId
     * @return
     * @throws ServiceException
     */
    @Override
    public int createNoteInit(int userId) throws ServiceException {

        Date localTime = new Date();

        //新增笔记对象
        Note note = Note.builder()
                .time(localTime)
                .updateTime(localTime)
                .userId(userId)
                .build();

        int count = 0;
        try {
            count = noteDao.insert(note);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("新建笔记失败", EventCode.INSERT_EXCEPTION);
        }

        if(count != 1){
            throw new ServiceRollbackException("新建笔记失败", EventCode.INSERT_ERROR);
        }

        //新增笔记的日志对象
//        FileLog log = FileLog.builder()
//                .noteId(note.getId())
//                .time(localTime)
//                .event(EventCode.NOTE_CREATE_SUCCESS)
//                .desc("新增笔记")
//                .userId(userId)
//                .build();
        FileLog log = new FileLog(
                localTime,
                userId,
                note.getId(),
                1,
                EventCode.NOTE_CREATE_SUCCESS,
                "新增笔记"
        );

        //新增笔记日志
        fileLogService.addOneLog(log, true);

        //返回新增笔记的编号
        return note.getId();
    }

    /**
     * 获取笔记信息
     *
     * @param noteId 笔记编号
     * @param userId 用户编号
     * @throws ServiceException 业务异常
     */
    @Override
    public Note getEditNote(int noteId, int userId) throws ServiceException {

        QueryWrapper wrapper = QueryWrapper.create()
                .select(Tables.NOTE.UPDATE_TIME, Tables.NOTE.TITLE, Tables.NOTE.CONTENT)
                .where(Tables.NOTE.STATUS.eq(1))
                .and(Tables.NOTE.ID.eq(noteId))
                .and(Tables.NOTE.USER_ID.eq(userId));

        Note note = null;
        try {
            note = noteDao.selectOneByQuery(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("笔记获取失败，请稍后再试", EventCode.SELECT_EXCEPTION);
        }

        if(note == null){
            throw new ServiceException("笔记不存在，请稍后再试！", EventCode.SELECT_NONE);
        }

        return note;
    }

    /**
     * 保存正在编辑的笔记
     *
     * @param noteId  笔记编号
     * @param userId  用户编号
     * @param title   笔记标题
     * @param body    笔记内容
     * @param content 笔记内容（完整的）
     * @throws ServiceException 业务异常
     */
    @Override
    public Date saveEditingNote(int noteId, int userId, String title, String body, String content) throws ServiceException {
        //修改记录的条件
        QueryWrapper wrapper = QueryWrapper.create()
                .where(Tables.NOTE.ID.eq(noteId))
                .and(Tables.NOTE.USER_ID.eq(userId))
                .and(Tables.NOTE.STATUS.eq(1));

        Date localTime = new Date();  //时间

        //修改的字段
        Note note = Note.builder()
                .title(title)
                .body(body)
                .content(content)
                .updateTime(localTime)
                .build();

        int count = 0;
        try {
            count = noteDao.updateByQuery(note, wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("保存失败", EventCode.UPDATE_EXCEPTION);
        }

        if(count != 1){
            throw new ServiceRollbackException("保存失败", EventCode.UPDATE_ERROR);
        }

        //添加保存笔记的日志
//        FileLog log = FileLog.builder()
//                .userId(userId)
//                .desc("保存笔记")
//                .event(EventCode.NOTE_UPDATE_SUCCESS)
//                .time(localTime)
//                .noteId(noteId)
//                .build();
        FileLog log = new FileLog(
                localTime,
                userId,
                noteId,
                1,
                EventCode.NOTE_UPDATE_SUCCESS,
                "保存笔记"
        );
        fileLogService.addOneLog(log, true);

        return localTime;
    }
}
