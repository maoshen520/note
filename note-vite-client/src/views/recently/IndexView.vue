<template>
    <div class="viewVue-box-nobg">

        <!-- 轮播图 -->
        <el-card class="carousel-box card-list-box">
            <el-carousel height="140px">
                <el-carousel-item v-for="item in carouselImg" :key="item.id">
                    <img class="carousel-img" :src="item.src" alt="" srcset="" @click="toHref(item.to, () => {}, (!item.offsite && !item.newTab), item.newTab)">
                </el-carousel-item>
            </el-carousel>
        </el-card>

        <!-- 功能按钮 -->
        <el-card class="card-list-box">
            <el-button 
                text 
                :bg="true"
                style="font-size: 16px;padding: 8px 15px;color:#F74800 "
                @click="createMain('note')" 
            >
                <el-icon :size="20" color="#F74800" style="margin-right: 5px;">
                    <Document/>
                </el-icon>
                新增笔记
            </el-button>

            <el-button 
                text 
                :bg="true"
                style="font-size: 16px;padding: 8px 15px;color: #197BF5;"  
                @click="createMain('thing')"
            >
                <el-icon :size="20" color="#197BF5" style="margin-right: 5px;">
                    <Collection/>
                </el-icon>
                新增小记
            </el-button>

            <el-button 
                text 
                :bg="true"
                style="font-size: 16px;padding: 8px 15px;color: red;"  
                
            >
                <el-icon :size="20" color="red" style="margin-right: 5px;">
                    <Umbrella/>
                </el-icon>
                商城中心
            </el-button>
        </el-card>

        <!-- 最近记录列表 -->
        <el-card class="card-list-box file-list-box">
            <template #header>
                <el-text size="large" style="font-weight: bold;color: #000;font-size: 18px;">最近记录</el-text>
            </template>

            <TransitionGroup 
                @before-enter="beforeEnter" 
                @enter="enterEvent"
                @before-leave="beforeLeave" 
                @leave="leaveEvent"
                move-class="move-transtion"
            >   
                <el-space style="width: calc(100% / 7);max-width: 246px;min-width: 200px;"  direction="vertical" alignment="start" :size="10"  v-for="item in recentlyList" :key="item" @click="clickCard(item.type, item.id)">
                    <el-space wrap :size="10" style="width: 100%;">
                        <el-card class="box-card-list" shadow="never" style="border-bottom: 0px;">
                            <div class="card-list-title">
                                <el-row>
                                    <el-col :span="5">
                                        <el-icon v-if="item.type == 1" :size="35" color="#F74800" style="margin-top: 4px;">
                                            <Document/>
                                        </el-icon>

                                        <el-icon v-else :size="35" color="#197BF5" style="margin-top: 4px;">
                                            <Collection/>
                                        </el-icon>
                                    </el-col>

                                    <el-col :span="19">
                                        <div>
                                            <el-text 
                                                size="large" 
                                                line-clamp="1"
                                                style="font-weight: 800;font-size: 14px;"
                                                :title="item.title"
                                            >
                                                {{ item.title ? item.title : '暂无标题'}}
                                            </el-text>
                                        </div>
                                        
                                        <div>
                                            <el-text 
                                                size="large" 
                                                line-clamp="1"
                                                style="font-size: 12px;"
                                            >
                                                {{fromNow(item.updateTime)}}
                                            </el-text>    
                                        </div>
                                    </el-col>
                                </el-row>
                            </div> 
                        </el-card>
                    </el-space>
                </el-space>
            </TransitionGroup>
        </el-card>
    </div>
</template>

<script setup>
    import {onBeforeMount, ref} from 'vue';
    import carouselImg1 from '@/assets/image/1.png';
    import carouselImg2 from '@/assets/image/2.png';
    import carouselImg3 from '@/assets/image/3.png';

    import { 
        Document, 
        Collection, 
        Umbrella
    } from '@element-plus/icons-vue';
    import gsap from "gsap";  //动画
    import {getUserToken,loginInvalid} from "@/utils/userLoginUtils.js";  //用户信息
    import {noteBaseRequest} from "@/request/note_request.js";  // 请求
    import {useRouter} from 'vue-router';  //路由
    import bus from 'vue3-eventbus';   //事件总线
    import {toHref} from '@/router/go';  //路径跳转函数
    import {fromNow} from '@/utils/dayUtils';  //时间格式
    
    const router = useRouter();

    //显示小记卡片是否需要延迟动画
    let enterDelay = true;  
    //隐藏卡片是否需要显示动画
    let hiddenAnimation = false;

    // 轮播图
    let carouselImg = ref([
        {
            id:1,
            src: carouselImg1,
            to: 'https://www.baidu.com',  //
            offsite: true, // 是否为站外地址  
            newTab: true   // 站内地址是否采用新标签页
        },
        {
            id:2,
            src: carouselImg2,
            to: '/thing',  //
            offsite: false, // 是否为站外地址
            newTab: false   // 站内地址是否采用新标签页
        },
        {
            id:3,
            src: carouselImg3,
            to: '/note',  //
            offsite: false, // 是否为站外地址
            newTab: true   // 站内地址是否采用新标签页
        },
    ])

    // 功能按钮事件
    const createMain = (type) => {
        // 跳转到小记页面
        if(type == 'thing'){
            // router.push('/thing').then(() => {
            //     bus.emit('newCreateThing');  //小记新增组件有事件中心监听
            // })
            toHref('/thing', () => {
                bus.emit('newCreateThing');
            });
        }else if(type == 'note'){     
            // router.push('/note').then(() => {
            //     bus.emit('newCreateNote');
            // })
            toHref('/note', () => {
                bus.emit('newCreateNote');
            });
        }
    }
    // 列表数据
    const recentlyList = ref([]);

    // 获取列表数据
    const getrecentlyList = async (ed, ha) => {
        
        enterDelay = ed;  //显示是否需要动画
        hiddenAnimation = ha;  //隐藏是否需要动画
       
        //判断用户的登录状态
        const userToken = await getUserToken();

        const { data: responseData } = await noteBaseRequest.get(
            "/file/recently-use",
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
                message: '获取列表请求失败',
                type: 'error',
            })
            throw '获取列表请求失败'
        })
        if(responseData.success){
            recentlyList.value = [];
            recentlyList.value = responseData.data;  //列表
            // isLoad.value = true;
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
    getrecentlyList(true, true)


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
    
    // 点击卡片事件
    const clickCard = (type,id) => {
        if(type == 1){ // 笔记编辑
            toHref('/note/edit/' + id);
        }else{  //跳转到小记
            toHref('/thing', () => {
                bus.emit('newCreateThing', id);
            });
        }
    }



</script>

<style lang="less" scoped>
    .card-list-box{
        border-radius: 5px;
        margin-bottom: 20px;
        box-shadow: none;
        border: none;
        
    }
    
    .carousel-box{
        // margin-bottom: 20px;
        /deep/.el-card__body {
            padding: 0;
        }
    }
    
    .carousel-img{
        width:100%;
        height:140px;
        object-fit: cover;
    }

    .file-list-box{
        height: calc(100% - 140px - 72px - 40px);
        min-height: 300px;
    }

    /deep/.file-list-box>.el-card__body{
        height: calc(100% - 61px - 60px);
        overflow: hidden;
        overflow-y: auto;
    }

    /deep/.el-space__item{
        width: 100%
    }

    .box-card-list{
        border: none;
        background-color: #F2F6F7;
        cursor: pointer;

        /deep/.el-card__body{
            padding: 10px;
        }

        .card-list-title{
            width: 100%;
            font-size: 14px;   
        }
    }
    
    

</style>