import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// element plus引入
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'


import { resolve } from "path";


// https://vitejs.dev/config/
export default defineConfig({
	//添加此段
	resolve: {
		alias: {
		  "@": resolve(__dirname, "src"), // 设置@指向src
		},
	},
	

	plugins: [
		vue(),


		AutoImport({
			resolvers: [ElementPlusResolver()],
		}),
		Components({
			resolvers: [ElementPlusResolver()],
		}),
	],
	
	server:{
		proxy:{
			'/note':{
				target:'http://127.0.0.1:18081/zhike-notes',  //代理地址
				changeOrigin: true,  // 允许跨域
				rewrite: path => path.replace(/^\/note/,'')
			}
		},
		host:'127.0.0.1',
	}
})
