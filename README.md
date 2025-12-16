## 课程设计：新闻信息管理系统

这是一个新闻信息管理系统，主要面向新闻编辑，实现新闻的发布与维护、分类管理、以及新闻的浏览和检索功能。系统基于软件工程的理论和方法，采用 `Java (Swing)` 技术、数据库采用 `MySQL` 技术设计开发

### 设计要求

1. 需要使用 `Java (Swing)` 技术，不依赖第三方库，实现一个GUI程序
2. 需要使用 `MySQL` 作为数据库
3. 需要实现新闻的标题/内容检索、新闻的增删改查、新闻分类（如按类型、时间等）
4. 需要使用面向对象编程、使用清晰的代码结构和类封装

### 开发指南

#### 1. 环境准备
*   JDK 8 或更高版本
*   MySQL 8.0+
*   将 `mysql-connector-j-8.x.x.jar` 放入 `lib/` 目录

#### 2. 数据库配置
1.  首先在 MySQL 中执行初始化脚本：
    ```bash
    mysql -u root -p < sql/init.sql
    ```
2.  修改配置文件 `src/db.properties`，填入你的数据库真实账号密码：
    ```properties
    db.url=jdbc:mysql://localhost:3306/news_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    db.username=root
    db.password=你的密码
    ```

#### 3. 编译与运行
由于没有使用构建工具（如Maven），请使用以下命令手动编译和运行。

**编译 (Compile):**
```bash
# 确保 bin 目录存在
mkdir -p bin

# 复制配置文件到 classpath (重要!)
cp src/db.properties bin/

# 编译 Java 源代码
javac -d bin -cp "lib/*" -sourcepath src src/App.java
```

**运行 (Run):**
```bash
# Windows (使用分号 ; 分隔)
java -cp "bin;lib/*" App

# macOS / Linux (使用冒号 : 分隔)
java -cp "bin:lib/*" App
```

#### 4. 默认账号
*   **用户名**: `admin`
*   **密码**: `admin123`
