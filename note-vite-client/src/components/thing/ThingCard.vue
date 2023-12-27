<template>
    <el-space  direction="vertical" alignment="start" :size="10" >
        <el-space wrap  :size="10" >
            <!-- 水印 -->
            <el-watermark 
                :width="200"
                :height="50" 
                :content="finished ? '已完成' : ''" 
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
                            <!-- 提示 -->
                            <el-tooltip  
                                class="box-item"
                                effect="light"
                                :content="title"
                                placement="top"
                                :offset="5"
                            >   
                                <!-- 标题 -->
                                <div class="card-list-title">{{ title }}</div> 
                            </el-tooltip>
                            
                            <div style="width:84px">
                                <!-- 删除 -->
                                <el-tooltip
                                    class="box-item"
                                    effect="light"
                                    content="删除"
                                    placement="top"
                                    :offset="5"
                                >
                                    <el-button 
                                        text 
                                        size="small" 
                                        style="padding: 0px;margin-left: 20px;"
                                        @click="emits('delete',{id,title})"
                                    >
                                        <el-icon size="16"><Delete /></el-icon>
                                    </el-button>
                                </el-tooltip>
                                
                                <!-- 置顶 / 取消置顶 -->
                                <el-tooltip
                                    class="box-item"
                                    effect="light"
                                    :content="thingCardTopContext().text"
                                    placement="top"
                                    :offset="5"
                                >
                                    <el-button 
                                        :disabled="topBtnDisabled" 
                                        text 
                                        size="small" 
                                        style="padding: 0px;margin-left: 8px;" @click="topThing(!top)"
                                    >
                                        <el-icon size="16"><component :is="thingCardTopContext().icon" /></el-icon>
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
                                    <el-button 
                                        text 
                                        size="small" 
                                        style="padding: 0px;margin-left: 8px;"
                                        @click="emits('edit')"
                                    >
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
                                <el-tag v-for="(item2,index2) in tags" :key="index2" class="ml-2" type="info" color="#E9E9EB" style="color: #000;">{{ item2 }}</el-tag>
                            </el-space>
                        </div>

                        <div style="margin-top: 8px;">
                            <el-button v-if="top == 1" size="small" type="success" color="#EAE9EA" style="color: #F74800;font-weight: bold;padding:0px 5px;height: 18px;background-color: #FFCCA9;margin-right: 20px;margin-top: 0px;">置顶</el-button>
                            <el-text class="mx-1" size="small" type="info">{{ time }}</el-text>
                        </div>
                    </div>   
                </el-card>
            </el-watermark>
        </el-space>
    </el-space>

</template>

<script setup>
    import {ref} from "vue"
    import { 
        Delete, 
        Upload, 
        Download, 
        EditPen
    } from '@element-plus/icons-vue';  //图标
    import {getUserToken,loginInvalid} from "@/utils/userLoginUtils.js";
    import {noteBaseRequest} from "@/request/note_request.js";

    //自定义属性
    const propsData = defineProps({
        id: {type:Number, required:true},   //小记标号
        finished: {type:Boolean, default: false},  //是否已经完成
        title: {type:String, required:true},   //标题
        top: {type:Boolean, default: false},  //是否置顶
        tags: {type:Array, default:['暂无标签']},  //标签
        time: {type:String, required:true},   //最后一次修改的时间
    })

    // 自定义事件
    const emits = defineEmits(['changeStatus','delete','edit']);

    //禁用置顶/取消置顶按钮
    const topBtnDisabled = ref(false);  

    // 置顶按钮和取消置顶按钮显示对象
    const thingCardTopContext = () => {
        if(propsData.top){
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

    // 置顶小记（取消置顶）
    const topThing = async (isTop) => {

        //判断用户的登录状态
        const userToken =  await getUserToken();

        // 禁用按钮
        topBtnDisabled.value = true;  //禁用按钮

        const { data: responseData } = await noteBaseRequest.get(
            "/thing/top",
            {
                params:{isTop, thingId: propsData.id},
                headers: {
                    userToken:userToken
                }
            }
        ).catch(() => {
            ElMessage({
                message:isTop ? '置顶小记请求失败' : '取消置顶小记请求失败',
                type: 'error',
            })
            topBtnDisabled.value = false; //解除禁用按钮
            throw isTop ? '置顶小记请求失败' : '取消置顶小记请求失败';
        })
        console.log(responseData)
        if(responseData.success){
            // things.value = responseData.data;  //小记列表
            ElMessage({
                message:responseData.message,
                type: 'success',
            });
            emits('changeStatus');  //调用父组件的请求列表的方法
            topBtnDisabled.value= false; //解除禁用按钮
        }else{
            ElMessage({
                message: responseData.message,
                type: 'error'
            });
            topBtnDisabled.value= false; //解除禁用按钮
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
        // justify-content: space-between;
        align-items: center;
    }
    .box-card-list{
        border: none;
        background-color: #F2F6F7;
        /deep/.el-card__body{
            padding: 10px;
        }
        .card-list-title{
            min-width: 100px;
            max-width: 162px;
            font-size: 14px;
            font-weight: 600;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
    }
</style>



