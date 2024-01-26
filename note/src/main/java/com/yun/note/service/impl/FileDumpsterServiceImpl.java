package com.yun.note.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.yun.note.dao.IFileDumpsterDao;
import com.yun.note.exception.ServiceException;
import com.yun.note.pojo.FileDumpster;
import com.yun.note.pojo.Note;
import com.yun.note.service.IFileDumpsterService;
import com.yun.note.util.code.EventCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.yun.note.pojo.table.Tables.FILE_DUMPSTER;

/**
 * 回收站的业务实现类
 */

@Service
public class FileDumpsterServiceImpl implements IFileDumpsterService {

    @Autowired
    private IFileDumpsterDao fileDumpsterDao;  //回收站中的数据库接口

    /**
     * 获取用户正常的回收站
     *
     * @param userId 回收站列表
     * @return 回收站列表
     * @throws ServiceException
     */
    @Override
    public List<FileDumpster> getUserFiles(int userId) throws ServiceException {

        QueryWrapper wrapper = QueryWrapper.create()
                .select(FILE_DUMPSTER.ID, FILE_DUMPSTER.TITLE, FILE_DUMPSTER.UPDATE_TIME, FILE_DUMPSTER.TYPE)
                .where(FILE_DUMPSTER.USER_ID.eq(userId));

        List<FileDumpster> files = null;
        try {
            files = fileDumpsterDao.selectListByQuery(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("获取回收站列表出错", EventCode.SELECT_EXCEPTION);
        }

        return files;
    }
}
