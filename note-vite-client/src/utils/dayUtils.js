import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';
import relativeTime from 'dayjs/plugin/relativeTime';

dayjs.locale('zh-cn'); //中文语言包

//安装插件--相对时间插件
dayjs.extend(relativeTime);

/**
 * 日期距离现在多久（1分钟前....）
 * @param {*} date  日期
 * @returns 
 */
export const fromNow = (date) => {
    const timeStr = dayjs(date).fromNow();
    return timeStr === '几秒前' ? '刚刚' : timeStr;
}

