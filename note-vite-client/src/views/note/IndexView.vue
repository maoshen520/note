
<!-- 笔记父组件 -->

<template>
    <div style="background-color: #fff;height: 100%;">
        <div class="note-list-box">
            <!-- 小记列表页的标头 -->
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

            <el-card class=" card-list card-active">
                <template #header>
                    <div class="card-header">
                        <span>Card name</span>
                    </div>
                </template>
                <div v-for="o in 4" :key="o" class="text item">{{ 'List item ' + o }}</div>
            </el-card>

        </div>
    </div>
</template>

<script setup>
    import {
        CirclePlusFilled, 

    } from '@element-plus/icons-vue';

</script>

<style lang="less" scoped>
    .note-list-box{
        width: 300px;
        height: 100%;
        border-right: 1px solid #dcdfe6 ;
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
        border: none;
        box-shadow: none;
    }
    .card-list:hover{
        background-color: #F3F7F8;
    }
    .card-active{
        background-color: #F3F7F8;
    }
</style>