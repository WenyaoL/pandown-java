import { asyncRoutes, constantRoutes } from '@/router'

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles:any, route:any) {
  if (route.meta && route.meta.roles) {
    return roles.some((role: any) => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes: any[], roles: any) {
  const res: any[] = []

  routes.forEach((route: any) => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state: { addRoutes: any; routes: ({ path: string; component: () => Promise<any>; hidden: boolean; redirect?: undefined; children?: undefined; name?: undefined } | { path: string; component: any; redirect: string; children: { path: string; name: string; component: () => Promise<any>; meta: { title: string; icon: string } }[]; hidden?: undefined; name?: undefined } | { path: string; component: any; children: { path: string; name: string; component: () => Promise<any>; meta: { title: string; icon: string } }[]; hidden?: undefined; redirect?: undefined; name?: undefined } | { path: string; component: any; redirect: string; name: string; children: { path: string; name: string; component: () => Promise<any>; meta: { title: string; icon: string } }[]; hidden?: undefined } | { path: string; component: any; children: { path: string; meta: { title: string; icon: string } }[]; hidden?: undefined; redirect?: undefined; name?: undefined })[] }, routes: ConcatArray<{ path: string; component: () => Promise<any>; hidden: boolean; redirect?: undefined; children?: undefined; name?: undefined } | { path: string; component: any; redirect: string; children: { path: string; name: string; component: () => Promise<any>; meta: { title: string; icon: string } }[]; hidden?: undefined; name?: undefined } | { path: string; component: any; children: { path: string; name: string; component: () => Promise<any>; meta: { title: string; icon: string } }[]; hidden?: undefined; redirect?: undefined; name?: undefined } | { path: string; component: any; redirect: string; name: string; children: { path: string; name: string; component: () => Promise<any>; meta: { title: string; icon: string } }[]; hidden?: undefined } | { path: string; component: any; children: { path: string; meta: { title: string; icon: string } }[]; hidden?: undefined; redirect?: undefined; name?: undefined }>) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes as any) as any
  }
}

const actions = {
  generateRoutes({ commit }: any, roles: string | string[]) {
    return new Promise(resolve => {
      let accessedRoutes
      if (roles.includes('admin')) {
        accessedRoutes = asyncRoutes || []
      } else {
        accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
      }
      commit('SET_ROUTES', accessedRoutes)
      console.log(roles);
      resolve(accessedRoutes)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
