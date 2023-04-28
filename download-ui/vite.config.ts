import { fileURLToPath, URL } from 'node:url'
import path from 'path'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'

console.log(process.env.NODE_ENV);
const isDevelopment = process.env.NODE_ENV === 'development'
const isProduction = process.env.NODE_ENV === 'production'

const getEnvVariable = () => {
  if(isDevelopment) return{
    ENV : 'development',
    //VUE_APP_BASE_API : 'http://eaomovie.top:8010'
    VUE_APP_BASE_API : 'http://localhost:8080'
  }

  if(isProduction)return{
    ENV : 'production',
    VUE_APP_BASE_API : '/api'
  }
}


// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueJsx(),
    createSvgIconsPlugin({
      // 指定需要缓存的图标文件夹
      iconDirs: [path.resolve(process.cwd(), 'src/assets/icons/svg')],
      // 指定symbolId格式
      symbolId: 'icon-[name]',
      customDomId: '__svg__icons__dom__',
    })
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  define: {
    'process.env': getEnvVariable()
  }
})
