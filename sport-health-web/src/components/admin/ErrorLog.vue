<template>
    <div>
     
     <!-- 面包屑导航 -->
      <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>系统工具</el-breadcrumb-item>
          <el-breadcrumb-item>系统日志</el-breadcrumb-item>
      </el-breadcrumb>

      <el-card>
            <el-row :gutter="25">
              <el-col :span="4">
                  <el-input v-model="logQueryCondition.descName" placeholder="接口描述" :clearable="true" @input="change($event)"></el-input>
              </el-col>

              <el-col :span="8">
                    <el-date-picker
                        v-model="logQueryCondition.times"
                        style="margin-left:-5px"
                        type="daterange"
                        align="right"
                        unlink-panels
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        :default-time="['00:00:00','23:59:59']"
                        :picker-options="pickerOptions2" 
                        @change="change($event)">
                    </el-date-picker>
              </el-col>

              <!-- <el-col :span="3">
                    <el-select v-model="logQueryCondition.logType" placeholder="日志等级">
                        <el-option label="所有" value="ALL"></el-option>
                        <el-option label="正常" value="INFO"></el-option>
                        <el-option label="错误" value="ERROR"></el-option>
                    </el-select>
              </el-col> -->

              <el-col :span="3">
                  <el-button type="primary" icon="el-icon-search" style="margin-left:-30px" @click="getLogList">搜索</el-button>
              </el-col>
              <el-col :span="3">
                  <el-button type="danger" icon="el-icon-delete" style="margin-left:-70px" @click="deleteLog">清空</el-button>
              </el-col>
          </el-row>

           <el-table :data="logList" border stripe="" style="width: 100%">
              <el-table-column type="expand">
                <template slot-scope="props">
                    <el-form label-position="left" class="demo-table-expand">
                        <el-form-item label="请求方法">
                            <span>{{ props.row.method }}</span>
                        </el-form-item>
                        <el-form-item label="请求参数">
                            <span>{{ props.row.params }}</span>
                        </el-form-item>
                    </el-form>
                </template>
              </el-table-column>
              <el-table-column label="用户名" prop="username"></el-table-column>
              <el-table-column label="IP" prop="requestIp">
                  <!-- 添加过滤器 -->
                  <!-- <template slot-scope="scope">
                      {{scope.row.requestIp | convertIp}}
                  </template> -->
              </el-table-column>
              <el-table-column label="IP来源" prop="address"></el-table-column>
              <el-table-column label="描述" prop="description"></el-table-column>              
              <el-table-column label="浏览器" prop="browser"></el-table-column>
              <el-table-column prop="time" label="请求耗时" align="center">
                    <template slot-scope="scope">
                    <el-tag v-if="scope.row.time <= 300">{{ scope.row.time }}ms</el-tag>
                    <el-tag v-else-if="scope.row.time <= 1000" type="warning">{{ scope.row.time }}ms</el-tag>
                    <el-tag v-else type="danger">{{ scope.row.time }}ms</el-tag>
                    </template>
               </el-table-column>
              <el-table-column label="创建日期" prop="createTime">
                <template slot-scope="scope">
                    <span>{{ parseTime(scope.row.createTime) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="异常详情" width="100px">
                <template slot-scope="scope">
                <el-button size="mini" type="text" @click="info(scope.row.id)">查看详情</el-button>
                </template>
             </el-table-column>

           </el-table>

            <el-dialog :visible.sync="dialog" title="异常详情" append-to-body top="30px" width="85%">
                <!-- 需要使用 v-html 进行渲染不然代码不会出现 -->
                <pre v-highlight>{{ errorInfo }}</pre><code></code>
            </el-dialog>
          <!-- 分页组件 -->
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="logQueryCondition.pageNum"
            :page-sizes="[5, 10, 20, 50]"
            :page-size="logQueryCondition.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
          </el-pagination>
      </el-card>
    </div>
</template>

<script>
// 处理请求参数
import QS from 'qs';

export default {
    created() {
        this.getLogList();
    },
    data() {
        return {
            logQueryCondition: {
                idSort: 'desc',
                pageNum: 1,
                pageSize: 10,
                descName: '',
                times: '',
                logType: 'ERROR'
            },
            pickerOptions2: {
                shortcuts: [{
                    text: '最近一周',
                    onClick(picker) {
                    const end = new Date();
                    const start = new Date();
                    start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                    picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近一个月',
                    onClick(picker) {
                    const end = new Date();
                    const start = new Date();
                    start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                    picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近三个月',
                    onClick(picker) {
                    const end = new Date();
                    const start = new Date();
                    start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                    picker.$emit('pick', [start, end]);
                    }
                }]
            },
            logList: [], // 日志列表
            total: 0, // 总记录数
            dialog: false,
            errorInfo: 'hello world',
        }
    },

    methods: {
        getLogList() {
            $http.get('/logs', {
                params: this.logQueryCondition,
                paramsSerializer: function(params) {
                    return QS.stringify(params, {arrayFormat: 'repeat'})
                }}).then(response => {
                const res = response.data;
                this.total = res.data.total;
                this.logList = res.data.entity;
            })
        },
        // 触发每页的大小
        handleSizeChange(newSize) {
            this.logQueryCondition.pageSize = newSize;
            this.getLogList();
        },
        // 触发点击页面操作
        handleCurrentChange(newPage) {
            this.logQueryCondition.pageNum = newPage;
            this.getLogList();
        },
        // 获取异常详情
        info(id) {
            $http.get('/log/error/' + id).then(response => {
                this.errorInfo = response.data.data;
                console.log(this.errorInfo);
            })
            this.dialog = true
        },
        // 解析时间
        parseTime(createTime) {
            return createTime.replace('T', ' ');
        },
        change(e) {
            // console.log(e);
            // this.$forceUpdate();
            if (e === null || e === '') {
                this.getLogList();
            }
        },
        deleteLog() {
            this.$confirm('此操作将永久删除所有的异常日志, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                $http.delete('/logs', {
                    params: {
                        "logType": this.logQueryCondition.logType
                    }
                }).then(response => {
                    this.getLogList();
                    this.$notify({
                        title: response.data.message,
                        type: 'success'
                    });
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });          
            });
        }
    },
    filters: {
        convertIp: function (ip) {
            if (!ip) return ''
            if (ip === "0:0:0:0:0:0:0:1") return '本地局域网';
            return ip;
        }
    }
}
</script>

<style scoped>
.el-breadcrumb {
    margin-bottom: 15px;
    font-size: 15px
}

.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 70px;
  color: #99a9bf;
}

.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 100%;
}
.demo-table-expand .el-form-item__content {
  font-size: 12px;
}
</style>