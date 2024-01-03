<!-- 头部组件 -->

<template>
    <div class="clearfix">
        <div class="fl header-title">知科笔记</div>
        <div class="fr" style="padding: 16px 0px;margin-right: 40px;">
            <el-space :size="20">

                <!-- 头像 -->
                <el-popover v-if="userId !== null" placement="bottom" class="elPopover222" :width="300" trigger="hover" >
                    
                    <!-- popper-style="padding:0px" -->
                    <template #reference>
                        <!-- <el-avatar :size="32" :icon="UserFilled"/> -->
                        <el-avatar shape="circle" fit="fill" :size="32"
                            :src="headImage"
                            style="cursor: pointer;" />
                    </template>

                    <div style="border-bottom: 1px solid#EAE9EA;">
                        <el-row style="padding: 10px;">
                            <el-col :span="5">
                                <el-avatar shape="circle" fit="fill" :size="40"
                                    :src="headImage"
                                    style="cursor: pointer;"
                                />
                            </el-col>
                            <el-col :span="19">
                                
                                <div style="margin-bottom: 2px;">
                                    <el-text class="mx-1" tag="b" size="large">{{defaultNickname}}</el-text>
                                </div>
                                <div>
                                    <el-button v-if="level === 1" size="small" type="success" color="#EAE9EA" style="color: #F74800;font-weight: bold;padding:0px 5px;height: 18px;background-color: #FFCCA9;margin-right: 20px;margin-top: -3px;" @click="changeLoginModalShowStatus(true)">会员</el-button>
                                    <el-text class="mx-1" size="small" type="info">2023-12-12到期</el-text>
                                </div>
                                
                            </el-col>
                        </el-row>
                    </div>

                    <el-menu
                        class="el-menu-vertical-demo"
                    >
                        <el-menu-item :index="item.key" v-for="(item,index) in userMenu" :key="index" @click="menuSelect(item.key)">
                            <el-icon><component :is="item.icon" /></el-icon>
                            <span>{{ item.label }}</span>
                        </el-menu-item>
                    </el-menu>
                </el-popover>

                <!-- 分割线 -->
                <el-divider v-if="userId !== null" direction="vertical" />

                <!-- 消息 -->
                <el-badge is-dot class="item">
                    <el-button :icon="Bell" circle color="#EAE9EA" />
                </el-badge>

                <!-- 登录按钮 -->
                <el-button v-if="userId === null" type="success" color="#EAE9EA" style="color: #F74800;font-weight: bold;" @click="changeLoginModalShowStatus(true)"
                    >登录</el-button>
            </el-space>
        </div>

        
    </div>
</template>

<script setup>
    import { Bell,Expand,Avatar,Tools} from '@element-plus/icons-vue';  //图标

    import {storeToRefs} from "pinia"
    import { userLoginModalStore } from '@/stores/LoginModalStore.js';
    import {useUserStore} from "@/stores/userStore.js";
    import {noteBaseRequest} from "@/request/note_request.js";
    import {loginInvalid,getUserToken} from "@/utils/userLoginUtils.js";

    // 登录模态框共享资源的对象
    const loginModalStore = userLoginModalStore();
    // 改变登录模态框显示的状态
    const {changeLoginModalShowStatus} = loginModalStore;

    // 用户的共享数据对象
    const userStore = useUserStore();
    const {id:userId,headImage,level,defaultNickname} = storeToRefs(userStore);


    // 用户头像的导航栏
    const userMenu = [
        {
            key:'user-center',
            label:'个人中心',
            icon: Avatar
        },
        {
            key:'account-setting',
            label:'账户设置',
            icon: Tools
        },
        {
            key:'sign-out-login',
            label:'退出登录',
            icon: Expand
        },
    ]
    
    // 用户菜单点击回调
    const menuSelect = (type) => {
        console.log(type)
        if(type === 'sign-out-login'){
            sigonOutLogin();
        }
    }

    // 退出登录
    const sigonOutLogin = async () => {
        // 删除redis中的对应的key
        // 用户共享的数据清空
        // userToken 本地存储删除

        // const userToken = localStorage.getItem('userToken');
        // if(userToken === null){
        //     ElMessage({
        //         message: '登录已失效',
        //         type: 'error',
        //     })
        //     return false;
        // }
        // 判断用户是否登录（检查本地存储的userToken 值）
        const userToken = await getUserToken();

        const { data: responseData } =await noteBaseRequest.get(
            "/user/login/out",
            {
                headers: {
                    userToken:userToken
                }
            }
        ).catch(() => {
            ElMessage({
                message: '发送退出登录请求失败',
                type: 'error',
            })
            throw '发送退出登录请求失败'
        })

        if(responseData.success){
            loginInvalid(true);
        }else{
            ElMessage({
                message: responseData.message,
                type: 'error'
            })
        }
    }


</script>

<style lang="less" scoped>
    .header-title {
        line-height: 64px;
    }

    /deep/.el-badge__content.is-fixed.is-dot {
        right: 8px;
        top: 3px;
    }

    .my-header {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        
    }

    .titleClass {
        margin: 0;
    }

    /deep/.el-checkbox__input.is-checked+.el-checkbox__label {
        color: #000 !important;
    }


    // 头像信息
    /deep/.el-card__header{
        padding: 0px;
    }

    .el-menu{
        border-right: none !important;
    }


</style>