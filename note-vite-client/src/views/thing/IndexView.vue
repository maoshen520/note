<template>
    <div>
        <!-- 小记列表页的标头 -->
        <el-card class="box-card" shadow="never">
            <div class="card-header">
                <h3>小记列表</h3>
                <el-button color="#FFCCA9" style="color: #F74800;" plain>新增小记</el-button>
            </div>
        </el-card>

        <el-card class="box-card" shadow="never">
            
                <el-space direction="vertical" alignment="start" :size="10" v-for="(item,index) in 10" :key="index">
                    <el-space wrap :size="10" >
                        <!-- 水印 -->
                        <el-watermark 
                            :width="200"
                            :height="50" 
                            :content="isWatermark ? '已完成' : ''" 
                            :font="{
                                fontSize: 30,
                                color: '#F74800',
                                fontWeight:'600'

                            }"
                            :offset="[60,70]"
                        >
                            <el-card class="box-card-list" shadow="never">
                                <!-- 头部 -->
                                <template #header>
                                    <div class="card-header">
                                        <div class="card-list-title">2023春节第一周作业小记列表客服电话关机梵蒂冈很多符合国家的喝咖啡韩国</div>
                                        <div>

                                            <!-- 删除 -->
                                            <el-tooltip
                                                class="box-item"
                                                effect="light"
                                                content="删除"
                                                placement="top"
                                                :offset="5"
                                            >
                                                <el-button text size="small" style="padding: 0px;margin-left: 20px;">
                                                    <el-icon size="16"><Delete /></el-icon>
                                                </el-button>
                                            </el-tooltip>
                                            
                                            <!-- 置顶 -->
                                            <el-tooltip
                                                class="box-item"
                                                effect="light"
                                                content="置顶"
                                                placement="top"
                                                :offset="5"
                                            >
                                                <el-button text size="small" style="padding: 0px;margin-left: 8px;">
                                                    <el-icon size="16"><Upload /></el-icon>
                                                </el-button>
                                            </el-tooltip>
                                            
                                            <!-- 取消置顶 -->
                                            <!-- <el-tooltip
                                                class="box-item"
                                                effect="light"
                                                content="取消置顶"
                                                placement="top"
                                                :offset="5"
                                            >
                                                <el-button text size="small"  style="padding: 0px;margin-left: 8px;">
                                                    <el-icon size="16"><Download /></el-icon>
                                                </el-button>
                                            </el-tooltip> -->
                                            
                                            <!-- 编辑 -->
                                            <el-tooltip
                                                class="box-item"
                                                effect="light"
                                                content="编辑"
                                                placement="top"
                                                :offset="5"
                                            >
                                                <el-button text size="small" style="padding: 0px;margin-left: 8px;">
                                                    <el-icon size="16"><EditPen /></el-icon>
                                                </el-button>    
                                            </el-tooltip>
                                        </div>
                                    </div>
                                </template>

                                <!-- 内容 -->
                                <div>
                                    <div>
                                        <!-- 标签 -->
                                        <el-space wrap>
                                            <el-tag class="ml-2" type="info" color="#E9E9EB" style="color: #000;">作业</el-tag>
                                            <el-tag class="ml-2" type="info" color="#E9E9EB" style="color: #000;">大学生</el-tag>
                                        </el-space>
                                    </div>
                                    <div style="margin-top: 8px;">
                                        <el-button size="small" type="success" color="#EAE9EA" style="color: #F74800;font-weight: bold;padding:0px 5px;height: 18px;background-color: #FFCCA9;margin-right: 20px;margin-top: 0px;" @click="changeLoginModalShowStatus(true)">置顶</el-button>
                                        <el-text class="mx-1" size="small" type="info">2023-12-12 11:11:11</el-text>
                                    </div>
                                </div>   
                            </el-card>
                        </el-watermark>
                    </el-space>
                </el-space>
            
        </el-card>

       
     
    </div>
</template>

<script setup>
    import {ref} from "vue"
    import { Delete, Upload, Download, EditPen} from '@element-plus/icons-vue';  //图标

    const isWatermark = ref(true)
</script>

<style lang="less" scoped>
    .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .box-card{
        border: none;
        margin-bottom: 20px;
    }

    .box-card-list{
        border: none;
        background-color: #F2F6F7;
        // padding: 10px;
        /deep/.el-card__body{
            padding: 10px;
        }
        .card-list-title{
            font-size: 14px;
            font-weight: 600;
            max-width: 162px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
    }

</style>