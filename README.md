# Library Management System (v3.0.0 - Web Service Edition)

这是一个基于 **Java 17** 和 **Spring Boot 3** 的现代化、前后端分离的图书管理系统。项目旨在演示和学习构建一个完整的全栈应用，其核心特性包括：

*   **前后端分离架构**: 项目被清晰地划分为 `backend` (后端服务) 和 `frontend` (前端应用) 两个独立模块。
*   **RESTful API**: 后端提供了一套标准的、无状态的 RESTful API，用于对图书资源进行 CRUD (创建、读取、更新、删除) 操作。
*   **多数据库支持**: 后端服务通过 API 参数动态支持两种完全不同的数据库：
    1.  **MongoDB**: 通过 **Spring Data MongoDB** 实现。
    2.  **MySQL**: 通过 **Spring Data JPA** 和 **Hibernate** 实现。
*   **现代化后端技术栈**: 采用 Spring Boot 3，利用自动配置、依赖注入和分层架构（Controller-Service-Repository）构建了一个健壮、可扩展的后台服务。
*   **简单的前端实现**: 提供了一个基于原生 HTML、CSS 和 JavaScript 的单页应用，通过 `fetch` API 与后端进行异步数据交互。

---

## 🚀 技术栈 (Tech Stack)

### Backend
*   **语言**: Java 17
*   **核心框架**: Spring Boot 3.2.5
*   **Web 框架**: Spring MVC
*   **数据访问**: Spring Data JPA, Spring Data MongoDB
*   **数据库**: MongoDB, MySQL 8.0+
*   **构建工具**: Apache Maven

### Frontend
*   **语言**: HTML, CSS, JavaScript (ES6+)
*   **核心技术**: DOM API, Fetch API

---

## 🏛️ 项目结构

```
library-manager-system/
├── backend/         # Spring Boot 后端服务 (Maven Module)
│   ├── src/
│   └── pom.xml
├── frontend/        # 静态前端应用 (Content Module)
│   └── index.html
└── README.md
```

---

## 🛠️ 环境准备 (Prerequisites)

1.  **JDK 17**: 必须安装 Java 17 或更高版本。
2.  **Maven**: 必须安装 Maven 并配置好环境变量。
3.  **数据库服务**:
    *   拥有一个可访问的 **MongoDB** 实例，并获取其连接字符串。
    *   拥有一个可访问的 **MySQL** 数据库实例，并记下其 URL、用户名和密码。
4.  **现代浏览器**: 如 Chrome, Firefox, Edge。
5.  **(可选) API 测试工具**: 如 Postman (需安装 Desktop Agent 以测试 localhost)。

---

## ⚙️ 配置 (Configuration)

### Backend 配置

1.  导航到 `backend/src/main/resources/` 目录。
2.  **复制** `application.properties.template` 文件并**重命名**为 `application.properties`。
3.  打开新的 `application.properties` 文件，根据注释提示，填写你自己的 MongoDB 和 MySQL 连接信息。

### MySQL 手动建表

在首次运行 MySQL 模式之前，**必须**手动连接到你的 MySQL 数据库，并执行以下 SQL 语句来创建 `books` 表：

```sql
CREATE TABLE books (
    id INT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255)
);
```

---

## ▶️ 如何运行 (How to Run)

你需要**同时运行后端服务和前端页面**。

### 1. 运行 Backend 服务

*   **使用 IntelliJ IDEA (推荐)**:
    1.  打开 IntelliJ IDEA，选择 `File -> Open...`。
    2.  导航到项目中的 `backend` 目录，选中 `pom.xml` 文件并打开。
    3.  等待 Maven 依赖下载完成。
    4.  导航到 `src/main/java/com/nantan/app/LibraryManagerSystemApplication.java`。
    5.  右键点击 `main` 方法并选择 `Run`。
    6.  看到控制台输出 `Tomcat started on port(s): 8080 (http)` 即表示后端服务启动成功。

*   **使用命令行**:
    ```bash
    cd backend
    ./mvnw spring-boot:run
    ```

### 2. 运行 Frontend 应用

1.  在你的文件资源管理器中，找到 `frontend/` 目录。
2.  **直接用你的浏览器打开 `index.html` 文件**。

现在，你可以在打开的浏览器页面中与应用进行交互了！

---

## 📖 API 端点说明

所有 API 的基础路径为 `http://localhost:8080/api/books`。所有端点都接受一个查询参数 `dataSource` (`mysql` 或 `mongo`)，默认为 `mysql`。

*   **获取所有图书**
    *   **URL**: `/`
    *   **方法**: `GET`
    *   **示例**: `GET http://localhost:8080/api/books?dataSource=mongo`

*   **添加一本新书**
    *   **URL**: `/`
    *   **方法**: `POST`
    *   **请求体 (JSON)**: `{"id": 101, "title": "New Book", "author": "Some Author"}`
    *   **示例**: `POST http://localhost:8080/api/books?dataSource=mysql`

*   **删除一本书**
    *   **URL**: `/{id}`
    *   **方法**: `DELETE`
    *   **示例**: `DELETE http://localhost:8080/api/books/101?dataSource=mysql`