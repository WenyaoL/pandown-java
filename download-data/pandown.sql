/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80015
 Source Host           : localhost:3306
 Source Schema         : pandown

 Target Server Type    : MySQL
 Target Server Version : 80015
 File Encoding         : 65001

 Date: 28/05/2023 15:18:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dbtable_common_account
-- ----------------------------
DROP TABLE IF EXISTS `dbtable_common_account`;
CREATE TABLE `dbtable_common_account`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '普通账号的用户名',
  `cookie` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '普通账号的cookies字符串',
  `state` tinyint(4) NOT NULL COMMENT '账号状态',
  `create_time` datetime NOT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dbtable_svip
-- ----------------------------
DROP TABLE IF EXISTS `dbtable_svip`;
CREATE TABLE `dbtable_svip`  (
  `id` bigint(20) NOT NULL COMMENT '账号id(雪花id)',
  `name` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账号名称',
  `svip_bduss` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '会员bduss',
  `svip_stoken` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '会员stoken',
  `state` tinyint(4) NOT NULL COMMENT '会员状态(1:正常,0:限速)',
  `vip_type` tinyint(4) NOT NULL COMMENT 'vip类型(1:vip,2:svip,3:evip)',
  `create_time` datetime NOT NULL COMMENT '会员账号加入时间',
  `update_time` datetime NOT NULL COMMENT '跟新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pandown_parse
-- ----------------------------
DROP TABLE IF EXISTS `pandown_parse`;
CREATE TABLE `pandown_parse`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ip',
  `filename` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件名',
  `size` bigint(20) NULL DEFAULT NULL COMMENT '文件大小',
  `md5` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '文件效验码',
  `path` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '文件路径',
  `server_ctime` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '文件创建时间',
  `realLink` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件下载地址',
  `parse_account_id` bigint(20) NOT NULL COMMENT '解析账号id',
  `create_time` datetime NOT NULL COMMENT '解析时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pandown_permission
-- ----------------------------
DROP TABLE IF EXISTS `pandown_permission`;
CREATE TABLE `pandown_permission`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限标识符',
  `description` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `url` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pandown_permission
-- ----------------------------
INSERT INTO `pandown_permission` VALUES (1, 'dbtableSvipApi', 'dbtableSvip服务接口服务权限', '/api/dbtableSvip/**');
INSERT INTO `pandown_permission` VALUES (2, 'downloadApi', 'downloadApi服务接口服务权限', '/api/download/**');
INSERT INTO `pandown_permission` VALUES (3, 'dbtableCommonAccountApi', 'dbtableCommonAccount服务接口权限', '/api/dbtable-common-account/**');
INSERT INTO `pandown_permission` VALUES (4, 'pandownUserFlowApi', '用户流量查询接口', '/api/pandown-user-flow/**');
INSERT INTO `pandown_permission` VALUES (5, 'pandownAdminApi', '管理员接口', '/api/pandownAdmin/**');

SET FOREIGN_KEY_CHECKS = 1;


-- ----------------------------
-- Table structure for pandown_role
-- ----------------------------
DROP TABLE IF EXISTS `pandown_role`;
CREATE TABLE `pandown_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_role_name`(`role_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pandown_role
-- ----------------------------
INSERT INTO `pandown_role` VALUES (1, 'admin', '管理员', '2023-05-11 21:14:50', NULL, '1');
INSERT INTO `pandown_role` VALUES (2, 'user', '普通用户', '2023-05-11 21:14:57', NULL, '1');

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for pandown_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `pandown_role_permission`;
CREATE TABLE `pandown_role_permission`  (
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`role_id`, `permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pandown_role_permission
-- ----------------------------
INSERT INTO `pandown_role_permission` VALUES (1, 1);
INSERT INTO `pandown_role_permission` VALUES (1, 2);
INSERT INTO `pandown_role_permission` VALUES (1, 3);
INSERT INTO `pandown_role_permission` VALUES (1, 4);
INSERT INTO `pandown_role_permission` VALUES (1, 5);
INSERT INTO `pandown_role_permission` VALUES (2, 2);
INSERT INTO `pandown_role_permission` VALUES (2, 4);

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for pandown_user
-- ----------------------------
DROP TABLE IF EXISTS `pandown_user`;
CREATE TABLE `pandown_user`  (
  `id` bigint(20) NOT NULL COMMENT '用户id',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pandown_user_flow
-- ----------------------------
DROP TABLE IF EXISTS `pandown_user_flow`;
CREATE TABLE `pandown_user_flow`  (
  `id` bigint(20) NOT NULL COMMENT '用户id',
  `parse_num` int(11) NULL DEFAULT NULL COMMENT '解析数',
  `parse_flow` bigint(20) NULL DEFAULT NULL COMMENT '解析流量',
  `limit_flow` bigint(20) NULL DEFAULT NULL COMMENT '流量限制',
  `state` tinyint(4) NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pandown_user_role
-- ----------------------------
DROP TABLE IF EXISTS `pandown_user_role`;
CREATE TABLE `pandown_user_role`  (
  `user_id` bigint(20) NOT NULL,
  `role_id` int(11) NOT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
