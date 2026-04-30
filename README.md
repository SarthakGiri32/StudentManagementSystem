# Student Management System App
This repo is the Final Internship project for Clinchsoft Technologies

To see the screenshot of the working app go to this section: [II) Launch, Navigation and Using All App Features](#II-Launch-Navigation-and-Using-All-App-Features)

The SQL file for database setup can be found in this location: [resources/Creating_Student_Table.sql](resources/Creating_Student_Table.sql)

Project Completion Form Link: [https://forms.gle/bU268M2b9NRUaQnF9](https://forms.gle/bU268M2b9NRUaQnF9)
    ![img.png](README_Application_Screenshots/Project_Completion_Form/img.png)


## Table of Contents
1. [Project Purpose](#Project-Purpose)
2. [Application usage instructions](#Application-usage-instructions)
3. [Future Improvements](#Future-Improvements)

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
#### Open MySQL Workbench

- Search for `MySQL Workbench` in windows search and open the app
- You will be able to see the Home Screen similar to the screenshot below:
    ![Home_Page.png](README_Application_Screenshots/MySQL_Workbench/Home_Page.png)
- Click on the `+` option in the `MySQL Connections` section:
    ![Home_Page_2.png](README_Application_Screenshots/MySQL_Workbench/Home_Page_2.png)
- Enter the name of the connection in the `Connection name` field in the `Setup New Connection` dialog box:
    ![Home_Page_3.png](README_Application_Screenshots/MySQL_Workbench/Home_Page_3.png)
- Don't edit any other fields, go to the `Password` field and click on the `Store in vault ...` option:
    ![Home_Page_4.png](README_Application_Screenshots/MySQL_Workbench/Home_Page_4.png)
- Enter the root password you configured during MySQL installation in the `Password` field in the `Store Password for Connection` dialog box, and click on `OK` to store the password for future use:
    ![Home_Page_5.png](README_Application_Screenshots/MySQL_Workbench/Home_Page_5.png)
- Click on the `Test Connection` option to test the connection to the local MySQL Database. You should be able to see a `MySQL Workbench` dialog box stating that a successful connection was made. Click on `OK` to dismiss the dialog box:  
    ![Home_Page_6.png](README_Application_Screenshots/MySQL_Workbench/Home_Page_6.png)
    ![Home_Page_7.png](README_Application_Screenshots/MySQL_Workbench/Home_Page_7.png)
- Click on `OK` in the `Setup New Connection` dialog box to connect to the local database server:
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
#### Step 1: Clone the repository into local storage

- Go to [https://github.com/SarthakGiri32/StudentManagementSystem](https://github.com/SarthakGiri32/StudentManagementSystem)
- Click on the `Code` option in the Code section of the repository
- In the menu box that opens, copy the `HTTPS` url for the repository displayed in the `local` section
- In your local computer, go to the directory where you want to store the repository, then write `cmd` in the top address bar for the directory after replacing the absolute path
- In the command prompt window, type the following git command:
```shell
git clone https://github.com/SarthakGiri32/StudentManagementSystem.git
```
- The repository will start cloning into your local directory

#### Step 2: Setup the code in your IDE

A) The IDE used for this step is `IntelliJ IDEA`, although other Java-compatible IDEs also have similar set-up steps.

- Open the `IntelliJ IDEA` App (from the desktop shortcut or from windows search)
- Click on the `Open` button in the `Projects` tab to open the cloned repository:
    ![Page_1.png](README_Application_Screenshots/Codebase_Setup/Intellij_IDEA/Page_1.png)
- In the `Open File or Project` dialog box, navigate to the code repository, single-click to select the repository, and click on the `Select Folder` button:
    ![Page_2.png](README_Application_Screenshots/Codebase_Setup/Intellij_IDEA/Page_2.png)
- If you are opening the repository for the first time in the IDE, then a dialog box will open asking whether you trust the author of the project. Select the 'Trust Author' option (paraphrasing), to get full access to view and edit the code (otherwise the code-base will open in restricted mode, which is a read-only mode)

B) We need to add 1 external libraries to the project settings for the code in the IDE:

- `mysql-connector-j`: to connect to the database and process SQL queries through Java

C) You can download the `mysql-connector-j` JAR by searching for it in google

- Or you can go to [https://dev.mysql.com/downloads/file/?id=552110](https://dev.mysql.com/downloads/file/?id=552110), and click on `No thanks, just start my download` option to start download by browsing and selecting a download location

D) To add the downloaded JAR to the project settings for the code, follow the steps below

- Click on the gear icon (similar to '⚙️') on the top right section of the IDE window:
    ![Page_3.png](README_Application_Screenshots/Codebase_Setup/Intellij_IDEA/Page_3.png)
- Select the `Project Structure...` option in the drop-down menu:  
    ![Page_4.png](README_Application_Screenshots/Codebase_Setup/Intellij_IDEA/Page_4.png)
- Click on the `Libraries` tab option inside the `Project Structure` dialog box:
    ![Page_5.png](README_Application_Screenshots/Codebase_Setup/Intellij_IDEA/Page_5.png)
- Click on the `+` icon in top left section of the `Libraries` tab:
    ![Page_6.png](README_Application_Screenshots/Codebase_Setup/Intellij_IDEA/Page_6.png)
- Select the `Java` option in the `New Project Library` drop-down menu:
    ![Page_7.png](README_Application_Screenshots/Codebase_Setup/Intellij_IDEA/Page_7.png)
- In the `Select Library Files` dialog box, browse to the downloaded JAR zip file location and extract the zip file (my downloaded JAR version may not be the up-to-date), browse to the unzipped folder location and single-click to select the JAR file, and click on the `Open` button:
    ![Page_8.png](README_Application_Screenshots/Codebase_Setup/Intellij_IDEA/Page_8.png)
- In the `Choose Modules` dialog box, the project repository is already selected as the target module, so click on the `OK` button:
    ![Page_9.png](README_Application_Screenshots/Codebase_Setup/Intellij_IDEA/Page_9.png)
- In the `Project Structure` dialog box, you can see that the `mysql-connector-j` JAR has been added as an external library. Click on the `OK` button to save the changes:
    ![Page_10.png](README_Application_Screenshots/Codebase_Setup/Intellij_IDEA/Page_10.png)

### II) Launch, Navigation and Using All App Features
#### Step 1: Launching the App

- Go to `src -> com -> sarthakgiri -> studentmanagementsystem -> main` to reach the directory containing the `MainApp.java` file
- If everything has been setup correctly, then you will be able to see a play icon (similar to '▶'️) beside the `Current File` option in the right-side of the top menu bar:
    ![Page_1.png](README_Application_Screenshots/App_Usage/Page_1.png)
- Click on that icon, and after some build processing steps done internally by Java and the IDE, the `Student Management System` App should load in the `MySQL Login` screen:
    ![Page_2.png](README_Application_Screenshots/App_Usage/Page_2.png)
- Enter the username and password that you created in the `Add a MySQL User Account` section of the MySQL installation steps in the `MySQL Username` and `MySQL Password` fields, and click on the `Login` button to validate the user credentials and enter the app:
    ![Page_3.png](README_Application_Screenshots/App_Usage/Page_3.png)
- If the username and password are correct, then you will see a `MySQL Login Success` dialog box. Click on the `OK` button to enter the app:
    ![Page_4.png](README_Application_Screenshots/App_Usage/Page_4.png)
    ![Page_7.png](README_Application_Screenshots/App_Usage/Page_7.png)

#### Step 2: Navigating through the App and using all app features

A) Creating a new student record:

- In the `Student Management System Options` screen, click on the `Create Student Record` button:
    ![Page_5.png](README_Application_Screenshots/App_Usage/Page_5.png)
- In the `Create New Student Record` screen, enter the data for the new student in all input fields:
    ![Page_6.png](README_Application_Screenshots/App_Usage/Page_6.png)
- Click on the `Create Student Record` button to create a new student record, and if all input values are valid, then a `New Student Record Creation Successful` information message dialog will be displayed, and can be dismissed by clicking on the `OK` button. Thus, a new student record was successfully created:
    ![Page_8.png](README_Application_Screenshots/App_Usage/Page_8.png)
    ![Page_9.png](README_Application_Screenshots/App_Usage/Page_9.png)
- Click on the `Return to User Options` button to return to the `Student Management System Options` screen:
    ![Page_10.png](README_Application_Screenshots/App_Usage/Page_10.png)

B) Read All Student Records:

- In the `Student Management System Options` screen, click on the `Read Student Records` button:
    ![Page_11.png](README_Application_Screenshots/App_Usage/Page_11.png)
- Since the `student` table already has some existing records, you will be able to see all records displayed in a table format:
    ![Page_12.png](README_Application_Screenshots/App_Usage/Page_12.png)
- In the `Student Table Display` screen, click on the `Download Student Table as CSV` button to export the entire `student` table as a CSV file:
    ![Page_13.png](README_Application_Screenshots/App_Usage/Page_13.png)
- In the `Save Student List as CSV File` dialog box, navigate to the location where you want to export the CSV file, change the name of the file in the `File Name` field (if necessary), and click on the `Save` button. If a CSV file with the same file name as the new CSV file already exists, then that file will be overridden:
    ![Page_14.png](README_Application_Screenshots/App_Usage/Page_14.png)
- If the database reading and file export operations were successful, then the `Export Successful` information message dialog box will be display. Click on the `OK` button to close the box:
    ![Page_15.png](README_Application_Screenshots/App_Usage/Page_15.png)
- The exported CSV file will look something like this:
    ![Page_16.png](README_Application_Screenshots/App_Usage/Page_16.png)
- In the `Student Table Display` screen, click on the `Return to User Options` button to return to the `Student Management System Options` screen:
    ![Page_17.png](README_Application_Screenshots/App_Usage/Page_17.png)

C) Update a Student Record:

- In the `Student Management System Options` screen, click on the `Update Student Record` button:
    ![Page_18.png](README_Application_Screenshots/App_Usage/Page_18.png)
- In the `Update Existing Student Record` screen, enter the necessary user input data, and click on the `Update Student Record` button:
    ![Page_19.png](README_Application_Screenshots/App_Usage/Page_19.png)
- If the user input data is valid, and the input roll number value exists in the `student` table, then `Data Update Successful` information message dialog box is displayed, which can be dismissed by clicking on the `OK` button:  
    ![Page_20.png](README_Application_Screenshots/App_Usage/Page_20.png)
- Click on the `Return to User Options` button to return to the `Student Management System Options` screen:
    ![Page_21.png](README_Application_Screenshots/App_Usage/Page_21.png)

D) Delete a Student Record:

- In the `Student Management System Options` screen, click on the `Delete Student Record` button:
    ![Page_22.png](README_Application_Screenshots/App_Usage/Page_22.png)
- In the `Delete Existing Student Record` screen, enter an existing roll number value, and click on the `Delete Student Record` button:
    ![Page_23.png](README_Application_Screenshots/App_Usage/Page_23.png)
- If the roll number value is valid, and it exists in the database, then a `Data Deletion Successful` information message dialog box is displayed, which can be dismissed by clicking on the `OK` button:
    ![Page_24.png](README_Application_Screenshots/App_Usage/Page_24.png)
- Click on the `Return to User Options` button to return to the `Student Management System Options` screen:
    ![Page_25.png](README_Application_Screenshots/App_Usage/Page_25.png)

E) Search for Student Records By Name:

- In the `Student Management System Options` screen, click on the `Search Student Record` button:
    ![Page_26.png](README_Application_Screenshots/App_Usage/Page_26.png)
- In the `Student Search Options` screen, click on the `Search By Name` button:
    ![Page_27.png](README_Application_Screenshots/App_Usage/Page_27.png)
- In the `Search By Name Input` screen section, enter an existing student name in the `Existing Name` field, and click on the `Search` button:
    ![Page_28.png](README_Application_Screenshots/App_Usage/Page_28.png)
- If the input value is valid and exists in the database, a `Search Successful` information message dialog box is displayed, along with the search result in table format. Click on the `OK` button to close the box and see the search result:  
    ![Page_29.png](README_Application_Screenshots/App_Usage/Page_29.png)
    ![Page_30.png](README_Application_Screenshots/App_Usage/Page_30.png)
- Click on the `Return to Search Options` button to return to the `Student Search Options` screen:
    ![Page_31.png](README_Application_Screenshots/App_Usage/Page_31.png)

F) Search for Student Records By Roll Number:

- In the `Student Search Options` screen, click on the `Search By Roll Number` button:
    ![Page_32.png](README_Application_Screenshots/App_Usage/Page_32.png)
- In the `Search By Roll Number Input` screen section, enter an existing roll number, and click on the `Search` button:
    ![Page_33.png](README_Application_Screenshots/App_Usage/Page_33.png)
- If the input is valid and exists in the database table, a `Search Successful` information message dialog box is displayed, along with the search result in table format. Click on the `OK` button to close the box and see the search result:  
    ![Page_34.png](README_Application_Screenshots/App_Usage/Page_34.png)
    ![Page_35.png](README_Application_Screenshots/App_Usage/Page_35.png)
- Click on the `Return to Search Options` button to return to the `Student Search Options` screen:
    ![Page_35.png](README_Application_Screenshots/App_Usage/Page_35.png)

E) Search for Student Records By Department:

- In the `Student Search Options` screen, click on the `Search By Department` button:
    ![Page_32.png](README_Application_Screenshots/App_Usage/Page_32.png)
- In the `Search By Department Input` screen section, enter an existing department name, and click on the `Search` button:
    ![Page_36.png](README_Application_Screenshots/App_Usage/Page_36.png)
- If the input is valid and exists in the database table, a `Search Successful` information message dialog box is displayed, along with the search result in table format. Click on the `OK` button to close the box and see the search result:  
    ![Page_37.png](README_Application_Screenshots/App_Usage/Page_37.png)
    ![Page_38.png](README_Application_Screenshots/App_Usage/Page_38.png)
- Click on the `Return to Search Options` button to return to the `Student Search Options` screen:
    ![Page_38.png](README_Application_Screenshots/App_Usage/Page_38.png)

F) Search for Student By Marks Range:

- In the `Student Search Options` screen, click on the `Search By Marks` button:
    ![Page_32.png](README_Application_Screenshots/App_Usage/Page_32.png)
- In the `Search By Marks Range Input` screen section, enter a upper and lower limit for the marks, and click on the `Search` button:
    ![Page_39.png](README_Application_Screenshots/App_Usage/Page_39.png)
- If the input is valid and student records exist in the marks range, a `Search Successful` information message dialog box is displayed, along with the search result in table format. Click on the `OK` button to close the box and see the search result:  
    ![Page_40.png](README_Application_Screenshots/App_Usage/Page_40.png)
    ![Page_41.png](README_Application_Screenshots/App_Usage/Page_41.png)
- Click on the `Return to Search Options` button to return to the `Student Search Options` screen:
    ![Page_41.png](README_Application_Screenshots/App_Usage/Page_41.png)

E) See Student Statistics:

- Click on the `Return to User Options` button to return to the `Student Management System Options` screen:
    ![Page_32.png](README_Application_Screenshots/App_Usage/Page_32.png)
- In the `Student Management System Options` screen, click on the `Student Statistics` button:
    ![Page_42.png](README_Application_Screenshots/App_Usage/Page_42.png)
- In the `Student Statistic Options` screen, click on the `Total Student Count and Highest And Lowest Marks` button:
    ![Page_43.png](README_Application_Screenshots/App_Usage/Page_43.png)
- The total student count and highest and lowest marks are displayed in table format:
    ![Page_44.png](README_Application_Screenshots/App_Usage/Page_44.png)
- Click on the `Return to Student Statistic Options` button to return to the `Student Statistic Options` screen:
    ![Page_44.png](README_Application_Screenshots/App_Usage/Page_44.png)
- In the `Student Statistic Options` screen, click on the `Student Count By Department` button:
    ![Page_43.png](README_Application_Screenshots/App_Usage/Page_43.png)
- The Department-wise student count is displayed in table format:
    ![Page_45.png](README_Application_Screenshots/App_Usage/Page_45.png)
- Click on the `Return to Student Statistic Options` button to return to the `Student Statistic Options` screen:
    ![Page_45.png](README_Application_Screenshots/App_Usage/Page_45.png)
    ![Page_43.png](README_Application_Screenshots/App_Usage/Page_43.png)

## Future Improvements

- Will add subject-wise marks entry option
- Will experiment with different UI libraries, and even creating UIs in other programming languages (like `ReactJS`)
