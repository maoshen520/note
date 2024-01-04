
<!-- 笔记父组件 -->

<template>
    <div style="height: 100%;">

        <!-- 笔记列表 -->
        <div class="note-list-box">
            <!-- 笔记列表页的标头 -->
            <el-card class="box-card" shadow="never">
                <div class="card-header">
                    <h3>笔记列表</h3>
                    <el-space>
                        <!-- 新增按钮 -->
                        <el-tooltip
                            class="box-item"
                            effect="light"
                            :offset="10"
                            content="新增笔记"
                            placement="top"
                        >  
                            <el-button style="border: none;" plain >
                                <el-icon :size="40" color="#F74800">
                                    <CirclePlusFilled/>
                                </el-icon>
                            </el-button>
                        </el-tooltip>
                    </el-space> 
                </div>
            </el-card>

            <!-- <div style="position: relative;"> -->
            <div style="height: calc(100% - 63px);overflow: hidden;overflow-y: auto;">
                <!--笔记列表-- 骨架 :throttle="200"-->
                <el-skeleton v-if="!isLoad" animated>
                    <template #template>
                        <el-card class="card-list" v-for="(item,index) in 3" :key="index">
                            <template #header>
                                <div class="card-header">
                                    <el-skeleton-item style="width: 100%;height: 20px;"/>
                                </div>
                            </template>
                            <div>
                                <div>
                                    <el-skeleton-item style="width: 100%;height: 40px;"/>
                                </div>
                                <div style="margin-top: 8px;">
                                    <el-space wrap>
                                        <el-skeleton-item style="width: 50px;height: 22px;margin-right: 5px;"/>
                                        <el-skeleton-item style="width: 150px;height: 22px;margin-right: 5px;"/>
                                    </el-space>
                                </div>
                            </div>   
                        </el-card>
                    </template>
                </el-skeleton>

                <!-- 笔记 -->
                <div v-if="isLoad && notes.length > 0">
                    <div 
                        v-for="(item,index) in notes" 
                        :key="item.id"
                        @click="cardClick(item.id)"
                    >
                        <noteCard 
                            :id="item.id"
                            :title="item.title"
                            :body="item.body"
                            :top="!!item.top"
                            :time="item.updateTime"
                            :cardIndex="cardIndex"
                        ></noteCard>
                    </div>
                </div>

                <!-- 暂无笔记数据 -->
                <el-empty v-if="isLoad && notes.length == 0" description="暂无笔记数据" />
            </div>

            
        </div>
    </div>
</template>

<script setup>
    import {ref} from 'vue';
    import {getUserToken,loginInvalid} from "@/utils/userLoginUtils.js";
    import {noteBaseRequest} from "@/request/note_request.js";
    import {
        CirclePlusFilled, 
    } from '@element-plus/icons-vue';
    import noteCard from '@/components/note/NoteCard.vue';

    const isLoad = ref(false);

    // 获取笔记列表数据
    const notes = ref([]);   //小记列表
    const getNoteList = async (ed, ha) => {
       
        //判断用户的登录状态
        const userToken = await getUserToken();
        const { data: responseData } = await noteBaseRequest.get(
            "/note/list",
            {   
                // params:{
                //     search:searchInput.value,
                //     filter:select.value
                // },
                headers: {
                    userToken:userToken
                }
            }
        ).catch(() => {
            ElMessage({
                message: '获取笔记列表请求失败',
                type: 'error',
            })
            throw '获取笔记列表请求失败'
        })
        console.log(responseData)
        if(responseData.success){
            notes.value = [];
            notes.value = responseData.data;  //小记列表
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
    //需要显示动画
    getNoteList(true, false);

    // 卡片点击事件--激活
    const cardIndex = ref(null)
    const cardClick = (id) => {
        cardIndex.value = id;
    }

</script>

<style lang="less" scoped>
    .note-list-box{
        width: 350px;
        height: 100%;
        border-right: 1px solid #dcdfe6 ;
        padding:0px 5px;
        background-color: #fff;
    }

    .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .box-card{
        border: none;
        box-shadow: none;
        /deep/.el-card__body{
            padding: 0px 20px;
        }
    }

    .card-list{
        box-shadow: none;
        cursor: pointer;
        margin: 10px 0px;
    }

    

    
</style>