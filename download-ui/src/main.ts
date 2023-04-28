import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import SvgIcon from '@/components/SvgIcon/index.vue'// svg component
import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import './assets/styles/index.scss' // global css

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import 'virtual:svg-icons-register'

const app = createApp(App)
app.component('svg-icon', SvgIcon)
app.use(router).use(ElementPlus).use(store)

app.mount('#app')
