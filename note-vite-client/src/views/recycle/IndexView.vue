<!-- 回收站 组件 -->

<template>
    <div style="margin: 20px;background-color: #fff;height:calc(100% - 40px);overflow: hidden;overflow-y: auto;">
        <!-- 标头 -->
        <el-card class="box-card" shadow="never">
            <div class="card-header">
                <h3>回收站</h3>
                <el-space> 
                    <!-- <el-button color="#F97F4D" style="color: #fff;">批量恢复</el-button> -->
                    <el-button type="danger" @click="delectMoreClick()">彻底批量删除</el-button>
                </el-space>
            </div>
        </el-card>

        <div style="margin: 0px 20px;border: 1px solid #F2F6F7;border-bottom: none;">
            <el-table
                ref="multipleTableRef"
                :data="filesData"
                style="width: 100%"
                @selection-change="handleSelectionChange"
            >
                <el-table-column type="selection" min-width="5%" />
                <el-table-column property="title" label="名称" min-width="35%">
                    <template #default="scope">{{ scope.row.title ? scope.row.title : '（ 暂无标题 ）'}}</template>
                </el-table-column>
                <el-table-column property="type" label="类型" min-width="15%">
                    <template #default="scope">
                        <el-tag v-if="scope.row.type === 2" type="" effect="dark">小记</el-tag>
                        <el-tag v-else type="success" effect="dark">笔记</el-tag>
                    </template>
                </el-table-column>
                <el-table-column property="updateTime" label="时间" min-width="20%">
                    <template #default="scope">{{ scope.row.updateTime }}</template>
                </el-table-column>
                <el-table-column label="操作" min-width="20%">
                    <template #default="scope">
                        <el-button color="#F97F4D" style="color: #fff;" size="small" @click="restoreOne(scope.row.id,scope.row.type)">恢复</el-button>
                        <el-button type="danger" size="small" @click="delectOneClick(scope.row.id,scope.row.title,scope.row.type)">彻底删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        
        <!-- 删除提示框 -->
        <deleteRemindDialog  
            @delect="deletedumpster"
        ></deleteRemindDialog>

    </div>
</template>

<script setup>
    import { ref } from 'vue'
    import {getUserToken,loginInvalid} from "@/utils/userLoginUtils.js";
    import {noteBaseRequest} from "@/request/note_request.js";
    import { useDeleteRemindDialogStore } from "@/stores/deleteRemindDialogStore"
    import { storeToRefs } from 'pinia';
    import deleteRemindDialog from "@/components/remind/DeleteRemindDialog.vue";
    import serverRequest from "@/request";
    import fileApi from '@/request/api/fileApi';

    // 删除提醒框共享资源
    const deleteRemindDialogStore = useDeleteRemindDialogStore();
    const {
        show,  //是否显示删除提醒框
    } = storeToRefs(deleteRemindDialogStore);
    // 删除提醒框显示(单个)
    const {showByDumpsterPageOneFile} = deleteRemindDialogStore;

    const {showByDumpsterPageMoreFile} = deleteRemindDialogStore;

    const isLoad = ref(false);
    const filesData = ref([]);

    // 多选表格的数组数据
    const selectionData = ref([]);
    // 多选
    const handleSelectionChange = (val) => {
        console.log(val)
        selectionData.value = [];
        if(val.length > 0){
            for(let i=0; i<val.length; i++){
                selectionData.value.push({
                    id:val[i].id,
                    type:val[i].type
                })
            }
        }
        console.log(selectionData.value)
    }

    // 获取列表数据
    const getDataList = async () => {
        
        //判断用户的登录状态
        const userToken = await getUserToken();
        const { data: responseData } = await noteBaseRequest.get(
            "/file-dumpster/list",
            {   
                // params:{
                //     search:searchInput.value,
                //     filter:select.value
                // },
                headers: {
                    userToken:userToken
                }
            }
        ).catch(() => {
            ElMessage({
                message: '获取回收站列表请求失败',
                type: 'error',
            })
            throw '获取回收站列表请求失败'
        })
        if(responseData.success){
            filesData.value = [];
            filesData.value = responseData.data;  //小记列表
            isLoad.value = true;
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
    getDataList();

    // 单个删除id
    const delectRemindId = ref(null);
    //类型  --1：笔记   2：小记
    const delectRemindType = ref(null);
    // 单个删除
    const delectOneClick = (id,title,type) => {
        let fileName = title ? title : '（ 暂无标题 ）'
        delectRemindId.value = id;
        delectRemindType.value = type;
        showByDumpsterPageOneFile(fileName, type)
    }

    //点击删除提示框中彻底删除按钮   --complete 是否彻底删除
    const deletedumpster = (complete) => {
        //关闭删除提醒框
        show.value = false;

        if(delectRemindType.value == 1){  //笔记
            deleteNote(complete);
        }else if(delectRemindType.value == 2) {  //小记
            deleteThing(complete);
        }else{
            batchDelect(complete);
        }
    }

    // 删除笔记   --complete 是否彻底删除
    const deleteNote =async (complete) => {
        //判断用户的登录状态
        const userToken =  await getUserToken();
        const { data: responseData } = await noteBaseRequest.delete(
            "/note/delete",
            {
                params:{
                    complete,
                    isRecycleBin:true,  //是否是回收站中的操作
                    noteId:delectRemindId.value
                },
                headers: {
                    userToken:userToken
                }
            }
        ).catch(() => {
            ElMessage({
                message: complete ? '彻底删除笔记请求失败' : '删除笔记请求失败',
                type: 'error',
            })
            throw complete ? '彻底删除笔记请求失败' : '删除笔记请求失败';
        })
        if(responseData.success){
            ElMessage({
                message:responseData.message,
                type: 'success',
            });
            getDataList();
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

    // 删除小记   --complete 是否彻底删除
    const deleteThing =async (complete) => {
        //判断用户的登录状态
        const userToken =  await getUserToken();
        const { data: responseData } = await noteBaseRequest.delete(
            "/thing/delete",
            {
                params:{
                    complete,
                    isRecycleBin:true,
                    thingId:delectRemindId.value
                },
                headers: {
                    userToken:userToken
                }
            }
        ).catch(() => {
            ElMessage({
                message: complete ? '彻底删除小记请求失败' : '删除小记请求失败',
                type: 'error',
            })
            throw complete ? '彻底删除小记请求失败' : '删除小记请求失败';
        })
        if(responseData.success){
            ElMessage({
                message:responseData.message,
                type: 'success',
            });
            getDataList();
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


    // 彻底批量删除按钮点击事件
    const delectMoreClick = () => {
        if(selectionData.value.length <= 0){
            return false;
        }
        delectRemindType.value = null;
        showByDumpsterPageMoreFile(selectionData.value.length)
    }

    // 批量删除请求方法
    const batchDelect =async (complete) => {

        // 获取请求api
        let API = {...fileApi.deleteBatch};

        // 封装请求体中（data）的参数
        API.data = {
            complete,
            dumpster:true,
            files:selectionData.value 
        }

        // 发送请求
        await serverRequest(API).then(responseData => {
            if(!responseData) return;

            getDataList();

        })
        .catch(error => {
            ElMessage({
                message:'彻底删除请求失败',
                type: 'error',
            })
        })

        //判断用户的登录状态
        // const userToken =  await getUserToken();
        // const { data: responseData } = await noteBaseRequest.post(
        //     "/file/delete-batch",
            
        //     {   
        //         complete,
        //         dumpster:true,
        //         files:selectionData.value 
        //     },
        //     {
        //         headers: {
        //             userToken:userToken
        //         }
        //     }
               
        // ).catch(() => {
        //     ElMessage({
        //         message:'彻底删除请求失败',
        //         type: 'error',
        //     })
        //     throw '彻底删除请求失败';
        // })
        // if(responseData.success){
        //     ElMessage({
        //         message:responseData.message,
        //         type: 'success',
        //     });
        //     getDataList();
        // }else{
        //     ElMessage({
        //         message: responseData.message,
        //         type: 'error'
        //     });
        //     // 登录已失效
        //     if(responseData.code == 'L_008'){
        //         loginInvalid(true);
        //     }
        // }
    }

    // 单个恢复小记或笔记
    const restoreOne =async (id,type) => {

        // 判断用户的登录状态
        const userToken =  await getUserToken();
        const { data: responseData } = await noteBaseRequest.post(
            "/file/restoreOne",
            {   
                id,
                type
            },
            {
                headers: {
                    userToken:userToken
                }
            }
               
        ).catch(() => {
            ElMessage({
                message:'恢复请求失败',
                type: 'error',
            })
            throw '恢复请求失败';
        })
        if(responseData.success){
            ElMessage({
                message:responseData.message,
                type: 'success',
            });
            getDataList();
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

<style lang="less" scoped>

</style>