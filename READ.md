### **Student Attendance Management System**



A Java Swing-based desktop application for managing student attendance records and monitoring attendance percentage.



#### Features



\- Add student attendance records

\- Update existing student records

\- Delete student records

\- Display student information in a table

\- Automatically calculate attendance percentage

\- Calculate the number of safe bunks available

\- Automatically set total hours based on branch

\- Save student records to a text file

\- Load previously saved student records

\- View attendance information through a popup menu

\- Input validation for attendance-related data

\- Simple and user-friendly graphical interface



#### Technologies Used



\- Java

\- Java Swing

\- AWT

\- File Handling

\- JTable

\- ArrayList



#### Application Details



The application allows users to enter a student's name, register number, roll number, branch, and number of bunks.



The total hours are automatically assigned according to the selected branch. The application then calculates the student's attendance percentage and the number of safe bunks remaining.



Student records are displayed using a "JTable" and can be added, updated, or deleted.



#### Attendance Calculation



Attendance percentage is calculated using:



Attendance % = ((Total Hours - Bunks) / Total Hours) × 100



The application also calculates the number of safe bunks based on maintaining the required attendance level.



#### Data Storage



Student records can be saved to a text file named:



students.txt



The saved records can later be loaded back into the application.



#### How to Run



###### Requirements



\- Java JDK installed on your system



###### Compile



Open a terminal inside the "src" folder and run:



javac StudentAttendanceManager.java



###### Run



java StudentAttendanceManager

#### 

#### Project Structure



Student-Attendance-Management-System/

│

├── src/

│   └── StudentAttendanceManager.java

│

├── README.md

│

└── .gitignore

