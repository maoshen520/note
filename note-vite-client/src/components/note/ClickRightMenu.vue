<template>
    <el-dropdown
        trigger="click"
        ref="dropdown1"
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
                    <el-button plain class="popoverBox-button"  @click="emits('delete')">
                        <el-icon :size="16" style="margin-right: 5px;">
                            <Delete/>
                        </el-icon>
                        删除笔记
                    </el-button>
                </el-dropdown-item>
                <el-dropdown-item >
                    <el-button plain class="popoverBox-button">
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

    let propsData = defineProps({
        clickRightClientY: {type: String, required:true},
        clickRightClientX: {type: String, required:true},
        top: {type: Boolean, required:true},
        // isShowRight: {type: Boolean, required:true},
    })

    //自定义事件
    const emits = defineEmits(['delete']) 

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
    const dropdown1 = ref(null)
    const dropdownOpen = () => {
        dropdown1.value.handleOpen()
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