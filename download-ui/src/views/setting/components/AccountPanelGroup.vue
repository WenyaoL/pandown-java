<template>
  <el-row :gutter="40" class="panel-group">
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-account">
          <svg-icon icon-class="account" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            普通账号数
          </div>
          <span class="card-panel-num">{{ accountNum }}</span>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-account-disable">
          <svg-icon icon-class="account-disable" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            不可用
          </div>
          <span class="card-panel-num">{{ disableAccountNum }}</span>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-user-vip">
          <svg-icon icon-class="user-vip" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            svip
          </div>
          <span class="card-panel-num">{{ svipNum }}</span>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-account-disable">
          <svg-icon icon-class="account-disable" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            不可用vip
          </div>
          <span class="card-panel-num">{{ disableSvipNum }}</span>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import {defineComponent,onMounted} from 'vue'

export default defineComponent({
  props:["accountNum","disableAccountNum","svipNum","disableSvipNum"],
  setup(){

  }
})
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    /*box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);*/
    box-shadow: var(--el-box-shadow);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-account {
        background: #36a3f7;
      }

      .icon-account-disable {
        background: #909399;
      }

      .icon-user-vip {
        background: #f4516c;
      }

    }

    .icon-account {
      color: #36a3f7;
    }

    .icon-account-disable {
      color: #909399;
    }

    .icon-user-vip {
      color: #f4516c;
    }



    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
