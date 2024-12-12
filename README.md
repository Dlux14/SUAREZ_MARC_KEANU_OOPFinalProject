# QuizMaster

QuizMaster is a Java-based quiz application that allows users to manage quiz topics and questions, take quizzes, and view their results. The application uses a MySQL database to store topics and questions.

## Features

- **Add Topics**: Create new quiz topics.
- **Add Questions**: Add questions to existing topics with different types (Identification, True/False).
- **Take Quizzes**: Take quizzes based on selected topics and receive scores.
- **View Questions**: View all questions associated with a specific topic.
- **View Topics**: List all available quiz topics.
- **Remove Topics**: Delete topics and their associated questions.
- **Remove Questions**: Delete specific questions from a topic.

## Requirements

- Java Development Kit (JDK) 8 or higher
- MySQL Database
- MySQL Connector/J (JDBC Driver)

## Setup

1. **Clone the repository** (or download the source code):
   ```bash
   git clone https://github.com/yourusername/QuizMaster.git
   cd QuizMaster

## Set up MySQL Database:

Create a database named mydb (or change the DB_URL in the code to match your database name).
### Create the following tables:
sql

CREATE TABLE Topics (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE Questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    topic_id INT,
    question_text TEXT NOT NULL,
    question_type INT NOT NULL,  -- 1 for Identification, 2 for True/False
    answer TEXT NOT NULL,
    FOREIGN KEY (topic_id) REFERENCES Topics(id) ON DELETE CASCADE
);
## Configure Database Connection:

Update the DB_URL, DB_USER, and DB_PASSWORD constants in the QuizMaster class to match your MySQL database credentials.
Compile and Run:

### Compile the Java file:

javac QuizMaster.java
Run the application:

## Usage
#### Add Topic:
Select option 1 to add a new topic.
#### Add Question:
Select option 2 to add a question to an existing topic.
### Take Quiz:
Select option 3 to take a quiz based on a specific topic.
### View Questions:
Select option 4 to view all questions for a specific topic.
### View Topics:
Select option 5 to list all available topics.
### Remove Topic: 
Select option 6 to remove a topic and its questions.
### Remove Question:
Select option 7 to remove a specific question.
### Exit:
Select option 8 to exit the application.

## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Acknowledgments
MySQL for the database management system.
Java for the programming language.
