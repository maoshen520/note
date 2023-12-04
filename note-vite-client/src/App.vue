<!-- <script setup>
import HelloWorld from './components/HelloWorld.vue'
</script>

<template>
  <div>
    <a href="https://vitejs.dev" target="_blank">
      <img src="/vite.svg" class="logo" alt="Vite logo" />
    </a>
    <a href="https://vuejs.org/" target="_blank">
      <img src="./assets/vue.svg" class="logo vue" alt="Vue logo" />
    </a>
  </div>
  <HelloWorld msg="Vite + Vue" />
</template>

<style scoped>
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}
.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa);
}
.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa);
}
</style> -->


<template>
	<div class="common-layout">
		<el-container>
			<el-header>
				<!-- header -->
				<header_c></header_c>
			</el-header>
			<el-container>
				<el-aside>
					<aside_c></aside_c>
				</el-aside>
				<el-main>Main</el-main>
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
		// request()
	})

	const request = async () => {
		const { data: responseData } = await noteBaseRequest.post(
			'/user/login/email/password',
			{
				email: '315105654@qq.com',
				password: 'eVF4TJ'
			}

		).catch(() => {
			throw '发送登录请求失败'
		})
		console.log(responseData)
	}



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