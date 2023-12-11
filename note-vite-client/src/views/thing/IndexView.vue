<template>
    <div>
        <!-- 小记列表页的标头 -->
        <el-card class="box-card" shadow="never">
            <div class="card-header">
                <h3>小记列表</h3>
                <el-button color="#FFCCA9" style="color: #F74800;" plain>新增小记</el-button>
            </div>
        </el-card>

        <el-card class="" shadow="never" >
            <el-space direction="vertical" alignment="start" :size="10" v-for="(item,index) in 20" :key="index">
                <el-space wrap :size="10" >
                    <el-card class="box-card-list" shadow="never" >
                        第一张卡片
                    </el-card>
                </el-space>
            </el-space>
            
        </el-card>

       
     
    </div>
</template>

<script setup>

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
    .box-card-list{
        border: none;
        background-color: #FAFAFC;
    }
</style>