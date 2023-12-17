<template>
    <div>
        <el-dialog 
            :model-value="true"
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            :show-close="false"
            :width="500"
        >
            <el-card class="box-card">
                <template #header>
                    <div>
                        <el-input  placeholder="Please input" />
                    </div>
                    <div style="margin-top:10px;">
                        <el-row>
                            <el-col :span="2">
                                <el-text size="large" style="line-height: 24px;" type="info">标签:</el-text>
                            </el-col>
                            <el-col :span="22">
                                <el-space wrap>
                                    <!-- 标签列表 -->
                                    <el-tag
                                        v-for="tag in dynamicTags"
                                        :key="tag"
                                        class="mx-1"
                                        effect="light"
                                        type="info"
                                        closable
                                        :disable-transitions="false"
                                        @close="handleClose(tag)"
                                    >
                                        {{ tag }}
                                    </el-tag>
                                    
                                    <!-- 标签输入框 -->
                                    <el-input
                                        v-if="tagsInputVisible"
                                        ref="InputRef"
                                        v-model="tagsInputValue"
                                        class="input-new-tag"
                                        size="small"
                                        @keyup.enter="handleInputConfirm"
                                        @blur="handleInputConfirm"
                                    />
                                    <!-- 增加标签按钮 -->
                                    <el-button 
                                        v-else 
                                        :disabled="tagsDisabledBtn"
                                        class="button-new-tag dashed" 
                                        size="small" 
                                        plain 
                                        bg
                                        @click="showInput"
                                    >
                                        +
                                    </el-button>
                                </el-space>
                            </el-col>
                        </el-row>
                    </div>
                    
                </template>

                <div>
                    <el-row v-for="item in 5" :key="item" style="margin-bottom:10px ;">
                        <el-col :span="2">
                            <el-checkbox v-model="checked1" label="" size="large" />
                        </el-col>
                        <el-col :span="17">
                            <el-input  placeholder="Please input" />
                        </el-col>
                        <el-col :span="5">
                            <el-button :icon="Bell" circle color="#EAE9EA" style="margin-left:15px"/>
                            <el-button :icon="Bell" circle color="#EAE9EA" />
                        </el-col>
                    </el-row>  
                </div>
                <template #footer>Footer content</template>

                <!-- <template #footer>
                    <el-button color="#FFCCA9" style="color: #F74800;" plain>新增小记</el-button>
                    <el-button color="#FFCCA9" style="color: #F74800;" plain>新增小记</el-button>
                </template> -->
            </el-card>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">Cancel</el-button>
                    <el-button type="primary" @click="dialogVisible = false">
                    Confirm
                    </el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
    import {ref,nextTick} from "vue";
    import { Plus,Bell} from '@element-plus/icons-vue';  //图标

    // // 父组件传值
    // const propsData = defineProps({
    //     show:{type:Boolean, default:false},  //是否显示提醒框
    //     describe:{type:String, required:true},  //提醒描述
    //     delectBtn: {type:Boolean, default:true},  //删除按钮是否显示
    //     completeDelectBtn: {type:Boolean, default:true},  //彻底删除是否显示
    // })

    // //自定义事件--彻底删除--删除--取消
    // const emits = defineEmits(['delect','cancel']) 
    const checked1 = ref(true)
    
    //增加标签输入框的值
    const tagsInputValue = ref(''); 
    // 标签数组
    const dynamicTags = ref([]);
    // 禁用增加标签  --最多5个标签
    const tagsDisabledBtn = ref(false); 
    // 点击增加标签显示输入框
    const tagsInputVisible = ref(false);
    // 标签增加输入框--ref绑定
    const InputRef = ref(null);
    // 删除标签
    const handleClose = (tag) => {
        dynamicTags.value.splice(dynamicTags.value.indexOf(tag), 1);
        if(dynamicTags.value.length <= 5){
            tagsDisabledBtn.value = false;
        }
    }
    // 标签增加
    const showInput = () => {
        tagsInputVisible.value = true;
        nextTick(() => {
            InputRef.value.focus(); //标签输入框获取焦点
        })
    }
    // 增加标签输入框失去焦点--增加
    const handleInputConfirm = () => {
        if (tagsInputValue.value) {
            dynamicTags.value.push(tagsInputValue.value);
        }
        tagsInputVisible.value = false;
        tagsInputValue.value = '';
        if(dynamicTags.value.length >= 5){
            tagsDisabledBtn.value = true;
        }
    }

</script>

<style lang="less" scoped>
    /deep/.el-dialog__header{
        display: none;
    }
    /deep/.el-dialog__body{
        padding: 0px;
        
    }

    // .el-tag + .el-tag {
    //     margin-left: 10px;
    // }
    // .button-new-tag {
    //     margin-left: 10px;
    //     height: 32px;
    //     line-height: 30px;
    //     padding-top: 0;
    //     padding-bottom: 0;
    // }
    .input-new-tag{
        width: 90px;
        // margin-left: 10px;
        vertical-align: bottom;
    }
    .button-new-tag{
        font-size:18px;
        // border-style: dashed;
    }
    // .button-new-tag:hover{
    //     color: #F74800;
    //     border-color: #F74800;
    // }
    
    // 多选框样式
    .el-checkbox.el-checkbox--large{
        height: 30px;
    }
    /deep/.el-checkbox__input.is-checked .el-checkbox__inner{
        background-color: #F74800;
        border-color: #F74800;
    }
    /deep/.el-checkbox__inner:hover{
        border-color: #F74800;
    }
</style>