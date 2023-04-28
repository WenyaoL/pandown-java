<template>
  <div class="app-container">
    <!-- 全选 -->
    <div class="button-group">
      <el-checkbox v-model="isAllSelected" class="button-item" @change="selectAll">{{ isAllSelected ? '取全选' : '全选' }}</el-checkbox>
      <el-button size="small" class="button-item" type="primary" @click="downloadSelected">下载所选文件</el-button>
      <el-button size="small" class="button-item" type="primary" @click="showAria2Dialog">Aria2配置</el-button>
      <el-button size="small" class="button-item" type="primary" @click="previousStep">上一级</el-button>
    </div>
    <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" fit highlight-current-row>
      <el-table-column type="expand">
        <template #default="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item label="文件id">
              <span>{{ props.row.fs_id }}</span>
            </el-form-item>
            <el-form-item label="目录">
              <span>{{ props.row.isdir }}</span>
            </el-form-item>
            <el-form-item label="md5">
              <span>{{ props.row.md5 ? props.row.md5 : 'null' }}</span>
            </el-form-item>
            <el-form-item label="seckey">
              <span>{{ props.row.seckey }}</span>
            </el-form-item>
            <el-form-item label="文件大小">
              <span>{{ formatSize(props.row.size) }}</span>
            </el-form-item>
            <el-form-item label="文件名">
              <span>{{ props.row.server_filename }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>

      <!-- 反选 -->
      <el-table-column align="center" width="50">
        <template #default="scope">
          <el-checkbox v-model="scope.row.isSelected" />
        </template>
      </el-table-column>

      <el-table-column label="Name" width="600">
        <template #default="scope">
          <span class="file-svg" v-html="getFileIcon(scope.row.server_filename, scope.row.isdir)" />
          <a class="file-name" @click.prevent="onClick(scope.row)">{{ scope.row.server_filename }}</a>
        </template>
      </el-table-column>

      <el-table-column label="Size">
        <template #default="scope">{{ formatSize(scope.row.size) }}</template>
      </el-table-column>
      <el-table-column label="Download Link">
        <template #default="scope">
          <el-button
            v-if="scope.row.isdir"
            type="success"
            icon="el-icon-link"
            size="small"
            plain
            @click="onClick(scope.row)"
          >open</el-button>
          <el-button
            v-else
            type="success"
            icon="el-icon-download"
            size="small"
            plain
            @click="onClick(scope.row)"
          >download</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="解析结果" :visible.sync="dialogVisible">
      <a :href="row.svipdlink" style="word-break: break-all; color: blue">下载链接</a>
      <template>
        <div class="download">
          <p>下载提示:因百度限制，需修改浏览器 User Agent为LogStatistic 后下载。</p>
          <el-divider />
          <div class="btn-group">
            <el-button type="primary" @click="idmDownload()">IDM下载</el-button>
            <!-- 单文件解析时，下载到当前目录即可，即不需要path参数 -->
            <el-button type="primary" @click="downloadFileByAria2($data.row)">Aria2下载</el-button>
          </div>
        </div>
      </template>
    </el-dialog>
    <el-dialog :visible.sync="aria2DialogVisible" title="配置 Aria2">
      <el-form :model="form" label-width="120px">
        <el-form-item label="RPC 地址">
          <el-input v-model="form.aria2RpcUrl" />
        </el-form-item>
        <el-form-item label="RPC 密钥">
          <el-input v-model="form.aria2RpcSecret" />
        </el-form-item>
        <el-form-item label="下载目录">
          <el-input v-model="form.saveDir" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="aria2DialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAria2Settings()">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
/* eslint-disable */
import baiduapi from '@/api/baidu'
import { mapState } from 'vuex'
import { getSvgByName } from './svgData'
import path from 'path-browserify'
export default {
  computed: {
    ...mapState(['surl', 'pwd']),
    isIndeterminate() {
      if (this.list.length === 0) {
        return false
      }
      return this.list.some(item => item.isSelected) && !this.isAllSelected
    },
    selectedList() {
      return this.list.filter(item => item.isSelected)
    }
  },

  data() {
    return {
      list: null, // 存储表格数据
      listLoading: false, // 控制表格加载状态
      isAllSelected: false, // 默认情况下未选中任何行
      sign: '',
      timestamp: '',
      dialogVisible: false,
      row: '',
      aria2DialogVisible: false,
      form: {
        aria2RpcUrl: localStorage.getItem('aria2RpcUrl') || 'http://localhost:6800/jsonrpc',
        aria2RpcSecret: localStorage.getItem('aria2RpcSecret') || 'Aria2 的 RPC 密钥',
        saveDir: localStorage.getItem('saveDir') || '/path/to/download/folder'
      },
      currPath:''
    }
  },

  mounted() {
    // 在组件加载完成后调用 fetchData 方法获取数据
    this.fetchData()
  },

  methods: {
    fetchData(result) {
      this.listLoading = true
      const store = this.$store
      result = result || store.state.pandownData.data
      // 将 API 返回的数据转换为表格所需的格式
      const fileList = []
      for (let i = 0; i < result?.length; i++) {
        var isSelected = this.isSelected
        const files = result[i].list
        const { uk, shareid, seckey } = result[i]
        for (let j = 0; j < files.length; j++) {
          const { fs_id, isdir, md5, path, size, server_filename } = files[j]
          fileList.push({
            fs_id,
            isdir,
            md5,
            path,
            size,
            server_filename,
            uk, shareid, seckey,
            isSelected
          })
        }
      }
      this.list = fileList
      this.listLoading = false
    },
    // 下载选框选中的所有文件，包含文件夹和单文件
    async downloadSelected() {
      // 获取选中行的数据并进行相应处理
      const selectedRows = this.selectedList
      await this.getSignAndTime(this.surl, selectedRows[0].uk, selectedRows[0].shareid)
      for (let i = 0; i < selectedRows.length; i++) {
        const row = selectedRows[i]
        // 这里的row有文件夹，也有单文件。
        if (row.isdir == 0) {
          try {
            // 发送 API 请求，获取文件的 svipdlink
            const form = {
              fs_id: row.fs_id,
              timestamp: this.timestamp,
              sign: this.sign,
              seckey: row.seckey, // 假设这里是 seckey 属性的值
              shareid: row.shareid, // 假设这里是 shareid 属性的值
              uk: row.uk // 假设这里是 uk 属性的值
            }
            const response = await baiduapi.get('/getsvipdlink', form, 'http://127.0.0.1:8010', {
              headers: {
                'Content-Type': 'application/json'
              }
            })
            row.svipdlink = response.data.svipdlink
          } catch (error) {
            console.error(error)
          }
          console.log(`Downloading ${row.name}...`)
          // 使用 aria2 等工具进行下载
          this.aria2Download(row.svipdlink, row.path, row.server_filename)
        } else { // 文件夹
          this.downloadFolderByAria2(row.path)
        }
      }
    },
    selectAll() {
      // this.isAllSelected = !this.isAllSelected;
      // 更新所有条目的选中状态
      if (this.isAllSelected) {
        this.list.forEach(item => {
          item.isSelected = true
        })
      } else {
        this.list.forEach(item => {
          item.isSelected = false
        })
      }
    },
    // 获取sign和time参数
    async getSignAndTime(surl, uk, shareid) {
      if (!surl || !uk || !shareid) {
        throw new Error('Invalid arguments: surl, uk and shareid are required')
      }
      const form = {
        surl: surl,
        uk: uk,
        shareid: shareid
      }
      if (this.sign && this.timestamp) {
        console.log(`sign: ${this.sign}, time: ${this.timestamp}`)
      } else {
        try {
          const response = await baiduapi.get('/api/getSignAndTime', form, process.env.VUE_APP_BASE_API, {
            headers: {
              'Content-Type': 'application/json'
            }
          })
          if (!response || !response.data) {
            throw new Error('Invalid API response: sign and timestamp should be non-empty strings')
          }
          const { sign, timestamp } = response.data
          this.sign = sign
          this.timestamp = timestamp
          console.log(`sign: ${this.sign}, time: ${this.timestamp}`)
        } catch (error) {
          console.error('Failed to get sign and time:', error)
          throw error // 抛出异常，交由上层调用方处理
        }
      }
    },
    // 按解析后的框框
    showDialog() {
      this.dialogVisible = true
    },
    // aria2相关方法
    showAria2Dialog() {
      this.aria2DialogVisible = true
    },
    saveAria2Settings() {
      const { aria2RpcUrl, aria2RpcSecret, saveDir } = this.form
      console.log('aria2:' + aria2RpcUrl, aria2RpcSecret, saveDir)
      localStorage.setItem('aria2RpcUrl', aria2RpcUrl)
      localStorage.setItem('aria2RpcSecret', aria2RpcSecret)
      localStorage.setItem('saveDir', saveDir)
      this.aria2DialogVisible = false
    },
    // aria2下载，路径path为自定义。
    async aria2Download(downloadUrl, path, server_filename) {
      if (!downloadUrl) {
        throw new Error('Invalid argument: downloadUrl should be a non-empty string')
      }
      const aria2RpcUrl = localStorage.getItem('aria2RpcUrl')
      const aria2RpcSecret = localStorage.getItem('aria2RpcSecret')
      const downloadDir = localStorage.getItem('saveDir') || ''
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
                'dir': downloadDir + path,
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
        throw error // 抛出异常，交由上层调用方处理
      }
    },
    // 单独的row里的文件下载，path为根目录，保存路径为用户aria2设定的根目录。
    downloadFileByAria2(row) {
      this.aria2Download(row.svipdlink, '', row.server_filename)
    },
    // 下载目录，需注意文件夹里面可能还有文件夹，需要保留。调用前确保sign和time已经存在否则请求svipdlink会失败。
    async downloadFolderByAria2(folderPath) {
      if (!folderPath) {
        console.error('Folder path is empty or null.')
        return
      }
      // 解析当前目录，解析后遍历row，若是文件则保留路径下载，若是目录则继续递归调用
      try {
        const result = await this.directoryparse(folderPath)
        for (let i = 0; i < result.length; i++) {
          const files = result[i].list
          const { uk, shareid, seckey } = result[i]
          for (let j = 0; j < files.length; j++) {
            const { fs_id, isdir, path, server_filename } = files[j]
            if (isdir == 0) { // 单文件，获取svipdlink然后进行下载
              try {
                const response = await this.fileparse(fs_id, seckey, shareid, uk)
                let downPath = ''
                if (path && path.trim() !== '') {
                  const lastIndex = path.lastIndexOf('/')
                  console.log('path:' + path + 'lastIndex:' + lastIndex)
                  if (lastIndex >= 0) {
                    downPath = path.substring(0, lastIndex)
                  }
                }
                if (downPath && response.data.svipdlink && server_filename) {
                  this.aria2Download(response.data.svipdlink, downPath, server_filename)
                } else {
                  console.error('Some required data is empty or null.')
                }
              } catch (error) {
                console.error(`Failed to parse file ${fs_id}: ${error}`)
              }
            } else if (path && path.trim() !== '') {
              // 目录则继续递归
              try {
                await this.downloadFolderByAria2(path)
              } catch (error) {
                console.error(`Failed to download folder ${path}: ${error}`)
              }
            }
          }
        }
      } catch (error) {
        console.error(`Failed to parse directory ${folderPath}: ${error}`)
      }
    },
    // 目录解析
    async directoryparse(path) {
      if (typeof path !== 'string' || !path.trim()) {
        throw new Error('directoryparse():Invalid argument: path should be non-empty string')
      }
      const surl = this.surl
      const pwd = this.pwd
      const form = { surl, pwd, directory: path }
      try {
        const response = await baiduapi.get('/list_files', form, process.env.VUE_APP_BASE_API, {
          headers: {
            'Content-Type': 'application/json'
          }
        })
        if (!response || !response.data) {
          throw new Error('Invalid API response')
        }
        return response.data.result
      } catch (error) {
        console.error(error)
        throw error
      }
    },
    // 单文件解析
    async fileparse(fs_id, seckey, shareid, uk) {
      if (!fs_id || !seckey || !shareid || !uk) {
        throw new Error('fileparse():Invalid arguments: fs_id, seckey, shareid and uk are required')
      }
      await this.getSignAndTime(this.surl, uk, shareid)
      const form = {
        fs_id: fs_id,
        timestamp: this.timestamp,
        sign: this.sign,
        seckey: seckey,
        shareid: shareid,
        uk: uk
      }
      try {
        const response = await baiduapi.get('/getsvipdlink', form, process.env.VUE_APP_BASE_API, {
          headers: {
            'Content-Type': 'application/json'
          }
        })
        if (!response || !response.data) {
          throw new Error('Invalid API response')
        }
        return response
      } catch (error) {
        console.error(error)
        throw error
      }
    },

    //to previousStep(返回上一级)
    async previousStep(){
      if(!this.currPath) return
      const previousPath = path.dirname(this.currPath) + "/"
      const result = await this.directoryparse(previousPath)
      if (result && result.length > 0) {
        this.fetchData(result)
        this.currPath = previousPath
        return
      } 
      
      //抓取根目录
      this.fetchData()
      this.currPath = previousPath

    },

    // 点击解析按钮
    async onClick(row) {
      try {
        if (!row) {
          throw new Error('Invalid argument: row is required')
        }
        if (row.isdir == 1) { // 是目录
          const result = await this.directoryparse(row.path)
          if (result && result.length > 0) {
            // 使用 router.push 方法跳转到同一组件，并更新 query 参数为新获取的数据
            // this.$router.push({ name: 'BaiduTable', query: { result: JSON.stringify(result) } });
            this.currPath = row.path
            this.fetchData(result)
          } else {
            console.warn(`directoryparse(${row.path}) returned empty result`)
          }
        } else { // 单文件，请求svipdlink
          const response = await this.fileparse(row.fs_id, row.seckey, row.shareid, row.uk)
          if (response && response.data && response.data.svipdlink) {
            const svipdlink = response.data.svipdlink
            row.svipdlink = svipdlink
            this.row = row
            this.showDialog(row)
            console.log(svipdlink) // 在控制台输出文件下载链接
          } else {
            console.warn(`fileparse(${row.fs_id}, ${row.seckey}, ${row.shareid}, ${row.uk}) returned invalid response`)
          }
        }
      } catch (error) {
        console.error(error)
        // 在发生异常时进行错误处理，例如提示用户或回退到上一页等
      }
    },
    formatSize(size) {
      if(size == 0) return '—'
      // 将字节数转换为 GB 或 MB，并保留两位小数
      if (size < 1024 * 1024) {
        return (size / 1024).toFixed(2) + ' KB'
      } else if (size < 1024 * 1024 * 1024) {
        return (size / (1024 * 1024)).toFixed(2) + ' MB'
      } else {
        return (size / (1024 * 1024 * 1024)).toFixed(2) + ' GB'
      }
    },
    getFileIcon(fileName, isdir) {
      const svg = getSvgByName(fileName, isdir)
      return svg.icon
    }
  }
}

</script>

<style scoped>
.download-btn {
    cursor: pointer;
}

.demo-table-expand {
    font-size: 0;
}

.demo-table-expand label {
    width: 90px;
    color: #99a9bf;
}

.demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
}

::v-deep.file-svg {
    margin-right: 8px;
}

::v-deep.file-svg .icon {
    width: 15px;
    top: 4px;
    position: relative;
}

.file-name:hover {
    color: #067FE1;
}
.button-group{
    margin-inline: 20px;
}

.button-item{
    margin-inline: 5px;
}
</style>
