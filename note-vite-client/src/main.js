import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import 'element-plus/dist/index.css'
import 'element-plus/es/components/icon/style/css'
import { createPinia } from 'pinia'



const app = createApp(App);
app.use(createPinia());

app.mount('#app')
