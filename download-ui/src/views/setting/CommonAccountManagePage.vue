<script setup lang='ts'>
import { getCommonAccountDetail, addCommonAccount, updateCommonAccount,deleteCommonAccount} from '@/api/accountService';
import { ElMessage } from 'element-plus';
import { onMounted, reactive, ref } from 'vue';

const props = defineProps<{}>()

const tableData = ref<any[]>([])
const dialogAddFormVisible = ref(false)
const dialogEditFormVisible = ref(false)

const form = reactive({
    cookie: ''
})

const editForm = reactive({
    id:'',
    name:'',
    cookie: ''
})

/**
 * 删除按钮处理
 * @param row 
 * @param index 
 */
const deleteButtonHandle = (row: any, index: number) => {
    deleteCommonAccount({
        id:row.id,
        name:row.name
    }).then(response=>{
        ElMessage.success("删除成功")
        tableData.value.splice(index,1)
    }).catch(err=>{
        ElMessage.error("删除失败")
    })

    
}

/**
 * 修改按钮处理
 * @param row 
 * @param index 
 */
const editButtonHandle = (row: any, index: number) => {
    editForm.cookie = row.cookie
    editForm.id = row.id
    editForm.name = row.name
    dialogEditFormVisible.value = true
}

/**
 * 添加操作
 */
const addAccountHandle = () => {
    addCommonAccount({cookie:form.cookie})
    .then(response=>{
        ElMessage.success("添加成功")
        dialogAddFormVisible.value = false
    }).catch(err=>{
        ElMessage.error("添加失败")
        dialogAddFormVisible.value = false
    })
}

/**
 * 修改存在
 */
const editAccountHandle = () => {
    updateCommonAccount({
        id:editForm.id,
        name:editForm.name,
        cookie:editForm.cookie
    }).then(response=>{
        ElMessage.success("修改成功")
        dialogEditFormVisible.value = false
    }).catch(err=>{
        ElMessage.error("修改失败")
        dialogEditFormVisible.value = false
    })
}

const refresh = () => {
    getCommonAccountDetail().then(response => {
        const data = response.data
        tableData.value = data
    })
}

onMounted(() => {
    refresh()
})

</script>

<template>
    <div class="app-container">
        <el-card class="box-card">
            <template #header>
                <div class="card-header">
                    <span>普通账号列表</span>
                    <el-button-group class="button-container">
                        <el-button type="primary" @click="dialogAddFormVisible = true">添加</el-button>
                        <el-button type="primary" @click="refresh">刷新</el-button>
                    </el-button-group>
                </div>
            </template>
            <el-table :data="tableData" style="width: 100%">
                <el-table-column type="expand">
                    <template #default="props">
                        <div class="data-des-container">
                            <p><span class="des-item">cookie: </span>{{ props.row.cookie }}</p>
                        </div>
                    </template>
                </el-table-column>

                <el-table-column prop="id" label="id" />
                <el-table-column prop="name" label="账号名" />
                <el-table-column prop="state" label="状态" />
                <el-table-column label="操作">
                    <template #default="scope">
                        <el-button link type="primary" size="small"
                            @click="deleteButtonHandle(scope.row, scope.$index)">删除</el-button>
                        <el-button link type="primary" size="small"
                            @click="editButtonHandle(scope.row, scope.$index)">修改</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-card>


        <el-dialog v-model="dialogAddFormVisible" title="添加">
            <el-form :model="form">
                <el-form-item label="cookie" :label-width="'60px'">
                    <el-input v-model="form.cookie" autocomplete="off" />
                </el-form-item>
                <el-alert type="info" show-icon :closable="false">
                    <p>需要填入普通账号的cookie,获取方法可以通过baiduwp-php项目的教程学习。<a target="_blank" style="color: #409EFF;" href="https://blog.imwcr.cn/2022/11/24/%e5%a6%82%e4%bd%95%e6%8a%93%e5%8c%85%e8%8e%b7%e5%8f%96%e7%99%be%e5%ba%a6%e7%bd%91%e7%9b%98%e7%bd%91%e9%a1%b5%e7%89%88%e5%ae%8c%e6%95%b4-cookie/">链接</a></p>
                </el-alert>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogAddFormVisible = false">撤销</el-button>
                    <el-button type="primary" @click="addAccountHandle">添加</el-button>
                </span>
            </template>
        </el-dialog>


        <el-dialog v-model="dialogEditFormVisible" title="修改">
            <el-form :model="editForm">
                <el-form-item label="cookie" :label-width="'60px'">
                    <el-input v-model="editForm.cookie" autocomplete="off" />
                </el-form-item>
                <el-alert type="info" show-icon :closable="false">
                    <p>需要填入普通账号的cookie,获取方法可以通过baiduwp-php项目的教程学习。<a target="_blank" style="color: #409EFF;" href="https://blog.imwcr.cn/2022/11/24/%e5%a6%82%e4%bd%95%e6%8a%93%e5%8c%85%e8%8e%b7%e5%8f%96%e7%99%be%e5%ba%a6%e7%bd%91%e7%9b%98%e7%bd%91%e9%a1%b5%e7%89%88%e5%ae%8c%e6%95%b4-cookie/">链接</a></p>
                </el-alert>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogEditFormVisible = false">撤销</el-button>
                    <el-button type="primary" @click="editAccountHandle">修改</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<style scoped>
.app-container {
    margin: 10px;
}

.button-container {
    margin-left: 20px;
}

.des-item {
    font-weight: bold;
    color: #646569;
}

p{
    word-break: break-all;
}
.data-des-container{
    margin-left: 20px;
}
</style>