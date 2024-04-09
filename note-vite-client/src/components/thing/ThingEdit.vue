<template>
    <div>
        <el-dialog 
            :model-value="dialogVisible"
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            :show-close="false"
            :width="500"
            @close="resetEditThing()"
        >   
            <!-- 骨架屏 -->
            <el-skeleton v-show="loading" animated>
                <template #template>
                    <el-card class="" shadow="never">
                        <!-- 头部 -->
                        <template #header>
                            <div class="card-header">
                                <div class="card-list-title" >
                                    <el-skeleton-item style="height: 25px;"/>    
                                </div> 

                                <!-- 是否置顶 -->
                                <div style="margin-top:10px;padding: 0px 11px;">
                                    <el-row>
                                        <el-col :span="2">
                                            <el-skeleton-item style="height: 25px;"/>
                                        </el-col>
                                        <el-col :span="1">
                                            
                                        </el-col>
                                        <el-col :span="3">
                                            <el-skeleton-item style="height: 25px;"/>
                                        </el-col>
                                    </el-row>
                                </div>

                                <!-- 标签 -->
                                <div style="margin-top:10px;padding: 0px 11px;">
                                    <el-row>
                                        <el-col :span="2">
                                            <el-skeleton-item style="height: 25px;"/>
                                        </el-col>
                                        <el-col :span="1"></el-col>
                                        <el-col :span="3">
                                            <el-skeleton-item style="height: 25px;"/>
                                        </el-col>
                                        <el-col :span="1"></el-col>
                                        <el-col :span="3">
                                            <el-skeleton-item style="height: 25px;"/>
                                        </el-col>
                                        <el-col :span="1"></el-col>
                                        <el-col :span="3">
                                            <el-skeleton-item style="height: 25px;"/>
                                        </el-col>
                                        <el-col :span="1"></el-col>
                                        <el-col :span="3">
                                            <el-skeleton-item style="height: 25px;"/>
                                        </el-col>
                                    </el-row>
                                </div>
                            </div>
                        </template>

                        <!-- 内容 -->
                        <div>
                            <el-skeleton-item style="height: 25px;margin-bottom: 10px;"/>
                            <el-skeleton-item style="height: 25px;margin-bottom: 10px;"/>
                        </div>   
                    </el-card> 

                </template>
            </el-skeleton>

            <!-- 水印 -->
            <el-watermark 
                v-show="!loading"
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
                                            v-for="(tag,index) in formValue.tags"
                                            :key="index"
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
                                    <el-input v-model="item.thing"  placeholder="请输入..." style="--el-input-border:none;"/>
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
            <template #footer >
                <div>
                    <el-button text bg style="width:calc((100% - 12px) / 2)" @click="dialogVisible = false">取消</el-button>
                    <el-button v-show="!loading" :disabled="saveBtnDisabled" class="orange-border-btn" gb style="width:calc((100% - 12px) / 2)" @click="saveThing()">保存</el-button>   
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
    import {ref,nextTick, computed,h, onBeforeUnmount} from "vue";
    import { CirclePlusFilled,DeleteFilled,Plus} from '@element-plus/icons-vue';  //图标
    import {getUserToken,loginInvalid} from "@/utils/userLoginUtils.js";
    import {disabledBtn} from "@/utils/disabledBtn.js";
    import {noteBaseRequest} from "@/request/note_request.js";

    import bus from 'vue3-eventbus'; 


    //自定义事件
    const emits = defineEmits(['save']) 
    
    //是否显示模态框
    const dialogVisible = ref(false);   
    
    //是否加载中---显示骨架屏
    const loading = ref(true);  

    // 该小记的用户的编号
    const userId = ref(null);  

    //小记编号
    const thingId = computed(() => formValue.value.id)

    // 小记表单值
    const formValue = ref({
        id: null,   //小记编号（修改）
        title:'',  //标题
        top:false,  //是否置顶
        tags:[], //标签
        content:[], //待办事项数组
        isFinished: computed( () => {  //是否完成
            const contents = formValue.value.content;
            if(contents.length ===0) return false;
            return contents.every(item => item.checked)
        })
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

    // 保存小记
    const saveThing = () => {
        let html = '';
        let num = 0;
        
        if(formValue.value.title === ''){
            num += 1;
            html += '<div style="font-size: 16px;color: #FF0000;margin:10px 0px">'+ num +'：请设置编辑小记的标题</div>'
        }

        if(formValue.value.tags.length === 0){
            num += 1;
            html += '<div style="font-size: 16px;color: #FF0000;margin:10px 0px">'+ num +'：请设置编辑小记的标签</div>'
        }

        if(formValue.value.content.length === 0){
            num += 1;
            html += '<div style="font-size: 16px;color: #FF0000;margin:10px 0px">'+ num +'：请设置编辑小记的待办事项</div>'
        }

        if(html === ''){
            if(formValue.value.id === null){  //新增
                createOrUpdate(true);
            }else{  //编辑
                createOrUpdate(false);
            }
        }else{
            ElNotification({
                title: '编辑小记保存提醒',
                type: 'error',
                dangerouslyUseHTMLString: true,
                // message: h('i', { style: 'color: teal' }, 'This is a reminder'),
                message: html,
            })
        } 
    }

    /**
     * 显示编辑小记的模态框
     * @param {number} id  小记编号（无值--新增小记   有值---编辑小记）
     */
    const showDialogVisible = id => {
        dialogVisible.value = true;
        loading.value = true;
        if(!id){
            loading.value = false;
        }else{
            formValue.value.id = id;
            getEditThing(id);
        }
    }

    // 重置
    const resetEditThing = () => {
        formValue.value.id = null;
        formValue.value.title = '';
        formValue.value.top = false;
        formValue.value.tags = [];
        formValue.value.content = [];
    }

    // 保存按钮是否需要禁用
    const saveBtnDisabled = ref(false)

    // 新增或修改小记的保存
    const createOrUpdate = async (type) => {
        //判断用户的登录状态
        const userToken = await getUserToken();

        // 按钮禁用
        disabledBtn(saveBtnDisabled, true);

        const thingObj = formValue.value;

        let url = '';

        let obj = {   
            title: thingObj.title,
            top: thingObj.top,
            tags: thingObj.tags.join(),
            content: JSON.stringify(thingObj.content),
            finished: thingObj.isFinished,
        };

        let errorText = '';

        if(type){  //新增
            url = '/thing/create';
            errorText = '新增小记请求失败';
        }else{  //编辑
            url = '/thing/update';
            obj.thingId = formValue.value.id;
            errorText = '修改小记请求失败';
        }

        // const { data: responseData } = await noteBaseRequest({
        //     method:'post/get',
        //     url,
        //     date:obj,
        //     headers: {userToken}
        // }).catch(() => {})

        const { data: responseData } = await noteBaseRequest.post(
            url,
            obj,
            {
                headers: {
                    userToken:userToken
                }
            }
        ).catch(() => {
            ElMessage({
                message: errorText,
                type: 'error',
            })
            disabledBtn(saveBtnDisabled, false, true, 2);
            throw errorText;
        })
        disabledBtn(saveBtnDisabled, false, true, 1);
        if(responseData.success){
            ElMessage({
                message: responseData.message,
                type: 'success',
            })
            dialogVisible.value = false;
            emits('save', false, false); // 不需要显示延迟动画
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

    // 获取小记信息
    const getEditThing = async (thingId) => {
        //判断用户的登录状态
        const userToken = await getUserToken();

        const { data: responseData } = await noteBaseRequest.get(
            "/thing/edit",
            {   
                params:{
                    thingId
                },
                headers: {
                    userToken:userToken
                }
            }
        ).catch(() => {
            ElMessage({
                message: '获取小记请求失败',
                type: 'error',
            })
            dialogVisible.value = false;   //关闭弹窗
            throw '获取小记请求失败'
        })
        if(responseData.success){
            loading.value = false;
            const editThing = responseData.data;
            userId.value = editThing.userId;  //当前小记的用户编号
            formValue.value.title = editThing.title;
            formValue.value.top = !!editThing.top;
            formValue.value.tags = editThing.tags.split(',');
            formValue.value.content = JSON.parse(editThing.content);
        }else{
            ElMessage({
                message: responseData.message,
                type: 'error'
            });
            dialogVisible.value = false;  //关闭弹窗
            // 登录已失效
            if(responseData.code == 'L_008'){
                loginInvalid(true);
            }
        }
    }

    // 将哪些函数导出
    defineExpose({showDialogVisible, dialogVisible, userId, thingId});

    // 监听知否触发了新建小记事件----显示新建小记窗口
    bus.on('newCreateThing', showDialogVisible);

    // 组件卸载之前，移除监听事件
    onBeforeUnmount(() => {
        bus.off('newCreateThing', showDialogVisible);  //停止监听新建小记事件
    })

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

    // 标签输入框样式
    .input-new-tag{
        width: 90px;
        vertical-align: bottom;
        margin-left: 8px;
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

    // 保存提示文字样式
    .saveErrorText{
        font-size: 16px;
        color: #FF0000;
    }

    /deep/.el-tag.is-closable{
        margin-left: 8px;
    }
    
</style>