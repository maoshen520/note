<template>
    <el-dropdown
        trigger="click"
        :hide-on-click="false"
        ref="dropdown"
        class="dropdownBox"
        :style="{top:clickRightClientY,left:clickRightClientX}"
        
    >
        <span class="el-dropdown-link">
        <!-- Dropdown List -->
        </span>
        <template #dropdown>
            <el-dropdown-menu>
                <el-dropdown-item>
                    <el-button plain class="popoverBox-button">
                        <el-icon :size="16" style="margin-right: 5px;" >
                            <EditPen/>
                        </el-icon>
                        重命名笔记
                    </el-button>
                </el-dropdown-item>

                <el-dropdown-item style="padding: 0px;">
                    <el-button plain class="popoverBox-button" @click="emits('delete',{id,title})">
                        <el-icon :size="16" style="margin-right: 5px;">
                            <Delete/>
                        </el-icon>
                        删除笔记
                    </el-button>
                </el-dropdown-item>
                <el-dropdown-item >
                    <el-button plain class="popoverBox-button" :disabled="topBtnDisabled"  @click="topNote(!propsData.top)">
                        <el-icon size="16" style="margin-right: 5px;"><component :is="thingCardTopContext().icon" /></el-icon>
                        {{thingCardTopContext().text}}
                    </el-button>
                    
                </el-dropdown-item>
                
                
            </el-dropdown-menu>
        </template>
    </el-dropdown>
</template>

<script setup>
    import {ref} from 'vue';
    import {
        EditPen,
        Delete,
        Download,
        Upload
    } from '@element-plus/icons-vue';
    import {disabledBtn} from "@/utils/disabledBtn.js";  //禁用按钮
    import {getUserToken,loginInvalid} from "@/utils/userLoginUtils.js";
    import {noteBaseRequest} from "@/request/note_request.js";

    let propsData = defineProps({
        clickRightClientY: {type: String, required:true},
        clickRightClientX: {type: String, required:true},
        top: {type: Boolean, required:true},
        id: {type: Number, required:true},
        title: {type:String, required:true},   //标题
        // isShowRight: {type: Boolean, required:true},
    })

    //自定义事件
    const emits = defineEmits(['delete', 'changeStatus']) 

    // 置顶按钮和取消置顶按钮显示对象
    const thingCardTopContext = () => {
        if(propsData.top){
            return {
                icon:Download,
                text:'取消置顶'
            }
        }else{
            return {
                icon:Upload,
                text:'置顶'
            }
        }
    }

    // 打开菜单
    const dropdown = ref(null)
    const dropdownOpen = () => {
        dropdown.value.handleOpen();  //打开菜单
    }

    //禁用置顶/取消置顶按钮
    const topBtnDisabled = ref(false);

    // 置顶笔记（取消置顶）
    const topNote = async (isTop) => {
        //判断用户的登录状态
        const userToken =  await getUserToken();

        // 禁用按钮
        // topBtnDisabled.value = true;  //禁用按钮
        disabledBtn(topBtnDisabled, true);

        const { data: responseData } = await noteBaseRequest.get(
            "/note/top",
            {
                params:{isTop, noteId: propsData.id},
                headers: {
                    userToken:userToken
                }
            }
        ).catch(() => {
            ElMessage({
                message:isTop ? '置顶笔记请求失败' : '取消置顶笔记请求失败',
                type: 'error',
            })
            // topBtnDisabled.value = false; //解除禁用按钮
            disabledBtn(topBtnDisabled, false, true, 1);
            dropdown.value.handleClose();  //关闭菜单
            throw isTop ? '置顶笔记请求失败' : '取消置顶笔记请求失败';
        })
        if(responseData.success){
            // things.value = responseData.data;  //小记列表
            ElMessage({
                message:responseData.message,
                type: 'success',
            });
            emits('changeStatus');  //调用父组件的请求列表的方法
            // topBtnDisabled.value= false; //解除禁用按钮
        }else{
            ElMessage({
                message: responseData.message,
                type: 'error'
            });
            // topBtnDisabled.value= false; //解除禁用按钮
            // 登录已失效
            if(responseData.code == 'L_008'){
                loginInvalid(true);
            }
        }
        disabledBtn(topBtnDisabled, false, true, 1);
        dropdown.value.handleClose();  //关闭菜单
    }

    

    defineExpose({dropdownOpen});
</script>

<style lang="less" scoped>
    .popoverBox{
        background-color: #fff;
        width: 113px;
        height: 100px;
        position:fixed;
        z-index: 1000;
        box-shadow: 0px 0px 10px #dcdfe6;
        border-radius: 2px;
       
    }
    .popoverBox-button{
        width: 100%;
        font-size: 14px;
        padding:0px 5px;
        border: none;
        --el-button-hover-text-color:#F74800;
        justify-content:left
    }

    /deep/.el-popper{
        overflow: hidden;
    }

    /deep/.el-dropdown-menu__item{
        padding: 0px;
    }
    /deep/.el-popper__arrow{
        display: none;
        width: 0px;
        height: 0px;
        left: -5px !important;
    }
    .el-popper[data-popper-placement^=bottom]>.el-popper__arrow{
        top: 0px !important;
    }
    
</style>