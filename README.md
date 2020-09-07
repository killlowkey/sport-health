# 运动健康平台

## 1. 项目技术栈

* 前端：vue + element ui
* 后端：Java8 + Maven + SpringBoot + MyBatis-Plus + Redis 

## 2. 主要特征

* 接口动态限流
* 详细日志记录
* 接口权限管理

## 3. 效果展示

* 系统日志

  ![](/doc/images/Snipaste_2020-09-07_10-06-44.png)

* 异常日志

  ![](/doc/images/Snipaste_2020-09-07_10-07-06.png)

* 接口限流

  ![](/doc/images/Snipaste_2020-09-07_10-08-42.png)

  ![](/doc/images/Snipaste_2020-09-07_10-08-55.png)

## 4. 运行项目

### 4.1 后端运行

* 创建 `sport_health` 数据库，将 `doc/sport_health.sql` 导入到数据库中
* 根据自己的需求修改 `application.yml` 配置（如修改 `mysql` 账号密码或  `redis` 账号密码）
* 使用 ide 打开 `sport_health` 项目，加载依赖之后，启动 `SportHealthApplication` 

## 4.2 前端运行

* 在控制台打开 `sport-health-web`文件，输入 `npm install` 命令安装依赖
* 安装依赖后输入  `npm run serve` 运行项目