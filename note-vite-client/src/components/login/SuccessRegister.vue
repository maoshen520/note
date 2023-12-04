<template>
    <div>
        <el-card class="box-card" style="text-align: center;">
            <div>
                <el-icon size="128" color="#2D852E"><CircleCheckFilled /></el-icon>
            </div>
            
            <div style="margin-top: 20px;">
                <el-text class="mx-1" size="large" style="font-weight: bold;color: #000;">欢迎加入知科笔记</el-text>
            </div>
            
            <div style="margin-top: 20px;">
                <el-text class="mx-1">请前往注册的邮箱，查看账号信息，</el-text>
                <el-button type="primary" text class="goRegisterBtn" @click="emits('changeStep',1)">
                    可点击此处前往登录
                </el-button>
            </div>
            
        </el-card>


        
    </div>
</template>

<script setup>
    // 自定义事件
    const emits = defineEmits(['changeStep']);
    import { CircleCheckFilled } from '@element-plus/icons-vue';  //图标

</script>

<style lang="less" scoped>
    .my-header {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
    }
    .titleClass{
        margin: 0;
    }
    .goRegisterBtn{
        padding:0px;
        margin-top: -2px;
        background-color: #fff !important;
    }
</style>