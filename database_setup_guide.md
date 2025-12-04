# 数据库初始化指南

## 问题原因

你看到"用户名或密码错误"是因为数据库中还没有创建表和初始数据。

## 解决方案

按照以下步骤初始化数据库：

### 步骤 1: 确认 MySQL 配置

检查你的 MySQL 配置（在 [application.yml](file:///d:/26582/Desktop/%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AF%BE%E8%AE%BE/%E8%80%83%E8%AF%95%E7%B3%BB%E7%BB%9F/exam-system-backend/src/main/resources/application.yml) 中）：
- 数据库名：`exam_system`
- 用户名：`root`
- 密码：`123456`
- 端口：`3306`

**如果你的 MySQL 密码不是 `123456`，请修改 [application.yml](file:///d:/26582/Desktop/%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AF%BE%E8%AE%BE/%E8%80%83%E8%AF%95%E7%B3%BB%E7%BB%9F/exam-system-backend/src/main/resources/application.yml) 中的密码！**

### 步骤 2: 执行数据库脚本

有两个 SQL 文件需要执行：

1. **创建表结构**: [src/main/resources/sql/schema.sql](file:///d:/26582/Desktop/%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AF%BE%E8%AE%BE/%E8%80%83%E8%AF%95%E7%B3%BB%E7%BB%9F/exam-system-backend/src/main/resources/sql/schema.sql)
2. **插入初始数据**: [src/main/resources/sql/init-data.sql](file:///d:/26582/Desktop/%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AF%BE%E8%AE%BE/%E8%80%83%E8%AF%95%E7%B3%BB%E7%BB%9F/exam-system-backend/src/main/resources/sql/init-data.sql)

#### 方法 1：使用 MySQL 命令行

```bash
# 1. 登录 MySQL
mysql -u root -p

# 2. 执行建表脚本
source D:/26582/Desktop/数据库课设/考试系统/exam-system-backend/src/main/resources/sql/schema.sql

# 3. 执行数据初始化脚本
source D:/26582/Desktop/数据库课设/考试系统/exam-system-backend/src/main/resources/sql/init-data.sql

# 4. 退出
exit;
```

#### 方法 2：使用 Navicat/MySQL Workbench 等图形化工具

1. 打开你的 MySQL 客户端
2. 连接到 MySQL 服务器
3. 依次打开并执行：
   - [schema.sql](file:///d:/26582/Desktop/%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AF%BE%E8%AE%BE/%E8%80%83%E8%AF%95%E7%B3%BB%E7%BB%9F/exam-system-backend/src/main/resources/sql/schema.sql) （创建数据库和表）
   - [init-data.sql](file:///d:/26582/Desktop/%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AF%BE%E8%AE%BE/%E8%80%83%E8%AF%95%E7%B3%BB%E7%BB%9F/exam-system-backend/src/main/resources/sql/init-data.sql) （插入测试数据）

### 步骤 3: 验证数据

执行完成后，检查数据是否插入成功：

```sql
USE exam_system;
SELECT * FROM user;
```

应该看到 6 个用户：
- admin（管理员）
- teacher1, teacher2（教师）
- student1, student2, student3（学生）

### 步骤 4: 登录测试

现在你可以使用以下账号登录了：

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 教师 | teacher1 | admin123 |
| 教师 | teacher2 | admin123 |
| 学生 | student1 | admin123 |
| 学生 | student2 | admin123 |
| 学生 | student3 | admin123 |

> 注意：所有默认密码都是 `admin123`

## 数据库说明

### 创建的表：

1. `user` - 用户表
2. `class` - 班级表
3. `student_class` - 学生班级关联表
4. `question` - 题库表
5. `paper` - 试卷表
6. `paper_question` - 试卷题目关联表
7. `exam` - 考试表
8. `answer` - 答题记录表
9. `grade` - 成绩表
10. `file` - 文件表

### 初始化的数据：

- **6 个用户**（1管理员 + 2教师 + 3学生）
- **3 个班级**
- **11 道题目**（单选、判断、填空、简答）
- **2 套试卷**（Java基础、数据库基础）
- **2 场考试**

## 常见问题

### Q: 执行 SQL 时报错怎么办？

A: 确保：
1. MySQL 服务已启动
2. 用户名密码正确
3. 有创建数据库的权限

### Q: 还是显示用户名密码错误？

A: 检查：
1. 数据是否成功插入（查询 user 表）
2. 后端应用是否重启
3. 浏览器缓存是否清除

### Q: 密码加密是什么格式？

A: 使用 BCrypt 加密，所有默认密码 `admin123` 加密后的值为：
```
$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi
```
