<template>
  <div class="form-container">
    <el-form v-if="showLogin" ref="loginFormRef" :model="loginForm" status-icon :hide-required-asterisk="true"
      :rules="rules" label-width="100px" class="login-form">
      <el-form-item label="账号" prop="email">
        <el-input v-model="loginForm.email" autocomplete="off" placeholder="请输入邮箱"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="loginForm.password" type="password" autocomplete="off" placeholder="请输入密码"
          show-password></el-input>
      </el-form-item>

      <el-form-item>
        <div class="btn-container">
          <el-button type="primary" style="width: 100%" @click="submitForm()">登录</el-button>
        </div>
        <div class="operation">
          <span class="free-register" @click="showLogin = !showLogin">注册</span>
          <span class="forget-password" @click="handleForget">忘记密码</span>
        </div>
      </el-form-item>
    </el-form>
    <el-form v-if="!showLogin" ref="registerRef" :model="registerForm" status-icon :hide-required-asterisk="true"
      :rules="rules" label-width="100px" class="login-form">
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="registerForm.email" autocomplete="off" placeholder="请输入注册邮箱">
          <template #append>
            <el-button :disabled="sendingCode" @click="handleGetCaptcha">{{ codeText }}</el-button>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item label="验证码" prop="captcha">
        <el-input v-model.number="registerForm.captcha" maxlength="10" autocomplete="off" placeholder="请输入验证码"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="registerForm.password" type="password" autocomplete="off" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="checkPass">
        <el-input v-model="registerForm.checkPass" type="password" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item>
        <div class="btn-container">
          <el-button type="primary" style="width: 100%" @click="handleRegister()">完成注册</el-button>
        </div>
        <div class="go-login">
          <span class="to-login" @click="showLogin = !showLogin">已有账号<span>去登陆</span></span>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>
<script lang="ts">
import { defineComponent, ref, toRefs, reactive } from 'vue'
import { useRouter, useRoute, onBeforeRouteUpdate } from 'vue-router'
import { ElMessage } from 'element-plus'
import { encrypt } from '@/utils/aes' // aes 密码加密
import { useStore } from 'vuex'

interface stateType {
  loginForm: {
    email: string
    password: string
  }
  registerForm: {
    email: string
    captcha: number | null
    password: string
    checkPass: string
  }
  showLogin: boolean
}

// eslint-disable-next-line no-unused-vars
type VoidNoop = (arg0?: Error) => void
export default defineComponent({
  emits: ['toResetPwd'],
  setup(_props, { emit }) {
    const router = useRouter()
    const route = useRoute()
    const loginFormRef = ref()
    const registerRef = ref()
    const store = useStore()
    const sendingCode = ref(false)
    const codeText = ref('获取验证码')
    const redirect = ref<any>(undefined)
    const state = reactive<stateType>({
      loginForm: {
        email: '',
        password: ''
      },
      registerForm: {
        email: '',
        captcha: null,
        password: '',
        checkPass: ''
      },
      showLogin: true
    })

    // eslint-disable-next-line no-unused-vars
    const validatePass2 = (rule: any, value: string, callback: VoidNoop) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== state.registerForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    const validatePass = (rule: any, value: string, callback: VoidNoop) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (state.registerForm.checkPass !== '') {
          registerRef.value.validateField('checkPass')
        }
        callback()
      }
    }
    const rules = {
      password: [
        { validator: validatePass, trigger: 'blur' },
        { min: 6, max: 16, message: '长度在 6 到 16 个字符', trigger: 'blur' }
      ],
      checkPass: [{ validator: validatePass2, trigger: 'blur' }],
      email: [
        { required: true, message: '请输入注册邮箱', trigger: ['blur'] },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] },
      ],
      captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
    }

    // methods

    /**
     * @description  用户登录接口
     *
     */
    const submitForm = () => {
      loginFormRef.value.validate(async (valid: any) => {
        if (valid) {
          try {
            const { email, password } = state.loginForm
            const data = {
              email,
              // password
              password: password
            }
            const token = await store.dispatch("user/login", data)
            if (!token) {
              ElMessage({
                type: 'warning',
                message: "账号密码错误"
              })
            } else {
              router.push({ path: redirect.value || '/' })
            }
          } catch (err: any) {
            ElMessage({
              type: 'warning',
              message: err.message
            })
          }
        }
        return false
      })
    }
    /**
     * @description 处理注册接口
     */
    const handleRegister = () => {
      registerRef.value.validate(async (valid: any) => {
        if (valid) {
          try {
            const { email, password, captcha } = state.registerForm
            const data = {
              email,
              captcha,
              // password
              password: password
            }
            store.dispatch('user/register', data)
              .then(() => {
                ElMessage({
                  type: 'success',
                  message: "注册成功"
                })
                state.showLogin = true
              })
          } catch (err: any) {
            ElMessage({
              type: 'error',
              message: err.message
            })
          }
        }
      })
    }
    /**
     * @description 获取验证码状态
     */
    const getCodeSucces = () => {
      let countDown = 60
      sendingCode.value = true
      const interval = setInterval(() => {
        if (countDown > 0) {
          codeText.value = `已发送(${countDown}s)`
          countDown -= 1
        } else {
          clearInterval(interval)
          sendingCode.value = false
          codeText.value = '获取验证码'
        }
      }, 1000)
    }
    /**
     * @description 获取验证码
     */
    const handleGetCaptcha = async (): Promise<boolean> => {
      try {
        const { email } = state.registerForm
        if (!email) {
          ElMessage({
            type: 'warning',
            message: '请输入注册邮箱'
          })
          return false
        }
        const data = {
          email
        }
        store.dispatch('user/postCaptcha', data)
          .then(() => {
            getCodeSucces()
          }).catch(() => {
            ElMessage({
              type: 'warning',
              message: '发送失败'
            })
          })
        return false
      } catch (err) {
        console.error(err)
        return false
      }
    }
    /**
     * @description 忘记密码
     */
    const handleForget = () => {
      emit('toResetPwd')
    }

    onBeforeRouteUpdate((to: any) => {
      redirect.value = router.currentRoute.value.query && router.currentRoute.value.query.redirect
    });
    return {
      ...toRefs(state),
      loginFormRef,
      registerRef,
      sendingCode,
      codeText,
      rules,
      submitForm,
      handleRegister,
      handleGetCaptcha,
      handleForget
    }
  }
})
</script>
<style lang="scss" scoped>
.form-container {
  width: 100%;

  :deep(.el-input-group__append) {
    padding: 0px 7px;
  }

  :deep(.el-input-group__prepend) {
    padding: 0px 7px;
  }

  .login-form {
    width: 100%;
    margin: 0 auto;
  }

  .go-login {
    font-size: 12px;
    cursor: pointer;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;

    .to-login {
      color: #9fa2a8;

      span {
        color: #2878ff;
      }
    }
  }

  .operation {
    font-size: 12px;
    cursor: pointer;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;

    .free-register {
      color: #2878ff;
    }

    .forget-password {
      color: #9fa2a8;
      margin-left: 20px;
    }
  }

  .btn-container {
    width: 100%;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
  }
}
</style>
