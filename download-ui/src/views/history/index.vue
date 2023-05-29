<template>
    <div class="app-container">
        <el-card class="box-card">
            <template #header>
                <div class="card-header">
                    <span>解析历史记录</span>
                    <el-button-group class="button-container">
                        <el-button type="primary" @click="refresh">刷新</el-button>
                        <el-button type="primary" @click="deleteSelected">删除选中的</el-button>
                    </el-button-group>
                </div>
            </template>
            <el-table :data="tableData" style="width:100%;" @selection-change="handleSelectionChange">

                <el-table-column type="expand" width="35">
                    <template #default="scope">
                        <div class="data-des-container">
                            <el-row>
                                <el-col :span="12">
                                    <span class="des-item">文件名: </span>{{ scope.row.filename }}
                                </el-col>
                                <el-col :span="8">
                                    <span class="des-item">文件大小: </span>{{ formatSize(scope.row.size) }}
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="12">
                                    <span class="des-item">md5: </span>{{ scope.row.md5 }}
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="24">
                                    <span class="des-item">path: </span>{{ scope.row.path }}
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="24">
                                    <p>
                                        <span class="des-item">reallink: </span>{{ scope.row.reallink }}
                                        <el-icon class="copyIcon" @click="copyIconHandle(scope.row.reallink)">
                                            <CopyDocument />
                                        </el-icon>
                                    </p>
                                </el-col>
                            </el-row>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column type="selection" width="55" />

                <el-table-column prop="filename" label="文件名" />
                <el-table-column label="文件大小">
                    <template #default="scope">
                        {{ formatSize(scope.row.size) }}
                    </template>
                </el-table-column>

                <el-table-column prop="createTime" label="解析时间" />
                <el-table-column label="操作" width="150">
                    <template #default="scope">
                        <el-button link type="primary" size="small"
                            @click="deleteButtonHandle(scope.row, scope.$index)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination-container">
                <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :page-sizes="[10, 20]" small
                    background layout=" prev, pager, next, sizes" :total="dataTotal" @size-change="handleSizeChange"
                    @current-change="handleCurrentChange" />
            </div>
        </el-card>
    </div>
</template>

<script setup lang='ts'>
import { ref, reactive, onMounted } from 'vue'
import { formatSize, copyStringToClipboard } from '@/utils/index'
import { getUserHistory, deleteUserParseHistory } from '@/api/pandownParseHistory'
import { ElMessage } from 'element-plus'
import { CopyDocument } from '@element-plus/icons-vue'

const props = defineProps<{}>()

const currentPage = ref(1)
const pageSize = ref(10)
const dataTotal = ref(0)

const tableData = ref<any[]>([])
const selectedList = ref<any[]>([]) //当前选中数据
const deleteButtonHandle = (row: any, index: number) => {
    deleteUserParseHistory({ deleteIds: [row.id] })
        .then(res => {
            tableData.value.splice(index, 1)
            ElMessage.success("删除成功")
        }).catch(err => {
            ElMessage.success("删除失败")
        })
}

const deleteSelected = () => {
    const deleteIds = selectedList.value.map(row => row.id)
    deleteUserParseHistory({ deleteIds })
        .then(res => {
            ElMessage.success("删除成功")
            refresh()
        }).catch(err => {
            ElMessage.success("删除失败")
        })

}

const handleSelectionChange = (val: any[]) => {
    selectedList.value = val
}

const copyIconHandle = (str:string) => {
  copyStringToClipboard(str)
  ElMessage.success('复制成功')
}

const refresh = () => {
    getUserHistory({
        pageNum: currentPage.value,
        pageSize: pageSize.value
    }).then(response => {
        const data = response.data
        const records = data.records
        dataTotal.value = data.total
        tableData.value = records
    }).catch(err => {
        console.log("请求失败");
    })
}

const handleSizeChange = (size: number) => {
    refresh()
}

const handleCurrentChange = (value: number) => {
    refresh()
}



onMounted(() => {
    refresh()
})

</script>

<style scoped lang="scss">
.data-des-container {
    margin-left: 20px;
}

.copyIcon {
  margin-left: 10px;
  color: #409EFF;
  top: 2px;
}

.des-item {
    font-weight: bold;
    color: #646569;
}

.el-row {
    margin-bottom: 20px;

    p {
        word-break: break-all;
        margin: 0;
    }
}

.el-row:last-child {
    margin-bottom: 0;
}

.button-container {
    margin-left: 20px;
}
</style>