<template>
    <div>
      <!-- 面包屑导航 -->
      <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>系统工具</el-breadcrumb-item>
          <el-breadcrumb-item>接口限流</el-breadcrumb-item>
      </el-breadcrumb>
      
      <el-card>
        
        <el-row :gutter="25">
            <!-- 搜索 -->
            <el-col :span="8">
                <!-- @input 监听输入框的输入事件 -->
                <!-- @keyup.enter 监听输入框回车事件 -->
                <el-input type="text" clearable="true" placeholder="请输入搜索内容">
                <el-button slot="append" type="primary" icon="el-icon-search" style="margin-left:-30px" @click="searchLimit" :loading="searchLoading">搜索</el-button>
                </el-input>
            </el-col>

            <!-- 添加限流接口 -->
            <el-col :span="4">
                <el-button type="primary" @click="openLimitEdit">添加限流接口</el-button>
            </el-col>
        </el-row>


        <el-table :data="limitList" border stripe="" style="width: 100%">
              <el-table-column label="http路径" prop="path"></el-table-column>
              <el-table-column label="http方法" prop="method"></el-table-column>
              <el-table-column label="限流类型" prop="limitType">
                  <!-- 添加过滤器 -->
                  <template slot-scope="scope">
                      {{scope.row.limitType | convertType}}
                  </template>
              </el-table-column>
              <el-table-column label="时间(秒)" prop="period"></el-table-column>              
              <el-table-column label="限制次数" prop="count"></el-table-column>
              <el-table-column label="限制状态" prop="state">
                  <!-- 作用域插槽 -->
                <template slot-scope="scope">
                    <el-switch v-model="scope.row.state" @change="userStateChanged(scope.row)"></el-switch>
                </template>
              </el-table-column>
              <el-table-column label="操作">
                <template slot-scope="scope">
                    <!-- 修改 -->
                    <el-button type="primary" icon="el-icon-edit" size="mini" @click="updateLimit(scope.row)" ></el-button>
                    <!-- 删除 -->
                    <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteLimit(scope.row.id)"></el-button>

                    <!-- 更新Limit dialog区域 -->
                    <el-dialog :title="updateLimitState ? '更新限流接口' : '添加限流接口'" :visible.sync="dialogFormVisible" :before-close="displayDislog">
                        <el-form :model="limitForm"  ref="limitFormRef">
                            <el-form-item label="请求路径" :label-width="formLabelWidth" prop="path">
                                <el-select v-model="limitForm.path" placeholder="请选择请求路径" @change="pathChange($event)" :disabled="edit">
                                    <el-option v-for="item in pathOption"       
                                        :key="item"
                                        :label="item.label"
                                        :value="item">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="请求方法" :label-width="formLabelWidth" prop="method">
                                <el-col :span="9">
                                    <el-input v-model="limitForm.method" placeholder="请求方法" :disabled="edit"></el-input>
                                </el-col>
                            </el-form-item>
                             <el-form-item label="限流类型" :label-width="formLabelWidth" prop="limitType">
                                <el-select v-model="limitForm.limitType" placeholder="请选择限制类型">
                                    <el-option label="PATH" value="PATH"></el-option>
                                    <el-option label="IP" value="IP"></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="时间(秒)" :label-width="formLabelWidth" prop="period">
                                <el-col :span="9">
                                    <el-input v-model="limitForm.period" placeholder="默认为1分钟"></el-input>
                                </el-col>
                            </el-form-item>
                            <el-form-item label="限制次数" :label-width="formLabelWidth" prop="count">
                                <el-col :span="9">
                                    <el-input v-model="limitForm.count" placeholder="指定时间接口最大的访问次数"></el-input>
                                </el-col>
                            </el-form-item>
                        </el-form>
                        <div slot="footer" class="dialog-footer">
                            <el-button @click="displayDislog">取 消</el-button>
                            <el-button type="primary" @click="optionLimit">确 定</el-button>
                        </div>
                    </el-dialog>

                </template>
            </el-table-column>

        </el-table>
      </el-card>
    </div>

</template>

<script>
export default {
    created() {
        this.getAllLimit();
    },
    data() {
        return {
            limitList: [],
            limitForm: {
                "id": 0,
                "path": '',
                "method": '',
                "limitType": '',
                "state": true,
                "period": 60,
                "count": 60
            },
            searchLoading: false, // 搜索加载
            limitConfig: {},
            pathOption: [],
            dialogTableVisible: false,
            dialogFormVisible: false,
            formLabelWidth: '120px',
            updateLimitState: false,
            edit: false
            // ,dialogTitle: updateLimitState ? '添加限流接口' : '更新限流接口'
        }
    },
    methods: {
        getAllLimit() {
            $http.get('/limits').then(res => {
                this.limitList = res.data.data;
            })
        },
        // 修改限制状态
        userStateChanged(row) {
            $http.put('/limit', row).then(response => {
                const res = response.data;
                this.$notify({
                    title: res.message,
                    type: 'success'
                });
            })
        },
        // 删除限流接口
        deleteLimit(id) {
            this.$confirm('此操作将永久删除该限流接口, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
            const path = '/limit/' + id;
                $http.delete(path).then(response => {
                    const res = response.data;
                    this.getAllLimit();
                    this.$notify({
                        title: res.message,
                        type: 'success'
                    });
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });          
            });
        },
        openLimitEdit() {
            this.dialogFormVisible = true;
            console.log(this.dialogFormVisible)
            $http.get('/limit/pathInfo').then(response => {
                this.pathOption = response.data.data;
            });
        },
        optionLimit() {
            // 升级限流接口
            if(this.updateLimitState) {
                $http.put('/limit', this.limitForm).then(response => {
                    this.dialogFormVisible = false;
                    this.getAllLimit();
                    this.$notify({
                        title: response.data.message,
                        type: 'success'
                    });
                    this.resetLimitForm();
                    this.updateLimitState = false;
                    this.display = false;
                })
            } else {
                // 添加限流接口
                $http.post('/limit', this.limitForm).then(response => {
                    console.log(response);
                    this.dialogFormVisible = false;
                    this.getAllLimit();
                    this.$notify({
                        title: response.data.message,
                        type: 'success'
                    });
                    this.resetLimitForm();
                })
            }
        },
        selectControllerName() {
            // console.log('controllerName：' + this.limitForm.controllerName);
        },
        // 选中了 path
        pathChange(path) {
            this.limitForm.method = '';
            $http.get('/limit/methodInfo', {
                params: {
                    "path": this.limitForm.path
                }
            }).then(response => {
                this.limitForm.method = response.data.data;
            })
        },
        // 清空limitForm表单
        resetLimitForm() {
            this.limitForm = {
                "id": 0,
                "controllerName": '',
                "path": '',
                "methodType": '',
                "limitType": '',
                "methodSignature": '',
                "state": true,
                "period": 60,
                "count": 60
            }
        },
        // 关闭 dialog
        displayDislog() {
            this.edit = false;
            this.dialogFormVisible = false
            this.resetLimitForm();
        },
        // 更新updateLimit
        updateLimit(row) {
            this.edit = true;
            this.limitForm = row;
            this.dialogFormVisible = true;
            this.updateLimitState = true;
        }
    },
    filters: {
        convertType: function (type) {
            if (!type) return ''
            if (type === "IP") return 'IP地址';
            if (type === "PATH") return 'path路径';
            return type;
        }
    }
}
</script>

<style>

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