<template>
  <div class="analysis-container">
    <div class="form-container">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>百度网盘分享解析</span>
          </div>
        </template>
        <el-form ref="formRef" :model="form" label-width="120px">
          <el-form-item label="分享链接">
            <el-input v-model="form.link" placeholder="请输入分享链接(可携带提取码)" />
          </el-form-item>
          <el-form-item label="提取码">
            <el-input v-model="form.pwd" placeholder="提取码" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">提取</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>



    <Transition>
      <div class="descriptions-container" v-show="showT">
        <el-descriptions title="提取信息" :column="1">
          <el-descriptions-item label="链接:">{{ parseResult.link }}<el-icon class="copyIcon"
              @click="copyIconHandle(parseResult.link)">
              <CopyDocument />
            </el-icon>
          </el-descriptions-item>
          <el-descriptions-item label="surl:">{{ parseResult.surl }}<el-icon class="copyIcon"
              @click="copyIconHandle(parseResult.surl)">
              <CopyDocument />
            </el-icon>
          </el-descriptions-item>
          <el-descriptions-item label="提取码:">{{ parseResult.pwd }}<el-icon class="copyIcon"
              @click="copyIconHandle(parseResult.pwd)">
              <CopyDocument />
            </el-icon>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </Transition>
  </div>
</template>

<script setup>
import { reactive, watch, computed } from 'vue'
import { copyStringToClipboard } from '@/utils/index'
import { getShareDir } from '@/api/downloadService'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { CopyDocument } from '@element-plus/icons-vue'

const store = useStore()
const router = useRouter()

const parseResult = reactive({
  link: '',
  surl: '',
  pwd: ''
})

const form = reactive({
  link: '',
  pwd: ''
})

const showT = computed(() => {
  return parseResult.link != '' || parseResult.surl != '' || parseResult.pwd != ''
})

const extractLink = () => {
  const shareLink = form.link
  const linkPattern = /https:\/\/pan\.baidu\.com\/s\/([a-zA-Z0-9_-]+)/g
  const surlPattern = /(?<=https:\/\/pan\.baidu\.com\/s\/)[a-zA-Z0-9_-]+/g
  const pwdPattern = /(?<=提取码[:：]?\s*)[a-zA-Z0-9_-]+/g
  const linkResult = shareLink.match(linkPattern)
  const surlResult = shareLink.match(surlPattern)
  const pwdResult = shareLink.match(pwdPattern)

  if (linkResult) {
    const link = linkResult[0]
    parseResult.link = link
    form.link = link
  }

  if (surlResult) {
    const surl = surlResult[0]
    parseResult.surl = surl
  }

  if (pwdResult) {
    const pwd = pwdResult[0]
    parseResult.pwd = pwd
    form.pwd = pwd
  }
}

const onSubmit = () => {
  const shareLink = form.link
  if (!shareLink) {
    ElMessage.error('分享链接不能为空')
    return
  }
  const { link, surl, pwd } = parseResult
  if (link && surl && pwd) {
    store.commit('setSurl', surl)
    store.commit('setPwd', pwd)
    getShareDir({ surl, pwd, dir: "" })
      .then(response => {
        if (response.code == "200") {
          const data = response.data
          store.commit('setPandownData', data)
          router.push({ name: 'BaiduDownload' })
        }
      }).catch(error => {
        console.error('网络请求失败', error)
      })

  }
}

const copyIconHandle = (str) => {
  copyStringToClipboard(str)
  ElMessage.success('复制成功')
}


watch(() => form.link, (val, oldVal) => {
  console.log("asdfasdf");
  extractLink()
})
watch(() => form.pwd, (val, oldVal) => {
  parseResult.pwd = val
})
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

.copyIcon {
  margin-left: 10px;
  color: #409EFF;
  top: 2px;
}

.analysis-container {
  margin: 0 50px;
  margin-top: 20px;
}

.form-container {
  width: 800px;
  margin: 50px auto;
}

.descriptions-container {
  width: 800px;
  margin: 0px auto;
}


.v-enter-active {
  transition: all 1s ease-out;
}

.v-leave-active {
  transition: all 1s ease-in;
}

.v-enter-from,
.v-leave-to {
  transform: translateX(50px);
  opacity: 0;
}
</style>
