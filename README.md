# Techie Planet Coding Challenge

This repository contains my answers for the Techie Planet Java/Spring Boot technical assessment.

## Project Structure

```text
algorithms/          Java solutions for the algorithm questions
sql/                 SQL answers
student-score-app/   Spring Boot + PostgreSQL student score application
```

## Requirements

- Java 17
- Maven 3.9+
- Docker Desktop

## Algorithm Questions

The algorithm answers are in `algorithms/src/main/java/com/techieplanet/algorithms`.

Run the tests:

```bash
cd algorithms
mvn test
```

Run question 1, Time in Words:

```bash
mvn exec:java -Dexec.mainClass="com.techieplanet.algorithms.question1.TimeInWords"
```

The input should be two lines, as requested in the question:

```text
9
47
```

Run question 2, Duplicate Remover:

```bash
mvn exec:java -Dexec.mainClass="com.techieplanet.algorithms.question2.DuplicateRemover"
```

Run question 3, Digit Sum:

```bash
mvn exec:java -Dexec.mainClass="com.techieplanet.algorithms.question3.DigitSum"
```

## SQL Questions

The SQL answers are in the `sql/` folder:

```text
question1_second_largest_salary.sql
question2_games_country.sql
question3_left_right_join.sql
question4_average_session_duration.sql
```

Each file contains the answer for the matching SQL question.

## Student Score App

The Spring Boot app accepts a student name and scores for exactly five subjects. It stores the data in PostgreSQL and returns reports showing each subject score, mean, median, and mode.

### Main Design Decisions

- A student is stored separately from subjects and scores.
- Subject names are stored once and reused across students.
- Each student can only have one score per subject.
- Scores are validated to stay between 0 and 100.
- API responses use one consistent response shape for success and error cases.
- The report list endpoint returns a simple page response instead of exposing Spring's full `Page` object.

### Run with Docker Compose

From the `student-score-app` folder:

```bash
cd student-score-app
docker compose up --build
```

This starts:

- PostgreSQL on port `5432`
- Spring Boot app on port `8080`

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

Stop the containers:

```bash
docker compose down
```

### Run Locally from an IDE

Start only PostgreSQL with Docker Compose:

```bash
cd student-score-app
docker compose up -d postgres
```

Then run this class from the IDE:

```text
com.techieplanet.scores.StudentScoreApplication
```

The app uses these local defaults:

```text
DB_URL=jdbc:postgresql://localhost:5432/student_scores_db
DB_USERNAME=postgres
DB_PASSWORD=postgres
SERVER_PORT=8080
```

### Run App Tests

```bash
cd student-score-app
mvn test
```

## API Endpoints

Create a student with five scores:

```http
POST /api/students
```

Example request:

```json
{
  "firstName": "Jane",
  "lastName": "Doe",
  "scores": [
    {"subject": "Mathematics", "score": 80},
    {"subject": "English", "score": 70},
    {"subject": "Physics", "score": 90},
    {"subject": "Chemistry", "score": 80},
    {"subject": "Biology", "score": 75}
  ]
}
```

Get student reports:

```http
GET /api/reports/students?page=0&size=10
```

Filter reports by student name:

```http
GET /api/reports/students?search=Jane&page=0&size=10
```

Get one student report:

```http
GET /api/reports/students/{studentId}
```

## Docker Notes

The application uses a multi-stage Docker build:

- `maven:3.9.9-amazoncorretto-17` builds the Spring Boot jar.
- `eclipse-temurin:17-jre` runs the packaged jar.

The app container connects to PostgreSQL using the Compose service name `postgres`.
