import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// element plus引入
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'


import { resolve } from "path";
// import { fileURLToPath, URL } from 'node:url'


import { createRequire } from 'node:module'
const require = createRequire( import.meta.url )
import ckeditor5 from '@ckeditor/vite-plugin-ckeditor5' // 引入 ckeditor5 vite 插件

// https://vitejs.dev/config/
export default defineConfig({
	base: './',
	//添加此段
	resolve: {
		alias: {
		  "@": resolve(__dirname, "src"), // 设置@指向src
		// '@': fileURLToPath( new URL( './src', import.meta.url ) )
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

		ckeditor5({ theme: require.resolve('@ckeditor/ckeditor5-theme-lark') }) // ckeditor5 插件
	],
	
	server:{
		port:'18080',  //端口号
		proxy:{
			'/note-serve':{
				target:'http://127.0.0.1:18081',  //代理地址
				// target:'http://47.107.58.184:18081',  //代理地址
				changeOrigin: true,  // 允许跨域
				rewrite: path => path.replace(/^\/note-serve/,'')
			}
		},
		// host:'0.0.0.0',
	}
})
