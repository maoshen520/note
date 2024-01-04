package com.zhike.note.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.zhike.note.dao.INoteDao;
import com.zhike.note.exception.ServiceException;
import com.zhike.note.pojo.Note;
import com.zhike.note.service.INoteService;
import com.zhike.note.util.code.EventCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import static com.zhike.note.pojo.table.Tables.NOTE;

/**
 * 笔记的业务实现类
 */

@Service
public class NoteServiceImpl implements INoteService {

    @Autowired
    private INoteDao noteDao;   //笔记的数据库接口





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
                .select(NOTE.ID, NOTE.TITLE, NOTE.BODY, NOTE.TOP, NOTE.UPDATE_TIME)
                .and(NOTE.STATUS.eq(1))
                .and(NOTE.USER_ID.eq(userId))
                .orderBy(NOTE.TOP.desc(), NOTE.UPDATE_TIME.desc());

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
}
