<template>
    <div>
        <el-dialog 
            :model-value="propsData.show"
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            :show-close="false"
        >
            <template #header>
                <div class="card-header">
                    <el-icon color="#F9A018" :size="26"><WarningFilled /></el-icon>
                    <el-text style="margin-left:10px;font-size: 18px;">删除提示</el-text>
                </div>  
            </template>

            <div>{{ describe }}</div>

            <template #footer>
                <span class="dialog-footer">
                    <el-button 
                        v-if="propsData.completeDelectBtn"
                        type="danger" 
                        text 
                        bg 
                        @click="emits('delect', true)"
                    >
                        彻底删除
                    </el-button>

                    <el-button 
                        v-if="propsData.delectBtn" 
                        type="danger" 
                        text 
                        bg 
                        @click="emits('delect', false)"
                    >
                        删除
                    </el-button>

                    <el-button 
                        text 
                        bg 
                        @click="emits('cancel')"
                    >
                        取消
                    </el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
    import {ref, computed} from "vue";
    import { WarningFilled} from '@element-plus/icons-vue';  //图标

    // 父组件传值
    const propsData = defineProps({
        show:{type:Boolean, default: false},  //是否显示提醒框
        title: {type:String},   //删除单个文件的标题 
        size: {type:Number, default: 1},  //删除文件个数
        delectBtn: {type:Boolean, default: true},  //删除按钮是否显示
        completeDelectBtn: {type:Boolean, default: true},  //彻底删除是否显示
    })

    //自定义事件--彻底删除--删除--取消
    const emits = defineEmits(['delect','cancel']) 

    // 删除提醒框中的描述
    const describe = computed( () => {
        // 单个文件
        if(propsData.size == 1){
            // 删除按钮和彻底删除按钮同时存在
            if(propsData.delectBtn && propsData.completeDelectBtn){
                return '删除《' + propsData.title + '》可在回收站恢复，彻底删除则无法恢复';
            }

            // 没有删除按钮，有彻底删除按钮
            if(!propsData.delectBtn && propsData.completeDelectBtn){
                return '彻底删除《' + propsData.title + '》将无法恢复';
            }

            return '删除提醒';
        } else {

            return '删除多个文件提醒';
        }
    })


</script>

<style lang="less" scoped>
    .card-header {
        display: flex;
        align-items: start;
    }
</style>