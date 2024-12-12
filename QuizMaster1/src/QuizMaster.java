import java.sql.*;
import java.util.*;

public class QuizMaster {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";  // Your MySQL database URL
    private static final String DB_USER = "root";  // Your MySQL username
    private static final String DB_PASSWORD = "prisab2019";  // Your MySQL password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n******************************************************");
            System.out.println("                   QuizMaster Menu                    ");
            System.out.println("******************************************************");
            System.out.println("1. Add Topic");
            System.out.println("2. Add Question");
            System.out.println("3. Take Quiz");
            System.out.println("4. View Questions");
            System.out.println("5. View Topics");
            System.out.println("6. Remove Topic");
            System.out.println("7. Remove Question");
            System.out.println("8. Exit");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addTopic(scanner);
                    break;
                case "2":
                    addQuestion(scanner);
                    break;
                case "3":
                    takeQuiz(scanner);
                    break;
                case "4":
                    viewQuestions(scanner);
                    break;
                case "5":
                    viewTopics(scanner);
                    break;
                case "6":
                    removeTopic(scanner);
                    break;
                case "7":
                    removeQuestion(scanner);
                    break;
                case "8":
                    System.out.println("Exiting QuizMaster. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    // Function to establish MySQL connection
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Function to add a topic to the database
    private static void addTopic(Scanner scanner) {
        System.out.print("Enter topic name: ");
        String topic = scanner.nextLine().trim();

        try (Connection conn = getConnection()) {
            String query = "INSERT INTO Topics (name) VALUES (?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, topic);
                stmt.executeUpdate();
                System.out.println("Topic '" + topic + "' added.");
            } catch (SQLException e) {
                System.out.println("Error adding topic: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    // Function to add a question to the database
    private static void addQuestion(Scanner scanner) {
        System.out.print("Enter topic name: ");
        String topic = scanner.nextLine().trim();

        try (Connection conn = getConnection()) {
            // Check if the topic exists
            String topicQuery = "SELECT id FROM Topics WHERE name = ?";
            try (PreparedStatement topicStmt = conn.prepareStatement(topicQuery)) {
                topicStmt.setString(1, topic);
                ResultSet rs = topicStmt.executeQuery();

                if (!rs.next()) {
                    System.out.println("Topic not found. Please add the topic first.");
                    return;
                }

                int topicId = rs.getInt("id");

                System.out.print("Enter question type (1 for Identification, 2 for True/False): ");
                int questionType = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter question: ");
                String questionText = scanner.nextLine().trim();

                System.out.print("Enter answer: ");
                String answer = scanner.nextLine().trim();

                String insertQuery = "INSERT INTO Questions (topic_id, question_text, question_type, answer) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                    stmt.setInt(1, topicId);
                    stmt.setString(2, questionText);
                    stmt.setInt(3, questionType);
                    stmt.setString(4, answer);
                    stmt.executeUpdate();
                    System.out.println("Question added to the database.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding question: " + e.getMessage());
        }
    }

    // Function to take a quiz
    private static void takeQuiz(Scanner scanner) {
        System.out.print("Enter topic name: ");
        String topic = scanner.nextLine().trim();

        try (Connection conn = getConnection()) {
            String query = "SELECT q.question_text, q.answer, q.question_type FROM Questions q JOIN Topics t ON q.topic_id = t.id WHERE t.name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, topic);
                ResultSet rs = stmt.executeQuery();

                if (!rs.next()) {
                    System.out.println("No questions found for this topic.");
                    return;
                }

                List<String> questions = new ArrayList<>();
                List<String> answers = new ArrayList<>();
                List<Integer> types = new ArrayList<>();

                do {
                    questions.add(rs.getString("question_text"));
                    answers.add(rs.getString("answer"));
                    types.add(rs.getInt("question_type"));
                } while (rs.next());

                System.out.print("Enter number of questions: ");
                int numQuestions = Integer.parseInt(scanner.nextLine());

                if (questions.size() < numQuestions) {
                    System.out.println("Not enough questions in this topic.");
                    return;
                }

                Random rand = new Random();
                int score = 0;

                System.out.println("\n------------------------------------------------------");
                System.out.println("Starting quiz on '" + topic + "' with " + numQuestions + " questions.");
                System.out.println("------------------------------------------------------");

                for (int i = 0; i < numQuestions; i++) {
                    int questionIndex = rand.nextInt(questions.size());
                    String question = questions.get(questionIndex);
                    String correctAnswer = answers.get(questionIndex);
                    int questionType = types.get(questionIndex);

                    System.out.println("Question: " + question);
                    String userAnswer = "";

                    if (questionType == 1) {
                        System.out.print("Your answer: ");
                        userAnswer = scanner.nextLine().trim();
                    } else if (questionType == 2) {
                        System.out.print("Your answer (true/false): ");
                        userAnswer = scanner.nextLine().trim();
                    }

                    if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                        System.out.println("Correct!");
                        score++;
                    } else {
                        System.out.println("Incorrect. The correct answer was: " + correctAnswer);
                    }
                }

                System.out.println("\nQuiz complete! Your score: " + score + "/" + numQuestions);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching quiz questions: " + e.getMessage());
        }
    }

    private static void viewTopics(Scanner scanner) {
        try (Connection conn = getConnection()) {
            String query = "SELECT name FROM Topics";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
    
                if (!rs.isBeforeFirst()) { // Check if the ResultSet has any rows
                    System.out.println("There are no topics yet.");
                    return;
                }
    
                System.out.println("\nAvailable Topics:");
                while (rs.next()) {
                    System.out.println("- " + rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching topics: " + e.getMessage());
        }
    }
    
    // Function to remove a topic
    private static void removeTopic(Scanner scanner) {
        System.out.print("Enter the name of the topic to remove: ");
        String topic = scanner.nextLine().trim();

        try (Connection conn = getConnection()) {
            String deleteQuestionsQuery = "DELETE FROM Questions WHERE topic_id = (SELECT id FROM Topics WHERE name = ?)";
            try (PreparedStatement stmt = conn.prepareStatement(deleteQuestionsQuery)) {
                stmt.setString(1, topic);
                stmt.executeUpdate();
                System.out.println("All questions related to the topic '" + topic + "' have been removed.");
            }

            String deleteTopicQuery = "DELETE FROM Topics WHERE name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteTopicQuery)) {
                stmt.setString(1, topic);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Topic '" + topic + "' removed successfully.");
                } else {
                    System.out.println("Topic not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error removing topic: " + e.getMessage());
        }
    }

    // Function to remove a question
    private static void removeQuestion(Scanner scanner) {
        System.out.print("Enter the question text to remove: ");
        String questionText = scanner.nextLine().trim();

        try (Connection conn = getConnection()) {
            String deleteQuestionQuery = "DELETE FROM Questions WHERE question_text = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteQuestionQuery)) {
                stmt.setString(1, questionText);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Question removed successfully.");
                } else {
                    System.out.println("Question not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error removing question: " + e.getMessage());
        }
    }

    // Function to view questions for a specific topic
    private static void viewQuestions(Scanner scanner) {
        System.out.print("Enter topic name: ");
        String topic = scanner.nextLine().trim();
    
        try (Connection conn = getConnection()) {
            String query = "SELECT q.question_text, q.answer FROM Questions q JOIN Topics t ON q.topic_id = t.id WHERE t.name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, topic);
                try (ResultSet rs = stmt.executeQuery()) {
    
                    if (!rs.isBeforeFirst()) { // Check if the ResultSet has any rows
                        System.out.println("No questions found for this topic.");
                        return;
                    }
    
                    System.out.println("\nQuestions for '" + topic + "':");
                    while (rs.next()) {
                        System.out.println("Question: " + rs.getString("question_text"));
                        System.out.println("Answer: " + rs.getString("answer"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching questions: " + e.getMessage());
        }
    }
}    