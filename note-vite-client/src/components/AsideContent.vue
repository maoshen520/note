<!-- 左侧导航栏组件 -->

<template>
    <div style="padding: 30px 0px;">
        <el-space direction="vertical" :size="30">

            <el-popover
                placement="left-start"
                :width="120"
                trigger="hover"
                :offset="-5"
            >
                <template #reference>
                    <el-button plain style="border: none;">
                        <!-- <el-icon :size="32" color="#22B14C"> -->
                        <el-icon :size="32" color="#F74800">
                            <CirclePlusFilled/>
                        </el-icon>
                    </el-button >
                </template>

                <div>
                    <div>
                        <el-button text style="font-size: 16px;padding: 8px 15px;" @click="createMain('note')">
                            <el-icon :size="20" color="#F74800" style="margin-right: 5px;">
                                <Document/>
                            </el-icon>
                            新增笔记
                        </el-button>
                    </div>
                    <div>
                        <el-button text style="font-size: 16px;padding: 8px 15px;" @click="createMain('thing')">
                            <el-icon :size="20" color="#197BF5" style="margin-right: 5px;">
                                <Collection/>
                            </el-icon>
                            新增小记
                        </el-button>
                    </div>
                </div>
            </el-popover>

            <el-tooltip
                class="box-item"
                effect="light"
                :offset="-5"
                :content="item.label"
                placement="right"
                v-for="(item,index) in menuList" 
                :key="item.label"
            >   
                <el-button 
                    style="border: none;background-color: #fff;--el-button-hover-text-color:#F74800;"
                    plain
                    @click="menuClick(item.to)">
                    <el-icon :size="item.iconSize" :color="isHightMenu(item.to) ? '#F74800' : ''">
                        <component :is="item.icon" />
                    </el-icon>
                </el-button>
            </el-tooltip>


            
            <!-- <el-button text dark>
                <el-icon :size="26" >
                    <Search/>
                </el-icon>
            </el-button >
            
            <el-button text>
                <el-icon :size="26" >
                    <House/>
                </el-icon>
            </el-button >

            <el-button text>
                <el-icon :size="26" >
                    <Document/>
                </el-icon>
            </el-button >

            <el-button text>
                <el-icon :size="26" >
                    <Collection/>
                </el-icon>
            </el-button >

            <el-button text>
                <el-icon :size="26" >
                    <Star/>
                </el-icon>
            </el-button >
            <el-button text>
                <el-icon :size="26" >
                    <Delete/>
                </el-icon>
            </el-button >
            <el-button text>
                <el-icon :size="26" >
                    <ShoppingBag/>
                </el-icon>
            </el-button > -->
            
            <!-- <el-divider direction="vertical" /> -->
            

        </el-space>
    </div>
</template>

<script setup>
    import {watch,ref} from 'vue'
    import {
        CirclePlusFilled, 
        Search, 
        Clock, 
        Document, 
        Collection, 
        Star, 
        Delete, 
        ShoppingBag, 
    } from '@element-plus/icons-vue';
    import {useRouter} from 'vue-router';
    import bus from 'vue3-eventbus';
    
    const router = useRouter();

    const routerPath = ref(router.currentRoute.value.path);  //路由地址

    watch(
        () => router.currentRoute.value,
        newData => {
            // routerPath.value ='/' + newData.path.split('/')[1];
            routerPath.value = newData.path;
        }
    )
    
    // 是否高亮显示
    const isHightMenu = (toRouterPath) => {
        if(!toRouterPath) return false;
        return routerPath.value.startsWith(toRouterPath);  //判断是否已父级路由开头
    }

    const createMain = (type) => {
        if(type == 'thing'){
            router.push('/thing').then(() => {
                bus.emit('newCreateThing');
            })
        }else{
            router.push('/note').then(() => {
                bus.emit('newCreateNote');
            })
        }
    }

    let menuList = [
        {
            label:'搜索',
            icon: Search,
            iconSize: 26,
            to:''
        },
        {
            label:'最近操作',
            icon: Clock,
            iconSize: 26,
            to:'/recently'
        },
        {
            label:'笔记',
            icon: Document,
            iconSize: 26,
            to:'/note'
        },
        {
            label:'小记',
            icon: Collection,
            iconSize: 26,
            to:'/thing'
        },
        {
            label:'收藏',
            icon: Star,
            iconSize: 26,
            to:''
        },
        {
            label:'商城',
            icon: ShoppingBag,
            iconSize: 26,
            to:''
        },
        {
            label:'回收站',
            icon: Delete,
            iconSize: 26,
            to:'/recycle'
        },
        
    ];

    const menuClick = (to) => {
        router.push(to)
    }

</script>

<style lang="less" scoped>

</style>