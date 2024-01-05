
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

                <!-- 笔记列表 -->
                <TransitionGroup 
                    @before-enter="beforeEnter" 
                    @enter="enterEvent"
                    @before-leave="beforeLeave" 
                    @leave="leaveEvent"
                    move-class="move-transtion"
                >
                    <!-- 笔记 -->
                    <template v-if="isLoad && notes.length > 0">
                        <div 
                            v-for="(item,index) in notes" 
                            :key="item.id"
                            @click="cardClick(item.id)"
                            :data-index="index"
                            @click.right="clickRight($event,item.id,item.top)"
                            
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
                    </template>
                </TransitionGroup>
                

                <!-- 暂无笔记数据 -->
                <el-empty v-if="isLoad && notes.length == 0" description="暂无笔记数据" />
            </div>

            <!-- 右键显示菜单栏 -->
            <clickRightMenu
                ref="clickRightVueRef"
                :clickRightClientY="clickRightClientY"
                :clickRightClientX="clickRightClientX"
                :top="isTop"
                @delete="deleteNote"
            ></clickRightMenu>
         
            
            
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
    import clickRightMenu from '@/components/note/ClickRightMenu.vue';
    import gsap from "gsap";

    const isLoad = ref(false);

    // 获取笔记列表数据
    const notes = ref([]);   //小记列表

    // 卡片点击事件--激活
    const cardIndex = ref(null)
    const cardClick = (id) => {
        cardIndex.value = id;
    }

    //显示小记卡片是否需要延迟动画
    let enterDelay = true;  

    //隐藏卡片是否需要显示动画
    let hiddenAnimation = false;

    //显示动画之前的初始位置 greenSock
    const beforeEnter = (el) => {
        gsap.set(el,{
            x: 30,
            opacity: 0
        })
    }
    // 显示动画
    const enterEvent = (el, done) => {
        gsap.to(el,{
            x: 0,  //偏移量
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
                left:left,
                backdropFilter:'blur(5px)'
            })
        }
        
    }
    // 隐藏动画
    const leaveEvent = (el, done) => {
        if(hiddenAnimation){
            // let tl = gsap.timeline();  //创建时间线动画
            // tl.to(el, {
            //     scale: 1.3,
            //     duration: 0.3,  //时间--秒
            // }).to(el, {
            //     scale: 0,
            //     duration: 0.3,  //时间--秒
            //     onComplete: done  //动画完成时调用的函数
            // })
            gsap.to(el, {
                scale: 0,
                duration: 0.5,  //时间--秒
                onComplete: done  //动画完成时调用的函数
            })
        } else {
            gsap.to(el,{
                duration: 0,  //时间--秒
                onComplete: done  //动画完成时调用的函数
            })
        }   
    }

    const getNoteList = async (ed, ha) => {
        
        enterDelay = ed;  //显示是否需要动画
        hiddenAnimation = ha;  //隐藏是否需要动画
       
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
    getNoteList(true, true);

    // 卡片右键显示信息
    const clickRightClientY = ref('');
    const clickRightClientX = ref('');
    const isTop = ref(false);
    const clickRightVueRef = ref(null);
    const clickRight = (e,id,top) => {
        e.preventDefault()
        cardIndex.value = id;
        isTop.value = !!top;
        clickRightClientY.value = (e.clientY - 15) + 'px'
        clickRightClientX.value = (e.clientX + 50) + 'px'
        clickRightVueRef.value.dropdownOpen();
        
    }


    const deleteNote = () => {
        console.log('点击删除')
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

    .move-transtion{
        transition: all 0.5s;
    }

    .dropdownBox{
        position:fixed;
    }
       
</style>