<template>
    <div class="app-container">
        <el-card class="box-card">
            <template #header>
                <div class="card-header">
                    <span>用户账号管理</span>
                    <el-button-group class="button-container">
                        <el-button type="primary" @click="addButtonHandle">添加</el-button>
                        <el-button type="primary" @click="refresh">刷新</el-button>
                    </el-button-group>
                </div>
            </template>
            <el-table :data="tableData" style="width:100%;">
                <el-table-column type="expand">
                    <template #default="props">
                        <div class="data-des-container">
                            <el-row>
                                <el-col :span="8"><span class="des-item">用户名: </span>{{ props.row.username }}</el-col>
                                <el-col :span="8"><span class="des-item">email: </span>{{ props.row.email }}</el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="8"><span class="des-item">解析文件数: </span>{{ props.row.parseNum }}</el-col>
                                <el-col :span="8"><span class="des-item">解析流量: </span>{{ formatSize(props.row.parseFlow)
                                }}</el-col>
                                <el-col :span="8"><span class="des-item">流量限额: </span>{{ formatSize(props.row.limitFlow)
                                }}</el-col>
                            </el-row>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="id" label="id" />
                <el-table-column prop="email" label="email" />
                <el-table-column prop="roleName" label="角色" />
                <el-table-column prop="state" label="流量状态" />
                <el-table-column label="操作">
                    <template #default="scope">
                        <el-button link type="primary" size="small" @click="updateButtonHandle(scope.row)">修改</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination-container">
                <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :page-sizes="[10, 20]" small
                    background layout=" prev, pager, next, sizes" :total="dataTotal" @size-change="handleSizeChange"
                    @current-change="handleCurrentChange" />
            </div>
        </el-card>

        <el-dialog v-model="dialogUpdateFormVisible" title="修改">
            <el-form :model="updateForm" style="max-width: 460px">
                <el-form-item label="用户名" :label-width="'80px'">
                    <el-input v-model="updateForm.username" autocomplete="off" />
                </el-form-item>
                <el-form-item label="角色" :label-width="'80px'">
                    <el-select v-model="updateForm.roleName" placeholder="角色选择">
                        <el-option label="user" value="user" />
                        <el-option label="admin" value="admin" />
                    </el-select>
                </el-form-item>
                <el-form-item label="流量限额" :label-width="'80px'">
                    <el-input v-model="updateForm.limitFlow" autocomplete="off" />
                </el-form-item>
                <el-form-item label="流量状态" :label-width="'80px'">
                    <el-select v-model="updateForm.state" placeholder="状态选择">
                        <el-option v-for="state in stateData" :value="state.value">
                            <span style="float: left">{{ state.value }}</span>
                            <span style="float: right;color: var(--el-text-color-secondary);font-size: 13px;">{{ state.des
                            }}</span>
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogUpdateFormVisible = false">撤销</el-button>
                    <el-button type="primary" @click="updateFormHandle">跟新</el-button>
                </span>
            </template>
        </el-dialog>


        <el-dialog v-model="dialogAddFormVisible" title="添加">
            <el-form :model="addForm" style="max-width: 460px">
                <el-form-item label="用户名" :label-width="'80px'">
                    <el-input v-model="addForm.username" autocomplete="off" />
                </el-form-item>
                <el-form-item label="邮箱" :label-width="'80px'">
                    <el-input v-model="addForm.email" autocomplete="off" />
                </el-form-item>
                <el-form-item type="password" label="密码" :label-width="'80px'">
                    <el-input v-model="addForm.password" autocomplete="off" />
                </el-form-item>
                <el-form-item label="角色" :label-width="'80px'">
                    <el-select v-model="addForm.roleName" placeholder="角色选择">
                        <el-option label="user" value="user" />
                        <el-option label="admin" value="admin" />
                    </el-select>
                </el-form-item>
                <el-form-item label="流量限额" :label-width="'80px'">
                    <el-input v-model="addForm.limitFlow" autocomplete="off" />
                </el-form-item>
                <el-form-item label="流量状态" :label-width="'80px'">
                    <el-select v-model="addForm.state" placeholder="状态选择">
                        <el-option v-for="state in stateData" :value="state.value">
                            <span style="float: left">{{ state.value }}</span>
                            <span style="float: right;color: var(--el-text-color-secondary);font-size: 13px;">{{ state.des
                            }}</span>
                        </el-option>
                    </el-select>
                </el-form-item>

            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogAddFormVisible = false">撤销</el-button>
                    <el-button type="primary" @click="addFormHandle">添加</el-button>
                </span>
            </template>
        </el-dialog>

    </div>
</template>

<script setup lang='ts'>
import { ref, reactive, onMounted } from 'vue'
import { getUserDetail, getUserNum, updateUserDetail, addUserDetail } from '@/api/userService'
import { ElMessage } from 'element-plus'
import { formatSize } from '@/utils/index'
const props = defineProps<{}>()

const stateData = [
    {
        value: 1,
        des: '可用'
    },
    {
        value: 0,
        des: '流量超标'
    },
    {
        value: -1,
        des: '冻结'
    },
]

const tableData = ref<any[]>([])
const dataTotal = ref(10)
const currentPage = ref(1)
const pageSize = ref(10)

const dialogUpdateFormVisible = ref(false)
const dialogAddFormVisible = ref(false)

const updateForm = reactive({
    id: '0',
    username: '',
    roleName: 'user',
    limitFlow: 0,
    state: 1
})

const addForm = reactive({
    roleName: 'user',
    username: '',
    password: '',
    email: '',
    limitFlow: 0,
    state: 1,
})

const updateButtonHandle = (row: any) => {
    dialogUpdateFormVisible.value = true
    updateForm.id = row.id
    updateForm.username = row.username
    updateForm.roleName = row.roleName
    updateForm.limitFlow = row.limitFlow
    updateForm.state = row.state
}

const addButtonHandle = (row: any) => {
    dialogAddFormVisible.value = true
}

const updateFormHandle = () => {
    updateUserDetail(updateForm)
        .then(res => {
            ElMessage.success('跟新成功')
            dialogUpdateFormVisible.value = false
        }).catch(err => {
            ElMessage.error('跟新失败')
            dialogUpdateFormVisible.value = false
        })
}

const addFormHandle = () => {
    addUserDetail(addForm)
        .then(res => {
            ElMessage.success('添加成功')
            dialogAddFormVisible.value = false
        }).catch(err => {
            ElMessage.error('添加失败')
            dialogAddFormVisible.value = false
        })
}

const handleSizeChange = (size: number) => {
    refresh()
}

const handleCurrentChange = (value: number) => {
    refresh()
}

const refresh = () => {
    getUserDetail({ pageNum: currentPage.value, pageSize: pageSize.value, })
        .then((res) => {
            const data = res.data
            tableData.value = data.records
            dataTotal.value = data.total
        })
}

onMounted(() => {
    refresh()
})
</script>

<style scoped>
.app-container {
    margin: 10px;
}

.data-des-container {
    margin-left: 20px;
}

.des-item {
    font-weight: bold;
    color: #646569;
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

.pagination-container {
    margin-top: 20px;
}

.el-card {
    height: calc(100vh - 110px);
    position: relative;
}

.pagination-container {
    margin-top: 20px;
    position: absolute;
    bottom: 40px;
}

.button-container {
    margin-left: 20px;
}
</style>