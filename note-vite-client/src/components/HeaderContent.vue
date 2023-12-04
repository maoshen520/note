<template>
    <div class="clearfix">
        <div class="fl header-title">知科笔记</div>
        <div class="fr" style="padding: 16px 0px;margin-right: 40px;">
            <el-space :size="20">

                <!-- 头像 -->
                <el-popover placement="bottom" title="用户内容" :width="300" trigger="hover"
                    content="this is content, this is content, this is content">
                    <template #reference>
                        <!-- <el-avatar :size="32" :icon="UserFilled"/> -->
                        <el-avatar shape="circle" fit="fill" :size="32"
                            src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                            style="cursor: pointer;" />
                    </template>
                    <div style="width: 300px;cursor: pointer;">
                        <div>11111111</div>
                        <div>11111111</div>
                        <div>11111111</div>
                        <div>11111111</div>
                        <div>11111111</div>
                    </div>
                </el-popover>

                <!-- 分割线 -->
                <el-divider direction="vertical" />

                <!-- 消息 -->
                <el-badge is-dot class="item">
                    <el-button :icon="Bell" circle color="#EAE9EA" />
                </el-badge>

                <!-- 登录按钮 -->
                <el-button type="success" color="#EAE9EA" style="color: #F74800;font-weight: bold;" @click="changeLoginModalShowStatus(true)"
                    >登录</el-button>
            </el-space>
        </div>

        
    </div>
</template>

<script setup>
    import { Bell } from '@element-plus/icons-vue';  //图标
    
    import { userLoginModalStore } from '@/stores/LoginModalStore.js';
    // 登录模态框共享资源的对象
    const loginModalStore = userLoginModalStore();
    // 改变登录模态框显示的状态
    const {changeLoginModalShowStatus} = loginModalStore;

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

    
</style>