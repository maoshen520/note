<!-- 小记父组件 -->

<template>
    <div style="margin: 20px;">
        <!-- 小记列表页的标头 -->
        <el-card class="box-card" shadow="never">
            <div class="card-header">
                <h3>小记列表</h3>
                <el-space>
                    <!-- 搜索 -->
                    <div class="mt-4">
                        <el-input
                            v-model="searchInput"
                            placeholder="搜索"
                            class="input-with-select"
                        >
                            <template #append>
                                <el-button class="searchBtn" color="#FFCCA9" style="color: #F74800;" plain :icon="Search" @click="getThingList(false)"/>
                            </template>
                        </el-input>
                    </div>

                    <div>
                        <el-select v-model="select" placeholder="默认" style="width: 115px" @change="getThingList(false)">
                            <el-option label="默认" value="" />
                            <el-option label="已完成" value="1" />
                            <el-option label="未完成" value="0" />
                        </el-select>
                    </div>

                    <!-- 新增按钮 -->
                    <el-button color="#FFCCA9" style="color: #F74800;" plain @click="editThingRef.showDialogVisible(null)">新增小记</el-button>
                </el-space>
                
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
                    <TransitionGroup 
                        @before-enter="beforeEnter" 
                        @enter="enterEvent"
                        @before-leave="beforeLeave" 
                        @leave="leaveEvent"
                        move-class="move-transtion"
                    >   
                        <template v-if="isLoad && things.length > 0">
                            <!-- class="thing-cards" -->
                            <ThingCard
                                
                                v-for="(item,index) in things" :key="item.id"
                                :id="item.id"
                                :data-index="index"
                                :finished="!!item.finished"
                                :title="item.title"
                                :top="!!item.top"
                                :tags="item.tags.split(',')"
                                :time="item.updateTime"
                                @changeStatus="getThingList(false,false)"
                                @delete="showDeleteRemindDialog"
                                @edit="editThingRef.showDialogVisible(item.id)"
                            ></ThingCard>
                        </template>
                    </TransitionGroup>
                </div>
                    
                <!-- 暂无小记数据 -->
                <template v-if="isLoad && things.length == 0">
                    <el-empty description="暂无小记数据" />
                </template>

                <!-- 删除提示框 -->
                <deleteRemindDialog 
                    :show="delectRemind.show"
                    :title="delectRemind.title"
                    :size="1"
                    @delect="deleteThing"
                    @cancel="delectRemind.show = false"
                ></deleteRemindDialog>

                <!-- 编辑小记 -->
                <ThingEdit 
                    ref="editThingRef"
                    @save="getThingList"
                >
                </ThingEdit>

        </el-card>
    </div>
</template>

<script setup>
    import {ref,watch} from "vue"
    import { Search} from '@element-plus/icons-vue';  //图标
    import {getUserToken,loginInvalid} from "@/utils/userLoginUtils.js";
    import {noteBaseRequest} from "@/request/note_request.js";
    import ThingCard from "@/components/thing/ThingCard.vue";
    import gsap from "gsap";
    import deleteRemindDialog from "@/components/remind/DeleteRemindDialog.vue";
    import ThingEdit from "@/components/thing/ThingEdit.vue";
    import { useUserStore } from '@/stores/userStore';
    import { storeToRefs } from 'pinia';

    // 用户的共享资源
	const userStore = useUserStore();
	// 获取响应的token值
	const {token, id:user_id} = storeToRefs(userStore);
    watch(
        () => token.value,
        newData => {
            // 是否重新进行登录
            if(newData !== null){
                // 重新获取用户的小记列表
                getThingList(true, false);

                // 判断编辑小记的窗口是否需要关闭 ==> 1.是编辑小记  2.当前用户和小记用户一致
                if(editThingRef.value.thingId != null && editThingRef.value.userId !== user_id.value){
                    editThingRef.value.dialogVisible = false;
                }

            }
        }
    )

    const isLoad = ref(false);  //请求中显示骨架

    //显示小记卡片是否需要延迟动画
    let enterDelay = true;  

    //隐藏卡片是否需要显示动画
    let hiddenAnimation = false;  

    // 获取小记列表数据
    const things = ref([]);   //小记列表
    const getThingList = async (ed, ha) => {
        enterDelay = ed;
        hiddenAnimation = ha;
        //判断用户的登录状态
        const userToken = await getUserToken();
        const { data: responseData } = await noteBaseRequest.get(
            "/thing/list",
            {   
                params:{
                    search:searchInput.value,
                    filter:select.value
                },
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
    //需要显示动画
    getThingList(true, false);

    //显示动画之前的初始位置 greenSock
    const beforeEnter = (el) => {
        gsap.set(el,{
            y: 30,
            opacity: 0
        })
    }
    // 显示动画
    const enterEvent = (el, done) => {
        gsap.to(el,{
            y: 0,  //偏移量
            opacity: 1,  //透明度
            duration: 0.5,  //时间--秒
            delay:() => (enterDelay ? 0 : el.dataset.index * 0.12),  //延迟动画
            onComplete: done  //动画完成时调用的函数
        })
    }

    //隐藏动画之前的初始位置 greenSock
    const beforeLeave = (el) => {
        if(hiddenAnimation){
            // 获取删除的元素距离父组件的左和上的位置
            let left = el.offsetLeft;
            let top = el.offsetTop;

            //设置删除组件的属性（需要脱离文档流）
            gsap.set(el,{
                position: 'absilute',
                boxShadow:'0 0 5px black',
                zIndex: 1,
                top: top,
                left:left
            })
        }
        
    }
    // 隐藏动画
    const leaveEvent = (el, done) => {
        if(hiddenAnimation){
            let tl = gsap.timeline();  //创建时间线动画
            tl.to(el, {
                scale: 1.3,
                duration: 0.3,  //时间--秒
            }).to(el, {
                scale: 0,
                duration: 0.3,  //时间--秒
                onComplete: done  //动画完成时调用的函数
            })
        } else {
            gsap.to(el,{
                duration: 0,  //时间--秒
                onComplete: done  //动画完成时调用的函数
            })
        }
        
    }

    // 删除提示框的对象
    const delectRemind = ref({
        show:false,  //是否显示
        id:'',  //小记编号
        title:'',  //提醒框的提示内容
    })

    // 显示删除提醒框
    const showDeleteRemindDialog = ({id, title}) => {
        delectRemind.value.id = id;  //将要删除的小记编号
        delectRemind.value.title = title;
        delectRemind.value.show = true;  //显示删除提醒框
    }   

    // 删除小记   --complete 是否彻底删除
    const deleteThing =async (complete) => {

        delectRemind.value.show = false;  //关闭删除提醒框

        //判断用户的登录状态
        const userToken =  await getUserToken();

        const { data: responseData } = await noteBaseRequest.delete(
            "/thing/delete",
            {
                params:{
                    complete,
                    isRecycleBin:false,
                    thingId:delectRemind.value.id
                },
                headers: {
                    userToken:userToken
                }
            }
        ).catch(() => {
            ElMessage({
                message: complete ? '彻底删除小记请求失败' : '删除小记请求失败',
                type: 'error',
            })
            throw complete ? '彻底删除小记请求失败' : '删除小记请求失败';
        })
        console.log(responseData)
        if(responseData.success){
            ElMessage({
                message:responseData.message,
                type: 'success',
            });
            getThingList(false, true); 
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
    
    // 编辑小记模态框组件的引用---子组件暴露出来的值---用ref绑定
    const editThingRef = ref(null);

    const searchInput = ref('');
    const select = ref(null)

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
    .move-transtion{
        transition: all 0.5s;
    }

    /deep/.el-input{
        --el-input-focus-border: #F74800 !important;
        --el-input-focus-border-color:#F74800;
        --el-input-hover-border-color:#F74800;
    }

    // .searchBtn:hover{
    //     border-color: #F74800 !important;
    //     border-top-left-radius: 0px !important;
    //     border-bottom-left-radius: 0px !important;
    // }
    .el-select{
        --el-select-focus-border: #F74800 !important;
        --el-select-input-focus-border-color:#F74800;
        --el-select-border-color-hover:#F74800;
    }

    

</style>