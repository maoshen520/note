<template>
    <div class="viewVue-box-nobg">

        <!-- 轮播图 -->
        <el-card class="carousel-box card-list-box">
            <el-carousel height="140px">
                <el-carousel-item v-for="item in carouselImg" :key="item.id">
                    <img class="carousel-img" :src="item.url" alt="" srcset="">
                </el-carousel-item>
            </el-carousel>
        </el-card>

        <!-- 功能按钮 -->
        <el-card class="card-list-box">
            <el-button 
                text 
                :bg="true"
                style="font-size: 16px;padding: 8px 15px;color:#F74800 " 
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
                @click="createMain('thing')"
            >
                <el-icon :size="20" color="red" style="margin-right: 5px;">
                    <Umbrella/>
                </el-icon>
                模板中心
            </el-button>
        </el-card>

        <!-- 最近记录 -->
        <el-card class="card-list-box">
            <template #header>
                <el-text size="large" style="font-weight: bold;color: #000;font-size: 18px;">最近记录</el-text>
            </template>

            <el-space direction="vertical" alignment="start" :size="10"  v-for="item in 10" :key="item">
                <el-space wrap :size="10" >
                    <el-card class="box-card-list" shadow="never" style="border-bottom: 0px;">
                        <div class="card-list-title">
                            <el-row>
                                <el-col :span="5">
                                    <el-icon v-if="item == 2" :size="35" color="#F74800" style="margin-top: 4px;">
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
                                            title="的数据返回数据端口和封建士大夫计划时间"
                                        >
                                            的数据返回数据端口和封建士大夫计划时间
                                        </el-text>
                                    </div>
                                    
                                    <div>
                                        <el-text 
                                            size="large" 
                                            line-clamp="1"
                                            style="font-size: 12px;"
                                        >
                                            4个月前
                                        </el-text>    
                                    </div>
                                </el-col>
                            </el-row>
                        </div> 
                    </el-card>
                </el-space>
            </el-space>
        </el-card>
    </div>
</template>

<script setup>
    import {ref} from 'vue';
    import carouselImg1 from '@/assets/image/1.png';
    import carouselImg2 from '@/assets/image/2.png';
    import carouselImg3 from '@/assets/image/3.png';

    import { 
        Document, 
        Collection, 
        Umbrella
    } from '@element-plus/icons-vue';

    import {useRouter} from 'vue-router';
    import bus from 'vue3-eventbus';
    
    const router = useRouter();

    // 轮播图
    let carouselImg = ref([
        {
            id:1,
            url: carouselImg1
        },
        {
            id:2,
            url: carouselImg2
        },
        {
            id:3,
            url: carouselImg3
        },
    ])

    const createMain = (type) => {
        if(type == 'thing'){
            router.push('/thing').then(() => {
                bus.emit('newCreateThing');
            })
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
        // height:140px;
        object-fit: cover;
    }

    .box-card-list{
        border: none;
        background-color: #F2F6F7;
        /deep/.el-card__body{
            padding: 10px;
        }
        .card-list-title{
            width: 216px;
            font-size: 14px;
            // font-weight: 600;
            // overflow: hidden;
            // text-overflow: ellipsis;
            // white-space: nowrap;

            .el-row{
                .el-col{
                    // display: flex;
                    // align-items: center;
                    // justify-content: center;
                }
                
            }
            
        }
    }
    
    

</style>