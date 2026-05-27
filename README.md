# My Shangri-La Referendum (MSLR)

![Java](https://img.shields.io/badge/Java-Spring%20Boot-blue)
![Spring MVC](https://img.shields.io/badge/Spring%20MVC-Web%20Application-brightgreen)
![REST API](https://img.shields.io/badge/REST%20API-Open%20Data-orange)
![Database](https://img.shields.io/badge/Database-MySQL%20%2F%20JPA-lightgrey)
![Maven](https://img.shields.io/badge/Build-Maven-red)

**My Shangri-La Referendum (MSLR)** is a full-stack Spring Boot web application for managing and participating in local referendums. The system supports two user roles: **Voters**, who can register and vote on active referendums, and the **Election Commission**, who can create, manage, open, close and monitor referendums.

The project was built as a university mini web application coursework, but it is presented here as a portfolio project demonstrating **server-side development, MVC architecture, authentication, database-backed CRUD, role-based workflows, REST API design and secure voting rules**.

---

## Screenshots

### Voter Dashboard

Voters can view active referendums, see all available referendums, and access their voting history.

<img width="415" height="214" alt="image" src="https://github.com/user-attachments/assets/876e93f1-8b05-4fb5-8be0-38f2b61b94f8" />

<img width="415" height="279" alt="image" src="https://github.com/user-attachments/assets/36f18e19-3215-4f70-a58a-7cb901e6a002" />



### Election Commission Dashboard

The Election Commission can create referendums, define options, open or close voting, and view referendum status.

<img width="415" height="230" alt="image" src="https://github.com/user-attachments/assets/73e0df00-dd8a-48fb-abfc-a7c750846f32" />




### Referendum Management

The system supports referendum state management, vote totals, editable referendum details and option management before opening.

<img width="415" height="469" alt="image" src="https://github.com/user-attachments/assets/eeb78af7-2a63-461b-996d-027aa2090d9d" />


---

## Core Features

### Voter Functionality

- Voter registration with email, full name, date of birth, password and Shangri-La Citizen Code (SCC).
- Login and sign-out flow for registered voters.
- Voter dashboard showing open and closed referendums.
- Single-vote enforcement so a voter can only vote once per referendum.
- Voting history so users can review referendums they have participated in.
- Validation for invalid or already-used SCC codes.
- Validation for duplicate voter email addresses.

### Election Commission Functionality

- Dedicated Election Commission login flow.
- Create new referendums with title, description and voting options.
- View all referendums and their current status.
- Open and close voting manually.
- View vote totals for each referendum option.
- Edit referendum details while still in a draft/read-only state.
- Protect Election Commission pages from normal voter access.

### REST API Functionality

The project includes an open data REST layer for retrieving anonymised referendum data.

Example endpoints:

```http
GET /mslr/referendums?status=open
GET /mslr/referendum/{id}
```

These endpoints are designed to expose referendum metadata, status, options and vote totals in JSON format.

---

## What This Project Demonstrates

This project is useful for recruiters because it shows practical software engineering skills beyond a basic CRUD app:

- **Full-stack web development** using Java and Spring Boot.
- **MVC architecture** with controllers, services, repositories, models and templates.
- **Role-based user flows** for voters and Election Commission users.
- **Authentication and protected routes** for different dashboard experiences.
- **Database-backed state management** for referendums, voters, SCC codes and votes.
- **REST API design** for exposing open data.
- **Business rule implementation**, including one-vote-per-referendum and referendum open/closed states.
- **Validation and error handling** for login, registration and voting workflows.
- **Maven-based project structure** with a standard Spring Boot layout.

---

## Tech Stack

| Area | Technology |
|---|---|
| Backend | Java, Spring Boot, Spring MVC |
| Frontend | Thymeleaf templates, HTML, CSS |
| Database | MySQL / JPA repositories / SQL seed data |
| API | REST endpoints returning JSON |
| Build Tool | Maven Wrapper |
| Security | Login flows, role-based access, protected dashboards |
| Testing | Spring Boot test structure |

---

## Project Structure

```text
mslr/
├── pom.xml
├── mvnw
├── src/
│   ├── main/
│   │   ├── java/com/example/mslr/
│   │   │   ├── MslrApplication.java
│   │   │   ├── api/
│   │   │   │   ├── MslrApiController.java
│   │   │   │   ├── MslrApiService.java
│   │   │   │   └── dto/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── repo/
│   │   │   ├── security/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── data.sql
│   │       ├── static/
│   │       └── templates/
│   │           ├── login.html
│   │           ├── register.html
│   │           ├── ec/
│   │           ├── scc/
│   │           └── voter/
│   └── test/
│       └── java/com/example/mslr/
└── README.md
```

---

## Database Design

The database supports referendum management, voter registration and vote tracking.

Main entities/tables include:

- `referendum` - referendum questions.
- `referendum_options` - voting options attached to a referendum.
- `scc_code` - valid Shangri-La Citizen Codes and whether they have been used.
- `voters` - registered voter accounts.
- `voter_history` - records the voter, referendum and selected option.

---

## How to Run Locally

### 1. Clone the repository

```bash
git clone https://github.com/rien1230/mslr.git
cd mslr
```

### 2. Check Java and Maven

This project uses the Maven Wrapper, so you do not need Maven installed globally.

```bash
java -version
./mvnw -v
```

If the Maven wrapper is not executable, run:

```bash
chmod +x mvnw
```

### 3. Configure the database

Check your database settings in:

```text
src/main/resources/application.properties
```

If using MySQL, create a database and import the provided SQL data:

```bash
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS cw2_2025;"
mysql -u root -p cw2_2025 < referendum.sql
```

Update `application.properties` with your local database username and password.

### 4. Start the application

```bash
./mvnw spring-boot:run
```

Then open:

```text
http://localhost:8080
```

Useful local routes may include:

```text
http://localhost:8080/login
http://localhost:8080/register
```

### 5. Run tests

```bash
./mvnw test
```

---

## Example API Usage

Return open referendums:

```bash
curl "http://localhost:8080/mslr/referendums?status=open"
```

Return a specific referendum by ID:

```bash
curl "http://localhost:8080/mslr/referendum/1"
```

---

## Implemented Coursework Scope

| Area | Status |
|---|---|
| Voter registration | Implemented |
| Voter login | Implemented |
| Voter dashboard | Implemented |
| Election Commission login | Implemented |
| Create/manage referendum | Implemented |
| REST API endpoint 2.1 | Implemented |
| REST API endpoint 2.2 | Implemented |
| Voter history bonus feature | Implemented |

---

## Security and Reliability Considerations

- Passwords should be stored securely using hashing.
- Voters should not be able to access Election Commission pages.
- Election Commission users should have a separate protected workflow.
- SCC codes should be validated and marked as used after successful registration.
- A voter should not be able to vote twice in the same referendum.
- API responses should avoid exposing personal voter information.
- Local demo credentials should be changed before any production deployment.

---

## Future Improvements

If continuing this project, the strongest next improvements would be:

- Add chart-based referendum result visualisation.
- Add QR scanning for SCC registration.
- Improve mobile responsiveness and accessibility.
- Add more automated tests for voting rules and API responses.
- Add clearer error pages and AJAX validation.
- Add JWT/OAuth2 protection for API routes.
- Deploy the application with a managed database.

---

## Portfolio Summary

This project demonstrates the ability to build a **database-backed web application with real user roles, voting rules, protected dashboards and REST API endpoints**. It combines backend engineering, web development, database design, authentication, validation and API design into a practical civic-tech style application.
