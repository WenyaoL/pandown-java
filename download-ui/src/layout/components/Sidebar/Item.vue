<!--<template>
  <i v-if="icon&&icon.includes('el-icon')" :class="[icon, 'sub-el-icon']"></i>
  <svg-icon v-else-if="icon && !icon.includes('el-icon')" :icon-class="icon"></svg-icon>

  <span v-slots="title">{{title}}</span>

</template>-->

<script>
import { h } from 'vue'
import SvgIcon from '@/components/SvgIcon/index.vue'
export default {
  name: 'MenuItem',
  functional: true,
  props: {
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    }
  },
  setup(props) {
    return () => {
      const { icon, title } = props
      const vnodes = []
      if (icon) {
        if (icon.includes('el-icon')) {
          vnodes.push(h('i', { class: [icon, 'sub-el-icon'] }))
        } else {
          vnodes.push(h(SvgIcon, { iconClass: icon }))
        }
      }

      if (title) {
        vnodes.push(h('span',{slot:"title"},title))
      }
      return vnodes
    }
  }
  /*render(context) {
    const { icon, title } = context.$.props
    const vnodes = []
    if (icon) {
      if (icon.includes('el-icon')) {
        vnodes.push(<i class={[icon, 'sub-el-icon']} />)
      } else {
        vnodes.push(<svg-icon icon-class={icon} />)
      }
    }

    if (title) {
      console.log("存在title==》", this.$slots['default']);
      vnodes.push(<span v-slots="title">{(title)}</span>)
    }
    return vnodes
  }*/
}
</script>

<style scoped>
.sub-el-icon {
  color: currentColor;
  width: 1em;
  height: 1em;
}
</style>
