# Employee-Project Spring Boot Application

The **Employee-Project** is a simple full-stack application built using Spring Boot and React that facilitates efficient management of employees, projects, and tickets within an organization. This repository contains the backend Spring Boot implementation for the application.

## Features

- **CRUD Operations**: Perform Create, Read, Update, and Delete operations for employees, projects, and tickets.

- **Spring Data JPA**: Utilize Spring Data JPA for simplified database interactions and management.

- **Spring Security with JWT**: Implement robust authentication and authorization using JSON Web Tokens (JWT). Only authorized users can access specific resources.

- **JPA Auditing**: Employ JPA auditing to automatically track and manage creation and modification timestamps of entities.

- **Project and Ticket Assignment**: Assign multiple employees to multiple projects and tickets for effective collaboration.

- **Admin Privileges**: Allow only admin users to create projects, ensuring proper access control.

- **Employee Ticket Creation**: Enable employees to create tickets for efficient issue tracking and resolution.

- **Access Control**: Limit access to projects and tickets based on employee assignments.

- **Commenting System**: Enable employees to comment on tickets, fostering better communication.

- **Comment Voting**: Implement upvoting and downvoting functionality for comments to gauge their usefulness.

## Installation and Setup

1. Clone the repository: 

   ```bash
   git clone https://github.com/anilgurungg/Employee-Project.git
   cd Employee-Project
   ```

2. Backend Setup:

   - Configure the database connection in `src/main/resources/application.properties`.
   - Run the Spring Boot backend using your preferred IDE or the command line:

     ```bash
     ./mvnw spring-boot:run
     ```

3. Frontend Setup:

   - The frontend React implementation can be found [here](https://github.com/anilgurungg/Employee-Project-Client). Follow the instructions in its readme to set it up.

## Usage

Once the backend and frontend are set up, you can access the Employee-Project application through your browser. Depending on your configuration, the application will be available at `http://localhost:8080` or a different URL you've specified.

- Log in using your credentials. 
- Navigate through the application to manage employees, projects, and tickets.
- Admin users can create projects, and all users can create tickets.
- Access to resources is determined by user roles and assignments.

## Contributing

Contributions to the Employee-Project are welcome! If you find any issues or want to enhance the application, feel free to create a pull request. Please ensure that your contributions align with the project's goals and guidelines.

## License

This project is licensed under the [MIT License](LICENSE).

---

For the React frontend of the Employee-Project, please visit the [frontend repository](https://github.com/anilgurungg/Employee-Project-Client).

For any questions or further clarifications, please contact [Anil Gurung](https://github.com/anilgurungg).
