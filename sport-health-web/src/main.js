import Vue from 'vue'
import './plugins/axios'
import App from './App.vue'
import router from './router'
import './plugins/element.js'
// 添加全局样式
import './assets/css/global.css'
// 引入 iconfont
import './assets/font/iconfont.css'
// 导入axios
import axios from 'axios'
// 导入session存储
import storage from './assets/js/storage.js'

// element-ui 通知
import { Notification } from 'element-ui'
// vue 路由
import _router from '@/router/index'

// 代码高亮
import VueHighlightJS from 'highlight.js'
import 'highlight.js/styles/atom-one-dark.css'

// axios.defaults.baseURL = "http://localhost:9000"
// axios.defaults.withCredentials=true; 

const service = axios.create({
  baseURL: "http://localhost:9000/api",
  // 超时时间，时间单位毫秒，默认为2分钟
  timeout: 1200000,
  // 保存服务器的session
  withCredentials: true
})

// 设置全局请求属性
window.$http = service

// 设置请求拦截器
service.interceptors.request.use(
  config => {
    config.headers['Content-Type'] = 'application/json'
    return config
  },
  error => {
    // Do something with request error
    console.log(error) // for debug
    Promise.reject(error)
  }
)

// 设置响应拦截器
service.interceptors.response.use(res => {
  const data = res.data;
  // 用户未登陆
  if (data.code === 100) {
    // console.log('用户未登陆......');
    // 用户未登陆跳转到 /login 页面
    // window.top.location.href='login';
    _router.push({path: '/login'});
    return Notification.error({
      title: data.message
    })
  } else if (data.code === 400) {
    console.log("400错误");
    return Notification.error({
      title: data.message
    })
  }
  return res
}, error => {
    if(error.message === 'Network Error') {
      Notification.error({
        title: '请检查本地的连接'
      })
    } else {
      Notification.error({
        title: error.message
      })
    }
    return Promise.reject(error)
});


Vue.config.productionTip = false

// 改造session设置过期时间
// console.log(storage);
window.storage = storage._local;


new Vue({
  router,
  render: h => h(App)
}).$mount('#app')

Vue.use(VueHighlightJS)
// 代码高亮
Vue.directive('highlight',function (el) {
  let blocks = el.querySelectorAll('pre code');
  console.log(blocks);
  blocks.forEach((block)=>{
    VueHighlightJS.highlightBlock(block)
  })
})
