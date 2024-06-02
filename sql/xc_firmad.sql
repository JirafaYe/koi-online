/*
 Navicat MySQL Data Transfer

 Source Server         : xc
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : 47.108.192.109:3306
 Source Schema         : xc_firmad

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 02/06/2024 16:58:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for advertise
-- ----------------------------
DROP TABLE IF EXISTS `advertise`;
CREATE TABLE `advertise`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `ad_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '广告商名称',
  `expense` decimal(8, 4) NOT NULL COMMENT '收取费用',
  `ad_start_date` datetime(0) NOT NULL COMMENT '广告开始时间',
  `ad_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '外接地址',
  `ad_end_date` datetime(0) NOT NULL COMMENT '广告结束时间',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `file_id` bigint NULL DEFAULT NULL COMMENT '上传图片文件id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '广告' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of advertise
-- ----------------------------
INSERT INTO `advertise` VALUES (8, 'Nike', 2000.0000, '2024-06-03 00:00:00', 'https://www.nike.com.cn/', '2024-06-30 00:00:00', '2024-06-02 04:16:03', '2024-06-02 04:16:03', 1796999158053777410);
INSERT INTO `advertise` VALUES (13, '鸿星尔克', 3000.0000, '2024-06-01 00:00:00', 'https://www.erke.com/', '2024-06-06 00:00:00', '2024-06-02 12:03:52', '2024-06-02 12:03:52', 1797116892875894786);
INSERT INTO `advertise` VALUES (14, '李宁', 3000.0000, '2024-06-01 00:00:00', 'https://store.lining.com/', '2024-06-06 00:00:00', '2024-06-02 12:05:37', '2024-06-02 12:05:37', 1797117338021572609);

-- ----------------------------
-- Table structure for corporate_partner
-- ----------------------------
DROP TABLE IF EXISTS `corporate_partner`;
CREATE TABLE `corporate_partner`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `partner_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '合作商名称',
  `uri_brand` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '品牌链接地址',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '备注',
  `file_id` bigint NULL DEFAULT NULL COMMENT '关联图片id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '合作企页表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of corporate_partner
-- ----------------------------
INSERT INTO `corporate_partner` VALUES (5, '米哈游', 'https://ys.mihoyo.com/', 'mhyyyy', 1796993456618909698);
INSERT INTO `corporate_partner` VALUES (6, '腾讯', 'https://www.tencent.com', NULL, 1796997262140289026);

SET FOREIGN_KEY_CHECKS = 1;
