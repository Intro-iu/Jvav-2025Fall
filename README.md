# Jvav News System (2025 Fall)

A **Modern Industrial Minimalist** desktop news management system built with Java Swing and MySQL.
This project demonstrates how to create a high-end, aesthetic, and functional GUI application without relying on third-party UI libraries.

## ✨ Features / 特性

### 🎨 Modern Industrial UI (现代工业风界面)
- **High-End Design**: Dark-themed aesthetics with "Cyberpunk/Industrial" accents (Yellow/Cyan).
- **Custom Components**: Completely rewritten Swing components including Buttons, TextFields, ComboBoxes, and Scrollbars.
- **Undecorated Window**: Custom-drawn application frame with integrated title bar and window controls (Min/Max/Close).

### 📰 Unified News Feed (一体化新闻流)
- **Website-Like Experience**: Browse news in a clean, responsive card layout (`NewsCard`).
- **Seamless Management**: "Edit" and "Delete" buttons are integrated directly into the cards for a "What You See Is What You Get" experience.
- **Smart Formatting**: Automatic handling of Chinese/English mixed fonts.

### 🏷️ Tile-Based Category Management (磁贴式分类管理)
- **Visual Tiles**: Categories managed via an interactive tile grid/list (`CategoryCard`) instead of boring tables.
- **Quick Actions**: Direct edit/delete capabilities on each tile.

### 🛠️ Core Functionality
- **User Authentication**: Secure login with dedicated UI.
- **Search & Filter**: Real-time filtering by Title and Category with "Clear" functionality.
- **Pagination**: Efficient data loading with paged results.
- **Data Persistence**: Robust MySQL integration via JDBC.

---

## 🛠️ Tech Stack (技术栈)

- **Language**: Java (JDK 8+)
- **GUI**: Java Swing (Standard Library only, No flatlaf/substance etc.)
- **Database**: MySQL (5.7 / 8.0)
- **Driver**: MySQL Connector/J

---

## 🚀 Getting Started (快速开始)

### Prerequisites (环境要求)
1.  **JDK**: Installed and configured in PATH.
2.  **MySQL**: Server running.
3.  **Drivers**: Place `mysql-connector-j-*.jar` in `lib/` directory.

### Database Setup (数据库配置)
1.  Connect to your MySQL server.
2.  Run the initialization script located at `sql/init.sql`.
    > This will create the `newssys` database and populate it with mock data (User: `admin`/`123456`).
3.  Verify/Edit `src/db.properties` configuration (created on first run or manually):
    ```properties
    url=jdbc:mysql://localhost:3306/newssys?useSSL=false&characterEncoding=utf8
    username=root
    password=your_password
    ```

### Packaging & Distribution (打包与发布)
To package the application into a JAR file:

1.  **Ensure MySQL Driver is in `lib/`**: `mysql-connector-j-8.2.0.jar` (or match your version in `MANIFEST.MF`).
2.  **Compile**: `javac -d bin -cp "lib/*" -sourcepath src -encoding UTF-8 -Xlint:all src/App.java`
3.  **Package**:
    ```bash
    jar cvfm JvavNews.jar MANIFEST.MF -C bin .
    ```
4.  **Run**:
    - **Windows**: Double-click `run.bat` or run `java -jar JvavNews.jar`.
    - **Note**: Ensure the `lib` folder is in the same directory as the JAR file.

### Directory Structure for Release (发布目录结构)
```
Release/
├── JvavNews.jar       # Executable Application (Single File)
└── run.bat            # Quick Start Script
```

### Build & Run (编译与运行)

**Windows (PowerShell):**

1.  **Compile:**
    ```powershell
    mkdir bin -Force
    javac -d bin -cp "lib/*" -encoding UTF-8 -sourcepath src src/App.java
    ```

2.  **Run:**
    ```powershell
    java -cp "bin;lib/*" App
    ```

---

## 📂 Project Structure (项目结构)

```
Jvav-2025Fall/
├── lib/                 # Dependencies (MySQL Connector)
├── sql/                 # Database scripts (init.sql)
├── src/                 # Source Code
│   ├── App.java         # Entry Point
│   ├── model/           # Data Models (Entity)
│   ├── demo/            # DAO Implementation
│   ├── service/         # Business Logic
│   ├── util/            # Utilities (DB, WindowResizer)
│   └── view/            # GUI Implementation
│       ├── component/   # Custom UI Kit (Buttons, Cards, Dialogs...)
│       ├── theme/       # Theme Constants (Colors, Fonts)
│       ├── HomeView.java
│       ├── MainView.java
│       └── ...
└── README.md
```

## 📝 License
Course Assignment - 2025 Fall.
