
QuizMaster Application - README
Overview
The QuizMaster Application is a Java-based command-line program that allows users to manage quiz topics, questions, and conduct quizzes interactively. It integrates with a MySQL database to store and retrieve data efficiently.

Features
Add Topic: Create and store new quiz topics in the database.
Add Question: Add questions under a specific topic. Supports:
Identification questions
True/False questions
Take Quiz: Attempt a randomized quiz for a chosen topic with a customizable number of questions.
View Topics: List all available quiz topics.
View Questions: Display questions and their correct answers for a selected topic.
Remove Topic: Delete a topic along with all associated questions.
Remove Question: Delete specific questions from the database.
Exit: Close the application gracefully.
Prerequisites
Java: Java Development Kit (JDK) installed on your system.
MySQL Database: MySQL server running with the required database (mydb) and tables set up.
JDBC Driver: Include the MySQL JDBC driver in your project to enable database connectivity.
Database Schema
Topics Table:

id (INT, Primary Key): Unique identifier for topics.
name (VARCHAR): Name of the topic.
Questions Table:

id (INT, Primary Key): Unique identifier for questions.
topic_id (INT, Foreign Key): References id in the Topics table.
question_text (VARCHAR): The text of the question.
question_type (INT): Type of question (1 for Identification, 2 for True/False).
answer (VARCHAR): The correct answer for the question.
How to Set Up
Clone the Repository:

bash
Copy code
git clone https://github.com/your-username/quizmaster.git
cd quizmaster
Set Up the Database:

Create a database in MySQL named mydb (or update the name in the code if different).
Execute the following SQL commands to create the required tables:
sql
Copy code
CREATE TABLE Topics (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE Questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    topic_id INT NOT NULL,
    question_text VARCHAR(255) NOT NULL,
    question_type INT NOT NULL,
    answer VARCHAR(255) NOT NULL,
    FOREIGN KEY (topic_id) REFERENCES Topics(id)
);
Update Database Credentials:

Open the QuizMaster.java file and update these constants:
java
Copy code
private static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";
private static final String DB_USER = "your-username";
private static final String DB_PASSWORD = "your-password";
Compile the Program:

bash
Copy code
javac QuizMaster.java
Run the Program:

bash
Copy code
java QuizMaster
Usage
Follow the on-screen menu to perform the following actions:

Add topics and questions.
Take quizzes with automatic scoring.
View or delete topics and questions.
Contributions
Contributions are welcome! If you'd like to improve the project:

Fork the repository.
Create a new branch (feature/new-feature).
Commit your changes.
Submit a pull request for review.
License
This project is open-source and available under the MIT License.

Enjoy using QuizMaster! 🚀
