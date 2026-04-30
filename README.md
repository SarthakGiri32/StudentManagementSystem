# Student Management System App
This repo is the Final Internship project for Clinchsoft Technologies

## Project Purpose
1. To demonstrate my understanding of connecting and executing SQL queries on a  
MySQL database through the Java Programming Language.
2. To create a Swing GUI based Student Management System that allows a user to perform   
CRUD operations on a database, to search for specific students and to display student  
statistics.

## Application usage instructions
1. [How to set up the database](#Database-Setup)
2. [How to run the application](#Application-Usage-Guide)

## Database Setup
### I) Installing MySQL Community Edition on Windows 11

---

#### Step 1: Download the Installer
1. Go to **https://dev.mysql.com/downloads/installer/**
2. Choose **"MySQL Installer for Windows"**
3. Select the **full offline installer** (`mysql-installer-community-x.x.x.msi` — the larger file ~450MB) for a complete package, or the web installer for a smaller download
4. Click **"No thanks, just start my download"** to skip login

---

#### Step 2: Run the Installer
1. Double-click the downloaded `.msi` file
2. If prompted by **User Account Control (UAC)**, click **Yes**

---

#### Step 3: Choose Setup Type
You'll be asked to select a setup type:

| Type                  | Best For                                                   |
|-----------------------|------------------------------------------------------------|
| **Developer Default** | Developers (installs Server, Workbench, Shell, connectors) |
| **Server Only**       | Just the database server                                   |
| **Full**              | Everything                                                 |
| **Custom**            | Pick and choose components                                 |

> ✅ **Recommended:** Choose **Developer Default** for most use cases

---

#### Step 4: Check Requirements & Install
1. The installer checks for required dependencies (like **Visual C++ Redistributable**)
2. Click **Execute** to install any missing prerequisites automatically
3. Once ready, click **Next → Execute** to install the selected MySQL products
4. Wait for all components to show ✅ **Complete**, then click **Next**

---

#### Step 5: Configure MySQL Server
The configuration wizard will launch:

A. Config Type & Connectivity
- Type: `Development Computer`
- Port: `3306` (default — keep as is)
- Click **Next**

B. Authentication Method
- Choose **"Use Strong Password Encryption"** (recommended)
- Click **Next**

C. Accounts and Roles

**Set Root Password**
- Enter a **strong password** for the `root` account — **save this somewhere safe!**
- Click **Next** only after confirming the password

**Add a MySQL User Account** *(optional but recommended)*
1. Click **"Add User"** button below the root password section
2. Fill in the details:

    | Field              | Description                                                                        |
    |--------------------|------------------------------------------------------------------------------------|
    | **Username**       | e.g., `myuser`                                                                     |
    | **Host**           | `localhost` (local access only) or `%` (remote access)                             |
    | **Role**           | `DB Admin` for full access, or `DB Designer` / `Backup Admin` for restricted roles |
    | **Authentication** | `MySQL` (default)                                                                  |
    | **Password**       | Set and confirm a strong password                                                  |

3. Click **OK** to add the user
4. Repeat to add more users if needed
5. Click **Next** when done

> 💡 **Tip:** It's good practice to avoid using the `root` account for day-to-day tasks. Create a dedicated user with only the permissions your application needs.

D. Windows Service
- Leave defaults: MySQL runs as a **Windows Service** starting automatically
- Service name: `MySQL80`
- Click **Next**

E. Server File Permissions *(if shown)*
- Keep default settings → Click **Next**

F. Apply Configuration
- Click **Execute** — the wizard applies all settings
- Once all steps show ✅, click **Finish**

---

#### Step 6: Complete Installation
- The installer may configure additional products (MySQL Router, Samples)
- Click through remaining configuration screens with defaults
- Click **Finish** on the final screen

---

#### Step 7: Verify the Installation
Open **Command Prompt** and run:
```bash
mysql -u root -p
```
Enter your root password when prompted. If you see the `mysql>` prompt, **MySQL is running successfully!**

---

#### Step 8: (Optional) Add MySQL to System PATH
If the `mysql` command isn't recognized:
1. Search for **"Environment Variables"** in Start Menu
2. Under **System Variables**, select **Path → Edit**
3. Add: `C:\Program Files\MySQL\MySQL Server 8.0\bin`
4. Click **OK** and restart Command Prompt

---

#### 🛠 Useful Tools Installed Alongside
- **MySQL Workbench** — GUI client for managing databases visually
- **MySQL Shell** — Advanced command-line client
- **MySQL Notifier** — System tray tool to start/stop the service

We will be using MySQL Workbench to interact with the `student` database table

### II) Database Setup in MySQL Workbench
#### Step 1: Open MySQL Workbench

- Search for `MySQL Workbench` in windows search and open the app
- You will be able to see the Home Screen similar to the screenshot below:
    ![Home_Page.png](README_Application_Screenshots/MySQL_Workbench/Home_Page.png)
- Click on the `+` option in the `MySQL Connections` section:
    ![Home_Page_2.png](README_Application_Screenshots/MySQL_Workbench/Home_Page_2.png)
- Enter the name of the connection in the `Connection name` field in the `Setup New Connection` dialog box:
    ![Home_Page_3.png](README_Application_Screenshots/MySQL_Workbench/Home_Page_3.png)
- Don't edit any other fields, go to the `Password` field and click on the `Store in vault ...` option:
    ![Home_Page_4.png](README_Application_Screenshots/MySQL_Workbench/Home_Page_4.png)
- Enter the root password you configured during MySQL installation in the `Password` field in the `Store Password for Connection` dialog box, and click on `Ok` to store the password for future use:
    ![Home_Page_5.png](README_Application_Screenshots/MySQL_Workbench/Home_Page_5.png)
- Click on the `Test Connection` option to test the connection to the local MySQL Database. You should be able to see a `MySQL Workbench` dialog box stating that a successful connection was made. Click on `Ok` to dismiss the dialog box:
    ![Home_Page_6.png](README_Application_Screenshots/MySQL_Workbench/Home_Page_6.png)
    ![Home_Page_7.png](README_Application_Screenshots/MySQL_Workbench/Home_Page_7.png)
- Click on `Ok` in the `Setup New Connection` dialog box to connect to the local database server:
    ![Home_Page_8.png](README_Application_Screenshots/MySQL_Workbench/Home_Page_8.png)
- After scrolling down in the home page, you will be able to see a new connection created in the `MySQL Connections` section. Click on the connection and a new tab will open:
    ![Home_Page_9.png](README_Application_Screenshots/MySQL_Workbench/Home_Page_9.png)
    ![Page_1.png](README_Application_Screenshots/MySQL_Workbench_Connection_Tab/Page_1.png)
- In this page, you can create a new schema by left-clicking on the empty space in the `SCHEMAS` tab and selecting the `Create Schema...` option:
    ![Page_2.png](README_Application_Screenshots/MySQL_Workbench_Connection_Tab/Page_2.png)
- A new tab will open in the query editing section. Enter the name of the schema in the `Name` field, don't change the other option fields, and click on the `Apply` button:
    ![Page_3.png](README_Application_Screenshots/MySQL_Workbench_Connection_Tab/Page_3.png)
- In the `Apply SQL Script to Database` dialog box, leave the `Online DDL` options unchanged, and click on the `Apply` button:
    ![Page_4.png](README_Application_Screenshots/MySQL_Workbench_Connection_Tab/Page_4.png)
- The schema should be created successfully. Click on the `Finish` button in the `Apply SQL Script to Database` dialog box:
    ![Page_5.png](README_Application_Screenshots/MySQL_Workbench_Connection_Tab/Page_5.png)
- Please ensure that the name of the schema is `studentdb`, since that is used in the url to connect to the database from the Student Management System App
- Click on the `studentdb` schema listed in the `SCHEMAS` tab:
    ![Page_6.png](README_Application_Screenshots/MySQL_Workbench_Connection_Tab/Page_6.png)
    ![Page_7.png](README_Application_Screenshots/MySQL_Workbench_Connection_Tab/Page_7.png)
- Under the `studentdb` schema option, you will be able to see all the tables created under this schema
- Click on the `File` menu option in the top menu bar, then select the `Open SQL Script...` option:
    ![Page_8.png](README_Application_Screenshots/MySQL_Workbench_Connection_Tab/Page_8.png)
    ![Page_9.png](README_Application_Screenshots/MySQL_Workbench_Connection_Tab/Page_9.png)
- In the `Open SQL Script` dialog box, locate the `Create_Student_Table.sql` file from the App's code base (in the `\resources` folder), single-click to select the file, and click on the `Open` button:
    ![Page_10.png](README_Application_Screenshots/MySQL_Workbench_Connection_Tab/Page_10.png)
- The SQL query for creating the `student` table will open in the query editing section. Click on the little lightning symbol (similar to '⚡') in the upper section of the query editing tab to execute the SQL query:
    ![Page_11.png](README_Application_Screenshots/MySQL_Workbench_Connection_Tab/Page_11.png)
- If the `student` table already exists in the `studentdb` schema, then a warning message will be shown in the `Action Output` section below the query editing tab:
```genericsql
17:45:10	CREATE TABLE IF NOT EXISTS student (     id INT AUTO_INCREMENT PRIMARY KEY,     name VARCHAR(100) UNIQUE NOT NULL,     roll_no VARCHAR(15) UNIQUE NOT NULL,     department VARCHAR(100) NOT NULL,     email VARCHAR(100) UNIQUE NOT NULL,     phone VARCHAR(15) UNIQUE NOT NULL,     marks INT NOT NULL,     grade CHAR(1) NOT NULL )	0 row(s) affected, 1 warning(s): 1050 Table 'student' already exists	0.000 sec
```
- You can click on the `studentdb` schema in the `SCHEMAS` tab, then click on the `Tables` option to see the created `student` table. If the table is not visible, you can refresh the `SCHEMAS` tab by left-clicking in the empty space of this tab and selecting the `Refresh All` option:
    ![Page_12.png](README_Application_Screenshots/MySQL_Workbench_Connection_Tab/Page_12.png)
- To see the content of the `student` table, you can hover over the `student` table name under the `Tables` option in the `SCHEMAS` tab, then click on the right-most of the 3 icons beside the table name:
    ![Page_13.png](README_Application_Screenshots/MySQL_Workbench_Connection_Tab/Page_13.png)
- I already have some record rows in the table, which you can see in the `Result Grid` tab. If there is no data in the table, an empty row will be displayed below the column header row filled with `null` values:
    ![Page_14.png](README_Application_Screenshots/MySQL_Workbench_Connection_Tab/Page_14.png)

## Application Usage Guide
### I) Setting up the code base
