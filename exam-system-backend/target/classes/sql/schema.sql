-- 在线考试系统数据库建表脚本
-- MySQL 8.0+

-- 创建数据库
CREATE DATABASE IF NOT EXISTS exam_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE exam_system;

-- 1. 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
  `role` VARCHAR(20) NOT NULL COMMENT '角色：ADMIN-管理员，TEACHER-教师，STUDENT-学生',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `gender` CHAR(1) DEFAULT NULL COMMENT '性别：M-男，F-女',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 2. 班级表
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '班级ID',
  `name` VARCHAR(50) NOT NULL COMMENT '班级名称',
  `grade` VARCHAR(20) DEFAULT NULL COMMENT '年级',
  `profession` VARCHAR(50) DEFAULT NULL COMMENT '专业',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='班级表';

-- 3. 学生班级关联表
DROP TABLE IF EXISTS `student_class`;
CREATE TABLE `student_class` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `student_id` BIGINT NOT NULL COMMENT '学生ID',
  `class_id` BIGINT NOT NULL COMMENT '班级ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_class` (`student_id`, `class_id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_class_id` (`class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生班级关联表';

-- 4. 题库表
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '题目ID',
  `type` VARCHAR(20) NOT NULL COMMENT '题型：SINGLE-单选，MULTIPLE-多选，JUDGE-判断，BLANK-填空，ESSAY-简答',
  `subject` VARCHAR(50) DEFAULT NULL COMMENT '科目',
  `content` TEXT NOT NULL COMMENT '题目内容',
  `options` TEXT DEFAULT NULL COMMENT '选项（JSON格式，如：["A. 选项1", "B. 选项2"]）',
  `answer` TEXT NOT NULL COMMENT '答案',
  `difficulty` VARCHAR(20) DEFAULT 'MEDIUM' COMMENT '难度：EASY-简单，MEDIUM-中等，HARD-困难',
  `teacher_id` BIGINT DEFAULT NULL COMMENT '创建教师ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_subject` (`subject`),
  KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题库表';

-- 5. 试卷表
DROP TABLE IF EXISTS `paper`;
CREATE TABLE `paper` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '试卷ID',
  `name` VARCHAR(100) NOT NULL COMMENT '试卷名称',
  `subject` VARCHAR(50) DEFAULT NULL COMMENT '科目',
  `total_score` INT DEFAULT 0 COMMENT '总分',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='试卷表';

-- 6. 试卷题目关联表
DROP TABLE IF EXISTS `paper_question`;
CREATE TABLE `paper_question` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `paper_id` BIGINT NOT NULL COMMENT '试卷ID',
  `question_id` BIGINT NOT NULL COMMENT '题目ID',
  `score` INT NOT NULL COMMENT '分数',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_paper_id` (`paper_id`),
  KEY `idx_question_id` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='试卷题目关联表';

-- 7. 考试表
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '考试ID',
  `paper_id` BIGINT NOT NULL COMMENT '试卷ID',
  `exam_name` VARCHAR(100) NOT NULL COMMENT '考试名称',
  `start_time` DATETIME NOT NULL COMMENT '开始时间',
  `end_time` DATETIME NOT NULL COMMENT '结束时间',
  `duration` INT NOT NULL COMMENT '考试时长（分钟）',
  `class_id` BIGINT DEFAULT NULL COMMENT '班级ID（为空表示全体可参加）',
  `allow_makeup` TINYINT DEFAULT 0 COMMENT '允许补考：0-不允许，1-允许',
  `password` VARCHAR(50) DEFAULT NULL COMMENT '考试密码',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_paper_id` (`paper_id`),
  KEY `idx_class_id` (`class_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='考试表';

-- 8. 答题记录表
DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `exam_id` BIGINT NOT NULL COMMENT '考试ID',
  `student_id` BIGINT NOT NULL COMMENT '学生ID',
  `question_id` BIGINT NOT NULL COMMENT '题目ID',
  `answer` TEXT DEFAULT NULL COMMENT '学生答案',
  `score` INT DEFAULT NULL COMMENT '得分',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_exam_student_question` (`exam_id`, `student_id`, `question_id`),
  KEY `idx_exam_id` (`exam_id`),
  KEY `idx_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='答题记录表';

-- 9. 成绩表
DROP TABLE IF EXISTS `grade`;
CREATE TABLE `grade` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `exam_id` BIGINT NOT NULL COMMENT '考试ID',
  `student_id` BIGINT NOT NULL COMMENT '学生ID',
  `total_score` INT DEFAULT 0 COMMENT '总分',
  `submit_time` DATETIME DEFAULT NULL COMMENT '提交时间',
  `status` VARCHAR(20) DEFAULT 'GRADING' COMMENT '状态：GRADING-阅卷中，COMPLETED-已完成',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_exam_student` (`exam_id`, `student_id`),
  KEY `idx_exam_id` (`exam_id`),
  KEY `idx_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成绩表';

-- 10. 文件表
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `url` VARCHAR(255) NOT NULL COMMENT '文件URL',
  `upload_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件表';
