// 提醒框全局状态

import { defineStore } from 'pinia';
import {computed, ref, watch} from 'vue';

// 关于删除模态框的全局状态
export const useDeleteRemindDialogStore = defineStore(
    "deleteRemindDialog",  //唯一id至
    () => {
        
        const show = ref(false);  //是否显示
        const fileName = ref(null);  // 删除的名称
        const size = ref(1);  //删除的个数
        const delectBtn = ref(true);  //删除按钮是否显示
        const completeDelectBtn = ref(true);  //彻底删除是否显示
        const type = ref(1);  // 删除的类型  1:笔记  2:小记

        // 默认情况下
        const showDefault = (name, fileType) => {
            fileName.value = name;
            type.value = fileType;
            show.value = true;
        }

        // 笔记页面删除
        const showByNotePage = (name) => {
            fileName.value = name;
            show.value = true;
        }

        // 小记页面删除
        const showByThingPage = (name) => {
            fileName.value = name;
            type.value = 2;
            show.value = true;
        }

        // 回收站单个删除的
        const showByDumpsterPageOneFile = (name, fileType) => {
            fileName.value = name;
            type.value = fileType;
            show.value = true;
            delectBtn.value = false;  //不显示删除按钮
        }

        // 回收站多个个删除的
        const showByDumpsterPageMoreFile = (num) => {
            size.value = num;
            show.value = true;
            delectBtn.value = false;  //不显示删除按钮
        }

        const reset = () => {
            fileName.value = null;
            size.value = 1;
            delectBtn.value = true;
            completeDelectBtn.value = true;
            type.value = 1;
        }

        //如果关闭了删除提醒框，则恢复初始值
        // watch( 
        //     () => show.value,
        //     newData => {
        //         if(!newData){
        //             fileName.value = null;
        //             size.value = 1;
        //             delectBtn.value = true;
        //             completeDelectBtn.value = true;
        //             type.value = 1;
        //         }
        //     }
        // )
        
        return {
            show, 
            fileName, 
            size, 
            delectBtn, 
            completeDelectBtn, 
            type,
            showDefault,
            showByNotePage,
            showByThingPage,
            showByDumpsterPageOneFile,
            showByDumpsterPageMoreFile,
            reset
        }

    }
)