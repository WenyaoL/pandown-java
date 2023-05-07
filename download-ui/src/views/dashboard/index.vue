<template>
  <div class="dashboard-container">
    <h1>欢迎来到，🍐哥的答辩项目</h1>
    <p>本项目仅供学习参考，严禁商业用途</p>
    <h2>实现原理</h2>
    <p>获取网盘文件信息,处理后显示在网页中。通过api接口以及SVIP账号的Cookie(BDUSS)获取高速下载链接。</p>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  }
}
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
