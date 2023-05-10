<script setup lang='ts'>
const props = defineProps<{}>()
</script>

<template>
    <div class="setting-page">
        <div class="box-card">
            <el-row>
                <el-col :offset="2" :span="8">
                    <el-card>
                        <template #header>
                            <div class="simple-header">
                                <span>普通账号状态</span>
                            </div>
                        </template>
                        <div class="simple-state">
                            <p><span>普通账号数量</span>:5</p>
                            <p><span>可用普通账号数量</span>:5</p>
                        </div>
                    </el-card>
                </el-col>
            </el-row>

        </div>
    </div>
</template>

<style scoped>
.box-card {
    margin-top: 20px;
    margin-left: 30px;
    margin-right: 30px;
}
</style>
