<template>
  <div class="analysis-container">
    <div class="form-container">
      <el-card>
      <template #header>
        <div class="card-header">
          <span>百度网盘分享解析</span>
        </div>
      </template>
      <el-form ref="form" :model="form" label-width="120px">
        <el-form-item label="分享链接">
          <el-input v-model="form.link" placeholder="请输入分享链接(可携带提取码)"/>
        </el-form-item>
        <el-form-item label="提取码">
          <el-input v-model="form.pwd" placeholder="提取码"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">提取</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    </div>
    


    <Transition>
      <div v-show="true" class="info">
        <div class="info-url"><span :class="parseResult.link ? 'success' : 'error'">链接: </span>{{ parseResult.link }}</div>
        <div class="info-surl"><span :class="parseResult.surl ? 'success' : 'error'">解析surl: </span> {{ parseResult.surl }}
        </div>
        <div class="info-pwd"><span :class="parseResult.pwd ? 'success' : 'error'">解析提取码: </span>{{ parseResult.pwd }}</div>
      </div>
    </Transition>
  </div>
</template>

<script>
import { getShareDir } from '@/api/downloadService'
import { mapState } from 'vuex'
import { mapMutations } from 'vuex'
import {ElMessage} from 'element-plus'

export default {
  data() {
    return {
      parseResult: {
        link: '',
        surl: '',
        pwd: ''
      },
      form: {
        link: '',
        pwd:''
      }
    }
  },
  computed: {
    ...mapState(['surl', 'pwd'])
  },
  watch: {
    "form.link": {
      handler(val, oldVal) {
        this.extractLink()
      },
      deep: true
    }
  },
  methods: {
    ...mapMutations(['setSurl', 'setPwd']),
    extractLink() {
      const shareLink = this.form.link
      const linkPattern = /https:\/\/pan\.baidu\.com\/s\/([a-zA-Z0-9_-]+)/g
      const surlPattern = /(?<=https:\/\/pan\.baidu\.com\/s\/)[a-zA-Z0-9_-]+/g
      const pwdPattern = /(?<=提取码[:：]?\s*)[a-zA-Z0-9_-]+/g
      const linkResult = shareLink.match(linkPattern)
      const surlResult = shareLink.match(surlPattern)
      const pwdResult = shareLink.match(pwdPattern)

      if (linkResult) {
        const link = linkResult[0]
        this.parseResult.link = link
        this.form.link = link
      }

      if (surlResult) {
        const surl = surlResult[0]
        this.parseResult.surl = surl
      }

      if (pwdResult) {
        const pwd = pwdResult[0]
        this.parseResult.pwd = pwd
        this.form.pwd = pwd
      }

    },
    onSubmit() {
      const shareLink = this.form.link
      if (!shareLink) {
        ElMessage.error('分享链接不能为空')
        return
      }
      const { link, surl, pwd } = this.parseResult
      if (link && surl && pwd) {
        this.$store.commit('setSurl', surl)
        this.$store.commit('setPwd', pwd)
        getShareDir({ surl, pwd, dir: "" })
          .then(response => {
            if (response.code == "200") {
              const data = response.data
              this.$store.commit('setPandownData', data)
              this.$router.push({ name: 'BaiduDownload' })
            }
          }).catch(error => {
            console.error('网络请求失败', error)
          })

      }
    }
  }
}
</script>

<style scoped>
.line {
  text-align: center;
}

.info {
  margin-left: 50px;
}

.info>div {
  font-size: 15px;
  color: #606266;
  margin: 5px 0;
}

.info .error {
  color: #F56C6C
}

.info .success {
  color: #67C23A
}

.analysis-container{
  margin: 0 50px;
  margin-top: 20px;
}
.form-container{
  margin: 0 150px;

}
</style>
