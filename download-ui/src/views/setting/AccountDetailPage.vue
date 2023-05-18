<script setup lang='ts'>
import { ref, onMounted, reactive } from 'vue'
import { useStore } from 'vuex'
const props = defineProps<{}>()
const store = useStore()

const svipAccountDetail = reactive({
    accountNum: 0,
    availableAccountNum: 0,
    tableData: []
})

const svipAccountDetailDialog = ref(false)


onMounted(() => {
    store.dispatch('system/getSvipAccountNum').then(data => {
        const { accountNum, availableAccountNum } = data
        svipAccountDetail.accountNum = accountNum
        svipAccountDetail.availableAccountNum = availableAccountNum
    })

    store.dispatch('system/getSvipAccountDetail').then(data => {
        svipAccountDetail.tableData = data
    })
})

</script>

<template>
    <div class="setting-page">
        <div class="box-card">
            <el-row>
                <el-col :offset="2" :span="9">
                    <el-card>
                        <template #header>
                            <div class="simple-header">
                                <span>svip账号状态</span>
                            </div>
                        </template>
                        <div class="simple-state">
                            <el-row class="mb-4"><span class="des-item">svip账号数量:</span>{{ svipAccountDetail.accountNum }}</el-row>
                            <el-row class="mb-4"><span class="des-item">可用svip账号数量:</span>{{ svipAccountDetail.availableAccountNum
                            }}</el-row>
                            <el-button type="primary" @click="svipAccountDetailDialog = true">详情</el-button>
                        </div>
                    </el-card>
                </el-col>
            </el-row>
        </div>

        <el-dialog v-model="svipAccountDetailDialog" title="svip详情" :width="'65%'">
            <el-table :data="svipAccountDetail.tableData" style="width: 100%">
                <el-table-column type="expand">
                    <template #default="props">
                        <div class="m-4">
                            <el-row class="mb-4 des-row"><span class="des-item">svipBduss: </span>{{ props.row.svipBduss }}</el-row>
                            <el-row class="mb-4 des-row"><span class="des-item">svipStoken: </span>{{ props.row.svipStoken }}</el-row>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="id" label="id" />
                <el-table-column prop="name" label="名字"  />
                <el-table-column prop="state" label="状态" />
                <el-table-column prop="createTime" label="添加时间" />
            </el-table>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="svipAccountDetailDialog = false" type="primary">确定</el-button>
                </span>
            </template>
        </el-dialog>

    </div>
</template>

<style scoped>
.box-card {
    margin-top: 20px;
    margin-left: 30px;
    margin-right: 30px;
}

.mb-4 {
    margin-bottom: 10px;
}
.m-4{
    margin: 4px;
}

.des-item {
  font-weight: bold;
  color: #646569;
}

.des-row{
    word-break: break-all;
}
</style>
