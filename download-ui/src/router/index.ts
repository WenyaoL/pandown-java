import { createRouter, createWebHistory } from 'vue-router'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'
import Layout from '../layout/index.vue'
import store from '@/store'
import {Plus} from '@element-plus/icons-vue'
import {markRaw} from 'vue'

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index.vue'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404.vue'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard', // redirect second level route
    children: [{ // second level route
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index.vue'),
      meta: { title: '声明', icon: 'dashboard' }
    }]
  },

  {
    path: '/form',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Link',
        component: () => import('@/views/form/index.vue'),
        meta: { title: '链接解析', icon: 'form' }
      }
    ]
  },

  {
    path: '/baidu',
    component: Layout,
    redirect: '/baidu/download',
    name: 'Baidu',
    children: [
      {
        path: 'download',
        name: 'BaiduDownload',
        component: () => import('@/views/download/index.vue'),
        meta: { title: '下载列表', icon: 'tree' }
      }
    ]
  },

  
]




// asyncRoutes(for permiss)
export const asyncRoutes = [
  {
    path: '/setting',
    component: Layout,
    redirect: '/setting/accountSetting',
    meta: { title: '管理', icon: 'setting',roles: ['admin'] },
    children: [
      {
        path:'accountSetting',
        component: () => import('@/views/setting/AccountDetailPage.vue'),
        meta: { title: '账号详情', icon: 'user',roles: ['admin']}
      },
      {
        path:'svipAccountManage',
        component: () => import('@/views/setting/SvipAccountManagePage.vue'),
        meta: { title: 'SVip管理', icon: markRaw(Plus),isElementIcon:true,roles: ['admin'] }
      },
      {
        path:'commonAccountManage',
        component: () => import('@/views/setting/CommonAccountManagePage.vue'),
        meta: { title: '普通账号管理', icon: markRaw(Plus),isElementIcon:true,roles: ['admin'] }
      },
      {
        path:'userManage',
        component: () => import('@/views/setting/UserManagePage.vue'),
        meta: { title: '用户管理', icon: markRaw(Plus),isElementIcon:true,roles: ['admin'] }
      },
    ]
  },
  //CommonAccountManagePage
  {
    path: '/github',
    component: Layout,
    children: [
      {
        path: 'https://github.com/WenyaoL/pandown-java',
        meta: { title: 'github', icon: 'link' }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '/:catchAll(.*)', redirect: '/404', hidden: true },
]

///:catchAll(.*)

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: constantRoutes as any
})





NProgress.configure({ showSpinner: false }) // NProgress Configuration
const whiteList = ['/login'] // no redirect whitelist
router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // set page title
  document.title = getPageTitle(to.meta.title as any)

  // determine whether the user has logged in
  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
      NProgress.done()
    } else {
      const hasRoles = store.getters.roles && store.getters.roles.length > 0
      if (hasRoles) {
        next()
      }else{
        try {
          // get user info
          // note: roles must be a object array! such as: ['admin'] or ,['developer','editor']
          const { role_name } = await store.dispatch('user/getInfo')
                    
          // generate accessible routes map based on roles
          const accessRoutes = await store.dispatch('permission/generateRoutes', [role_name]) as []
     
          // dynamically add accessible routes
          accessRoutes.forEach(accessRoute=>{
            router.addRoute(accessRoute)
          })
          
          // hack method to ensure that addRoutes is complete
          // set the replace: true, so the navigation will not leave a history record
          next({ ...to, replace: true })
          return to.fullPath
        } catch (error) {
          // remove token and go to login page to re-login
          await store.dispatch('user/resetToken')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
      NProgress.done()
    }
  } else {
    /* has no token*/
    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})


export function resetRouter() {
  // @ts-ignore
  const newRouter = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: constantRoutes as any
  })
  // @ts-ignore
  router.matcher = newRouter.matcher // reset router
}

export default router
