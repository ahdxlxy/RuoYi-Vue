/*
 Navicat Premium Dump SQL

 Source Server         : 1
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44)
 Source Host           : localhost:3306
 Source Schema         : exam_system

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44)
 File Encoding         : 65001

 Date: 02/12/2025 18:53:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for answer
-- ----------------------------
DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `exam_id` bigint NOT NULL COMMENT '考试ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `question_id` bigint NOT NULL COMMENT '题目ID',
  `answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '学生答案',
  `score` int NULL DEFAULT NULL COMMENT '得分',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_exam_student_question`(`exam_id` ASC, `student_id` ASC, `question_id` ASC) USING BTREE,
  INDEX `idx_exam_id`(`exam_id` ASC) USING BTREE,
  INDEX `idx_student_id`(`student_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '答题记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of answer
-- ----------------------------
INSERT INTO `answer` VALUES (1, 1, 4, 1, 'A. static', 0, NULL, NULL);
INSERT INTO `answer` VALUES (2, 1, 4, 2, 'A. clone()', 0, NULL, NULL);
INSERT INTO `answer` VALUES (3, 1, 4, 5, '正确', 10, NULL, NULL);
INSERT INTO `answer` VALUES (4, 1, 4, 6, '正确', 10, NULL, NULL);
INSERT INTO `answer` VALUES (5, 1, 4, 8, '是', 0, NULL, NULL);
INSERT INTO `answer` VALUES (6, 1, 4, 9, '1', 0, NULL, NULL);
INSERT INTO `answer` VALUES (7, 1, 4, 11, '1', 1, NULL, NULL);

-- ----------------------------
-- Table structure for class
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '班级ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '班级名称',
  `grade` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '年级',
  `profession` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '专业',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '班级表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of class
-- ----------------------------
INSERT INTO `class` VALUES (1, '软件工程2021级1班', '2021级', '软件工程', '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `class` VALUES (2, '计算机科学2021级1班', '2021级', '计算机科学与技术', '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `class` VALUES (3, '软件工程2022级1班', '2022级', '软件工程', '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);

-- ----------------------------
-- Table structure for exam
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '考试ID',
  `paper_id` bigint NOT NULL COMMENT '试卷ID',
  `exam_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '考试名称',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `duration` int NOT NULL COMMENT '考试时长（分钟）',
  `class_id` bigint NULL DEFAULT NULL COMMENT '班级ID（为空表示全体可参加）',
  `allow_makeup` tinyint NULL DEFAULT 0 COMMENT '允许补考：0-不允许，1-允许',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '考试密码',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_paper_id`(`paper_id` ASC) USING BTREE,
  INDEX `idx_class_id`(`class_id` ASC) USING BTREE,
  INDEX `idx_start_time`(`start_time` ASC) USING BTREE,
  INDEX `idx_end_time`(`end_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '考试表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exam
-- ----------------------------
INSERT INTO `exam` VALUES (1, 1, 'Java基础期中考试', '2025-11-30 17:00:00', '2025-12-05 03:00:00', 120, 1, 0, NULL, '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `exam` VALUES (2, 2, '数据库基础考试', '2024-12-05 14:00:00', '2024-12-05 16:00:00', 120, 1, 1, '123456', '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件URL',
  `upload_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of file
-- ----------------------------

-- ----------------------------
-- Table structure for grade
-- ----------------------------
DROP TABLE IF EXISTS `grade`;
CREATE TABLE `grade`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `exam_id` bigint NOT NULL COMMENT '考试ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `total_score` int NULL DEFAULT 0 COMMENT '总分',
  `submit_time` datetime NULL DEFAULT NULL COMMENT '提交时间',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'GRADING' COMMENT '状态：GRADING-阅卷中，COMPLETED-已完成',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_exam_student`(`exam_id` ASC, `student_id` ASC) USING BTREE,
  INDEX `idx_exam_id`(`exam_id` ASC) USING BTREE,
  INDEX `idx_student_id`(`student_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '成绩表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of grade
-- ----------------------------
INSERT INTO `grade` VALUES (1, 1, 4, 21, '2025-12-01 11:18:33', 'COMPLETED', NULL, NULL);

-- ----------------------------
-- Table structure for paper
-- ----------------------------
DROP TABLE IF EXISTS `paper`;
CREATE TABLE `paper`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '试卷ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '试卷名称',
  `subject` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '科目',
  `total_score` int NULL DEFAULT 0 COMMENT '总分',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '试卷表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of paper
-- ----------------------------
INSERT INTO `paper` VALUES (1, 'Java基础测试卷', 'Java', 100, '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `paper` VALUES (2, '数据库基础测试卷', 'Database', 100, '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `paper` VALUES (3, '计算机考研综合', '408', 120, NULL, NULL, 0);

-- ----------------------------
-- Table structure for paper_question
-- ----------------------------
DROP TABLE IF EXISTS `paper_question`;
CREATE TABLE `paper_question`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `paper_id` bigint NOT NULL COMMENT '试卷ID',
  `question_id` bigint NOT NULL COMMENT '题目ID',
  `score` int NOT NULL COMMENT '分数',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_paper_id`(`paper_id` ASC) USING BTREE,
  INDEX `idx_question_id`(`question_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '试卷题目关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of paper_question
-- ----------------------------
INSERT INTO `paper_question` VALUES (1, 1, 1, 10, 1, '2025-11-30 23:25:29');
INSERT INTO `paper_question` VALUES (2, 1, 2, 10, 2, '2025-11-30 23:25:29');
INSERT INTO `paper_question` VALUES (3, 1, 5, 10, 3, '2025-11-30 23:25:29');
INSERT INTO `paper_question` VALUES (4, 1, 6, 10, 4, '2025-11-30 23:25:29');
INSERT INTO `paper_question` VALUES (5, 1, 8, 10, 5, '2025-11-30 23:25:29');
INSERT INTO `paper_question` VALUES (6, 1, 9, 10, 6, '2025-11-30 23:25:29');
INSERT INTO `paper_question` VALUES (7, 1, 11, 40, 7, '2025-11-30 23:25:29');
INSERT INTO `paper_question` VALUES (8, 2, 3, 15, 1, '2025-11-30 23:25:29');
INSERT INTO `paper_question` VALUES (9, 2, 4, 15, 2, '2025-11-30 23:25:29');
INSERT INTO `paper_question` VALUES (10, 2, 7, 10, 3, '2025-11-30 23:25:29');
INSERT INTO `paper_question` VALUES (11, 2, 10, 10, 4, '2025-11-30 23:25:29');
INSERT INTO `paper_question` VALUES (12, 2, 12, 50, 5, '2025-11-30 23:25:29');
INSERT INTO `paper_question` VALUES (13, 3, 1, 10, 1, NULL);
INSERT INTO `paper_question` VALUES (14, 3, 2, 10, 2, NULL);
INSERT INTO `paper_question` VALUES (15, 3, 3, 10, 3, NULL);
INSERT INTO `paper_question` VALUES (16, 3, 4, 10, 4, NULL);
INSERT INTO `paper_question` VALUES (17, 3, 5, 10, 5, NULL);
INSERT INTO `paper_question` VALUES (18, 3, 6, 10, 6, NULL);
INSERT INTO `paper_question` VALUES (19, 3, 7, 10, 7, NULL);
INSERT INTO `paper_question` VALUES (20, 3, 8, 10, 8, NULL);
INSERT INTO `paper_question` VALUES (21, 3, 9, 10, 9, NULL);
INSERT INTO `paper_question` VALUES (22, 3, 10, 10, 10, NULL);
INSERT INTO `paper_question` VALUES (23, 3, 11, 10, 11, NULL);
INSERT INTO `paper_question` VALUES (24, 3, 12, 10, 12, NULL);

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '题目ID',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '题型：SINGLE-单选，MULTIPLE-多选，JUDGE-判断，BLANK-填空，ESSAY-简答',
  `subject` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '科目',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '题目内容',
  `options` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '选项（JSON格式，如：[\"A. 选项1\", \"B. 选项2\"]）',
  `answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '答案',
  `difficulty` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'MEDIUM' COMMENT '难度：EASY-简单，MEDIUM-中等，HARD-困难',
  `teacher_id` bigint NULL DEFAULT NULL COMMENT '创建教师ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_type`(`type` ASC) USING BTREE,
  INDEX `idx_subject`(`subject` ASC) USING BTREE,
  INDEX `idx_teacher_id`(`teacher_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '题库表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question` VALUES (1, 'SINGLE', 'Java', 'Java中，以下哪个关键字用于定义常量？', '[\"A. static\", \"B. final\", \"C. const\", \"D. constant\"]', 'B', 'EASY', 2, '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `question` VALUES (2, 'SINGLE', 'Java', '下列哪个是Object类的方法？', '[\"A. clone()\", \"B. finalize()\", \"C. toString()\", \"D. 以上都是\"]', 'D', 'MEDIUM', 2, '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `question` VALUES (3, 'SINGLE', 'Database', 'SQL语句中，用于删除表中数据的关键字是？', '[\"A. DROP\", \"B. DELETE\", \"C. REMOVE\", \"D. TRUNCATE\"]', 'B', 'EASY', 2, '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `question` VALUES (4, 'SINGLE', 'Database', '在关系数据库中，主键的作用是？', '[\"A. 唯一标识一条记录\", \"B. 创建索引\", \"C. 提高查询速度\", \"D. 以上都是\"]', 'D', 'MEDIUM', 2, '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `question` VALUES (5, 'JUDGE', 'Java', 'Java是一种纯面向对象的编程语言。', '[\"正确\", \"错误\"]', '正确', 'EASY', 2, '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `question` VALUES (6, 'JUDGE', 'Java', 'Java中的接口可以包含方法的实现。', '[\"正确\", \"错误\"]', '正确', 'MEDIUM', 2, '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `question` VALUES (7, 'JUDGE', 'Database', 'SQL语句对大小写不敏感。', '[\"正确\", \"错误\"]', '正确', 'EASY', 2, '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `question` VALUES (8, 'BLANK', 'Java', 'Java中，用____关键字来实现继承。', NULL, 'extends', 'EASY', 2, '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `question` VALUES (9, 'BLANK', 'Java', 'Java中，____关键字用于实现接口。', NULL, 'implements', 'EASY', 2, '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `question` VALUES (10, 'BLANK', 'Database', 'SQL中，____语句用于查询数据。', NULL, 'SELECT', 'EASY', 2, '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `question` VALUES (11, 'ESSAY', 'Java', '请简述Java面向对象的三大特征。', NULL, '封装、继承、多态。封装是将数据和操作数据的方法封装在一起；继承是子类继承父类的属性和方法；多态是同一个方法可以有不同的实现。', 'MEDIUM', 2, '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `question` VALUES (12, 'ESSAY', 'Database', '请简述数据库事务的ACID特性。', NULL, 'A原子性：事务不可分割；C一致性：事务前后数据保持一致；I隔离性：多个事务互不影响；D持久性：事务提交后永久保存。', 'HARD', 2, '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);

-- ----------------------------
-- Table structure for student_class
-- ----------------------------
DROP TABLE IF EXISTS `student_class`;
CREATE TABLE `student_class`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `class_id` bigint NOT NULL COMMENT '班级ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_student_class`(`student_id` ASC, `class_id` ASC) USING BTREE,
  INDEX `idx_student_id`(`student_id` ASC) USING BTREE,
  INDEX `idx_class_id`(`class_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学生班级关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student_class
-- ----------------------------
INSERT INTO `student_class` VALUES (2, 5, 1, '2025-11-30 23:25:29');
INSERT INTO `student_class` VALUES (3, 6, 2, '2025-11-30 23:25:29');
INSERT INTO `student_class` VALUES (4, 6, 1, NULL);
INSERT INTO `student_class` VALUES (5, 4, 2, NULL);
INSERT INTO `student_class` VALUES (6, 5, 2, NULL);
INSERT INTO `student_class` VALUES (7, 4, 1, NULL);
INSERT INTO `student_class` VALUES (8, 4, 3, NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色：ADMIN-管理员，TEACHER-教师，STUDENT-学生',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别：M-男，F-女',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', 'ADMIN', 'admin@exam.com', '13800138000', 'M', '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `user` VALUES (2, 'teacher1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张老师', 'TEACHER', 'teacher1@exam.com', '13800138001', 'M', '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `user` VALUES (3, 'teacher2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '李老师', 'TEACHER', 'teacher2@exam.com', '13800138002', 'F', '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `user` VALUES (4, 'student1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '王小明', 'STUDENT', 'student1@exam.com', '13800138003', 'M', '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `user` VALUES (5, 'student2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '刘小红', 'STUDENT', 'student2@exam.com', '13800138004', 'F', '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);
INSERT INTO `user` VALUES (6, 'student3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '陈大伟', 'STUDENT', 'student3@exam.com', '13800138005', 'M', '2025-11-30 23:25:29', '2025-11-30 23:25:29', 0);

SET FOREIGN_KEY_CHECKS = 1;
