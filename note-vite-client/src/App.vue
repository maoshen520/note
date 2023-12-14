
<template>
	<div class="common-layout">
		<el-container>
			<el-header>
				<!-- header -->
				<header_c></header_c>
			</el-header>
			<el-container style="height:calc(100% - 64px) ;">
				<!-- 导航栏 -->
				<el-aside>
					<aside_c></aside_c>
				</el-aside>

				<!-- 主界面 -->
				<el-main style="background-color: #F2F6F7;">
					<router-view />
				</el-main>
			</el-container>
		</el-container>

		<LoginModal />
	</div>
</template>

<script setup>
	import { onMounted } from 'vue';
	import { noteBaseRequest } from "@/request/note_request";
	import md5 from 'md5';
	import header_c from '@/components/HeaderContent.vue';
	import aside_c from '@/components/AsideContent.vue';
	import LoginModal from '@/components/login/LoginModal.vue'

	onMounted(() => {

		// 监听本地存储是否发生变化
		// window.addEventListener('storage', event => {
		// 	// console.log(event)
		// 	// 如果发生改变，event.key就是改变的
		// 	// event.newValue就是新值
		// })
	})
</script>

<style lang="less" scoped>
	.common-layout{
		width: 100%;
		height: 100%;
	}
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