/*
 Navicat MySQL Data Transfer

 Source Server         : xc
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : 47.108.192.109:3306
 Source Schema         : xc_user

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 02/06/2024 16:59:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_base
-- ----------------------------
DROP TABLE IF EXISTS `user_base`;
CREATE TABLE `user_base`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `user_role` tinyint UNSIGNED NULL DEFAULT 1 COMMENT '1 用户  2管理员 ',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
  `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账号',
  `nick_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '密码',
  `mobile` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号(唯一)',
  `gender` tinyint UNSIGNED NULL DEFAULT 2 COMMENT '性别 0-female 1-male  2-未知',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `srcface` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '头像',
  `default_address` int NULL DEFAULT NULL COMMENT '默认地址id\n',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户基础信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_base
-- ----------------------------
INSERT INTO `user_base` VALUES (8312297327392, 1, 0, 'pyl9999', '05tRCG', 'd3b1294a61a07da9b49b6e22b2cbd7f9', NULL, 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-05-21 14:34:15', '2024-06-02 11:12:06');
INSERT INTO `user_base` VALUES (8312390765600, 1, 0, 'pyl999', 'jMKaSc', 'd3b1294a61a07da9b49b6e22b2cbd7f9', NULL, 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-05-21 14:40:20', '2024-06-02 11:12:08');
INSERT INTO `user_base` VALUES (8312616802592, 1, 1, 'pyl99', '9HTOEy', 'd3b1294a61a07da9b49b6e22b2cbd7f9', NULL, 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-05-21 14:54:55', '2024-06-02 11:12:10');
INSERT INTO `user_base` VALUES (8513963058208, 1, 1, 'Riou', 'kbd5DP', '77c21c9ea568d95ff2e120a2bd241283', NULL, 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-05-30 17:23:23', '2024-06-02 11:12:13');
INSERT INTO `user_base` VALUES (8513973549856, 1, 1, 'w', 'BUkMNy01001', 'd3b1294a61a07da9b49b6e22b2cbd7f9', NULL, 2, NULL, 'http://47.108.192.109:9000/files/b36b4200aa4b45a9a30c44308d3f7bac.jpg', NULL, '2024-05-30 17:24:04', '2024-06-02 05:07:48');
INSERT INTO `user_base` VALUES (8519267896608, 1, 1, 'hero', 'yYTgJS', 'ae8563c3137e3b754bd1fefd4a33f2c0', NULL, 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-05-30 23:08:45', '2024-06-02 11:12:15');
INSERT INTO `user_base` VALUES (8562500698144, 1, 1, 'pp', 'bug--', 'd3b1294a61a07da9b49b6e22b2cbd7f9', NULL, 0, NULL, 'http://47.108.192.109:9000/files/0d1ef9171b2e4ee9a41aadadfaa9ac69.jpg', NULL, '2024-06-01 22:03:24', '2024-06-02 11:39:00');
INSERT INTO `user_base` VALUES (8562696882976, 1, 1, 'tuski', '购物狂', 'd3b1294a61a07da9b49b6e22b2cbd7f9', NULL, 0, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-06-01 22:16:10', '2024-06-02 11:12:17');
INSERT INTO `user_base` VALUES (8571533410592, 1, 1, '测试账号1', 'RPpVXT', 'c38dc3dcb8f0b43ac8ea6a70b5ec7648', NULL, 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-06-02 07:51:28', '2024-06-02 11:12:22');
INSERT INTO `user_base` VALUES (8572290023456, 1, 1, '测试账号2', 'n5yk7u', 'd3b1294a61a07da9b49b6e22b2cbd7f9', NULL, 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-06-02 08:40:43', '2024-06-02 11:12:23');
INSERT INTO `user_base` VALUES (8572349178912, 1, 1, '测试账号3', 'Dh03i3', 'd3b1294a61a07da9b49b6e22b2cbd7f9', NULL, 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-06-02 08:44:34', '2024-06-02 11:12:25');
INSERT INTO `user_base` VALUES (8572691172640, 1, 1, 'bug--', 'APFacx', 'd3b1294a61a07da9b49b6e22b2cbd7f9', NULL, 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-06-02 09:06:50', '2024-06-02 11:12:28');
INSERT INTO `user_base` VALUES (8574239724320, 1, 1, '测试5', 'aCKCWH', 'd3b1294a61a07da9b49b6e22b2cbd7f9', NULL, 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-06-02 10:47:39', '2024-06-02 11:12:29');
INSERT INTO `user_base` VALUES (8575039437600, 1, 1, 'consumer1', 'P6zJiq', 'd3b1294a61a07da9b49b6e22b2cbd7f9', NULL, 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-06-02 11:39:43', '2024-06-02 11:39:43');
INSERT INTO `user_base` VALUES (8575375339296, 1, 1, 'jirafa', 'RyMHdM', 'd3b1294a61a07da9b49b6e22b2cbd7f9', NULL, 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-06-02 12:01:35', '2024-06-02 12:01:35');
INSERT INTO `user_base` VALUES (8575465700640, 1, 1, 'Yoe', 'VV12s8', 'd3b1294a61a07da9b49b6e22b2cbd7f9', NULL, 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-06-02 12:07:28', '2024-06-02 12:07:28');
INSERT INTO `user_base` VALUES (1790297174534524928, 1, 1, 'pyl', 'OzSh2G', 'c38dc3dcb8f0b43ac8ea6a70b5ec7648', '18583866898', 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-05-14 16:24:43', '2024-06-02 11:12:31');
INSERT INTO `user_base` VALUES (1790371178272407552, 1, 1, 'pyl2', 'AEypOH', 'c38dc3dcb8f0b43ac8ea6a70b5ec7648', NULL, 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-05-14 21:18:47', '2024-06-02 11:12:32');
INSERT INTO `user_base` VALUES (1790734802388131840, 1, 1, 'asdas', 'zlp0sg', '34fe889242a0511328acdf2448bc318d', NULL, 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-05-15 21:23:41', '2024-06-02 11:12:36');
INSERT INTO `user_base` VALUES (1790735837504487424, 1, 1, 'ysx', 'ysx123', 'c00bdd358e5c03227a0c4a10de05e5fe', NULL, 1, NULL, 'http://47.108.192.109:9000/files/9c7059205264455f9fd439b1f5528d3e.png', NULL, '2024-05-15 21:27:48', '2024-06-02 11:12:35');
INSERT INTO `user_base` VALUES (1791720621433032704, 1, 1, 'saradess', 'AI7kgT', 'd3b1294a61a07da9b49b6e22b2cbd7f9', NULL, 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-05-18 14:40:59', '2024-06-02 11:12:38');
INSERT INTO `user_base` VALUES (1791732472061898752, 1, 1, 'sara', 'yM0V9e', 'd6142799db1c0bd74a4e0df79da08eb4', NULL, 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-05-18 15:28:04', '2024-06-02 11:12:40');
INSERT INTO `user_base` VALUES (1791732597085319168, 1, 1, 'sarara', 'jsOnBX', 'a610ae6ab1db3f6e04c9a364a9221246', NULL, 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-05-18 15:28:34', '2024-06-02 11:12:41');
INSERT INTO `user_base` VALUES (1792188141227216896, 2, 1, 'koi', 'Zb1Qxw', '86576660ae6c6038e2fe996aeb1eb308', NULL, 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-05-19 21:38:45', '2024-05-19 21:38:45');
INSERT INTO `user_base` VALUES (1792191063872909312, 1, 0, 'pyl3', '8hqFlt', 'c38dc3dcb8f0b43ac8ea6a70b5ec7648', NULL, 2, NULL, 'https://bpic.588ku.com/element_origin_min_pic/19/04/09/cb02b26df1cd2ff6dfc97456e2cab54d.jpg', NULL, '2024-05-19 21:50:22', '2024-06-02 11:12:45');

SET FOREIGN_KEY_CHECKS = 1;
