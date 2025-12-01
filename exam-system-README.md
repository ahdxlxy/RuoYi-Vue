# 在线考试系统 - 使用指南

## 项目说明

这是一个完整的在线考试系统，包含前后端分离架构，实现了用户管理、班级管理、题库管理、试卷管理、考试管理、在线考试、自动判卷和成绩管理等核心功能。

## 技术栈

### 后端
- Spring Boot 3.2.0
- MyBatis-Plus 3.5.5
- Spring Security + JWT
- MySQL 8.0+
- JDK 17+

### 前端
- Vue 3.4.0
- Vite 5.0.0
- Element Plus 2.5.0
- Pinia 2.1.7
- Axios 1.6.2

## 快速开始

### 1. 数据库配置

1. 创建数据库并执行SQL脚本：
```bash
mysql -u root -p < exam-system-backend/src/main/resources/sql/schema.sql
mysql -u root -p < exam-system-backend/src/main/resources/sql/init-data.sql
```

2. 修改后端配置文件 `application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/exam_system
    username: root
    password: 123456
```

### 2. 启动后端

```bash
cd exam-system-backend
mvn clean install
mvn spring-boot:run
```

后端将运行在 `http://localhost:8080/api`

### 3. 启动前端

```bash
cd exam-system-frontend
npm install
npm run dev
```


前端将运行在 `http://localhost:3000`

## 默认账号

- **管理员**: `admin` / `admin123`
- **教师**: `teacher1` / `admin123`
- **学生**: `student1` / `admin123`

## 主要功能

### 管理员功能
- ✅ 用户管理（增删改查）
- ✅ 班级管理
- ✅ 题库管理
- ✅ 试卷管理（手动组卷）
- ✅ 考试管理
- ✅ 成绩管理和阅卷

### 教师功能
- ✅ 班级管理
- ✅ 题库管理
- ✅ 试卷管理
- ✅ 考试管理
- ✅ 成绩管理和阅卷

### 学生功能
- ✅ 查看可参加的考试
- ✅ 在线答题（支持倒计时、自动保存）
- ✅ 查看成绩

## 核心特性

### 1. 自动判卷
- 单选题、判断题：提交后立即自动判分
- 填空题：字符串精确匹配自动判分
- 简答题：需要教师人工阅卷

### 2. 考试功能
- 倒计时功能
- 自动保存答案（失焦时保存）
- 时间到自动提交
-考试密码保护（可选）
- 班级限制（可选）

### 3. 权限控制
- 基于JWT的身份认证
- Spring Security权限管理
- 前端路由守卫

## 项目结构

### 后端结构
```
exam-system-backend/
├── src/main/java/com/exam/
│   ├── config/           # 配置类
│   ├── controller/       # 控制器
│   ├── dto/             # 数据传输对象
│   ├── entity/          # 实体类
│   ├── mapper/          # MyBatis Mapper
│   ├── service/         # 服务层
│   ├── vo/              # 视图对象
│   ├── common/          # 公共类
│   ├── security/        # 安全配置
│   └── utils/           # 工具类
└── src/main/resources/
    ├── sql/             # SQL脚本
    └── application.yml  # 配置文件
```

### 前端结构
```
exam-system-frontend/
├── src/
│   ├── api/             # API接口
│   ├── components/      # 公共组件
│   ├── router/          # 路由配置
│   ├── stores/          # 状态管理
│   ├── utils/           # 工具函数
│   ├── views/           # 页面组件
│   ├── App.vue
│   └── main.js
├── index.html
├── vite.config.js
└── package.json
```

## API文档

所有API返回格式统一：
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

详细API列表请参考各Controller类的注释。

## 注意事项

1. 确保MySQL数据库版本为8.0+
2. 确保JDK版本为17+
3. 首次运行需要执行数据库初始化脚本
4. 前端开发时，代理配置已在vite.config.js中设置
5. 生产环境部署时需要修改JWT密钥

## 开发建议

1. 后端修改后需重启服务
2. 前端使用热重载，修改后自动刷新
3. 数据库密码等敏感信息不要提交到版本控制
4. 建议使用环境变量管理配置

## 许可证

MIT License
