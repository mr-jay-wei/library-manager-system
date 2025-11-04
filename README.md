# Library Management System API (v3.0.0)

这是一个基于 **Java 17** 和 **Spring Boot 3** 构建的、生产级的 RESTful API 服务。项目从一个简单的命令行工具演进而来，全面展示了现代企业级 Java 后端服务的核心架构与最佳实践。

## ✨ 核心特性

*   **分层架构**: 清晰的 Controller, Service, Repository 三层架构，职责分明，易于维护。
*   **双数据库支持**: 通过 Spring Data JPA (MySQL) 和 Spring Data MongoDB，在业务层实现了对关系型和 NoSQL 数据库的动态切换。
*   **RESTful API**: 提供了一套设计良好、无状态的 RESTful API，用于对图书资源进行 CRUD 操作。
*   **统一响应格式**: 所有 API 响应都被封装在标准的 `ApiResponse` 对象中，提供了统一的成功和失败数据结构。
*   **全局异常处理**: 通过 `@RestControllerAdvice` 实现了全局异常捕获，确保了 API 的健壮性和友好的错误反馈。
*   **环境分离配置**: 使用 Spring Profiles 将开发 (`dev`) 和生产 (`prod`) 环境的配置完全分离。
*   **外部化敏感配置**: 数据库密码等敏感信息通过**环境变量**注入，遵循了安全开发规范，避免了机密泄露。
*   **专业日志系统**: 集成 SLF4J + Logback，通过 `logback-spring.xml` 实现了结构化、分级别、环境感知的日志记录。
*   **自动化 API 文档**: 集成 `springdoc-openapi`，自动生成交互式的 Swagger UI 文档，实现了“代码即文档”。

---

## 🚀 技术栈 (Tech Stack)

*   **语言**: Java 17
*   **核心框架**: Spring Boot 3.2.5
*   **Web**: Spring MVC
*   **数据访问**: Spring Data JPA, Spring Data MongoDB
*   **数据库驱动**: MySQL Connector/J, MongoDB Sync Driver
*   **API 文档**: SpringDoc OpenAPI 3.0 (Swagger UI)
*   **构建工具**: Apache Maven
*   **日志**: SLF4J, Logback

---

## 🛠️ 环境准备 (Prerequisites)

1.  **JDK 17**: 必须安装 Java 17 或更高版本。
2.  **Maven**: 必须安装 Maven 3.6+。
3.  **数据库服务**:
    *   一个可访问的 MongoDB 实例。
    *   一个可访问的 MySQL 8.0+ 实例。
4.  **开发工具**: 推荐使用 IntelliJ IDEA。

---

## ⚙️ 快速启动 (Quick Start)

### 1. 克隆项目

```bash
git clone <repository_url>
cd library-manager-system/backend
```

### 2. 配置环境变量

本项目将敏感配置（如数据库密码）外部化到了环境变量中。在运行应用之前，你必须在你的操作系统或 IDE 的运行配置中设置以下环境变量：

*   `MONGO_DEV_PASSWORD`: 你的开发环境 MongoDB 数据库密码。
*   `MYSQL_DEV_PASSWORD`: 你的开发环境 MySQL 数据库密码。

### 3. 在 IntelliJ IDEA 中配置

1.  打开 IntelliJ IDEA，选择 **File -> Open...**，然后选择项目根目录下的 `backend/pom.xml` 文件。
2.  等待 Maven 下载所有依赖。

### 4. 运行应用

*   在项目视图中，找到 `LibraryManagerSystemApplication.java`。
*   右键点击 `main` 方法并选择 **Run 'LibraryManagerSystemApplication.main()'**。
*   应用将在 `dev` profile 下启动，并监听 `8080` 端口。

### 5. 手动建表 (MySQL)

在首次运行 MySQL 模式之前，**必须**手动连接到你的 MySQL 数据库，并执行以下 SQL 语句来创建 `books` 表：

```sql
CREATE TABLE books (
    id INT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255)
);
```

---

## API 文档与测试

应用启动后，你可以通过以下方式访问和测试 API：

### 访问交互式 API 文档 (Swagger UI)

打开浏览器并访问：
> **http://localhost:8080/swagger-ui.html**

你可以在这个页面上看到所有 API 的详细文档，并直接进行在线测试。

### 使用 API 测试工具 (如 Postman)

*   **获取所有图书 (MySQL)**:
    *   `GET http://localhost:8080/api/books?dataSource=mysql`
*   **添加一本新书 (MongoDB)**:
    *   `POST http://localhost:8080/api/books?dataSource=mongo`
    *   **Headers**: `Content-Type: application/json`
    *   **Body**: `{"id": 101, "title": "Spring in Action", "author": "Craig Walls"}`
*   **删除一本书 (MySQL)**:
    *   `DELETE http://localhost:8080/api/books/101?dataSource=mysql`

---

## 🏛️ 项目架构 (Architecture)

*   **`controller`**: 包含 `BookController`，负责处理 HTTP 请求，是 API 的入口。
*   **`service`**: 包含 `BookService` 接口和 `BookServiceImpl` 实现，封装了核心业务逻辑。
*   **`jpa` & `mongo`**: 分别包含 `BookJpaRepository` 和 `BookMongoRepository`，由 Spring Data 自动实现，负责与数据库交互。
*   **`dto`**: 包含 `ApiResponse` 类，定义了 API 响应的统一数据结构。
*   **`exception`**: 包含 `GlobalExceptionHandler`，用于全局捕获异常并返回标准化的错误响应。
*   **`resources`**:
    *   `application.properties`: 通用配置和默认 profile 设置。
    *   `application-dev.properties`: 开发环境专用配置。
    *   `application-prod.properties`: 生产环境专用配置。
    *   `logback-spring.xml`: Logback 日志框架的详细配置。