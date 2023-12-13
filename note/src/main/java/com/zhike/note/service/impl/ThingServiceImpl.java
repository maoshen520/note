package com.zhike.note.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.zhike.note.dao.IThingDao;
import com.zhike.note.exception.ServiceException;
import com.zhike.note.exception.ServiceRollbackException;
import com.zhike.note.pojo.Thing;

import com.zhike.note.service.IThingService;

import com.zhike.note.util.code.EventCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.zhike.note.pojo.table.Tables.THING;

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
}
