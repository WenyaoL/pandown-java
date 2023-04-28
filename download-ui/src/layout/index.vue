<template>
  <div :class="classObj" class="app-wrapper">
    <div v-if="device === 'mobile' && sidebar.opened" class="drawer-bg" @click="handleClickOutside" />
    <sidebar class="sidebar-container" />
    <div class="main-container">
      <div :class="{ 'fixed-header': fixedHeader }">
        <navbar />
      </div>
      <app-main />
    </div>
  </div>
</template>

<script>
import { Navbar, Sidebar, AppMain } from './components'
import { useStore } from 'vuex'
import { computed, onBeforeMount, onUnmounted, reactive,onBeforeUnmount } from 'vue'
import { useRouter, onBeforeRouteUpdate } from 'vue-router'

export default {
  name: 'Layout',
  components: {
    Navbar,
    Sidebar,
    AppMain
  },
  setup() {
    const store = useStore()
    const router = useRouter()

    const { body } = document
    const WIDTH = 992 // refer to Bootstrap's responsive design

    const sidebar = computed(() => store.state.app.sidebar)
    const device = computed(() => store.state.app.device)
    const fixedHeader = computed(() => store.state.settings.fixedHeader)

    const classObj = computed(() => {
      return {
        hideSidebar: !sidebar.value.opened,
        openSidebar: sidebar.value.opened,
        withoutAnimation: sidebar.value.withoutAnimation,
        mobile: device.value == 'mobile'
      }
    })

    const handleClickOutside = () => {
      store.dispatch('app/closeSideBar', { withoutAnimation: false })
    }

    const $_isMobile = () => {
      const rect = body.getBoundingClientRect()
      return rect.width - 1 < WIDTH
    }

    const $_resizeHandler = () => {
      if (!document.hidden) {
        const isMobile = $_isMobile()
        store.dispatch('app/toggleDevice', isMobile ? 'mobile' : 'desktop')

        if (isMobile) {
          store.dispatch('app/closeSideBar', { withoutAnimation: true })
        }
      }
    }

    onBeforeMount(() => {
      window.addEventListener('resize', $_resizeHandler)
    })

    onBeforeUnmount(() => {
      window.removeEventListener('resize', $_resizeHandler)
    })

    //路由守卫
    onBeforeRouteUpdate((to) => {
      if (device === 'mobile' && sidebar.opened) {
        store.dispatch('app/closeSideBar', { withoutAnimation: false })
      }
    });

    return {
      sidebar,
      device,
      fixedHeader,
      classObj,
      handleClickOutside
    }
  },
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/mixin.scss";
@import "@/assets/styles/variables.module.scss";

.app-wrapper {
  @include clearfix;
  position: relative;
  height: 100%;
  width: 100%;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.fixed-header {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 9;
  width: calc(100% - #{$sideBarWidth});
  transition: width 0.28s;
}

.hideSidebar .fixed-header {
  width: calc(100% - 54px)
}

.mobile .fixed-header {
  width: 100%;
}
</style>
