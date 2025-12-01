-- 初始化数据脚本
USE exam_system;

-- 插入管理员用户（默认密码：admin123）
INSERT INTO `user` (`username`, `password`, `real_name`, `role`, `email`, `phone`, `gender`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', 'ADMIN', 'admin@exam.com', '13800138000', 'M'),
('teacher1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张老师', 'TEACHER', 'teacher1@exam.com', '13800138001', 'M'),
('teacher2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '李老师', 'TEACHER', 'teacher2@exam.com', '13800138002', 'F'),
('student1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '王小明', 'STUDENT', 'student1@exam.com', '13800138003', 'M'),
('student2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '刘小红', 'STUDENT', 'student2@exam.com', '13800138004', 'F'),
('student3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '陈大伟', 'STUDENT', 'student3@exam.com', '13800138005', 'M');

-- 插入班级数据
INSERT INTO `class` (`name`, `grade`, `profession`) VALUES
('软件工程2021级1班', '2021级', '软件工程'),
('计算机科学2021级1班', '2021级', '计算机科学与技术'),
('软件工程2022级1班', '2022级', '软件工程');

-- 插入学生班级关联（假设student1-3在ID为1-3的班级）
INSERT INTO `student_class` (`student_id`, `class_id`) VALUES
(4, 1),  -- student1 在 软件工程2021级1班
(5, 1),  -- student2 在 软件工程2021级1班
(6, 2);  -- student3 在 计算机科学2021级1班

-- 插入示例题目数据
-- 单选题
INSERT INTO `question` (`type`, `subject`, `content`, `options`, `answer`, `difficulty`, `teacher_id`) VALUES
('SINGLE', 'Java', 'Java中，以下哪个关键字用于定义常量？', '["A. static", "B. final", "C. const", "D. constant"]', 'B', 'EASY', 2),
('SINGLE', 'Java', '下列哪个是Object类的方法？', '["A. clone()", "B. finalize()", "C. toString()", "D. 以上都是"]', 'D', 'MEDIUM', 2),
('SINGLE', 'Database', 'SQL语句中，用于删除表中数据的关键字是？', '["A. DROP", "B. DELETE", "C. REMOVE", "D. TRUNCATE"]', 'B', 'EASY', 2),
('SINGLE', 'Database', '在关系数据库中，主键的作用是？', '["A. 唯一标识一条记录", "B. 创建索引", "C. 提高查询速度", "D. 以上都是"]', 'D', 'MEDIUM', 2);

-- 判断题
INSERT INTO `question` (`type`, `subject`, `content`, `options`, `answer`, `difficulty`, `teacher_id`) VALUES
('JUDGE', 'Java', 'Java是一种纯面向对象的编程语言。', '["正确", "错误"]', '正确', 'EASY', 2),
('JUDGE', 'Java', 'Java中的接口可以包含方法的实现。', '["正确", "错误"]', '正确', 'MEDIUM', 2),
('JUDGE', 'Database', 'SQL语句对大小写不敏感。', '["正确", "错误"]', '正确', 'EASY', 2);

-- 填空题
INSERT INTO `question` (`type`, `subject`, `content`, `options`, `answer`, `difficulty`, `teacher_id`) VALUES
('BLANK', 'Java', 'Java中，用____关键字来实现继承。', NULL, 'extends', 'EASY', 2),
('BLANK', 'Java', 'Java中，____关键字用于实现接口。', NULL, 'implements', 'EASY', 2),
('BLANK', 'Database', 'SQL中，____语句用于查询数据。', NULL, 'SELECT', 'EASY', 2);

-- 简答题
INSERT INTO `question` (`type`, `subject`, `content`, `options`, `answer`, `difficulty`, `teacher_id`) VALUES
('ESSAY', 'Java', '请简述Java面向对象的三大特征。', NULL, '封装、继承、多态。封装是将数据和操作数据的方法封装在一起；继承是子类继承父类的属性和方法；多态是同一个方法可以有不同的实现。', 'MEDIUM', 2),
('ESSAY', 'Database', '请简述数据库事务的ACID特性。', NULL, 'A原子性：事务不可分割；C一致性：事务前后数据保持一致；I隔离性：多个事务互不影响；D持久性：事务提交后永久保存。', 'HARD', 2);

-- 插入示例试卷
INSERT INTO `paper` (`name`, `subject`, `total_score`) VALUES
('Java基础测试卷', 'Java', 100),
('数据库基础测试卷', 'Database', 100);

-- 试卷1的题目（Java基础测试卷）
INSERT INTO `paper_question` (`paper_id`, `question_id`, `score`, `sort`) VALUES
(1, 1, 10, 1),   -- 单选题1
(1, 2, 10, 2),   -- 单选题2
(1, 5, 10, 3),   -- 判断题1
(1, 6, 10, 4),   -- 判断题2
(1, 8, 10, 5),   -- 填空题1
(1, 9, 10, 6),   -- 填空题2
(1, 11, 40, 7);  -- 简答题1

-- 试卷2的题目（数据库基础测试卷）
INSERT INTO `paper_question` (`paper_id`, `question_id`, `score`, `sort`) VALUES
(2, 3, 15, 1),   -- 单选题3
(2, 4, 15, 2),   -- 单选题4
(2, 7, 10, 3),   -- 判断题3
(2, 10, 10, 4),  -- 填空题3
(2, 12, 50, 5);  -- 简答题2

-- 插入示例考试
INSERT INTO `exam` (`paper_id`, `exam_name`, `start_time`, `end_time`, `duration`, `class_id`, `allow_makeup`, `password`) VALUES
(1, 'Java基础期中考试', '2024-12-01 09:00:00', '2024-12-01 11:00:00', 120, 1, 0, NULL),
(2, '数据库基础考试', '2024-12-05 14:00:00', '2024-12-05 16:00:00', 120, NULL, 1, '123456');
