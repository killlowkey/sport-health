<template>
  <div>
      <!-- 面包屑导航 -->
      <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>权限管理</el-breadcrumb-item>
          <el-breadcrumb-item>用户列表</el-breadcrumb-item>
      </el-breadcrumb>
      <!-- 用户列表区域 -->
      <el-card>
          <el-row :gutter="25">
              <!-- 搜索 -->
              <el-col :span="8">
                  <!-- @input 监听输入框的输入事件 -->
                  <!-- @keyup.enter 监听输入框回车事件 -->
                  <el-input type="text" clearable="true" placeholder="请输入搜索内容" v-model="queryInfo.queryName" @input="change($event)">
                      <el-button slot="append" type="primary" icon="el-icon-search" @click="searchUser" :loading="enable">搜索</el-button>
                  </el-input>
              </el-col>


              <!-- 搜索按钮 -->
              <el-col :span="4">
                  <el-button type="primary" @click="addUser">添加用户</el-button>
              </el-col>
          </el-row>

          <!-- 用户列表 -->
          <el-table
              :data="userList"
              border stripe=""
              style="width: 100%">
              <el-table-column type="index"></el-table-column>
              <el-table-column label="用户名" prop="username"></el-table-column>
              <el-table-column label="邮箱" prop="email"></el-table-column>
              <!-- <el-table-column label="密码" prop="password"></el-table-column> -->
              <!-- <el-table-column label="角色" prop="role"> -->
                  <!-- 添加过滤器 -->
                  <!-- <template slot-scope="scope">
                      {{scope.row.role | convert}}
                  </template> -->
              <!-- </el-table-column> -->
              <el-table-column label="状态" prop="enable">
                  <!-- 作用域插槽 -->
                <template slot-scope="scope">
                    <el-switch v-model="scope.row.enable" @change="userStateChanged(scope.row)"></el-switch>
                </template>
              </el-table-column>
            <el-table-column label="操作">
            <template slot-scope="scope">
                <!-- 修改 -->
                <el-button type="primary" icon="el-icon-edit" size="mini" @click="editUser(scope.row)"></el-button>
                <!-- 删除 -->
                <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteUser(scope.row.id)"></el-button>
                <!-- 权限 -->
                <el-tooltip effect="dark" content="分配权限" placement="top-start" :enterable="false">
                    <el-button type="warning" icon="el-icon-setting" size="mini"></el-button>
                </el-tooltip>
                <!-- 表单编辑 -->
                <!-- :before-close 关闭dialog之前进行操作 -->
                <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" :before-close="displayEdit">
                    <el-form :model="userForm" :rules="userFormRules" ref="userFormRef">
                        <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
                            <el-input v-model="userForm.username" auto-complete="off" :disabled="edit"></el-input>
                        </el-form-item>
                         <el-form-item label="密码" :label-width="formLabelWidth" prop="password">
                            <el-input v-model="userForm.password" auto-complete="off"></el-input>
                        </el-form-item>
                         <el-form-item label="邮箱" :label-width="formLabelWidth" prop="email">
                            <el-input v-model="userForm.email"></el-input>
                        </el-form-item>
                        <!-- <el-form-item label="角色" :label-width="formLabelWidth">
                            <el-select v-model="userForm.role" placeholder="请选择用户权限">
                                <el-option label="超级管理员" value="ADMIN"></el-option>
                                <el-option label="普通用户" value="USER"></el-option>
                            </el-select>
                        </el-form-item> -->
                        <el-form-item label="用户状态" :label-width="formLabelWidth">
                            <el-switch v-model="userForm.enable"></el-switch>
                        </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="displayEdit">取 消</el-button>
                        <el-button type="primary" @click="optionUser">确 定</el-button>
                    </div>
                </el-dialog>
            </template>
            </el-table-column>
          </el-table>

          <!-- 分页组件 -->
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="queryInfo.pageNum"
            :page-sizes="[5, 10, 20, 50]"
            :page-size="queryInfo.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
          </el-pagination>
      </el-card>
  </div>
</template>

<script>
export default {
    created() {
        this.getUserList();
    },
    data() {
        return {
            queryInfo: {
                queryName: '',  // 查询信息
                pageNum: 1,
                pageSize: 10
            },
            userForm: {
                id: 0,
                username: '',
                email: '',
                password: '',
                role: 'USER',
                enable: false
            },
            userList: [], // 用户列表
            total: 0, // 总记录数
            enable: false,
            dialogTableVisible: false,
            dialogFormVisible: false,
            formLabelWidth: '120px',
            edit: false,
            dialogTitle: '',
             // 表单数据校验
            userFormRules: {
                // 校验用户名
                username: [
                    { required: true, message: '请输入账号', trigger: 'blur' },
                    { min: 5, max: 12, message: '长度在 5 到 12 个字符', trigger: 'blur' }
                ],
                // 校验密码
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 6, max: 10, message: '长度在 6 ~ 10 个字符', trigger: 'blur' }
                ],
                // 校验邮箱
                email: [
                    { required: true, message: '请输入邮箱', trigger: 'blur' },
                ]
            }
        }
    },
    methods: {
        // 获取所有用户
        getUserList() {
            $http.get("/users", {params: this.queryInfo}).then(response => {
                const res = response.data;
                this.total = res.data.total;
                this.userList = res.data.entity;
            })
        },
        // 触发每页的大小
        handleSizeChange(newSize) {
            this.queryInfo.pageSize = newSize;
            this.getUserList();
        },
        // 触发点击页面操作
        handleCurrentChange(newPage) {
            this.queryInfo.pageNum = newPage;
            this.getUserList();
        },
        // 用户状态的改变
        userStateChanged(row) {
            // const path = '/user/' + row.id;
            // console.log(path);
            $http.put("/user", row).then(response => {
                const res = response.data;
                this.$notify({
                    title: res.message,
                    type: 'success'
                });
            })
        },
        // 用户搜索
        searchUser() {
            this.enable = true;
            this.getUserList();
            this.enable = false;
        },
        // 删除用户
        deleteUser(id) {
            this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                const path = "/user/" + id;
                $http.delete(path).then(response => {
                    const res = response.data;
                    this.$notify({
                        title: res.message,
                        type: 'success'
                    });
                    this.getUserList();
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });          
            });
        },
        change(e) {
            // console.log(e);
            // this.$forceUpdate();
            if (e === '') {
                this.getUserList();
            }
        },
        // 显示修改用户编辑框
        editUser(row) {
            this.dialogFormVisible = true;
            this.userForm = row;
            // 禁用用户输入框
            this.edit = true;
            this.dialogTitle = '用户修改';
        },
        // 显示添加用户的编辑框
        addUser() {
            this.dialogFormVisible = true;
            this.dialogTitle = '用户添加';
        },
        // 关闭用户编辑框
        displayEdit() {
            // 释放 input 输入框
            this.edit = false;
            // 关闭 dialog
            this.dialogFormVisible = false;
            // 清除表单
            this.restUserForm();
            // this.$refs.userForm.resetFields();
            // this.$refs[userForm].resetFields();
            this.$refs.userFormRef.clearValidate();
        },
        restUserForm() {
            this.userForm = {
                id: 0,
                username: '',
                email: '',
                password: '',
                role: 'USER',
                state: false
            }
        },
        optionUser() {
            // console.log();
            this.$refs.userFormRef.validate(valid => {
                
                // 校验未通过则不进行请求
                if (!valid) {
                    return;
                };

                if (this.edit) {
                    // 修改用户
                    $http.put('/user', this.userForm).then(response => {
                        const res = response.data;
                        this.$notify({
                            title: res.message,
                            type: 'success'
                        });
                        // 释放 input 输入框
                        this.edit = false;
                        // 关闭 dialog
                        this.dialogFormVisible = false;
                        // 清除表单
                        this.restUserForm();
                    })
                } else {
                    // 添加用户
                    $http.post('/user', this.userForm).then(response => {
                        const res = response.data;
                        this.$notify({
                            title: res.message,
                            type: 'success'
                        });
                        // 关闭 dialog
                        this.dialogFormVisible = false;
                        // 清除表单
                        this.restUserForm();
                    })
                }
            })
       },
    },
    filters: {
        convert: function (value) {
            if (!value) return ''
            if (value === "ADMIN") return '超级管理员';
            if (value === "USER") return '普通用户';
            return value;
        }
    }
};
</script>

<style lang="less" scoped>
.el-breadcrumb {
    margin-bottom: 15px;
    font-size: 15px
}
</style>