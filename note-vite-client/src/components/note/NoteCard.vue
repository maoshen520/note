
<!-- 笔记卡片 -->
<!-- npm install html-to-text -->

<template>
    <div style="position: relative;">
        <el-card class="card-list" :class="propsData.cardIndex == propsData.id ? 'card-active' : ''" @click.right="handleContextmenu">
            <template #header>
                <div class="card-header">
                    <span>{{ propsData.title }}</span>
                </div>
            </template>
            <div>
                <div class="content">
                   {{ htmlToText(propsData.body)}}
                </div>

                <div style="margin-top: 8px;">
                    <el-button v-if="propsData.top" size="small" type="success" color="#EAE9EA" style="color: #F74800;font-weight: bold;padding:0px 5px;height: 18px;background-color: #FFCCA9;margin-right: 20px;margin-top: 0px;">置顶</el-button>
                    <el-text class="mx-1" size="small" type="info">{{ propsData.time }}</el-text>
                </div>
            </div> 
            <!-- <div class="popoverBox" ref="popoverCard" :style="{top:t,left:l}"></div>   -->
        </el-card>
        
    </div>
</template>

<script setup>
    import { ref,watch } from 'vue';
    import {htmlToText} from 'html-to-text';
    //自定义属性
    // const propsData = defineProps({
    //     id: {type:Number, required:true},   //小记标号
    //     finished: {type:Boolean, default: false},  //是否已经完成
    //     title: {type:String, required:true},   //标题
    //     top: {type:Boolean, default: false},  //是否置顶
    //     tags: {type:Array, default:['暂无标签']},  //标签
    //     time: {type:String, required:true},   //最后一次修改的时间
    // })
    
    // 自定义属性
    const propsData = defineProps({
        id: {type: Number, required:true},
        title: {type:String, required:true},
        body: {type:String, required:true},
        top: {type:Boolean, required:true},
        time: {type:String, required:true},
        cardIndex: {type:Number, required:false},
    })

    // const cardIndex = ref(null)
    // const cardClick = () => {
    //     console.log(propsData)
    //     cardIndex.value = propsData.id;
    //     console.log(cardIndex.value)
    // }
    

    const popoverCard = ref(null)
    const t = ref(0)
    const l = ref(0)

    const handleContextmenu = (e) => {
        e.preventDefault()
        console.log(e)
        // this.$refs.menu.show(e.clientX, e.clientY)
        // t.value = e.clientY + 'px'
        // l.value = e.clientX + 'px'
        t.value = e.layerY + 'px'
        l.value = e.layerX + 'px'

    }
</script>

<style lang="less" scoped>
    .card-list{
        border: none;
        box-shadow: none;
        cursor: pointer;
        // position: relative;
    }
    .card-list:hover{
        background-color: #F3F7F8;
    }
    .card-active{
        background-color: #F3F7F8;
    }

    /deep/.el-card__header{
        padding: 10px 20px;
        border-bottom: none;
    }
    /deep/.el-card__body{
        padding: 10px 20px 15px 20px;
    }
    .card-header{
        font-size: 14px;
        font-weight: 600;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    .content{
        font-size: 14px;
        color: #808080;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
    }

    .popoverBox{
        background-color: #F74800;
        width: 100px;
        height: 100px;
        position: absolute;
        z-index: 1000;
    }
</style>
