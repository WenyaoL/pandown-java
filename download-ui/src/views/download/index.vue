<template>
  <div class="link-container">
    <el-row>
      <el-col :offset="0" :span="23">
        <div class="button-group">
          <el-button-group class="setting-button-group">
            <el-button class="button-item" type="primary" @click="aria2SettingDialogVisible = true">Aria2配置</el-button>
          </el-button-group>
        </div>
        <el-collapse v-model="activeCollapseNames" class="collapse-panel">
          <el-collapse-item title="分享描述" name="Describe" :size='"small"'>
            <template #title>
              <span class="collapse-title">分享描述: </span><el-icon class="header-icon"><info-filled /></el-icon>
            </template>

            <el-row>
              <el-col :span="12"><span class="des-item">分享者: </span>{{ sharerInfo.uname }}<el-avatar :size="18"
                  class="avatar" :src="sharerInfo.avatar" /></el-col>
            </el-row>
            <el-row>
              <el-col :span="12"><span class="des-item">分类标题: </span>{{ sharerInfo.title }}</el-col>
              <el-col :span="6"><span class="des-item">分享时间: </span>{{ sharerInfo.dateStr }}</el-col>
              <el-col :span="6"><span class="des-item">过期类型: </span>{{ sharerInfo.expiredStr }}</el-col>
            </el-row>
            <el-row>
              <el-col :span="12"><span class="des-item">surl: </span>{{ surl }}</el-col>
              <el-col :span="12"><span class="des-item">提取码: </span>{{ pwd }}</el-col>
            </el-row>
          </el-collapse-item>

        </el-collapse>
      </el-col>
    </el-row>


    <el-row>
      <el-col :offset="1" :span="22">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>分享列表</span>
              <el-button-group class="share-button-group">
                <el-button size="small" class="button-item" type="primary" @click="previousStep">上一级</el-button>
                <el-button size="small" class="button-item" type="primary" @click="toRootStep">返回根目录</el-button>

              </el-button-group>
              <el-button-group class="share-button-group">
                <el-button size="small" class="button-item" type="primary"
                  @click="downloadSelectedHandle">下载所选文件</el-button>
              </el-button-group>
            </div>
          </template>

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
                    <el-col :span="12"><span class="des-item">md5: </span>{{ props.row.md5 ? props.row.md5 : 'null'
                    }}</el-col>
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
                <span class="file-svg">
                  <component :is="getFileIcon(scope.row.server_filename, scope.row.isdir)" />
                </span>
                <a class="file-name" @click.prevent="handleFileClick(scope.row)">{{ scope.row.server_filename }}</a>
              </template>
            </el-table-column>

            <el-table-column label="Size">
              <template #default="scope">{{ formatSize(scope.row.size) }}</template>
            </el-table-column>

            <el-table-column label="操作">
              <template #default="scope">
                <el-button v-if="scope.row.isdir" type="primary" link size="small"
                  @click="handleFileClick(scope.row)">打开</el-button>
                <el-button v-else type="primary" link size="small" @click="handleFileClick(scope.row)">下载</el-button>
              </template>
            </el-table-column>

          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="aria2SettingDialogVisible" title="配置 Aria2">
      <el-form :model="aria2Setting" :label-width='"120px"'>
        <el-form-item label="RPC 地址">
          <el-input v-model="aria2Setting.aria2RpcUrl" autocomplete="off" />
        </el-form-item>
        <el-form-item label="RPC 密钥">
          <el-input v-model="aria2Setting.aria2RpcSecret" autocomplete="off" />
        </el-form-item>
        <el-form-item label="下载目录">
          <el-input v-model="aria2Setting.saveDir" autocomplete="off" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="aria2SettingDialogVisible = false" type="primary">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="downloadDialogVisible" title="下载当前文件">

      <p>下载提示:将提供Aria2下载,IDM下载,浏览器下载。(通过复制链接去下载，需修改浏览器 User Agent为LogStatistic 后下载。)</p>
      <el-table :data="currDownloadList" v-loading="downloadListLoading" max-height="315">
        <el-table-column property="server_filename" label="文件名" width="380" />
        <el-table-column property="size" label="size">
          <template #default="scope">
            {{ formatSize(scope.row.size) }}
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="primary" link size="small" @click="copyLink(scope.row.svipDlink)">复制</el-button>
          </template>
        </el-table-column>
      </el-table>

      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="downloadFileByAria2()">Aria2下载</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="downloadAllFileDialogVisible" title="批量下载任务">
      <div>可以下载包含所有链接的txt文件:<el-button type="text" @click="downloadAllLinkTxt">下载链接文件</el-button></div>
      <el-table :data="currDownloadList" v-loading="downloadListLoading" max-height="315">
        <el-table-column property="server_filename" label="文件名" width="380" />
        <el-table-column property="size" label="size">
          <template #default="scope">
            {{ formatSize(scope.row.size) }}
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="primary" link size="small" @click="copyLink(scope.row.svipDlink)">复制</el-button>
          </template>
        </el-table-column>
      </el-table>

      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="downloadFileByAria2()">发送到Aria2</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup lang='ts'>
import { ref, reactive, onMounted, computed } from 'vue'
import { useStore } from 'vuex'
import { formatSize } from '@/utils'
import { getShareDir, getSignAndTime, getSvipDlink, getAllSvipDlink } from '@/api/downloadService'
import { simpleInstance } from '@/api/request'
import path from 'path-browserify'
import { InfoFilled } from '@element-plus/icons-vue'
import { getSvgByName } from '@/components/StaticIcon/svgData'
import { ElMessageBox } from 'element-plus'

interface ShareFile {
  fs_id: any,
  isdir: any,
  md5: string,
  path: string,
  size: number,
  server_filename: string,
  uk: string,
  shareid: number,
  seckey: any,
  dlink?: any,
  svipDlink?: any,
}

const props = defineProps<{}>()

const store = useStore()
const surl = ref(store.state.surl)
const pwd = ref(store.state.pwd)

let shareLinkDetail = {
  uk: '',
  shareid: '',
  seckey: ''
}

const currPath = ref('') //当前路径
const listLoading = ref(false) //列表加载状态
const list = ref<ShareFile[]>([]) //当前数据列表
const selectedList = ref<ShareFile[]>([]) //当前选中数据
const currDownloadList = ref<any[]>([]) //当前要下载的文件
const activeCollapseNames = ref<String[]>(['Describe'])  //打开的折叠板


const sharerInfo = reactive({
  dateStr: 'null',
  expiredStr: 'null',
  uname: '',
  title: '',
  avatar: ''
})

const aria2SettingDialogVisible = ref(false)
const downloadDialogVisible = ref(false)
const downloadAllFileDialogVisible = ref(false)

const downloadListLoading = ref(false)

const aria2Setting = reactive({
  aria2RpcUrl: localStorage.getItem('aria2RpcUrl') || 'http://localhost:6800/jsonrpc',
  aria2RpcSecret: localStorage.getItem('aria2RpcSecret') || 'Aria2 的 RPC 密钥',
  saveDir: localStorage.getItem('saveDir') || '/path/to/download/folder'
})


/**
 * 获取文件icon
 * @param fileName 
 * @param isdir 
 */
const getFileIcon = (fileName: string, isdir: number) => {
  const svg = getSvgByName(fileName, isdir == 1 ? true : false)
  return svg.icon
}

/**
 * 获取时间搓和签名
 * @param surl 
 * @param uk 
 * @param shareid 
 */
const fetchSignAndTime = async (surl: string, uk: string, shareid: any) => {
  const form = {
    surl: surl,
    uk: uk,
    shareid: shareid
  }
  const response = await getSignAndTime(form)

  const { sign, timestamp } = response.data
  return { sign, timestamp }
}


/**
 * 数据解析，从后端给定是数据中抓取分享文件信息
 * @param result 给定数据
 */
const parseData = (result?: any) => {
  if (!result) {
    return
  }
  const { uk, shareid, seckey } = result
  shareLinkDetail = { uk, shareid, seckey }
  const resultList = result.list as any[]
  if (!resultList) {
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
  return fileList
}

/**
 * 加载分享数据
 */
const loadData = () => {
  listLoading.value = true
  const pandownData = store.state.pandownData
  list.value = parseData(pandownData) as any
  listLoading.value = false
}

/**
 * 加载分享者数据
 */
const loadSharerData = () => {
  const data = store.state.pandownData
  if (!data) return
  const avatar = data?.user?.avatar
  const { uname, title, link_ctime, expired_type } = data

  //fill sharerInfo
  sharerInfo.dateStr = new Date(link_ctime * 1000).toLocaleDateString()
  sharerInfo.expiredStr = expired_type == 0 ? "永久有效" : "非永久有效"
  sharerInfo.uname = uname
  sharerInfo.title = title
  sharerInfo.avatar = avatar
}



/**
 * 打开目录
 * @param path 
 */
const openDirectory = async (path: string) => {
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
    list.value = parseData(data) as any
  })
}

const fetchSvipDlink = async (form: any) => {
  const response = await getSvipDlink(form)
  const data = response.data
  if (!data || data.length == 0) return
  return data
}

/**
 * 单文件下载
 */
const downloadFile = async (row: ShareFile) => {
  //downloadDialogVisible.value = true
  const { fs_id, seckey, shareid, uk } = row

  const signAndTime = await fetchSignAndTime(surl.value, uk, shareid)

  if (!signAndTime) return
  const { sign, timestamp } = signAndTime

  const form = {
    shareFileList: [row],
    timestamp,
    sign,
    seckey,
    shareid,
    uk
  }
  const svipDlinkList = await fetchSvipDlink(form)
  console.log(svipDlinkList);

  if (!svipDlinkList || svipDlinkList.length == 0) return

  row.svipDlink = svipDlinkList[0].svipDlink
  currDownloadList.value = [row]
  downloadDialogVisible.value = true
  return
}


/**
 * aria2下载
 * @param downloadUrl 
 * @param path 
 * @param server_filename 
 */
const aria2Download = async (downloadUrl: string, path: string, server_filename: string) => {
  if (!downloadUrl) {
    throw new Error('Invalid argument: downloadUrl should be a non-empty string')
  }
  const { aria2RpcUrl, aria2RpcSecret, saveDir } = aria2Setting

  try {
    const response = await fetch(aria2RpcUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        jsonrpc: '2.0',
        id: '1',
        method: 'aria2.addUri',
        params: [
          'token:' + aria2RpcSecret,
          [downloadUrl],
          {
            'header': ['User-Agent: LogStatistic'],
            'dir': saveDir + path,
            'max-connection-per-server': '16',
            'out': server_filename
          }
        ]
      })
    })
    const data = await response.json()
    console.log('Aria2 下载结果：', data)
  } catch (error) {
    console.error('Aria2 下载出错：', error)
  }

}

/**
 * aria2下载按钮
 */
const downloadFileByAria2 = () => {
  currDownloadList.value.forEach(row => {
    aria2Download(row.svipDlink, row.path, row.server_filename)
  })
}

/**
 * 普通浏览器下载(因百度跨域限制和浏览器拒绝设置User-Agent的安全行为，该方法废弃)
 * @param downloadUrl 
 * @param path 
 * @param server_filename 
 */
const commonDownload = (downloadUrl: string, path: string, server_filename: string) => {
  simpleInstance.request({
    url: downloadUrl,
    method: "get",
    headers: {
      'User-Agent': 'LogStatistic',
    },
    withCredentials: true,
    responseType: 'blob'
  }).then(response => {
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', path + server_filename);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link)
  })
}
/**
 * 浏览器下载按钮处理（弃用）
 */
const downloadFileByCommon = () => {
  currDownloadList.value.forEach(row => {
    commonDownload(row.svipDlink, row.path, row.server_filename)
  })
}

/**
 * 文件点击
 * @param row 
 */
const handleFileClick = (row: ShareFile) => {
  if (!row) {
    return
  }

  if (row.isdir == 1) { // 是目录
    openDirectory(row.path)
  } else { // 单文件，请求svipdlink
    ElMessageBox.confirm(
      '是否解析下载该文件?(消耗流量)',
      'Warning',
      {
        confirmButtonText: '确定',
        cancelButtonText: '撤销',
        type: 'warning',
      }
    ).then(() => {
      downloadFile(row)
    })
  }
}



/**
 * 复制link字符串
 * @param link 
 */
const copyLink = (link: string) => {
  window.navigator.clipboard.writeText(link)
}


const handleSelectionChange = (val: ShareFile[]) => {
  selectedList.value = val
}

/**
 * 下载链接的txt文件
 */
const downloadAllLinkTxt = () => {
  const linkList: string[] = []
  currDownloadList.value.forEach(row => {
    linkList.push(row.svipDlink)
  })

  const url = window.URL.createObjectURL(new Blob([linkList.join("\n")]));
  const link = document.createElement('a');
  link.href = url;
  link.download = "link.txt"
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link)

}

const downloadSelectedHandle = () => {
  ElMessageBox.confirm(
    '是否解析选中的文件,包含文件夹?(消耗流量)',
    'Warning',
    {
      confirmButtonText: '确定',
      cancelButtonText: '撤销',
      type: 'warning',
    }
  ).then(() => {
    downloadSelected()
  })
}



/**
 * 下载选中的文件
 */
const downloadSelected = () => {
  // 获取选中行的数据并进行相应处理
  downloadAllFileDialogVisible.value = true
  downloadListLoading.value = true
  getAllSvipDlink({
    surl: surl.value,
    pwd: pwd.value,
    uk: shareLinkDetail.uk,
    shareid: shareLinkDetail.shareid,
    seckey: shareLinkDetail.seckey,
    shareFileList: selectedList.value
  }).then(res => {
    currDownloadList.value = res.data
    downloadListLoading.value = false
  }).catch(err => {
    currDownloadList.value = []
    downloadListLoading.value = false
  })
}


/**
 * 返回上一级目录
 */
const previousStep = () => {
  if (!currPath.value) return

  if (currPath.value == sharerInfo.title) {
    loadData()
    return
  }
  const previousPath = path.dirname(currPath.value)
  openDirectory(previousPath)
}

/**
 * 返回根目录
 */
const toRootStep = () => {
  loadData()
  currPath.value = ""
}

onMounted(() => {
  loadData()
  loadSharerData()
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

.collapse-title {
  font-size: 16px;
  font-weight: bold;
  color: #646569;
}

.collapse-panel {
  padding-left: 30px;

}

.setting-button-group {
  margin: 20px 20px;
}

.share-button-group {
  margin-left: 40px;
}

.avatar {
  position: relative;
  left: 5px;
  top: 5px;
}
</style>
