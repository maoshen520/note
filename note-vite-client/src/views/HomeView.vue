<template>
    <div style="width: 100%;height: 100%;">
        <el-container>

            <!-- 头部 -->
			<el-header>
				<header_c></header_c>
			</el-header>

			<el-container style="height:calc(100% - 64px);">
				<!-- 导航栏 -->
				<el-aside>
					<aside_c></aside_c>
				</el-aside>

				<!-- 主界面 -->
				<el-main style="background-color: #F2F6F7;padding:0px">
					<router-view />
				</el-main>
			</el-container>

			<!-- 登录、注册弹框 -->
			<LoginModal />

            <!-- 重新加载页面对话框--提示 -->
            <el-dialog
                v-model="dialogVisible"
                width="500"
                :show-close="false"
                :close-on-click-modal="false"
                :close-on-press-escape="false" 
            >
                <span style="font-size: 16px;">登录状态已发生改变，请重新加载页面 ！</span>
                <template #footer>
                    <span class="dialog-footer">
                        <el-button type="warning" @click="closeDialog()">
                            重新加载页面
                        </el-button>
                    </span>
                </template>
            </el-dialog>
		</el-container>
    </div>
</template>

<script setup>
    import {ref, onMounted, inject, watch} from 'vue';
    import header_c from '@/components/HeaderContent.vue';
	import aside_c from '@/components/AsideContent.vue';
	import LoginModal from '@/components/login/LoginModal.vue';
    import { userLoginModalStore } from '@/stores/LoginModalStore.js';
    // 登录模态框共享资源的对象
    const loginModalStore = userLoginModalStore();
    // 改变登录模态框显示的状态
    const {changeLoginModalShowStatus} = loginModalStore;

    // 重新加载页面对话框
    const dialogVisible = ref(false);

    // 点击重新加载页面按钮事件
    const closeDialog = () => {
        dialogVisible.value = false;
        window.location.reload();
    }

    // onMounted(() => {
	// 	// 监听本地存储是否发生变化
	// 	window.addEventListener('storage', async event => {
	// 		if(event.key === 'user'){

    //             // 先隐藏登录对话框
    //             await changeLoginModalShowStatus(false);  

    //             // 在显示重新加载页面
    //             dialogVisible.value = true;
	// 		}
	// 	})
	// })
    
    // 接收祖先组件提供的数据： 是否需要重新加载页面  ---就不用上面的onMounted
    const needReload = inject('needReload');
    
    // 如果needReload的值为true，则重新加载页面
    watch(
        () => needReload.value,
        async newData => {
            if(newData){
                // 先隐藏登录对话框
                await changeLoginModalShowStatus(false);  

                // 在显示重新加载页面
                dialogVisible.value = true;
            }
        }
    )


</script>

<style lang="less" scoped>
    .el-container{
		height: 100%;
	}
	.el-header{
		// background-color: aqua;
		height: 64px;
		border-bottom: 1px solid #EAE9EA;
	}
	.el-aside{
		// background-color: aquamarine;
		width: 64px;
		border-right: 1px solid #EAE9EA;
		
	}
</style>