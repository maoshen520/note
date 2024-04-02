
<!-- 笔记父组件 -->

<template>
    <div class="clearfix" style="height: 100%;">

        <!-- 笔记列表容器 -->
        <div class="note-list-box fl">
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
                            <el-button 
                                style="border: none;" 
                                plain 
                                @click="createNote"
                                :disabled="createNoteBtn"
                            >
                                <el-icon :size="40" color="#F74800">
                                    <CirclePlusFilled/>
                                </el-icon>
                            </el-button>
                        </el-tooltip>
                    </el-space> 
                </div>
            </el-card>


            <!-- <div style="position: relative;"> -->
            <div style="height: calc(100% - 63px);" class="scrollbar_div">
                <el-scrollbar @scroll="clickRightMenuRef.dropdown.handleClose();">

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
                                @click.right="clickRight($event,item.id,item.top,item.title)"
                            >
                                <noteCard 
                                    :id="item.id"
                                    :title="item.title ? item.title : createTitle"
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
                </el-scrollbar>
            </div>

            <!-- 右键显示菜单栏 -->
            <clickRightMenu
                ref="clickRightMenuRef"
                :clickRightClientY="clickRightClientY"
                :clickRightClientX="clickRightClientX"
                :top="isTop"
                :id="noteId"
                :title="noteTitle"
                @changeStatus="getNoteList(false,false)"
                @delete="showDeleteRemindDialog"
            ></clickRightMenu>

            <!-- 删除提示框 -->
            <deleteRemindDialog 
                @delect="deleteNote"
            ></deleteRemindDialog>  
            <!-- <deleteRemindDialog 
                :show="delectRemind.show"
                :title="delectRemind.title"
                :size="1"
                @delect="deleteNote"
                @cancel="delectRemind.show = false"
            ></deleteRemindDialog>   -->
        </div>

        <!-- 笔记编辑容器 -->
        <div class="fl"  style="width: calc(100% - 361px);height: 100%;">
            <router-view />
        </div>
    </div>
</template>

<script setup>
    import {ref,watch,onBeforeUnmount} from 'vue';
    import { useRouter } from 'vue-router';
    import {getUserToken,loginInvalid} from "@/utils/userLoginUtils.js";
    import {noteBaseRequest} from "@/request/note_request.js";
    import {
        CirclePlusFilled,
    } from '@element-plus/icons-vue';
    import noteCard from '@/components/note/NoteCard.vue';
    import clickRightMenu from '@/components/note/ClickRightMenu.vue';
    import gsap from "gsap";
    import deleteRemindDialog from "@/components/remind/DeleteRemindDialog.vue";
    import { useUserStore } from '@/stores/userStore';
    import { useDeleteRemindDialogStore } from "@/stores/deleteRemindDialogStore"
    import { storeToRefs } from 'pinia';

    import bus from 'vue3-eventbus'; 


    // 用户的共享资源
	const userStore = useUserStore();
	// 获取响应的token值
	const {token, id:user_id} = storeToRefs(userStore);
    // 重新登录----获取笔记列表
    watch(
        () => token.value,
        newData => {
            // 是否重新进行登录
            if(newData !== null){
                // 重新获取用户的笔记列表
                getNoteList(true, false);

                cardIndex.value = null;

                // 判断编辑小记的窗口是否需要关闭 ==> 1.是编辑小记  2.当前用户和小记用户一致
                // if(editThingRef.value.thingId != null && editThingRef.value.userId !== user_id.value){
                //     editThingRef.value.dialogVisible = false;
                // }

            }
        }
    )
    
    // 删除提醒框共享资源
    const deleteRemindDialogStore = useDeleteRemindDialogStore();
    const {
        show,  //是否显示提醒框
    } = storeToRefs(deleteRemindDialogStore);
    // 笔记页删除提醒框显示
    const {showByNotePage} = deleteRemindDialogStore;

    const isLoad = ref(false);

    const router = useRouter();

    //激活笔记标志
    const cardIndex = ref(null);  

    // 获取已经激活的笔记  ---防止刷新没激活
    const getRouterPath = () => {
        //路由地址
        const routerPath = ref(router.currentRoute.value.path);  
        // const arrPath = routerPath.value.split('/');
        // if(arrPath.length > 2){
        //     cardIndex.value = Number(arrPath[arrPath.length -1])
        // }
        const index = routerPath.value.indexOf('/note/edit/');
        if(index === -1) return false;
        cardIndex.value = parseInt(routerPath.value.substring('/note/edit/'.length));
    }
    getRouterPath();

    // 获取笔记列表数据
    const notes = ref([]);   //小记列表

    // 卡片点击事件--激活
    const cardClick = async (id) => {
        //判断用户的登录状态
        const userToken = await getUserToken();

        cardIndex.value = id;
        goEditNoteView(id);  //跳转至编辑路由
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

    // 获取笔记列表
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
    const noteId = ref(0);
    const noteTitle = ref('');
    const clickRightMenuRef = ref(null);
    const clickRight = async (e,id,top,title) => {
        e.preventDefault()
        //判断用户的登录状态
        const userToken = await getUserToken();
        cardIndex.value = id;
        isTop.value = !!top;
        noteId.value = id;
        noteTitle.value = title ? title : createTitle;
        clickRightClientY.value = (e.clientY - 15) + 'px'
        clickRightClientX.value = (e.clientX + 50) + 'px'
        clickRightMenuRef.value.dropdownOpen();  //调用右键组件打开菜单
    }

    // 删除提示框的对象
    const delectRemind = ref({
        show:false,  //是否显示
        id:'',  //小记编号
        title:'',  //删除单个文件名称
    })
    // 显示删除提醒框
    const showDeleteRemindDialog = ({id, title}) => {
        delectRemind.value.id = id;  //将要删除的笔记编号
        // delectRemind.value.title = title;
        // delectRemind.value.show = true;  //显示删除提醒框
        showByNotePage(title)
    }   
    // 删除笔记   --complete 是否彻底删除
    const deleteNote =async (complete) => {

        //关闭删除提醒框
        // delectRemind.value.show = false;  
        show.value = false;  

        //判断用户的登录状态
        const userToken =  await getUserToken();

        const { data: responseData } = await noteBaseRequest.delete(
            "/note/delete",
            {
                params:{
                    complete,
                    isRecycleBin:false,  //
                    noteId:delectRemind.value.id
                },
                headers: {
                    userToken:userToken
                }
            }
        ).catch(() => {
            ElMessage({
                message: complete ? '彻底删除笔记请求失败' : '删除笔记请求失败',
                type: 'error',
            })
            throw complete ? '彻底删除笔记请求失败' : '删除笔记请求失败';
        })
        if(responseData.success){
            ElMessage({
                message:responseData.message,
                type: 'success',
            });
            router.push('/note');  
            getNoteList(false, true); 
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

    const createNoteBtn = ref(false);
    let createTitle = "暂未设置标题";
    // 新增笔记
    const createNote = async () => {
        createNoteBtn.value = true;
        //判断用户的登录状态
        const userToken =  await getUserToken();

        const { data: responseData } = await noteBaseRequest.put(
            "/note/create",
            {},
            {
                headers: {
                    userToken:userToken
                }
            }
        ).catch(() => {
            createNoteBtn.value = false;
            ElMessage({
                message: '新增笔记请求失败',
                type: 'error',
            })
            throw '新增笔记请求失败';
        })
        if(responseData.success){
            ElMessage({
                message:responseData.message,
                type: 'success',
            });
            goEditNoteView(responseData.data);  //跳转至编辑路由
            cardIndex.value = responseData.data;
            getNoteList(false, false); //获取笔记列表（新增笔记不需要有显示的延迟效果）
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
        createNoteBtn.value = false;
    }

    //跳转至编辑路由
    const goEditNoteView = (id) => {
        if(id){
            router.push('/note/edit/' + id);  
        }else {
            ElMessage({
                message: '编辑笔记编号不能为空',
                type: 'error',
            });
            return false;
        }
    }

    // 监听知否触发了新建笔记事件
    bus.on('newCreateNote', createNote)
    // 组件卸载之前，移除监听事件
    onBeforeUnmount(() => {
        bus.off('newCreateNote', createNote);  //停止监听新建笔记事件
    })


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
 
    // /deep/.el-scrollbar__wrap {
    //     overflow-x: hidden !important;
    // }

       
</style>