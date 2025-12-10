# Render 数据库部署指南

本指南将帮助您在 Render 上设置托管的 PostgreSQL 数据库，并将您的应用程序连接到该数据库。

## 第一步：在 Render 上创建 PostgreSQL 数据库

1.  登录您的 [Render 控制台](https://dashboard.render.com/)。
2.  点击 **"New +"** 按钮并选择 **"PostgreSQL"**。
3.  填写详细信息：
    -   **Name**: `dumpling-house-db` (或者您喜欢的任何名字)
    -   **Database**: `restaurant` (这与我们的默认配置匹配)
    -   **User**: `restaurant` (这与我们的默认配置匹配)
    -   **Region**: 选择离您最近的区域 (例如 Singapore, Oregon)。
    -   **PostgreSQL Version**: 15 或 16 (最新的稳定版即可)。
    -   **Instance Type**: "Free" (用于开发/测试)。
4.  点击 **"Create Database"**。

## 第二步：获取连接详情

数据库创建完成后（可能需要一分钟），您将看到该数据库的仪表板。

1.  找到 **"Connections"** (连接) 部分。
2.  找到 **"Internal Database URL"** (用于在 Render 上运行的应用) 和 **"External Database URL"** (用于从您的本地机器连接)。
3.  记下以下信息（或者复制完整的 URL）：
    -   **Hostname** (主机名，例如 `dpg-xxxx-a.oregon-postgres.render.com`)dpg-d4sj809r0fns73a7rvj0-aoregon-postgres.render.com
    dpg-d4sj809r0fns73a7rvj0-a
    -   **Port** (端口，通常是 `5432`)
    -   **Database Name** (数据库名)restaurant_xxk0
    -   **Username** (用户名)restaurant
    -   **Password** (密码)PjbpcJaAw4OoTlSDuRdH3GxdIjdKMTli

## 第三步：配置您的应用程序

您有两个选择：在本地运行并连接远程数据库，或者将应用程序部署到 Render。

### 选项 A：本地运行并连接远程数据库

您可以在本地运行 Spring Boot 应用，但连接到 Render 上的数据库。

1.  打开您的终端。
2.  设置环境变量，使用 **External Connection Details** (外部连接详情)：

    ```bash
    # 请替换为您的实际值
    export DB_HOST=your-external-hostname.render.com
    export DB_PORT=5432
    export DB_NAME=restaurant
    export DB_USER=restaurant
    export DB_PASSWORD=your-password
    ```

3.  运行应用程序：

    ```bash
    ./mvnw spring-boot:run
    ```

    *注意：首次运行时，Flyway 会自动创建数据库表并插入初始数据。*

### 选项 B：将应用部署到 Render

1.  在 Render 上创建一个新的 **Web Service**，指向您的 GitHub 仓库。
2.  在 Web Service 的 **Environment Variables** (环境变量) 部分，添加以下变量（为了更好的性能，请使用 **Internal Connection Details**）：
    -   `DB_HOST`: (Internal Hostname)
    -   `DB_PORT`: `5432`
    -   `DB_NAME`: `restaurant`
    -   `DB_USER`: `restaurant`
    -   `DB_PASSWORD`: (您的密码)
    -   `JWT_SECRET`: (生成一个复杂的随机字符串作为密钥)
3.  部署服务。

## 第四步：验证

一旦连接成功，应用程序会自动执行以下操作：
1.  通过 Flyway 脚本 `V1__init_schema.sql` 创建所有必要的表（`restaurants`, `customers` 等）。
2.  通过 Flyway 脚本 `V2__insert_initial_data.sql` 填充初始数据（西安、北京、成都的餐厅数据）。

您可以通过使用数据库工具（如 DBeaver 或 pgAdmin）连接到 **External Database URL** 来验证表是否已创建且包含数据。
