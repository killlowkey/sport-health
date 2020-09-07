/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : sport_health

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 07/09/2020 09:50:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for main_menu
-- ----------------------------
DROP TABLE IF EXISTS `main_menu`;
CREATE TABLE `main_menu`  (
  `id` int(50) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 301 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of main_menu
-- ----------------------------
INSERT INTO `main_menu` VALUES (100, '权限管理', 'iconfont icon-quanxian', '/admin', 'ADMIN', '2020-09-03 12:43:53', '2020-09-03 12:43:58');
INSERT INTO `main_menu` VALUES (200, '运动平台', 'iconfont icon-yundong', '/use', 'USER', '2020-09-03 12:43:56', '2020-09-03 12:44:00');
INSERT INTO `main_menu` VALUES (300, '系统工具', 'iconfont icon-gongju', '/system', 'ADMIN', '2020-09-03 12:49:04', '2020-09-03 12:49:06');

-- ----------------------------
-- Table structure for sport_limit
-- ----------------------------
DROP TABLE IF EXISTS `sport_limit`;
CREATE TABLE `sport_limit`  (
  `id` int(22) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `controller_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'bean 名称',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'url 路径',
  `method_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '请求方法',
  `method_signature` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '方法签名',
  `state` int(1) NOT NULL COMMENT '0关闭 1启用',
  `limit_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '限制类型（方法、ip）',
  `count` int(10) NOT NULL COMMENT '在period范围内接口最大的访问次数',
  `period` int(10) NOT NULL COMMENT '时间秒',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sport_limit
-- ----------------------------
INSERT INTO `sport_limit` VALUES (1, 'com.xzc.sport.health.controller.AuthController', '/login', 'POST', 'login(LoginFormDto, HttpSession)', 1, 'METHOD', 60, 60, '2020-09-02 13:00:44', '2020-09-06 20:12:17');
INSERT INTO `sport_limit` VALUES (6, 'com.xzc.sport.health.controller.MenuController', '/menus', 'GET', 'getMenu()', 1, 'METHOD', 10, 60, '2020-09-06 20:00:37', '2020-09-07 09:23:06');
INSERT INTO `sport_limit` VALUES (7, 'com.xzc.sport.health.controller.LimitController', '/limits', 'GET', 'getAllLimit()', 1, 'METHOD', 60, 60, '2020-09-06 20:12:35', '2020-09-06 20:12:35');

-- ----------------------------
-- Table structure for sport_log
-- ----------------------------
DROP TABLE IF EXISTS `sport_log`;
CREATE TABLE `sport_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `log_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `params` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `request_ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `time` bigint(20) NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `browser` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `exception_detail` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `log_create_time_index`(`create_time`) USING BTREE,
  INDEX `inx_log_type`(`log_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 823 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统日志' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sport_log
-- ----------------------------
INSERT INTO `sport_log` VALUES (758, '清空日志', 'INFO', 'com.xzc.sport.health.controller.LogController#deleteAllLog(String)', '{INFO}', '0:0:0:0:0:0:0:1', 267, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:14:06');
INSERT INTO `sport_log` VALUES (759, '获取日志', 'INFO', 'com.xzc.sport.health.controller.LogController#getLogByPage(LogQueryDto)', '{LogQueryDto(pageNum=1, pageSize=10, idSort=desc, descName=, logType=INFO, times=[])}', '0:0:0:0:0:0:0:1', 4, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:14:06');
INSERT INTO `sport_log` VALUES (760, '获取日志', 'INFO', 'com.xzc.sport.health.controller.LogController#getLogByPage(LogQueryDto)', '{LogQueryDto(pageNum=1, pageSize=10, idSort=desc, descName=, logType=ERROR, times=[])}', '0:0:0:0:0:0:0:1', 2, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:14:21');
INSERT INTO `sport_log` VALUES (761, '获取日志', 'INFO', 'com.xzc.sport.health.controller.LogController#getLogByPage(LogQueryDto)', '{LogQueryDto(pageNum=1, pageSize=10, idSort=desc, descName=, logType=INFO, times=[])}', '0:0:0:0:0:0:0:1', 4, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:14:26');
INSERT INTO `sport_log` VALUES (762, '获取限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#getAllLimit()', '{}', '0:0:0:0:0:0:0:1', 3, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:14:34');
INSERT INTO `sport_log` VALUES (763, '获取日志', 'INFO', 'com.xzc.sport.health.controller.LogController#getLogByPage(LogQueryDto)', '{LogQueryDto(pageNum=1, pageSize=10, idSort=desc, descName=, logType=INFO, times=[])}', '0:0:0:0:0:0:0:1', 4, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:14:45');
INSERT INTO `sport_log` VALUES (764, '获取限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#getAllLimit()', '{}', '0:0:0:0:0:0:0:1', 3, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:14:47');
INSERT INTO `sport_log` VALUES (765, '删除限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#deleteLimit(long)', '{8}', '0:0:0:0:0:0:0:1', 82, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:14:49');
INSERT INTO `sport_log` VALUES (766, '获取限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#getAllLimit()', '{}', '0:0:0:0:0:0:0:1', 2, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:14:49');
INSERT INTO `sport_log` VALUES (767, '获取限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#getAllLimit()', '{}', '0:0:0:0:0:0:0:1', 3, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:15:41');
INSERT INTO `sport_log` VALUES (768, '获取限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#getAllLimit()', '{}', '0:0:0:0:0:0:0:1', 2, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:15:50');
INSERT INTO `sport_log` VALUES (769, '获取限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#getAllLimit()', '{}', '0:0:0:0:0:0:0:1', 2, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:15:53');
INSERT INTO `sport_log` VALUES (770, '获取限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#getAllLimit()', '{}', '0:0:0:0:0:0:0:1', 2, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:16:05');
INSERT INTO `sport_log` VALUES (771, '获取限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#getAllLimit()', '{}', '0:0:0:0:0:0:0:1', 2, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:16:09');
INSERT INTO `sport_log` VALUES (772, '获取用户', 'INFO', 'com.xzc.sport.health.controller.UserController#getAllUser(int, int, String)', '{1, 10, }', '0:0:0:0:0:0:0:1', 8, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:16:15');
INSERT INTO `sport_log` VALUES (773, '获取用户', 'INFO', 'com.xzc.sport.health.controller.UserController#getAllUser(int, int, String)', '{1, 10, }', '0:0:0:0:0:0:0:1', 5, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:16:42');
INSERT INTO `sport_log` VALUES (774, '获取用户', 'INFO', 'com.xzc.sport.health.controller.UserController#getAllUser(int, int, String)', '{1, 10, }', '0:0:0:0:0:0:0:1', 4, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:16:48');
INSERT INTO `sport_log` VALUES (775, '获取用户', 'INFO', 'com.xzc.sport.health.controller.UserController#getAllUser(int, int, String)', '{1, 10, }', '0:0:0:0:0:0:0:1', 8, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:16:52');
INSERT INTO `sport_log` VALUES (776, '获取用户', 'INFO', 'com.xzc.sport.health.controller.UserController#getAllUser(int, int, String)', '{1, 10, }', '0:0:0:0:0:0:0:1', 5, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:16:59');
INSERT INTO `sport_log` VALUES (777, '获取用户', 'INFO', 'com.xzc.sport.health.controller.UserController#getAllUser(int, int, String)', '{2, 10, }', '0:0:0:0:0:0:0:1', 3, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:17:06');
INSERT INTO `sport_log` VALUES (778, '删除用户', 'INFO', 'com.xzc.sport.health.controller.UserController#deleteUser(long)', '{33}', '0:0:0:0:0:0:0:1', 164, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:17:09');
INSERT INTO `sport_log` VALUES (779, '获取用户', 'INFO', 'com.xzc.sport.health.controller.UserController#getAllUser(int, int, String)', '{2, 10, }', '0:0:0:0:0:0:0:1', 7, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:17:09');
INSERT INTO `sport_log` VALUES (780, '获取用户', 'INFO', 'com.xzc.sport.health.controller.UserController#getAllUser(int, int, String)', '{1, 10, }', '0:0:0:0:0:0:0:1', 5, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:17:46');
INSERT INTO `sport_log` VALUES (781, '获取用户', 'INFO', 'com.xzc.sport.health.controller.UserController#getAllUser(int, int, String)', '{1, 10, }', '0:0:0:0:0:0:0:1', 5, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:17:52');
INSERT INTO `sport_log` VALUES (782, '获取用户', 'INFO', 'com.xzc.sport.health.controller.UserController#getAllUser(int, int, String)', '{1, 10, }', '0:0:0:0:0:0:0:1', 6, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:18:07');
INSERT INTO `sport_log` VALUES (783, '获取用户', 'INFO', 'com.xzc.sport.health.controller.UserController#getAllUser(int, int, String)', '{1, 10, }', '0:0:0:0:0:0:0:1', 5, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:18:14');
INSERT INTO `sport_log` VALUES (784, '查询菜单', 'INFO', 'com.xzc.sport.health.controller.MenuController#getMenu()', '{}', '0:0:0:0:0:0:0:1', 11, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:18:28');
INSERT INTO `sport_log` VALUES (785, '获取用户', 'INFO', 'com.xzc.sport.health.controller.UserController#getAllUser(int, int, String)', '{1, 10, }', '0:0:0:0:0:0:0:1', 9, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:18:28');
INSERT INTO `sport_log` VALUES (786, '获取用户', 'INFO', 'com.xzc.sport.health.controller.UserController#getAllUser(int, int, String)', '{1, 10, }', '0:0:0:0:0:0:0:1', 5, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:18:51');
INSERT INTO `sport_log` VALUES (787, '查询菜单', 'INFO', 'com.xzc.sport.health.controller.MenuController#getMenu()', '{}', '0:0:0:0:0:0:0:1', 12, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:18:51');
INSERT INTO `sport_log` VALUES (788, '获取用户', 'INFO', 'com.xzc.sport.health.controller.UserController#getAllUser(int, int, String)', '{1, 10, }', '0:0:0:0:0:0:0:1', 4, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:19:34');
INSERT INTO `sport_log` VALUES (789, '获取用户', 'INFO', 'com.xzc.sport.health.controller.UserController#getAllUser(int, int, String)', '{1, 10, }', '0:0:0:0:0:0:0:1', 5, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:19:39');
INSERT INTO `sport_log` VALUES (790, '用户注销', 'INFO', 'com.xzc.sport.health.controller.AuthController#logout(HttpSession)', '{org.apache.catalina.session.StandardSessionFacade@394cfdba}', '0:0:0:0:0:0:0:1', 0, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:20:05');
INSERT INTO `sport_log` VALUES (791, '用户登录', 'INFO', 'com.xzc.sport.health.controller.AuthController#login(LoginFormDto, HttpSession)', '{LoginFormDto(username=admin, password=123456), org.apache.catalina.session.StandardSessionFacade@394cfdba}', '0:0:0:0:0:0:0:1', 0, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:20:06');
INSERT INTO `sport_log` VALUES (792, '查询菜单', 'INFO', 'com.xzc.sport.health.controller.MenuController#getMenu()', '{}', '0:0:0:0:0:0:0:1', 8, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:20:07');
INSERT INTO `sport_log` VALUES (793, '用户注销', 'INFO', 'com.xzc.sport.health.controller.AuthController#logout(HttpSession)', '{org.apache.catalina.session.StandardSessionFacade@394cfdba}', '0:0:0:0:0:0:0:1', 1, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:20:11');
INSERT INTO `sport_log` VALUES (794, '用户登录', 'INFO', 'com.xzc.sport.health.controller.AuthController#login(LoginFormDto, HttpSession)', '{LoginFormDto(username=admin, password=123456), org.apache.catalina.session.StandardSessionFacade@41f56456}', '0:0:0:0:0:0:0:1', 19, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:22');
INSERT INTO `sport_log` VALUES (795, '查询菜单', 'INFO', 'com.xzc.sport.health.controller.MenuController#getMenu()', '{}', '0:0:0:0:0:0:0:1', 42, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:22');
INSERT INTO `sport_log` VALUES (796, '获取用户', 'INFO', 'com.xzc.sport.health.controller.UserController#getAllUser(int, int, String)', '{1, 10, }', '0:0:0:0:0:0:0:1', 43, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:24');
INSERT INTO `sport_log` VALUES (797, '获取用户', 'INFO', 'com.xzc.sport.health.controller.UserController#getAllUser(int, int, String)', '{1, 10, }', '0:0:0:0:0:0:0:1', 17, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:29');
INSERT INTO `sport_log` VALUES (798, '查询菜单', 'INFO', 'com.xzc.sport.health.controller.MenuController#getMenu()', '{}', '0:0:0:0:0:0:0:1', 24, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:29');
INSERT INTO `sport_log` VALUES (799, '获取日志', 'INFO', 'com.xzc.sport.health.controller.LogController#getLogByPage(LogQueryDto)', '{LogQueryDto(pageNum=1, pageSize=10, idSort=desc, descName=, logType=INFO, times=[])}', '0:0:0:0:0:0:0:1', 16, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:31');
INSERT INTO `sport_log` VALUES (800, '获取限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#getAllLimit()', '{}', '0:0:0:0:0:0:0:1', 8, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:38');
INSERT INTO `sport_log` VALUES (801, '获取限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#getAllLimit()', '{}', '0:0:0:0:0:0:0:1', 7, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:45');
INSERT INTO `sport_log` VALUES (802, '查询菜单', 'INFO', 'com.xzc.sport.health.controller.MenuController#getMenu()', '{}', '0:0:0:0:0:0:0:1', 19, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:45');
INSERT INTO `sport_log` VALUES (803, '获取限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#getAllLimit()', '{}', '0:0:0:0:0:0:0:1', 4, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:47');
INSERT INTO `sport_log` VALUES (804, '查询菜单', 'INFO', 'com.xzc.sport.health.controller.MenuController#getMenu()', '{}', '0:0:0:0:0:0:0:1', 14, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:47');
INSERT INTO `sport_log` VALUES (805, '获取限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#getAllLimit()', '{}', '0:0:0:0:0:0:0:1', 4, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:48');
INSERT INTO `sport_log` VALUES (806, '查询菜单', 'INFO', 'com.xzc.sport.health.controller.MenuController#getMenu()', '{}', '0:0:0:0:0:0:0:1', 18, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:48');
INSERT INTO `sport_log` VALUES (807, '获取限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#getAllLimit()', '{}', '0:0:0:0:0:0:0:1', 5, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:49');
INSERT INTO `sport_log` VALUES (808, '查询菜单', 'INFO', 'com.xzc.sport.health.controller.MenuController#getMenu()', '{}', '0:0:0:0:0:0:0:1', 19, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:49');
INSERT INTO `sport_log` VALUES (809, '获取限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#getAllLimit()', '{}', '0:0:0:0:0:0:0:1', 3, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:51');
INSERT INTO `sport_log` VALUES (810, '查询菜单', 'INFO', 'com.xzc.sport.health.controller.MenuController#getMenu()', '{}', '0:0:0:0:0:0:0:1', 14, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:51');
INSERT INTO `sport_log` VALUES (811, '获取限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#getAllLimit()', '{}', '0:0:0:0:0:0:0:1', 2, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:52');
INSERT INTO `sport_log` VALUES (812, '查询菜单', 'INFO', 'com.xzc.sport.health.controller.MenuController#getMenu()', '{}', '0:0:0:0:0:0:0:1', 18, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:52');
INSERT INTO `sport_log` VALUES (813, '获取限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#getAllLimit()', '{}', '0:0:0:0:0:0:0:1', 2, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:53');
INSERT INTO `sport_log` VALUES (814, '查询菜单', 'INFO', 'com.xzc.sport.health.controller.MenuController#getMenu()', '{}', '0:0:0:0:0:0:0:1', 12, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:53');
INSERT INTO `sport_log` VALUES (815, '获取限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#getAllLimit()', '{}', '0:0:0:0:0:0:0:1', 3, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:54');
INSERT INTO `sport_log` VALUES (816, '查询菜单', 'INFO', 'com.xzc.sport.health.controller.MenuController#getMenu()', '{}', '0:0:0:0:0:0:0:1', 12, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:54');
INSERT INTO `sport_log` VALUES (817, '获取限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#getAllLimit()', '{}', '0:0:0:0:0:0:0:1', 3, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:54');
INSERT INTO `sport_log` VALUES (818, '获取限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#getAllLimit()', '{}', '0:0:0:0:0:0:0:1', 4, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:22:57');
INSERT INTO `sport_log` VALUES (819, '更新限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#updateLimit(LimitDto, BindingResult)', '{LimitDto(id=6, controllerName=com.xzc.sport.health.controller.MenuController, path=/menus, methodType=GET, limitType=METHOD, methodSignature=getMenu(), state=false, period=60, count=10), org.springframework.validation.BeanPropertyBindingResult: 0 errors}', '0:0:0:0:0:0:0:1', 112, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:23:03');
INSERT INTO `sport_log` VALUES (820, '获取限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#getAllLimit()', '{}', '0:0:0:0:0:0:0:1', 3, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:23:04');
INSERT INTO `sport_log` VALUES (821, '查询菜单', 'INFO', 'com.xzc.sport.health.controller.MenuController#getMenu()', '{}', '0:0:0:0:0:0:0:1', 12, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:23:04');
INSERT INTO `sport_log` VALUES (822, '更新限流接口', 'INFO', 'com.xzc.sport.health.controller.LimitController#updateLimit(LimitDto, BindingResult)', '{LimitDto(id=6, controllerName=com.xzc.sport.health.controller.MenuController, path=/menus, methodType=GET, limitType=METHOD, methodSignature=getMenu(), state=true, period=60, count=10), org.springframework.validation.BeanPropertyBindingResult: 0 errors}', '0:0:0:0:0:0:0:1', 45, 'admin', '本地局域网', 'Chrome', NULL, '2020-09-07 09:23:06');

-- ----------------------------
-- Table structure for sport_user
-- ----------------------------
DROP TABLE IF EXISTS `sport_user`;
CREATE TABLE `sport_user`  (
  `id` int(22) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户密码',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户邮箱',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户角色',
  `state` int(1) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '用户状态（1为已激活 0为未激活），默认为0',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sport_user
-- ----------------------------
INSERT INTO `sport_user` VALUES (14, 'admin', '123456', '2860072080@qq.com', 'ADMIN', 1, '2020-09-03 22:00:20', '2020-09-05 13:14:12');
INSERT INTO `sport_user` VALUES (16, 'user0', '123456', '2860072080@qq.com', 'USER', 1, '2020-09-03 22:01:01', '2020-09-04 20:42:00');
INSERT INTO `sport_user` VALUES (17, 'user1', '123456', '2860072080@qq.com', 'USER', 1, '2020-09-03 22:01:01', '2020-09-04 20:43:54');
INSERT INTO `sport_user` VALUES (18, 'user2', '123456', '2860072080@qq.com', 'USER', 1, '2020-09-03 22:01:01', '2020-09-05 13:28:07');
INSERT INTO `sport_user` VALUES (19, 'user3', '123456', '2860072080@qq.com', 'USER', 1, '2020-09-03 22:01:01', '2020-09-04 13:46:00');
INSERT INTO `sport_user` VALUES (20, 'user4', '123456', '2860072080@qq.com', 'USER', 0, '2020-09-03 22:01:01', '2020-09-05 12:12:08');
INSERT INTO `sport_user` VALUES (21, 'user5', '123456', '2860072080@qq.com', 'USER', 0, '2020-09-03 22:01:01', '2020-09-05 12:12:11');
INSERT INTO `sport_user` VALUES (22, 'user6', '123456', '2860072080@qq.com', 'USER', 0, '2020-09-03 22:01:01', '2020-09-03 22:01:01');
INSERT INTO `sport_user` VALUES (23, 'user7', '123456', '2860072080@qq.com', 'USER', 0, '2020-09-03 22:01:01', '2020-09-05 12:11:45');
INSERT INTO `sport_user` VALUES (24, 'user8', '123456', '2860072080@qq.com', 'USER', 0, '2020-09-03 22:01:01', '2020-09-03 22:01:01');
INSERT INTO `sport_user` VALUES (25, 'user9', '123456', '2860072080@qq.com', 'USER', 0, '2020-09-03 22:01:01', '2020-09-03 22:01:01');
INSERT INTO `sport_user` VALUES (26, 'user10', '123456', '2860072080@qq.com', 'USER', 0, '2020-09-03 22:01:01', '2020-09-05 13:27:07');
INSERT INTO `sport_user` VALUES (27, 'user11', '123456', '2860072080@qq.com', 'USER', 0, '2020-09-03 22:01:01', '2020-09-03 22:01:01');
INSERT INTO `sport_user` VALUES (28, 'user12', '123456', '2860072080@qq.com', 'USER', 0, '2020-09-03 22:01:01', '2020-09-03 22:01:01');
INSERT INTO `sport_user` VALUES (29, 'user13', '123456', '2860072080@qq.com', 'USER', 0, '2020-09-03 22:01:01', '2020-09-03 22:01:01');
INSERT INTO `sport_user` VALUES (30, 'user14', '123456', '2860072080@qq.com', 'USER', 0, '2020-09-03 22:01:01', '2020-09-03 22:01:01');
INSERT INTO `sport_user` VALUES (31, 'user15', '123456', '2860072080@qq.com', 'USER', 0, '2020-09-03 22:01:01', '2020-09-03 22:01:01');
INSERT INTO `sport_user` VALUES (32, 'user16', '123456', '2860072080@qq.com', 'USER', 0, '2020-09-03 22:01:01', '2020-09-05 14:03:11');

-- ----------------------------
-- Table structure for sub_menu
-- ----------------------------
DROP TABLE IF EXISTS `sub_menu`;
CREATE TABLE `sub_menu`  (
  `id` int(50) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `mid` int(50) NOT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `mid`(`mid`) USING BTREE,
  CONSTRAINT `submenu_ibfk_1` FOREIGN KEY (`mid`) REFERENCES `main_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 305 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sub_menu
-- ----------------------------
INSERT INTO `sub_menu` VALUES (101, '用户列表', 'iconfont icon-yonghuliebiao', '/admin/user', 100, '2020-09-03 13:04:57', '2020-09-03 13:05:35');
INSERT INTO `sub_menu` VALUES (102, '修改权限', 'iconfont icon-quanxian1', '/admin/rights', 100, '2020-09-03 13:05:01', '2020-09-03 13:05:03');
INSERT INTO `sub_menu` VALUES (103, '运动模块', 'iconfont icon-yundong', '/admin/sport', 100, '2020-09-03 13:05:11', '2020-09-03 13:05:05');
INSERT INTO `sub_menu` VALUES (104, '商品模块', 'iconfont icon-shangpin', '/admin/goods', 100, '2020-09-03 13:05:13', '2020-09-03 13:05:09');
INSERT INTO `sub_menu` VALUES (201, '运动科普', 'iconfont icon-kepu', '/Introduction', 200, '2020-09-03 13:05:15', '2020-09-03 13:05:33');
INSERT INTO `sub_menu` VALUES (202, '卡路里', 'iconfont icon-qialuli', '/calories', 200, '2020-09-03 13:05:20', '2020-09-03 13:05:30');
INSERT INTO `sub_menu` VALUES (203, '营养配餐', 'iconfont icon-shiwu', '/food', 200, '2020-09-03 13:05:23', '2020-09-03 13:05:27');
INSERT INTO `sub_menu` VALUES (301, '系统日志', 'iconfont icon-rizhi', '/admin/log', 300, '2020-09-04 20:07:47', '2020-09-04 20:07:50');
INSERT INTO `sub_menu` VALUES (302, '异常日志', 'iconfont icon-cuowurizhi', '/admin/errLog', 300, '2020-09-06 13:40:20', '2020-09-06 13:40:22');
INSERT INTO `sub_menu` VALUES (303, '接口限流', 'icon-rongcuoxianliuxitong\r\nicon-rongcuoxianliuxitong\r\nicon-rongcuoxianliuxitong\r\nicon-rongcuoxianliuxitong\r\niconfont icon-rongcuoxianliuxitong', '/admin/limit', 300, '2020-09-04 20:09:44', '2020-09-04 20:09:48');
INSERT INTO `sub_menu` VALUES (304, 'SQL监控', 'iconfont icon-shujukuguanli', '/admin/druid', 300, '2020-09-04 20:11:47', '2020-09-04 20:11:49');

SET FOREIGN_KEY_CHECKS = 1;
