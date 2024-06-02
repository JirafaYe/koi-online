/*
 Navicat MySQL Data Transfer

 Source Server         : xc
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : 47.108.192.109:3306
 Source Schema         : xc_remark

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 02/06/2024 16:59:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for liked_record
-- ----------------------------
DROP TABLE IF EXISTS `liked_record`;
CREATE TABLE `liked_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `biz_id` bigint NOT NULL COMMENT '点赞的业务id',
  `biz_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '点赞的业务类型',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_biz_user`(`biz_id`, `user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '点赞记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of liked_record
-- ----------------------------

-- ----------------------------
-- Table structure for reply
-- ----------------------------
DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply`  (
  `id` bigint NOT NULL COMMENT '回答id',
  `review_id` bigint NOT NULL COMMENT '评论id',
  `answer_id` bigint NULL DEFAULT 0 COMMENT '回复的上级回答id',
  `user_id` bigint NOT NULL COMMENT '回答者id',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '回答内容',
  `target_user_id` bigint NULL DEFAULT 0 COMMENT '回复的目标用户id',
  `target_reply_id` bigint NULL DEFAULT 0 COMMENT '回复的目标回复id',
  `reply_times` int NOT NULL DEFAULT 0 COMMENT '评论数量',
  `liked_times` int NOT NULL DEFAULT 0 COMMENT '点赞数量',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '回复表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reply
-- ----------------------------
INSERT INTO `reply` VALUES (1797125481340403714, 1797125115131527170, 0, 8575039437600, '我也觉得', 0, 0, 0, 0, '2024-06-02 12:37:58', '2024-06-02 12:37:58');
INSERT INTO `reply` VALUES (1797125503230476289, 1797125115131527170, 0, 8575039437600, '是的', 0, 0, 0, 0, '2024-06-02 12:38:03', '2024-06-02 12:38:03');
INSERT INTO `reply` VALUES (1797125907456524290, 1797125766448218113, 0, 8575039437600, '是的，我也觉得', 0, 0, 0, 0, '2024-06-02 12:39:39', '2024-06-02 12:39:39');

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `product_id` bigint NOT NULL COMMENT '商品id',
  `order_detail_id` bigint NOT NULL COMMENT '订单详细id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `reply_times` int NOT NULL DEFAULT 0 COMMENT '评论数量',
  `liked_times` int NOT NULL DEFAULT 0 COMMENT '点赞数量',
  `have_pic` tinyint(1) NULL DEFAULT NULL COMMENT '是否有图片',
  `pics` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片id',
  `have_video` tinyint(1) NULL DEFAULT 0 COMMENT '是否有视频',
  `video` bigint NULL DEFAULT NULL COMMENT '视频id',
  `description_score` int NOT NULL DEFAULT 0 COMMENT '描述评分',
  `product_score` int NOT NULL COMMENT '商品评分',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE,
  INDEX `order_id`(`order_detail_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1796803529639788547 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of review
-- ----------------------------
INSERT INTO `review` VALUES (1797125115131527170, 1796974849533415426, 56, 8562500698144, '真的很不错', 2, 0, 0, NULL, 0, 97, 2, 4, '2024-06-02 12:36:31', '2024-06-02 12:38:03');
INSERT INTO `review` VALUES (1797125766448218113, 1796589026782744577, 56, 8562500698144, '上脚特别软，推荐购买', 1, 2, 0, NULL, 0, 97, 5, 5, '2024-06-02 12:39:06', '2024-06-02 12:55:26');
INSERT INTO `review` VALUES (1797126819063332865, 1796886168793518082, 56, 8562500698144, '上脚特别很帅气，推荐购买', 0, 0, 0, NULL, 0, 97, 5, 5, '2024-06-02 12:43:17', '2024-06-02 12:43:17');

SET FOREIGN_KEY_CHECKS = 1;
