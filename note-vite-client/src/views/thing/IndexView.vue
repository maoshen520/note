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
            
                <el-space direction="vertical" alignment="start" :size="10" v-for="(item,index) in things" :key="item.id">
                    <el-space wrap :size="10" >
                        <!-- 水印 -->
                        <el-watermark 
                            :width="200"
                            :height="50" 
                            :content="item.finished == 1 ? '已完成' : ''" 
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
                                        <el-tooltip
                                            class="box-item"
                                            effect="light"
                                            :content="item.title"
                                            placement="top"
                                            :offset="5"
                                        >
                                            <div class="card-list-title">{{ item.title }}</div> 
                                        </el-tooltip>
                                        
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
                                                :content="thingCardTopContext(item.top).text"
                                                placement="top"
                                                :offset="5"
                                            >
                                                <el-button :disabled="item.toTop" text size="small" style="padding: 0px;margin-left: 8px;" @click="topThing(!!!item.top, item.id, index)">
                                                    <el-icon size="16"><component :is="thingCardTopContext(item.top).icon" /></el-icon>
                                                </el-button>
                                            </el-tooltip>
                                            
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
                                            <el-tag v-for="(item2,index2) in thingTags(item.tags)" :key="index2" class="ml-2" type="info" color="#E9E9EB" style="color: #000;">{{ item2 }}</el-tag>
                                        </el-space>
                                    </div>
                                    <div style="margin-top: 8px;">
                                        <el-button v-if="item.top == 1" size="small" type="success" color="#EAE9EA" style="color: #F74800;font-weight: bold;padding:0px 5px;height: 18px;background-color: #FFCCA9;margin-right: 20px;margin-top: 0px;" @click="changeLoginModalShowStatus(true)">置顶</el-button>
                                        <el-text class="mx-1" size="small" type="info">{{ item.updateTime }}</el-text>
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
    import {getUserToken,loginInvalid} from "@/utils/userLoginUtils.js";
    import {noteBaseRequest} from "@/request/note_request.js";

    // 获取小记列表数据
    const things = ref([]);   //小记列表
    const getThingList = async () => {
        //判断用户的登录状态
        const userToken = await getUserToken();
        const { data: responseData } = await noteBaseRequest.get(
            "/thing/list",
            {
                headers: {
                    userToken:userToken
                }
            }
        ).catch(() => {
            ElMessage({
                message: '获取小记列表请求失败',
                type: 'error',
            })
            throw '获取小记列表请求失败'
        })
        console.log(responseData)
        if(responseData.success){
            things.value = responseData.data;  //小记列表
        }else{
            ElMessage({
                message: responseData.message,
                type: 'error'
            });
            // 登录已失效
            if(responseData.code == 'L_008'){
                loginInvalid(true);
            }
        }

    }
    getThingList();

    // 置顶按钮和取消置顶按钮显示对象
    const thingCardTopContext = top => {
        if(top === 1){
            return {
                icon:Download,
                text:'取消置顶'
            }
        }else{
            return {
                icon:Upload,
                text:'置顶'
            }
        }
    }

    // 标签
    const thingTags = tags => {
        if(tags == ''){
            return [];
        }else {
            return tags.split(',');
        }
    }

    // 置顶小记（取消置顶）
    const topThing = async (isTop, thingId, index) => {

        //判断用户的登录状态
        const userToken =  await getUserToken();

        // 禁用按钮
        const thing = things.value[index];
        thing.toTop = true;

        const { data: responseData } = await noteBaseRequest.get(
            "/thing/top",
            {
                params:{isTop, thingId},
                headers: {
                    userToken:userToken
                }
            }
        ).catch(() => {
            ElMessage({
                message:isTop ? '置顶小记请求失败' : '取消置顶小记请求失败',
                type: 'error',
            })
            thing.toTop = false; //解除禁用按钮
            throw isTop ? '置顶小记请求失败' : '取消置顶小记请求失败';
        })
        console.log(responseData)
        if(responseData.success){
            // things.value = responseData.data;  //小记列表
            ElMessage({
                message:responseData.message,
                type: 'success',
            });
            getThingList();
        }else{
            ElMessage({
                message: responseData.message,
                type: 'error'
            });
            thing.toTop = false; //解除禁用按钮
            // 登录已失效
            if(responseData.code == 'L_008'){
                loginInvalid(true);
            }
        }
    }

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