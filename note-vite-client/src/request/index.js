import axios from "axios";
import qs from 'qs'

/**
 * 请求时处理
 * @param {*} config 配置
 */
const request = config => {
    // 加载条开始
    // 判断是否需要请求头中的userToken 参数
    // 是否
}


//自定义的请求对象
export const serverRequest = axios.create({
    baseURL:'/note-serve'
})

// 请求拦截器
serverRequest.interceptors.request.use(
    config => {

    }, 
    err => {

    }
)

// 响应拦截器
serverRequest.interceptors.response.use(
    response => {

    }, 
    err => {

    }
)

// 导出请求对象
export default serverRequest