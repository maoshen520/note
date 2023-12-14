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

                <!--小记列表-- 骨架 :throttle="200"-->
                <el-skeleton v-if="!isLoad" animated>
                    <template #template>
                        <el-space  direction="vertical" alignment="start" :size="10" v-for="(item,index) in 12" :key="index">
                            <el-space wrap :size="10" >
                                <el-card class="" shadow="never">
                                    <!-- 头部 -->
                                    <template #header>
                                        <div class="card-header">
                                            <div class="card-list-title" >
                                                <el-skeleton-item style="width: 150px;height: 25px;"/>    
                                            </div> 

                                            <!-- 按钮 -->
                                            <div style="margin-left: 10px;">    
                                                <el-skeleton-item style="width: 20px;height: 20px;margin-left: 5px;" variant="circle"/>
                                                <el-skeleton-item style="width: 20px;height: 20px;margin-left: 5px;" variant="circle"/>
                                                <el-skeleton-item style="width: 20px;height: 20px;margin-left: 5px;" variant="circle"/>
                                            </div>
                                        </div>
                                    </template>

                                    <!-- 内容 -->
                                    <div>
                                        <div>
                                            <!-- 标签 -->
                                            <el-space wrap>
                                                <el-skeleton-item style="width: 50px;height: 24px;margin-right: 5px;"/>
                                                <el-skeleton-item style="width: 80px;height: 24px;margin-right: 5px;"/>
                                                <el-skeleton-item style="width: 60px;height: 24px;margin-right: 5px;"/>
                                            </el-space>
                                        </div>
                                        <div style="margin-top: 8px;">
                                            <el-skeleton-item variant="text" style="width: 140px;"/>
                                        </div>
                                    </div>   
                                </el-card> 
                            </el-space>
                        </el-space>
                    </template>
                </el-skeleton>

                <!-- 小记列表 -->
                <div>
                    <TransitionGroup @before-enter="beforeEnter" @enter="enterEvent">   
                        <template v-if="isLoad && things.length > 0">
                            <ThingCard
                                class="thing-cards"
                                v-for="(item,index) in things" :key="item.id"
                                :id="item.id"
                                :data-index="index"
                                :finished="!!item.finished"
                                :title="item.title"
                                :top="!!item.top"
                                :tags="item.tags.split(',')"
                                :time="item.updateTime"
                                @changeStatus="getThingList()"
                            ></ThingCard>
                        </template>
                    </TransitionGroup>
                </div>
                    
                <!-- 暂无小记数据 -->
                <template v-if="isLoad && things.length == 0">
                    <el-empty description="暂无小记数据" />
                </template>

        </el-card>
    </div>
</template>

<script setup>
    import {ref} from "vue"
    import { Delete, Upload, Download, EditPen} from '@element-plus/icons-vue';  //图标
    import {getUserToken,loginInvalid} from "@/utils/userLoginUtils.js";
    import {noteBaseRequest} from "@/request/note_request.js";
    import ThingCard from "@/components/thing/ThingCard.vue";
    import gsap from "gsap";

    const isLoad = ref(false);  //请求中显示骨架

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
            things.value = [];
            things.value = responseData.data;  //小记列表
            isLoad.value = true;
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


    // greenSock
    const beforeEnter = (el) => {
        gsap.set(el,{
            y: 30,
            opacity: 0
        })
    }

    const enterEvent = (el, done) => {
        gsap.to(el,{
            y: 0,  //偏移量
            opacity: 1,  //透明度
            duration: 0.5,  //时间--秒
            delay:el.dataset.index * 0.12,
            onComplete: done  //动画完成时调用的函数
        })
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

    .thing-cards{
        transition: all 0.5s;
    }

    

</style>