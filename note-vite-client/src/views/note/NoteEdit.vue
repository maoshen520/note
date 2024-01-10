<template>
    <!-- 骨架屏 -->
    <div v-if="loading" style="margin: 20px;width:calc(100% - 40px);height: calc(100% - 40px);">
        <el-skeleton animated>
            <template #template>
                <el-card class="box-card" shadow="never">
                    <div class="card-header"> 
                        <el-skeleton-item style="width: 50%;height: 32px;"/>
                        <el-space>
                            <el-skeleton-item style="width: 32px;height: 32px;border-radius: 50%;"/>
                            <el-skeleton-item style="width: 32px;height: 32px;border-radius: 50%;"/>
                            <el-skeleton-item style="width: 32px;height: 32px;border-radius: 50%;"/>
                        </el-space> 
                    </div>
                </el-card>

                <el-card class="box-card" shadow="never" style="margin-top: 20px;">
                    <div class="card-header">
                        <el-skeleton-item style="width: 100%;height: 32px;"/>
                    </div>
                    <div style="margin-top: 20px;">
                        <el-skeleton-item style="width: 100%;height: 26px;margin: 10px 0px;"/>
                        <el-skeleton-item style="width: 80%;height: 26px; margin: 10px 0px;"/>
                        <el-skeleton-item style="width: 100%;height: 26px; margin: 10px 0px;"/>
                        <el-skeleton-item style="width: 80%;height: 26px; margin: 10px 0px;"/>
                    </div>
                </el-card>
            </template>
        </el-skeleton>
    </div>

    <div v-else style="margin: 20px;width:calc(100% - 40px);height: calc(100% - 40px);">
        <!-- 头部 -->
        <el-card class="box-card" shadow="never">
            <div class="card-header"> 
                <el-space>
                    <span style="display: inline-block;width: 10px;height: 10px;border-radius: 50%;background-color: #F74800;"></span>
                    <span style="color: #919091;">保存并发布于：{{ note.updateTime }}</span>
                </el-space>

                <el-space>
                    <!-- 分享 -->
                    <el-button 
                        round 
                        size="small"
                        style="background-color: #fff;--el-button-hover-text-color:#F74800;--el-button-hover-border-color:#F74800;margin-right: 6px;"
                    >
                        分享
                    </el-button>

                    <!-- 收藏 -->
                    <el-tooltip
                        class="box-item"
                        effect="light"
                        :offset="0"
                        content="收藏"
                        placement="top"
                    >  
                        <el-button 
                            plain
                            style="border: none;background-color: #fff;--el-button-hover-text-color:#F74800;padding: 8px 5px;"
                        >
                            <!-- <el-icon :size="26" color="#F74800"> -->
                            <el-icon :size="20">
                                <Star/>
                            </el-icon>
                        </el-button>
                    </el-tooltip>

                    <el-tooltip
                        class="box-item"
                        effect="light"
                        :offset="0"
                        content="更多"
                        placement="top"
                    >  
                        <el-button 
                            plain
                            style="border: none;background-color: #fff;--el-button-hover-text-color:#F74800;padding: 8px 5px;"
                        >
                            <el-icon :size="20">
                                <MoreFilled/>
                            </el-icon>
                        </el-button>
                    </el-tooltip>
                </el-space> 
            </div>
        </el-card>
        
        <div style="background-color: #fff;height: 500px;margin-top: 20px;">
            <ckeditor5 
                :editor="editorType" 
                @ready="readyEditor"
                v-model="note.content"
                :config="getEditorConfigs()"
            />
        </div>
    </div>
</template>

<script setup>
    import {ref,watch,onMounted} from 'vue'
    import {getUserToken,loginInvalid} from "@/utils/userLoginUtils.js";
    import {noteBaseRequest} from "@/request/note_request.js";
    import {
        Star,
        MoreFilled
    } from '@element-plus/icons-vue';

    // 文本编辑器
    import CKEditor from '@ckeditor/ckeditor5-vue';
    import {editorType, getEditorConfigs} from '@/editor'

    const ckeditor5 = CKEditor.component;  //注册ckeditor5组件
    
    const propsData = defineProps({
        id: {type: String, required: true}
    })
    const note = ref({});

    const loading = ref(true);

    // 富文本编辑器对象
    let editor = null;

    // 获取笔记信息
    const getNoteEdit = async () => {

        loading.value = true;
        
        //判断用户的登录状态
        const userToken =  await getUserToken();

        const { data: responseData } = await noteBaseRequest.get(
            "/note/edit",
            {   
                params: {
                    noteId:propsData.id
                },
                headers: {
                    userToken:userToken
                }
            }
        ).catch(() => {
            ElMessage({
                message: '获取笔记请求失败',
                type: 'error',
            })
            throw '获取笔记请求失败';
        })
        // console.log(responseData.data.content)
        if(responseData.success){
            note.value = responseData.data;
            loading.value = false;
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
    getNoteEdit();

    watch(
        () => propsData.id,
        () => {
            getNoteEdit();
        }
    )

    //富文本编辑器初始化完毕
    const readyEditor = (editorObj) => {
        editor = editorObj;
        // 在编辑器区域插入工具栏
        editorObj.ui.getEditableElement().parentElement.insertBefore(
            editorObj.ui.view.toolbar.element,
            editorObj.ui.getEditableElement()
        );
    }

    onMounted( () => {
        window.addEventListener('keydown', (e) => {
            if(e.keyCode === 83 && e.ctrlKey){
                e.preventDefault();
                e.returnValue = false;
                saveNote();
            }
        })
    })

    const saveNote = async () => {
        // 4. 可通过editor.plugins.get('Title').getBody()获取除标题外的内容（内容包含HTML元素）
        // 5. 可通过editor.plugins.get('Title').getTitle()获取除文档标题（文档标题不包含HTML元素）
        // console.log(propsData.id)
        // console.log('文档标题 ==>',editor.plugins.get('Title').getTitle())
        // console.log('内容 ==>',editor.plugins.get('Title').getBody())
        // console.log('完整内容 ==>', note.value.content)

        const noteId = propsData.id;
        const title = editor.plugins.get('Title').getTitle();
        const body = editor.plugins.get('Title').getBody();
        const content = note.value.content;

        //判断用户的登录状态
        const userToken =  await getUserToken();

        const { data: responseData } = await noteBaseRequest.post(
            "/note/save",
            {noteId, title, body, content},
            {   
                headers: {
                    userToken:userToken
                }
            }
        ).catch(() => {
            ElMessage({
                message: '保存笔记请求失败',
                type: 'error',
            })
            throw '保存笔记请求失败';
        })

        if(responseData.success){
            note.value.updateTime = responseData.data;
            ElMessage({
                message: '保存成功',
                type: 'success',
            })
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

</script>

<style lang="less">
    .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .box-card{
        border: none;
        box-shadow: none;
        /deep/.el-card__body{
            padding: 10px 20px;
        }
    }

    // 编辑器样式修改
    .ck.ck-balloon-panel.ck-balloon-panel_visible{
        display: none ;
    }
    .ck.ck-editor__editable_inline{
        border: none;
    }
    .ck.ck-editor__editable.ck-focused:not(.ck-editor__nested-editable){
        outline: none;
        border: none;
        box-shadow: none;
    }
    .ck-content{
        overflow-y: scroll;
    }
</style>