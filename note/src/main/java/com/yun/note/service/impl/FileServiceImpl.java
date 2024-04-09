package com.yun.note.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.yun.note.dao.IFileRecentlyUseDao;
import com.yun.note.dao.INoteDao;
import com.yun.note.dao.IThingDao;
import com.yun.note.exception.ServiceException;
import com.yun.note.exception.ServiceRollbackException;
import com.yun.note.pojo.*;
import com.yun.note.pojo.table.Tables;
import com.yun.note.service.IFileService;
import com.yun.note.service.IFileLogService;
import com.yun.note.util.code.EventCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.yun.note.pojo.table.Tables.FILE_RECENTLY_USE;

@Service
public class FileServiceImpl implements IFileService {

    @Autowired
    private INoteDao noteDao;   //关于笔记的数据库接口
    @Autowired
    private IThingDao thingDao; //关于小记的数据库接口
    @Autowired
    private IFileLogService fileLogService;  //日志对象
    @Autowired
    private IFileRecentlyUseDao fileRecentlyUseDao;  //最近用户使用的数据库接口



    /**
     * 批量删除文件
     *
     * @param complete 是否彻底删除
     * @param dumpster 是否是回收站中的操作
     * @param u_id     用户编号
     * @param files    需要删除的文件对象集合（{id，type}）
     * @throws ServiceException 业务异常
     */
    @Override
    public void deleteBatch(boolean complete, boolean dumpster, int u_id, List<FileDumpster> files) throws ServiceException {
        // 默认为正常删除操作，并不是彻底删除，也不是回收站中的删除
        String desc = "删除笔记";
        String tdesc = "删除小记";
        String eventSuccess = EventCode.NOTE_DELETE_SUCCESS;
        String eventSuccessT = EventCode.THING_DELETE_SUCCESS;
        int beForeStatus = 1;  //之前的状态
        int afterStatus = 0;  // 删除之后的状态

        if (complete){  //彻底删除
            afterStatus = -1;
            desc = "彻底删除笔记";
            tdesc = "彻底删除小记";
            eventSuccess = EventCode.NOTE_COMPLETE_DELETE_SUCCESS;
            eventSuccessT = EventCode.THING_COMPLETE_DELETE_SUCCESS;
            if(dumpster){  //回收站中只能是彻底删除（因为都是已删除的）
                beForeStatus = 0;
            }
        }

        Date localTime = new Date();

        List<Integer> nidArr = new ArrayList<>();  //笔记的编号集合
        List<Integer> tidArr = new ArrayList<>();  //小记的编号集合
        List<FileLog> logs = new ArrayList<>();  //日志对象的集合
        for(FileDumpster file: files){
            int type;
            String event;
            String descs;

            if(file.getType() == 1){
                nidArr.add(file.getId());

                type = 1;
                event = eventSuccess;
                descs = desc;
            }else{
                tidArr.add(file.getId());

                type = 1;
                event = eventSuccessT;
                descs = tdesc;
            }

            logs.add(new FileLog(
                    localTime,
                    u_id,
                    file.getId(),
                    type,
                    event,
                    descs
            ));
        }

        QueryWrapper wrapper;
        int count = 0;

        // ---------------------------------------删除笔记操作--------------------------------------
        if(nidArr.size() != 0){
            //封装修改条件  WHERE `id` = ? AND `u_id` = ? AND `status` = ?(1/0)
            wrapper = QueryWrapper.create()
                    .where(Tables.NOTE.ID.in(nidArr))
                    .and(Tables.NOTE.USER_ID.eq(u_id))
                    .and(Tables.NOTE.STATUS.eq(beForeStatus));


            //要修改的哪些字段  status
            Note note = Note.builder()
                    .status(afterStatus)
                    .updateTime(localTime)
                    .build();


            try {
                count = noteDao.updateByQuery(note, wrapper);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException(desc + "失败", EventCode.UPDATE_EXCEPTION);
            }
//        if(count != 1){
//            throw new ServiceRollbackException(desc + "失败", EventCode.UPDATE_ERROR);
//        }
        }

        int num = 0;
        // ---------------------------------------删除小记操作--------------------------------------
        if (tidArr.size() != 0){
            //封装修改条件  WHERE `id` = ? AND `u_id` = ? AND `status` = ?(1/0)
            wrapper = QueryWrapper.create()
                    .where(Tables.THING.ID.in(tidArr))
                    .and(Tables.THING.USER_ID.eq(u_id))
                    .and(Tables.THING.STATUS.eq(beForeStatus));


            //要修改的哪些字段  status
            Thing thing = Thing.builder()
                    .status(afterStatus)
                    .updateTime(localTime)
                    .build();

            try {
                num = thingDao.updateByQuery(thing, wrapper);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException(tdesc + "失败", EventCode.UPDATE_EXCEPTION);
            }


        }

        //判断删除的个数是否正确
        if(count + num != files.size()){
            throw new ServiceRollbackException("批量删除失败", EventCode.UPDATE_ERROR);
        }

        //新增小记日志记录（删除业务）
//        NoteThingLog log = NoteThingLog.builder()
//                .time(localTime)
//                .event(eventSuccess)
//                .desc(desc)
//                .noteId(noteId)
//                .userId(userId)
//                .build();
//
//        noteThingLogService.addOneLog(log,true);

        fileLogService.addMoreLog(logs, true);
    }



    /**
     * 回收站单个恢复
     *
     * @param u_id 用户id
     * @param id   小记或笔记id
     * @param type 是小记还是笔记
     * @throws ServiceException
     */
    @Override
    public void restoreOne(int u_id, int id, int type) throws ServiceException {

        QueryWrapper wrapper;
        int count = 0;
        Date localTime = new Date();
        String desc = "恢复笔记";
        String eventSuccess = EventCode.NOTE_RESTORE_SUCCESS;

        if(type == 1){  //笔记
            wrapper = QueryWrapper.create()
                    .where(Tables.NOTE.ID.eq(id))
                    .and(Tables.NOTE.USER_ID.eq(u_id));

            //要修改的哪些字段  status
            Note note = Note.builder()
                    .status(1)
                    .updateTime(localTime)
                    .build();

            try {
                count = noteDao.updateByQuery(note, wrapper);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("恢复笔记失败", EventCode.UPDATE_EXCEPTION);
            }
        }else {
            desc = "恢复小记";
            eventSuccess = EventCode.THING_RESTORE_SUCCESS;

            wrapper = QueryWrapper.create()
                    .where(Tables.THING.ID.eq(id))
                    .and(Tables.THING.USER_ID.eq(u_id));

            //要修改的哪些字段  status
            Thing thing = Thing.builder()
                    .status(1)
                    .updateTime(localTime)
                    .build();

            try {
                count = thingDao.updateByQuery(thing, wrapper);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("恢复小记失败", EventCode.UPDATE_EXCEPTION);
            }

        }


        FileLog log = new FileLog(
                localTime,
                u_id,
                id,
                1,
                eventSuccess,
                desc
        );

        fileLogService.addOneLog(log,true);
    }

    /**
     * 获取用户最近使用的文件
     *
     * @param u_id 用户编号
     * @throws ServiceException 业务异常
     */
    @Override
    public List<FileRecentlyUse> getRecentlyUseFiles(int u_id) throws ServiceException {
        // 查询的条件
        QueryWrapper wrapper = QueryWrapper.create()
                .select(FILE_RECENTLY_USE.ID, FILE_RECENTLY_USE.TITLE, FILE_RECENTLY_USE.UPDATE_TIME, FILE_RECENTLY_USE.TYPE)
                .where(FILE_RECENTLY_USE.USER_ID.eq(u_id))
                .orderBy(FILE_RECENTLY_USE.UPDATE_TIME.desc());

        try {
            return fileRecentlyUseDao.selectListByQuery(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("获取最近操作文件失败", EventCode.SELECT_ERROR);
        }

    }
}
