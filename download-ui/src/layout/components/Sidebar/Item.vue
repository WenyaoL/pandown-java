<script>
import { h,markRaw,shallowRef} from 'vue'
import SvgIcon from '@/components/SvgIcon/index.vue'
import {ElIcon} from 'element-plus'
export default {
  name: 'MenuItem',
  functional: true,
  props: {
    icon: {
      type: [String,Object],
      default: ''
    },
    title: {
      type: String,
      default: ''
    },
    isElementIcon: {
      type: Boolean,
      default:false
    }
  },
  setup(props) {
    return () => {
      const { icon, title, isElementIcon} = props
      const vnodes = []
      if (icon) {
        if (isElementIcon) {
          vnodes.push(h(ElIcon,()=>h(icon)))
        } else {
          vnodes.push(h(SvgIcon, { iconClass: icon }))
        }
        
      }

      if (title) {
        vnodes.push(h('span',{},title))
      }
      return vnodes
    }
  }
}
</script>

<style scoped>
.sub-el-icon {
  color: currentColor;
  width: 1em;
  height: 1em;
}

.sub-el-icon .el-icon{
  margin-right: 6px;
}
</style>
