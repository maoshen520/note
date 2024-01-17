import axios from "axios";
import qs from 'qs';
import {getUserToken,loginInvalid} from "@/utils/userLoginUtils.js";

/**
 * 请求时处理
 * @param {*} config 配置
 */
const request = async config => {
    // 加载条开始
    // 判断是否需要请求头中的userToken 参数
    // 是否需要请求体中的参数转成url的参数（一般post请求）
    // 返回最终的配置

    if(config.userPower) config.headers.userToken = await getUserToken();  //记得去@/utils/userLoginUtils.js" 把throw去掉

    // 是否需要请求体中的参数转成url的参数（一般post请求）
    if(config.dataParam) config.transformRequest = [data => qs.stringify(data)];
        
    return config;
}

/**
 * 请求失败
 * @param {*} error  //失败信息
 */
const requestError = error => {
    //加载条异常结束
    // 采用消息块展示失败的原因
    // 返回失败的原因

    ElMessage({
        message: '发送' + error.config.name + '请求失败',
        type: 'error',
    })

    // return Promise.reject(error);
}

/**
 * 响应处理
 * @param {*} response 
 */
const requestResponse = response => {
    //得到服务器中返回的数据
    const responseData = response.data;

    //判断请求未成功
    if(!responseData.success){
        ElMessage({
            message: responseData.message,
            type: 'error'
        });

        // 判断是否登录失效
        // 登录已失效
        if(responseData.code == 'L_008'){
            loginInvalid(true);
        }

        // 将无数据返回调用者
        return null;
    } 

    // 请求成功
    // 判断是否需要弹出请求成功的消息
    if(response.config.successMessage) {
        ElMessage({
            // message: response.config.name +'成功',
            message: responseData.message,
            type: 'success'
        });
    }

    return responseData;
}


//自定义的请求对象
export const serverRequest = axios.create({
    baseURL:'/note-serve'
})

// 请求拦截器
serverRequest.interceptors.request.use(
    config => request(config), 
    error => requestError(error)
)

// 响应拦截器
serverRequest.interceptors.response.use(
    response => requestResponse(response), 
    error => requestError(error)
)

// 导出请求对象
export default serverRequest