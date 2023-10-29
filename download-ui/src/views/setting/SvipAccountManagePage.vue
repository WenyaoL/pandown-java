<script setup lang='ts'>
import { onMounted, ref, reactive } from 'vue'
import { getSvipAccountDetail, addSvipAccount, deleteSvipAccount,updateSvipAccount } from '@/api/accountService'
import {ElMessage} from 'element-plus'
const props = defineProps<{}>()
const tableData = ref<any[]>([])

const dialogFormVisible = ref(false)
const dialogEditFormVisible = ref(false)

const form = reactive({
    bduss: '',
    stoken: ''
})

const editForm = reactive({
    id:'',
    bduss: '',
    stoken: ''
})


const addSvipAccountHandle = function () {
    addSvipAccount({
        svipBduss: form.bduss,
        svipStoken: form.stoken
    }).then(res => {
        const data = res.data as any
        tableData.value.push(data)
        dialogFormVisible.value = false
        ElMessage.success("添加成功")
    }).catch(err => {
        console.log(err);
        dialogFormVisible.value = false
    })
}

const handleDelete = (row: any, index: number) => {
    deleteSvipAccount({
        id: row.id
    }).then(res => {
        tableData.value.splice(index, 1)
        ElMessage.success("删除成功")
    })

}

const editButtonHandle = (row: any, index: number) =>{
    editForm.id = row.id
    editForm.bduss = row.svipBduss
    editForm.stoken = row.svipStoken
    dialogEditFormVisible.value = true
}

const handleEdit = () =>{
    updateSvipAccount({
        id:editForm.id,
        svipBduss:editForm.bduss,
        svipStoken:editForm.stoken
    })
    .then(res=>{
        dialogEditFormVisible.value = false
        ElMessage.success("修改成功")
    }).catch(err=>{
        ElMessage.error("修改失败")
    })
}

const formatVipType = (type:number) =>{
    if(type==1){
        return '普通会员'
    }

    if(type>=1){
        return '超级会员'
    }
}

const refresh = () =>{
    getSvipAccountDetail().then(response => {
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
              <span>SVIP账号列表</span>
              <el-button-group class="button-container">
                <el-button type="primary" @click="dialogFormVisible = true">添加</el-button>
                <el-button type="primary" @click="refresh">刷新</el-button>
              </el-button-group>
            </div>
          </template>

          <el-table :data="tableData"  style="width: 100%">
            <el-table-column type="expand">
                <template #default="props">
                    <div class="data-des-container">
                        <p><span class="des-item">Bduss: </span>{{ props.row.svipBduss }}</p>
                        <p><span class="des-item">Stoken: </span>{{ props.row.svipStoken }}</p>
                    </div>
                </template>
            </el-table-column>

            <el-table-column prop="id" label="id" />
            <el-table-column prop="name" label="账号名" />
            <el-table-column prop="vipType" label="账号类型">
                <template #default="scope">
                    {{ formatVipType(scope.row.vipType) }}
                </template>
            </el-table-column>
            <el-table-column prop="state" label="状态" />
            <el-table-column label="操作">
                <template #default="scope">
                    <el-button link type="primary" size="small"
                        @click="handleDelete(scope.row, scope.$index)">删除</el-button>
                    <el-button link type="primary" size="small" @click="editButtonHandle(scope.row,scope.$index)">修改</el-button>
                </template>
            </el-table-column>
        </el-table>

        </el-card>

        
        <el-dialog v-model="dialogFormVisible" title="添加">
            <p>添加提升bduss和stoken只需要后面的值即可,如(BDUSS=XXX;STOKEN=XXX;)只需获取XXX</p>
            <el-form :model="form">
                <el-form-item label="bduss" :label-width="'60px'">
                    <el-input v-model="form.bduss" autocomplete="off" />
                </el-form-item>
                <el-form-item label="stoken" :label-width="'60px'">
                    <el-input v-model="form.stoken" autocomplete="off" />
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">撤销</el-button>
                    <el-button type="primary" @click="addSvipAccountHandle">添加</el-button>
                </span>
            </template>
        </el-dialog>

        <el-dialog v-model="dialogEditFormVisible" title="修改">
            <el-form :model="editForm">
                <el-form-item label="bduss" :label-width="'60px'">
                    <el-input v-model="editForm.bduss" autocomplete="off" />
                </el-form-item>
                <el-form-item label="stoken" :label-width="'60px'">
                    <el-input v-model="editForm.stoken" autocomplete="off" />
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogEditFormVisible = false">撤销</el-button>
                    <el-button type="primary" @click="handleEdit">修改</el-button>
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