import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../components/Login.vue'
import Home from '../components/Home.vue'
import Weclome from '../components/Weclome.vue'
import User from '../components/admin/User.vue' 
import Limit from '../components/admin/Limit.vue' 
import Druid from '../components/admin/Druid.vue' 
import Log from '../components/admin/Log.vue'
import ErrorLog from '../components/admin/ErrorLog'

Vue.use(VueRouter)

  const routes = [
    {
      path: "/",
      redirect: "/login" // 访问主页的时候重定向到login页面
    },
    {
      path: "/login",
      component: Login
    },
    {
      path: "/home",
      component: Home,
      redirect: "/welcome",
      children: [
        {path: "/welcome", component: Weclome},
        {path: "/admin/user", component: User},
        {path: "/admin/limit", component: Limit},
        {path: "/admin/druid", component: Druid},
        {path: "/admin/log", component: Log},
        {path: "/admin/errLog", component: ErrorLog}
      ]
    }
]

const router = new VueRouter({
  routes
})

// 挂载路由导航守卫
router.beforeEach((to, from, next) => {
  // to：要去哪里
  // from: 从哪来
  // next：接着干什么 next(url),重定向url上

  const user = window.storage.get("user");

  // 访问首页就放行
  if (to.path == '/login') {
    if (user) {
      return next(window.storage.get("activePath"));
    } else {
      return next();
    }
  };

  // 从本地session中获取 session
  // const user = window.sessionStorage.getItem("user");
  if (!user) {
    // 说明用户未登陆，直接跳转到登录页面
    return next("/login");
  } 

  // 符合要求
  next();

})

export default router
