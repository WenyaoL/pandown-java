

<template>
  <div class="link-container">
    <div class="button-group">
      <el-button size="small" class="button-item" type="primary" @click="downloadSelected">下载所选文件</el-button>
      <el-button size="small" class="button-item" type="primary" @click="showAria2Dialog">Aria2配置</el-button>
      <el-button size="small" class="button-item" type="primary" @click="previousStep">上一级</el-button>
    </div>
    <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" highlight-current-row
      @selection-change="handleSelectionChange">

      <el-table-column type="expand">
        <template #default="props">
          <div class="link-des-container">
            <el-row>
              <el-col :span="12"><span class="des-item">文件id: </span>{{ props.row.fs_id }}</el-col>
              <el-col :span="12"><span class="des-item">目录: </span>{{ props.row.isdir }}</el-col>
            </el-row>
            <el-row>
              <el-col :span="12"><span class="des-item">md5: </span>{{ props.row.md5 ? props.row.md5 : 'null' }}</el-col>
              <el-col :span="12"><span class="des-item">seckey: </span>{{ props.row.seckey }}</el-col>
            </el-row>
            <el-row>
              <el-col :span="12"><span class="des-item">文件大小: </span>{{ formatSize(props.row.size) }}</el-col>
              <el-col :span="12"><span class="des-item">文件名: </span>{{ props.row.server_filename }}</el-col>
            </el-row>
          </div>
        </template>
      </el-table-column>

      <el-table-column type="selection" width="55" />

      <el-table-column label="Name" width="600">
        <template #default="scope">
          <span class="file-svg" v-html="getFileIcon(scope.row.server_filename, scope.row.isdir)" />
          <a class="file-name" @click.prevent="handleFileClick(scope.row)">{{ scope.row.server_filename }}</a>
        </template>
      </el-table-column>

      <el-table-column label="Size">
        <template #default="scope">{{ formatSize(scope.row.size) }}</template>
      </el-table-column>

      <el-table-column label="Download Link">
        <template #default="scope">
          <el-button v-if="scope.row.isdir" type="success" :icon="Link" size="small" plain
            @click="handleFileClick(scope.row)">open</el-button>
          <el-button v-else type="success" :icon="Download" size="small" plain
            @click="handleFileClick(scope.row)">download</el-button>
        </template>
      </el-table-column>

    </el-table>
  </div>
</template>

<script setup lang='ts'>
import { ref, reactive, onMounted } from 'vue'
import { useStore } from 'vuex'
import { formatSize } from '@/utils/format'
import { getShareDir, getSignAndTime, getsvipdlink } from '@/api/download'
import path from 'path-browserify'
import { Download,Link } from '@element-plus/icons-vue'
import { getSvgByName } from './svgData'

interface ShareFile {
  fs_id: any,
  isdir: any,
  md5: string,
  path: string,
  size: number,
  server_filename: string,
  uk: string,
  shareid: number,
  seckey: any
}

const props = defineProps<{}>()

const store = useStore()
const surl = ref(store.state.surl)
const pwd = ref(store.state.pwd)
const currPath = ref('')
const listLoading = ref(false)
const list = ref<ShareFile[]>([]) //当前数据列表
const selectedList = ref<ShareFile[]>([]) //当前
const aria2DialogVisible = ref(false)
const dialogVisible = ref(false)
const sign = ref('')
const timestamp = ref('')
const form = reactive({
  aria2RpcUrl: localStorage.getItem('aria2RpcUrl') || 'http://localhost:6800/jsonrpc',
  aria2RpcSecret: localStorage.getItem('aria2RpcSecret') || 'Aria2 的 RPC 密钥',
  saveDir: localStorage.getItem('saveDir') || '/path/to/download/folder'
})

const getFileIcon = (fileName: string, isdir: boolean) => {
  const svg = getSvgByName(fileName, isdir)
  return svg.icon
}


/**
 * 获取时间搓和签名
 * @param surl 
 * @param uk 
 * @param shareid 
 */
const fetchSignAndTime = (surl: string, uk: string, shareid: any) => {
  if (!surl || !uk || !shareid) {
    return
    //throw new Error('Invalid arguments: surl, uk and shareid are required')
  }
  const form = {
    surl: surl,
    uk: uk,
    shareid: shareid
  }
  getSignAndTime(form).then(response => {
    const data = response.data
    sign.value = data.sign
    timestamp.value = data.timestamp
  })
}


/**
 * 数据抓取
 * @param result 
 */
const fetchData = (result?: any) => {
  listLoading.value = true
  result = result || store.state.pandownData

  if (!result) {
    listLoading.value = false
    return
  }
  const { uk, shareid, seckey } = result
  fetchSignAndTime(surl.value, uk, shareid)
  const resultList = result.list as any[]
  if (!resultList) {
    listLoading.value = false
    return
  }

  const fileList: ShareFile[] = []
  resultList.forEach(file => {
    const { fs_id, isdir, md5, path, size, server_filename } = file
    fileList.push({
      fs_id,
      isdir,
      md5,
      path,
      size,
      server_filename,
      uk,
      shareid,
      seckey,
    })
  })
  list.value = fileList
  listLoading.value = false
}

/**
 * 目录解析
 * @param path 
 */
const directoryparse = async (path: string) => {
  if (typeof path !== 'string' || !path.trim()) {
    return
    //throw new Error('directoryparse():Invalid argument: path should be non-empty string')
  }
  const form = {
    surl: surl.value,
    pwd: pwd.value,
    dir: path
  }

  getShareDir(form).then(response => {
    const data = response.data
    currPath.value = path
    fetchData(data)
  })
}

/**
 * 文件解析
 * @param fs_id 
 * @param seckey 
 * @param shareid 
 * @param uk 
 */
const fileparse = (fs_id: any, seckey: any, shareid: any, uk: string) => {
  if (!fs_id || !seckey || !shareid || !uk) {
    return
    //throw new Error('fileparse():Invalid arguments: fs_id, seckey, shareid and uk are required')
  }
  const form = {
    fs_id: fs_id,
    timestamp: timestamp.value,
    sign: sign.value,
    seckey: seckey,
    shareid: shareid,
    uk: uk
  }

  getsvipdlink(form).then(response => {
    const data = response.data
  })
}

/**
 * 文件点击
 * @param row 
 */
const handleFileClick = (row: ShareFile) => {
  try {
    if (!row) {
      return
      //throw new Error('Invalid argument: row is required')
    }
    if (row.isdir == 1) { // 是目录
      directoryparse(row.path)
    } else { // 单文件，请求svipdlink
      fileparse(row.fs_id, row.seckey, row.shareid, row.uk)
    }
  } catch (error) {
    console.error(error)
  }
}

const handleSelectionChange = (val: ShareFile[]) => {
  selectedList.value = val
}

const downloadSelected = () => {
  // 获取选中行的数据并进行相应处理
}

const showAria2Dialog = () => {

}

const previousStep = () => {

}

onMounted(() => {
  fetchData()
})

</script>

<style scoped>
::v-deep.file-svg {
  margin-right: 8px;
}

::v-deep.file-svg .icon {
  width: 15px;
  top: 3px;
  position: relative;
}

::v-deep .el-scrollbar__view {
  width: 100%;
}

.el-row {
  margin-bottom: 20px;
}

.el-row:last-child {
  margin-bottom: 0;
}

.el-col {
  border-radius: 4px;
}

.link-des-container {
  margin-left: 60px;
}

.des-item {
  font-weight: bold;
  color: #646569;
}
</style>