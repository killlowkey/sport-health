<template>
  <!-- 引入container布局 -->
  <el-container class="home-container">
    <!-- 头部 -->
    <el-header>
      <div>
        <img src="../assets/logo.png" class="logo-img" alt />
        <span>运动健康平台</span>
      </div>
      <!-- <el-button type="info" @click="logout">注销</el-button>-->
      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <!-- 头像 -->
          <img src="../assets/avatar.png" class="user-avatar" />
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <span style="display:block;" @click="logout">
            <el-dropdown-item>退出登录</el-dropdown-item>
          </span>
        </el-dropdown-menu>
      </el-dropdown>
    </el-header>
    <!-- 主体 -->
    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="200px">
        <el-menu background-color="#545c64" text-color="#fff"
          active-text-color="#409eff" 
          :router="true"
          :default-active="activePath">
          <!-- 一级菜单 -->
          <el-submenu :index="item.id" v-for="item in menuList" :key="item.id">
            <template slot="title">
              <!-- <i class="el-icon-location"></i> -->
              <i :class="item.icon" class="icon-padding"></i>
              <span>{{item.title}}</span>
            </template>
            <!-- 二级菜单 -->
            <el-menu-item :index="subItem.subPath" v-for="subItem in item.subMenuList" :key="subItem.id" @click="savePathState(subItem.subPath)">
              <template slot="title">
                <i :class="subItem.subIcon" class="icon-padding"></i>
                <span>{{subItem.subTitle}}</span>
              </template>
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <!-- 主体内容 -->
      <el-main>
        <!-- 路由页面 -->
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import cookieUtil from '@/util/cookie-util'

export default {

  data() {
    return {
      // 菜单列表
      menuList: [

      ],
      activePath: '/user' // 默认路径
    }
  },

  //页面加载时查询菜单
  created() {
    this.getMenuList();
    // 取出缓存中的 activePath
    this.activePath = window.storage.get("activePath");
    // this.$router.push({path: activePath});
  },
  methods: {
    // 用户注销
    logout() {
      $http.post("/authorize/logout").then((response) => {
          const res = response.data;
            // 清除本地存储的用户
          // window.storage.clear();
          cookieUtil.removeCookie('Authorization')

            // 登出成功重定向到登录页
            this.$router.push("/login");
            this.$notify({
              title: res.message,
              type: "success",
            });
        });
    },
    // 获取导航菜单方法
    async getMenuList() {
      $http.get("/menus").then(response => {
        const res = response.data;
        this.menuList = res.data;
      })
    },
    // 保存path状态
    savePathState(activePath) {
      // 将状态进行存储
      window.storage.set("activePath", activePath);
      this.activePath = activePath;
    }
  },
};
</script>


<style lang="less" scoped>
// 布局样式
.home-container {
  height: 100%;
}

// 用户头像样式
.user-avatar {
  cursor: pointer;
  width: 40px;
  height: 40px;
  border-radius: 30px;
}

// 头部样式
.el-header {
  background-color: #373d41;
  display: flex;
  justify-content: space-between; // 左右贴边
  padding-left: 0%; // 距离左边界
  padding-top: 10px;
  color: #ffffff;
  font-size: 20px;
  > div {
    display: flex;
    align-items: center;
    span {
      margin-left: 15px;
    }
  }
}

// 侧边样式
.el-aside {
  background-color: #373d44;
  .el-menu {
    border-right: none;
  }
}

// 主体样式
.el-main {
  background-color: #eaedf1;
}

.logo-img {
  height: 55px;
  width: 55px;
}

.icon-padding {
  padding-right: 10px;
}
</style>