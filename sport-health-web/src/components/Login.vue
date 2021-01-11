<template>
  <div class="login_container">
    <div class="login_box">
        <!-- 头像 -->
      <div class="avatar_box">
         <img src="../assets/logo.png" alt="" />
      </div>
        <!-- 表单区域 -->
      <el-form ref="loginFormRef" :rules="loginFormRules" :model="loginForm" class="login_form" label-width="0">
          <!-- 用户名 -->
        <el-form-item prop="username">
            <el-input v-model="loginForm.username" prefix-icon="iconfont icon-zhanghao" placeholder="请输入账号"></el-input>
        </el-form-item>
        <!-- 账号 -->
        <el-form-item prop="password">
            <el-input v-model="loginForm.password" prefix-icon="iconfont icon-mima" placeholder="请输入密码" type="password"></el-input>
        </el-form-item>
        <!-- 登录按钮 -->
        <el-form-item class="btns">
            <el-button type="primary" @click="login" :loading="userLogin">登录</el-button>
            <el-button type="info" @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script >
import cookie from '../util/cookie-util'

export default {
    data() {
        return {
            // 表单数据
            loginForm: {
                username: 'admin',
                password: '12345678'
            },
            // 表单数据校验
            loginFormRules: {
                // 校验用户名
                username: [
                    { required: true, message: '请输入账号', trigger: 'blur' },
                    { min: 5, max: 12, message: '长度在 5 到 12 个字符', trigger: 'blur' }
                ],
                // 校验密码
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 6, max: 10, message: '长度在 6 ~ 10 个字符', trigger: 'blur' }
                ]
            },
            userLogin: false
        }
    },
    methods: {
        resetForm() {
            // this.$refs.username.resetFields();
            // console.log(this.$refs.loginFormRef);
            // this.$refs.loginFormRef.resetFields();
            // this.$refs.loginFormRef.resetFields();
            this.loginForm.username = '';
            this.loginForm.password = '';
            // console.log(this.$refs.loginFormRef.resetFields());
            // this.$refs.loginFormRef.resetFields();
            // console.log(this.$refs);
        },
        login() {
            this.$refs.loginFormRef.validate(valid => {
                if (!valid) return;

                // const loading = this.$loading({
                //     lock: true,
                //     text: '正在登录...',
                //     spinner: 'el-icon-loading',
                //     background: 'rgba(0, 0, 0, 0.7)'
                // });
                this.userLogin = true;
                $http.post("/authorize/login", this.loginForm).then(response => {
                    // loading.close();
                    this.userLogin = false;
                    const res = response.data;
                    // 本地存储用户 session
                    // window.sessionStorage.setItem("user", res.data);

                    // 用户状态在120分钟后过期
                    // window.storage.set("user", res.data, 120 * 60 * 1000);

                    // 存储用户 token，7天之后过期
                    cookie.setCookie('Authorization', res.data.token, 7 * 24 * 60 * 60 * 1000);
                    
                    // 登录成功重定向到主页
                    this.$router.push({path: '/home'});
                    this.$notify({
                        title: '欢迎回来',
                        type: 'success'
                    });
                })
            })
        }
    }
};
</script>

<style lang="less" scoped>

.login_container {
    background-color: #2b4b6b;
    height: 100%;
}

.login_box {
    width: 450px;
    height: 300px;
    background-color: #ffffff;
    border-radius: 5px;  // 圆角
    position: absolute;  // 绝对定位
    left: 50%;
    top: 50%;
    transform: translate(-50%,-50%);
    .avatar_box {
        width: 130px;
        height: 130px;
        border: 1px solid #eeeeee;
        border-radius: 50%;
        padding: 5px;
        box-shadow: 0 0 10px #dddddd;
        position: absolute;  // 绝对定位
        left: 50%;
        transform: translate(-50%,-50%);
        background-color: #eeeeee;
        img {
            width: 100%;
            height: 100%;
            border-radius: 50%;
            background-color: #eeeeee;
        }
    }
}

.btns {
    display: flex;
    justify-content: flex-end;
}

.login_form {
    position: absolute;
    bottom: 0%;
    width: 100%;
    padding: 0 10px;
    box-sizing: border-box;
}
</style>