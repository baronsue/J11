# Deploying Database to Render

This guide will help you set up a managed PostgreSQL database on Render and connect your application to it.

## Step 1: Create a PostgreSQL Database on Render

1.  Log in to your [Render Dashboard](https://dashboard.render.com/).
2.  Click on the **"New +"** button and select **"PostgreSQL"**.
3.  Fill in the details:
    -   **Name**: `dumpling-house-db` (or any name you prefer)
    -   **Database**: `restaurant` (this matches our default config)
    -   **User**: `restaurant` (this matches our default config)
    -   **Region**: Choose the one closest to you (e.g., Singapore, Oregon).
    -   **PostgreSQL Version**: 15 or 16 (latest stable is fine).
    -   **Instance Type**: "Free" (for development/testing).
4.  Click **"Create Database"**.

## Step 2: Get Connection Details

Once the database is created (it may take a minute), you will see a dashboard for it.

1.  Locate the **"Connections"** section.
2.  Find the **"Internal Database URL"** (for apps running on Render) and **"External Database URL"** (for connecting from your local machine).
3.  Note down the following values from the connection info (or copy the entire URL):
    -   **Hostname** (e.g., `dpg-xxxx-a.oregon-postgres.render.com`)
    -   **Port** (usually `5432`)
    -   **Database Name**
    -   **Username**
    -   **Password**

## Step 3: Configure Your Application

You have two options: running locally with the remote DB, or deploying the app to Render.

### Option A: Running Locally with Remote DB

You can run your Spring Boot app locally but connect to the Render database.

1.  Open your terminal.
2.  Set the environment variables with the **External Connection Details**:

    ```bash
    export DB_HOST=your-external-hostname.render.com
    export DB_PORT=5432
    export DB_NAME=restaurant
    export DB_USER=restaurant
    export DB_PASSWORD=your-password
    ```

3.  Run the application:

    ```bash
    ./mvnw spring-boot:run
    ```

    *Note: The first time you run it, Flyway will automatically create the tables and insert the initial data.*

### Option B: Deploying the App to Render

1.  Create a new **Web Service** on Render, pointing to your GitHub repository.
2.  In the **Environment Variables** section of the Web Service, add the following (use **Internal Connection Details** for better performance):
    -   `DB_HOST`: (Internal Hostname)
    -   `DB_PORT`: `5432`
    -   `DB_NAME`: `restaurant`
    -   `DB_USER`: `restaurant`
    -   `DB_PASSWORD`: (Your Password)
    -   `JWT_SECRET`: (Generate a strong random string)
3.  Deploy the service.

## Step 4: Verification

Once connected, the application will automatically:
1.  Create all necessary tables (`restaurants`, `customers`, etc.) via Flyway `V1__init_schema.sql`.
2.  Populate initial data (Xi'an, Beijing, Chengdu restaurants) via `V2__insert_initial_data.sql`.

You can verify this by using a database tool (like DBeaver or pgAdmin) to connect to the **External Database URL** and checking if the tables exist and contain data.
