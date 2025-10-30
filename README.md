# Library Management System (v2.0.0)

这是一个基于 Java 17 的命令行图书管理系统。项目从一个简单的单体应用演进而来，旨在演示和学习现代 Java 后端开发中的核心概念，包括：

*   **构建工具**: 使用 Apache Maven 进行项目构建和依赖管理。
*   **分层架构**: 清晰地划分用户接口层 (`app.java`)、数据访问接口 (`BookRepository`) 和数据访问实现层 (`DAO`)。
*   **多数据库支持**: 动态支持两种完全不同的数据库后端：
    1.  **MongoDB**: 使用官方 Java Sync Driver，展示 NoSQL 数据库的灵活性和动态模式特性。
    2.  **MySQL**: 使用 **JPA (Java Persistence API)** 和 **Hibernate** 实现，展示 ORM 框架的强大功能和企业级开发标准。
*   **面向接口编程**: 通过 `BookRepository` 接口将业务逻辑与具体的数据库实现完全解耦。

---

## 🚀 技术栈 (Tech Stack)

*   **语言**: Java 17
*   **构建**: Apache Maven 3.8+
*   **数据库**:
    *   MongoDB (通过 MongoDB Atlas 云服务)
    *   MySQL 8.0+ (通过云服务或本地安装)
*   **ORM / 驱动**:
    *   Jakarta Persistence API (JPA) 3.1
    *   Hibernate ORM 6.4+
    *   MongoDB Sync Driver 4.11+
    *   MySQL Connector/J 8.4+
*   **日志**: SLF4J + `java.util.logging`

---

## 🛠️ 环境准备 (Prerequisites)

在运行此项目之前，请确保你的开发环境满足以下要求：

1.  **JDK 17**: 必须安装 Java 17 或更高版本。
2.  **Maven**: 必须安装 Maven，并配置好环境变量。
3.  **Git**: 必须安装 Git。
4.  **IntelliJ IDEA**: 推荐使用最新版的 IntelliJ IDEA 作为开发工具。
5.  **数据库服务**:
    *   **MongoDB Atlas**:
        *   注册一个免费的 MongoDB Atlas 账户。
        *   创建一个集群 (Cluster)。
        *   在 **Network Access** 中，将你当前的公网 IP 地址加入白名单（为方便测试，可添加 `0.0.0.0/0`）。
        *   获取你的数据库连接字符串 (Connection String)。
    *   **MySQL**:
        *   拥有一个可访问的 MySQL 数据库实例（云服务或本地 Docker 均可）。
        *   记下数据库的 URL、端口、数据库名、用户名和密码。

---

## ⚙️ 配置 (Configuration)

**重要**: 本项目使用 `.gitignore` 来忽略包含敏感密码的配置文件。你需要根据提供的模板文件创建自己的本地配置。

### 1. MongoDB 配置

1.  在 `src/main/resources/` 目录下，找到 `application.properties.template` 文件。
2.  **复制**该文件并**重命名**为 `application.properties`。
3.  打开新的 `application.properties` 文件，将 `<password>` 占位符替换为你自己的 MongoDB Atlas 数据库密码。

### 2. MySQL (JPA) 配置

1.  在 `src/main/resources/META-INF/` 目录下，找到 `persistence.xml.template` 文件。
2.  **复制**该文件并**重命名**为 `persistence.xml`。
3.  打开新的 `persistence.xml` 文件，将 `<your_password>` 占位符替换为你自己的 MySQL 数据库密码，并根据需要修改 URL 和用户名。

### 3. 手动建表 (MySQL)

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

1.  **克隆项目**:
    ```bash
    git clone <repository_url>
    cd library-manager-system
    ```

2.  **创建本地配置**:
    *   根据上一节“配置”部分的说明，从 `.template` 文件创建并填写你自己的 `application.properties` 和 `persistence.xml` 文件。

3.  **使用 IntelliJ IDEA 打开**:
    *   打开 IntelliJ IDEA，选择 **File -> Open...**，然后选择项目根目录下的 `pom.xml` 文件。
    *   IDEA 会自动识别并加载 Maven 项目。等待所有依赖下载完成。

4.  **运行主程序**:
    *   在项目视图中，导航到 `src/main/java/com/nantan/app/app.java`。
    *   右键点击 `main` 方法，或点击方法旁的绿色“运行”按钮，选择 **Run 'app.main()'**。

5.  **与程序交互**:
    *   程序启动后，会首先要求你选择使用 MongoDB (`1`) 还是 MySQL (`2`)。
    *   选择后，即可根据菜单提示进行图书的增、删、查等操作。

---

## 🏛️ 项目架构 (Architecture)

*   **`app.java`**: 应用程序的入口和用户交互层。
*   **`Book.java`**: 核心实体类，同时作为 POJO 和 JPA Entity。
*   **`BookRepository.java`**: 数据访问接口，定义了数据操作的契约。
*   **`BookMongoDAO.java`**: `BookRepository` 的 MongoDB 实现。
*   **`BookJpaDAO.java`**: `BookRepository` 的 JPA (MySQL) 实现。
*   **`MongoManager.java`**: 管理 MongoDB 连接的单例类。
*   **`JpaManager.java`**: 管理 JPA `EntityManagerFactory` 的单例类。