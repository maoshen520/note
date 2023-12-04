<template>
    <div style="padding: 30px 16px;">
        <el-space direction="vertical" :size="30">
            <div>
                <!-- <el-icon :size="32" color="#22B14C"> -->
                <el-icon :size="32" color="#F74800">
                    <CirclePlusFilled/>
                </el-icon>
            </div>
            <div>
                <el-icon :size="26" >
                    <Search/>
                </el-icon>
            </div>

            <!-- <el-divider /> -->
            
            <div>
                <el-icon :size="26" >
                    <House/>
                </el-icon>
            </div>

            <div>
                <el-icon :size="26" >
                    <Document/>
                </el-icon>
            </div>

            <div>
                <el-icon :size="26" >
                    <Collection/>
                </el-icon>
            </div>

            <div>
                <el-icon :size="26" >
                    <Star/>
                </el-icon>
            </div>
            <div>
                <el-icon :size="26" >
                    <Delete/>
                </el-icon>
            </div>
            <div>
                <el-icon :size="26" >
                    <QuestionFilled/>
                </el-icon>
            </div>
            
            <!-- <el-divider direction="vertical" /> -->
            

        </el-space>
    </div>
</template>

<script setup>
    import {CirclePlusFilled, Search, House, Document, Collection, Star, Delete, QuestionFilled} from '@element-plus/icons-vue';
</script>

<style lang="less" scoped>

</style>