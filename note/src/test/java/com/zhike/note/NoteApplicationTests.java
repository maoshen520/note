package com.zhike.note;

import cn.hutool.extra.mail.MailUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.zhike.note.dao.IUserDao;
import com.zhike.note.exception.ServiceException;
import com.zhike.note.pojo.User;
import com.zhike.note.service.IMailservice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static com.zhike.note.pojo.table.Tables.USER;

@SpringBootTest
class NoteApplicationTests {

	@Autowired
	private IUserDao userDao;  //关于用户的数据库接口

	@Autowired
	private IMailservice mailservice; //

	@Test
	void contextLoads() throws ServiceException {

//		测试查询邮箱数据
//		QueryWrapper wrapper = QueryWrapper.create()
//				.where(USER.EMAIL.eq("315105654@qq.com"));
//
//		User user = userDao.selectOneByQuery(wrapper);
//		System.out.println(user);

//		测试发送邮箱
//		MailUtil.send(
//				"535179400@qq.com",  //收件人
//				"测试邮箱发送标题",         //标题
//				"测试邮箱发送<b>内容</b>",   //内容
//				true  //是否为html格式解析
//		);

		String emailRegisterVC = mailservice.getEmailRegisterVC("535179400@qq.com");
		System.out.println(emailRegisterVC);

	}

}
