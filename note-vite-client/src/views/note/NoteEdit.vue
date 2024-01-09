<template>
    <div style="margin: 20px;width:calc(100% - 40px);height: calc(100% - 40px);">

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
        
        <div style="background-color: #fff;height: 500px;">
            <ckeditor5 
                :editor="editorType" 
                @ready="readyEditor"
                v-model="note.content"
                :config="getEditorConfigs()"
            />
            <!-- <ckeditor5 
                :editor="editorType" 
                @ready="readyEditor"
                v-model="note.content"
                :config="getEditorConfigs()"
            /> -->
        </div>


    </div>
</template>

<script setup>
    import {ref,watch} from 'vue'
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

    // 获取笔记信息
    const getNoteEdit = async () => {
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
    const readyEditor = (editor) => {
        // 在编辑器区域插入工具栏
        editor.ui.getEditableElement().parentElement.insertBefore(
            editor.ui.view.toolbar.element,
            editor.ui.getEditableElement()
        );
    }

</script>

<style lang="less" scoped>
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

</style>