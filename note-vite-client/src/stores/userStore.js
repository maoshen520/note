import { defineStore } from 'pinia';
import {computed, ref} from 'vue';

// 关于登录模态框的全局状态
export const useUserStore = defineStore(
    "user",  //唯一id至
    () => {
        const id = ref(null);  //编号
        const email = ref(null);  //邮箱
        const nickname = ref(null);  //昵称
        const headPic = ref(null);  //头像
        const level = ref(null);  //等级
        const time = ref(null);  //注册时间

        // 设置头像默认
        let url = "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png";
        const headImage = computed( () => {
            if(headPic.value == undefined || headPic.value === null) {
                return url;
            }else {
                return headPic.value;
            }
        })

        //设置昵称默认-使用邮箱
        const defaultNickname = computed( () => {
            if(nickname.value == undefined || nickname.value === null){
                return email.value;
            }else{
                return nickname.value;
            }
        })

        // 设置用户的信息
        const setUserInfo = (u_id,u_email,u_nickname,u_headPic,u_level,u_time) => {
            id.value = u_id;
            email.value = u_email;
            nickname.value = u_nickname;
            headPic.value = u_headPic;
            level.value = u_level;
            time.value = u_time;
        }
        

        //重置用户信息
        const resetUserInfo = () => {
            id.value = null;
            email.value = null;
            nickname.value = null;
            headPic.value = null;
            level.value = null;
            time.value = null;
        }

        // 暴露出去
        return {
            id,
            email,
            nickname,
            headPic,
            level,
            time,
            setUserInfo,
            resetUserInfo,
            headImage,
            defaultNickname
        }
    },
    {    //本地存储  使用pinia-plugin-persistedstate插件
        persist: {
            storage: localStorage,  //本地存储
        }
    }
)