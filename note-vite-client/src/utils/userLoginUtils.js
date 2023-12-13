

import {userLoginModalStore} from "@/stores/loginModalStore.js"

// 用户的共享数据对象
import {useUserStore} from "@/stores/userStore.js";


// 获取本地存储中的userToken 的值。如果没有显示登录窗口
export const getUserToken = () => {

    // 获取本地存储中的userToken 的值
    const token = localStorage.getItem('userToken');

    if(token === null){
        // 未登录
        const {changeLoginModalShowStatus} = userLoginModalStore();  //改变登录窗口显示的函数
        changeLoginModalShowStatus(true);
        throw "未登录";
    }else {
        return token;
    }
}

// 登录失效，是否显示登录窗口
export const loginInvalid = show => {
    // 用户共享的数据清空
    const {resetUserInfo} = useUserStore();
    resetUserInfo();
    // userToken 本地存储删除
    localStorage.removeItem('userToken');
    
    if(show){
        // 未登录
        const {changeLoginModalShowStatus} = userLoginModalStore();  //改变登录窗口显示的函数
        changeLoginModalShowStatus(true); //弹出登录
        throw "未登录";
    }
}