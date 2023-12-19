<template>
    <div>
        <el-dialog 
            :model-value="dialogVisible"
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            :show-close="false"
            :width="500"
        >   
            <!-- 水印 -->
            <el-watermark 
                :width="500"
                :height="500" 
                :content="formValue.isFinished ? '已完成' : ''" 
                :font="{
                    fontSize: 30,
                    color: '#F74800',
                    fontWeight:'600'
                }"
                :offset="[220,0]"
            >
                <el-card class="box-card">
                    <template #header>
                        <!-- 小记标题部分 -->
                        <div>
                            <el-input v-model="formValue.title"  placeholder="请输入小记标题" />
                        </div>

                        <!-- 是否置顶 -->
                        <div style="margin-top:10px;padding: 0px 11px;">
                            <el-row>
                                <el-col :span="2">
                                    <el-text size="large" style="line-height: 32px;" type="info">置顶:</el-text>
                                </el-col>
                                <el-col :span="22">
                                    <el-switch
                                        v-model="formValue.top"
                                        class="ml-2"
                                        style="--el-switch-on-color: #F74800; --el-switch-off-color: #EAE9EA;margin-left: 8px;"
                                    />
                                </el-col>
                            </el-row>
                        </div>

                        <!-- 小记标签部分 -->
                        <div style="margin-top:10px;padding: 0px 11px;">
                            <el-row>
                                <el-col :span="2">
                                    <el-text size="large" style="line-height: 24px;" type="info">标签:</el-text>
                                </el-col>
                                <el-col :span="22">
                                    <el-space wrap>
                                        <!-- 标签列表 -->
                                        <el-tag
                                            v-for="tag in formValue.tags"
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
                                            <el-icon size="14"><Plus /></el-icon>
                                        </el-button>
                                    </el-space>
                                </el-col>
                            </el-row>
                        </div>   
                    </template>

                    <!-- 内容 -->
                    <div class="content">

                        <el-button 
                            v-if="formValue.content.length == 0"
                            style="width: 100%;font-size: 14px;border-style: dashed;"
                            class="button-new-tag dashed" 
                            plain 
                            @click="addTuDoThing(0)"
                        >
                            <el-icon size="14"><Plus /></el-icon>
                            增加一个待办事件
                        </el-button>

                        <template v-else>
                            <el-row v-for="(item,index) in formValue.content" :key="item" style="margin-bottom:10px ;">
                                <!-- 复选框 -->
                                <el-col :span="2">
                                    <el-checkbox v-model="item.checked" label="" size="large" />
                                </el-col>
                                <!-- 输入框 -->
                                <el-col :span="16">
                                    <el-input v-model="item.thing"  placeholder="请输入..." style="--el-input-border:none"/>
                                </el-col>
                                <el-col :span="6">
                                    <!-- 增加 -->
                                    <el-button circle color="#EAE9EA" style="margin-left:15px;" @click="addTuDoThing(index+1)">
                                        <el-icon size="16" color="#74787E"><CirclePlusFilled /></el-icon>
                                    </el-button>
                                    <!-- 删除 -->
                                    <el-button circle color="#EAE9EA" @click="removeTuDoThing(index)">
                                        <el-icon size="16" color="#74787E"><DeleteFilled /></el-icon>
                                    </el-button>
                                </el-col>
                            </el-row>  
                        </template>
                    </div>
                </el-card>
            </el-watermark>
            <template #footer>
                <div>
                    <el-button text bg style="width:calc((100% - 12px) / 2)" @click="dialogVisible = false">取消</el-button>
                    <el-button class="orange-border-btn" gb style="width:calc((100% - 12px) / 2)" @click="dialogVisible = false">保存</el-button>   
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
    import {ref,nextTick, computed} from "vue";
    import { CirclePlusFilled,DeleteFilled,Plus} from '@element-plus/icons-vue';  //图标

    // // 父组件传值
    // const propsData = defineProps({
    //     show:{type:Boolean, default:false},  //是否显示提醒框
    //     describe:{type:String, required:true},  //提醒描述
    //     delectBtn: {type:Boolean, default:true},  //删除按钮是否显示
    //     completeDelectBtn: {type:Boolean, default:true},  //彻底删除是否显示
    // })

    // //自定义事件--彻底删除--删除--取消
    // const emits = defineEmits(['delect','cancel']) 

    const dialogVisible = ref(true);

    const value2 = ref(true)

    // 小记表单值
    const formValue = ref({
        title:'',
        top:false,
        tags:[], //标签
        content:[], //待办事项数组
        finished: computed( () => {
            const contents = formValue.value.content;
            console.log(contents)
            if(contents.length ===0) return false;
            return contents.every(item => item.checked)
        })
    })

    const finished = computed( () => {
        const contents = formValue.value.content;
        if(contents.length ===0) return false;
        return contents.every(item => item.checked)
    })

  
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
        formValue.value.tags.splice(formValue.value.tags.indexOf(tag), 1);
        if(formValue.value.tags.length <= 5){
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
            formValue.value.tags.push(tagsInputValue.value);
        }
        tagsInputVisible.value = false;
        tagsInputValue.value = '';
        if(formValue.value.tags.length >= 5){
            tagsDisabledBtn.value = true;
        }
    }

    // 增加待办事件
    const addTuDoThing = (index) => {
        if(index === 0){
            formValue.value.content.push({
                checked:false,  //是否已经完成
                thing:''  // 待办事项
            });
        }else{
            formValue.value.content.splice(index,0,{
                checked:false,  //是否已经完成
                thing:''  // 待办事项
            });
        }   
    }
    // 删除待办事件
    const removeTuDoThing = (index) => {   
        formValue.value.content.splice(index,1);  
    }

</script>

<style lang="less" scoped>
    /deep/.el-dialog__header{
        display: none;
    }
    /deep/.el-dialog__body{
        padding: 0px;  
    }
    /deep/.el-card{
        box-shadow: none;
    }

    .input-new-tag{
        width: 90px;
        // margin-left: 10px;
        vertical-align: bottom;
    }
    .button-new-tag{
        font-size:18px;
        border-style: dashed;
    }

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

    // 输入框边框样式
    /deep/.el-input{
        --el-input-focus-border: #F74800 !important;
        --el-input-focus-border-color:#F74800;
    }

    // 增加标签按钮样式
    .button-new-tag{
        --el-button-hover-border-color:#F74800;
        --el-button-hover-text-color:#F74800 ;
        --el-button-active-border-color:#F74800;
    }

    /deep/.el-dialog__footer{
        padding: 15px;
    }

    // 橙色边框无背景
    .orange-border-btn{
        border-color:#F74800;
        color: #F74800;
        --el-button-hover-bg-color:#FAD4A9;
    }

    .content{
        max-height: 210px;
        overflow: hidden;
        overflow-y: auto;
        padding: 0px 11px;
    }
    
</style>