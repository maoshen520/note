
<template>
	<div class="common-layout">
		<homeView></homeView>
	</div>
</template>

<script setup>
	import { onMounted, watch, ref, provide } from 'vue';
	import { noteBaseRequest } from "@/request/note_request";
	import md5 from 'md5';
	import homeView from '@/views/HomeView.vue'
	import { useUserStore } from './stores/userStore';
	import { storeToRefs } from 'pinia';

	// 用户的共享资源
	const userStore = useUserStore();
	// 获取响应的token值
	const {token} = storeToRefs(userStore);

	// 如果用户的登录状态发生改变 (主要是登录后)，重新加载本页面
	// watch(
	// 	() => token.value,
	// 	newData => {
	// 		if(newData !== null){
	// 			location.reload();
	// 		}
	// 	}
	// )
	
	// 是否需要重新加载页面
	const needReload = ref(false);

	onMounted(() => {
		// 监听本地存储是否发生变化
		window.addEventListener('storage', event => {
			// console.log(event)
			// 如果发生改变，event.key就是改变的
			// event.newValue就是新值

			if(event.key === 'user'){
				// console.log('用户状态发生改变')
				// location.reload();  //强制刷新页面--用户状态发生改变

				// 判断token的值是否发生了变化  ---防止改变用户其他信息导致弹出重新加载页面弹框
				const newToken = JSON.parse(event.newValue).token;
				const oldToken = JSON.parse(event.oldValue).token;

				if(newToken === oldToken){
					userStore.$hydrate();
				}else {
					needReload.value = true;
					setTimeout( () => {
						needReload.value = false;
					},1000)
				}
			}
		})
	})

	//为后代组件提供数据
	provide('needReload', needReload)
</script>

<style lang="less" scoped>
	.common-layout{
		width: 100%;
		height: 100%;
	}
	

	
</style>