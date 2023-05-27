<template>
  <div class="dashboard-container">
    <h2>欢迎来到,<span>{{ userName }}</span></h2>

    <PanelGroup :parseFlow="flowData.parseFlow" :parseNum="flowData.parseNum" :residueFlow="residueFlow" />

    <el-descriptions title="用户信息">
      <el-descriptions-item label="用户名">{{ userName }}</el-descriptions-item>
      <el-descriptions-item label="角色">
        <el-tag v-for="role in roles" class="ml-2" type="success">{{ role }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="已解析流量">{{ formatSize(flowData.parseFlow) }}</el-descriptions-item>
      <el-descriptions-item label="已解析次数">{{ flowData.parseNum }}</el-descriptions-item>
      <el-descriptions-item label="剩余流量">{{ formatSize(residueFlow,true) }}</el-descriptions-item>
    </el-descriptions>
    <h2>声明</h2>
    <p>
      <strong>本项目仅供学习参考。</strong><br />
      项目GitHub地址:<a target="_blank" style="color: #409EFF;" href="https://github.com/WenyaoL/pandown-java">链接</a>
      开源协议:MIT
    </p>
    <h3>教程</h3>
    <p>在左边导航栏选择链接解析-->复制分享链接(粘贴)-->点击解析按钮-->自动跳转分享页面-->选择要下载的文件。</p>
    <p>切记,每次解析下载无论发不发送给aria2都会进行流量消耗统计。</p>
  </div>
</template>

<script>
import { defineComponent, computed, onMounted, reactive } from 'vue'
import { getUserFlowInfo } from '@/api/dashboardService'
import { useStore } from 'vuex'
import { formatSize } from '@/utils/index'
import PanelGroup from './components/PanelGroup.vue'

export default defineComponent({
  name: 'Dashboard',
  components: {
    PanelGroup
  },
  setup() {
    const store = useStore()
    const userName = computed(() => store.state.user.name)
    const roles = computed(() => store.state.user.roles)
    const flowData = reactive({
      limitFlow: 0,
      parseFlow: 0,
      parseNum: 0,
      state: 0
    })
    const residueFlow = computed(() => {
      const flow =  flowData.limitFlow - flowData.parseFlow
      if(flow<0)return 0
      return flow
    })


    getUserFlowInfo().then(response => {
      const { limitFlow, parseFlow, parseNum, state } = response.data
      flowData.limitFlow = limitFlow
      flowData.parseFlow = parseFlow
      flowData.parseNum = parseNum
      flowData.state = state
    })

    return {
      userName,
      roles,
      flowData,
      residueFlow,
      formatSize,
      PanelGroup
    }
  }
})
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }

  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
