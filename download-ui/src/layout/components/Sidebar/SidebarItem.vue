<template>
  <div v-if="!item.hidden">
    <template
      v-if="hasOneShowingChild(item.children, item) && (!onlyOneChild?.children || onlyOneChild?.noShowingChildren) && !item.alwaysShow">
      <app-link v-if="onlyOneChild?.meta" :to="resolvePath(onlyOneChild?.path)">
        <el-menu-item :index="resolvePath(onlyOneChild?.path)" :class="{ 'submenu-title-noDropdown': !isNest }">
          <item :icon="onlyOneChild?.meta.icon || (item.meta && item.meta.icon)" :isElementIcon="item.meta && item.meta.isElementIcon"/>
          <template #title>
            <span>{{ onlyOneChild?.meta.title }}</span>
          </template>
        </el-menu-item>
      </app-link>
    </template>

    <el-sub-menu v-else ref="subMenu" :index="resolvePath(item.path)">
      <template #title>
        <item v-if="item.meta" :icon="item.meta && item.meta.icon" :title="item.meta.title" :isElementIcon="item.meta.isElementIcon" />
      </template>
      <sidebar-item v-for="child in item.children" :key="child.path" :is-nest="true" :item="child"
        :base-path="resolvePath(child.path)" class="nest-menu" />
    </el-sub-menu>
  </div>
</template>

<script lang="ts">
import path from 'path-browserify'
import { isExternal } from '@/utils/validate'
import Item from './Item.vue'
import AppLink from './Link.vue'
import {Location} from '@element-plus/icons-vue'
import { ref, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
export default {
  name: 'SidebarItem',
  components: { Item, AppLink,Location },
  props: {
    // route object
    item: {
      type: Object,
      required: true
    },
    isNest: {
      type: Boolean,
      default: false
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  setup(props) {
    const subMenu = ref()

    const store = useStore()
    const device = computed(() => store.state.app.device)
    const onlyOneChild = ref<any>(null)


    const hasOneShowingChild = (children: any[] = [], parent: any) => {

      const showingChildren = children.filter(item => {
        if (item.hidden) {
          return false
        } else {
          // Temp set(will be used if only has one showing child)
          onlyOneChild.value = item
          return true
        }
      })

      // When there is only one child router, the child router is displayed by default
      if (showingChildren.length === 1) {
        return true
      }

      // Show parent if there are no child router to display
      if (showingChildren.length === 0) {
        onlyOneChild.value = { ...parent, path: '', noShowingChildren: true }
        return true
      }

      return false
    }

    const resolvePath = (routePath: any) => {
      if (isExternal(routePath)) {
        return routePath
      }
      if (isExternal(props.basePath)) {
        return props.basePath
      }
      return path.resolve(props.basePath, routePath)
    }

    const fixBugIniOS = () => {
      const $subMenu = subMenu.value
      if ($subMenu) {
        const handleMouseleave = $subMenu.handleMouseleave
        $subMenu.handleMouseleave = (e: any) => {
          if (device.value === 'mobile') {
            return
          }
          handleMouseleave(e)
        }
      }
    }

    onMounted(() => fixBugIniOS())

    return {
      onlyOneChild,
      hasOneShowingChild,
      resolvePath,
      fixBugIniOS,
    }
  },

}
</script>
