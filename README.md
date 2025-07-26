# Simple Spring JPA Rest and Swagger Example

## Description

This project is a Spring Boot Web MVC application that exposes a REST API secured with OAuth2 Resource Server using asymmetrically signed JWT tokens. It uses self-signed RSA key pairs (public and private) for token verification and does not require an external Authorization Server.

The API is fully documented and accessible via Swagger UI and via Postman.

## Software requires: 
1. MySQL Server(not included in the project, for more details check URL: [MySQL community server](https://dev.mysql.com/downloads/mysql));
2. Java 21 LTS or latest;
3. Postman (Optional)

Useful tools (Optional):
1. MySQL Workbench user interface tool URL: [MySQL Workbench](https://dev.mysql.com/downloads/workbench)
2. Multiple DB user interface tool URL: [DBever - Universal Database Tool](https://dbeaver.io/download)

## Project setup:

1. Install MySQL Server/Start MySQL via Docker or any other way you prefer;
2. Install Java 21+ LTS
3. Create new user in the DB with all privileges by running this script in the MySQL Workbench:

```sql
CREATE USER 'mysqluser'@'localhost' IDENTIFIED BY 'mysqluser';
GRANT ALL PRIVILEGES ON * . * TO 'mysqluser'@'localhost';
```

4. Create new database in the DB with name `employee_directory` 

```sql
CREATE DATABASE employee_directory;
```

5. Before running the project change configuration `spring.jpa.hibernate.ddl-auto=create` in application.properties
6. Create folder in `src/main/resources/certs`
7. Inside generate public and private keys RSA 2048 bits and save them in `src/main/resources/certs` with the following commands:

Linux/Mac OS:
 - Private ket:
```bash
openssl genpkey -algorithm RSA -out private.pem -pkeyopt rsa_keygen_bits:2048
```

 - Public key:
```bash
openssl rsa -pubout -in private.pem -out public.pem
```

Alternative generation: [jwt.io](https://www.jwt.io/)


## Test

Base User:
   - username: user
   - password: password

Get token (Optional)

```bash
curl --location --request POST 'localhost:8080/api/v1/auth/token' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

Test via Postman

> [!IMPORTANT]  
> Set environment `Web_MVC_JWT_env` and use request `Get token` in order to use automatically the token in all requests.

> [!TIP]  
> The POST request uses dynamic variables for generating valid firstName, lastName and email.

[Postman collection](postman/Web_MVC_JWT.postman_collection.json)

Test via Swagger-UI

[Swagger-UI](http://localhost:8080/swagger)

Test JWT validity

[free JWT website](https://www.jwt.io/)

Test via JUnit

- Replace `<url>` with your database URL
- Replace `<username>` with your database username
- Replace `<password>` with your database password
- Make sure `mvnw` file is executable

```bash
./mvnw test -Dspring.datasource.url=<url> -Dspring.datasource.username=<username> -Dspring.datasource.password=<password>
```

## Gallery

![Preview swagger-ui](images/Capture.PNG)

<table>
  <tr>
    <td><img src="images/Capture1.PNG" alt="swagger-ui"></td>
    <td><img src="images/Capture2.PNG" alt="swagger-ui"></td>
    <td><img src="images/Capture3.PNG" alt="swagger-ui"></td>
  </tr>
  <tr>
    <td><img src="images/Capture4.PNG" alt="swagger-ui"></td>
    <td><img src="images/Capture5.PNG" alt="swagger-ui"></td>
    <td><img src="images/Capture6.PNG" alt="swagger-ui"></td>
  </tr>
 </table>


## Test timeouts

### Stop MySQL service after backend is up then run some requests
```bash
sudo systemctl stop mysql
```

### Start MySQL service
```bash
sudo systemctl start mysql
```

## Build native image (Optional)

- Make sure GraalVM CE 21 or higher is installed on your system and "JAVA_HOME" is set
- Install all necessary dependencies for your OS: https://www.graalvm.org/latest/reference-manual/native-image/
- Make sure `mvnw` file is executable

Build native image:

```bash
native-image --version
```

Build the executable image (Stored in .target/<your-project-name>)

```bash
./mvnw -Pnative native:compile
```

Run the native image

```bash
./target/<your-project-name>
```



