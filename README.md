<h1 align="center">Your Review API</h1>

![Badge Em Desenvolvimento](https://img.shields.io/static/v1?label=Status&message=Em%20Desenvolvimento&color=yellow&style=for-the-badge)
![Badge Java](https://img.shields.io/static/v1?label=Java&message=17&color=437291&style=for-the-badge&logo=openjdk&logoColor=437291)
![Badge Spring](https://img.shields.io/static/v1?label=SpringBoot&message=v3.2.0&color=brightgreen&style=for-the-badge&logo=SpringBoot)
![Badge Postgresql](https://img.shields.io/static/v1?label=PostgreSQL&message=v15.3&color=blue&style=for-the-badge&logo=PostgreSQL)

## Resumo do projeto

Sistema para avaliação de filmes e séries.

## Tecnologias e ferramentas

<a href="https://www.jetbrains.com/idea/" target="_blank"><img src="https://img.shields.io/badge/intellij-000000.svg?&style=for-the-badge&logo=intellijidea&logoColor=white" target="_blank"></a>

<a href="https://pt.wikipedia.org/wiki/Java_(linguagem_de_programa%C3%A7%C3%A3o)" target="_blank"><img src="https://img.shields.io/badge/java%2017-D32323.svg?&style=for-the-badge&logo=java&logoColor=white" target="_blank"></a>

<a href="https://spring.io/projects/spring-boot" target="_blank"><img src="https://img.shields.io/badge/Springboot-6db33f.svg?&style=for-the-badge&logo=springboot&logoColor=white" target="_blank"></a>
<a href="https://spring.io/projects/spring-data-jpa" target="_blank"><img src="https://img.shields.io/badge/Spring%20Data%20JPA-6db33f.svg?&style=for-the-badge&logo=spring&logoColor=white" target="_blank"></a>
<a href="https://spring.io/projects/spring-security" target="_blank"><img src="https://img.shields.io/badge/Spring%20Security-6db33f.svg?&style=for-the-badge&logo=springsecurity&logoColor=white" target="_blank"></a>

<a href="https://maven.apache.org/" target="_blank"><img src="https://img.shields.io/badge/Apache%20Maven-b8062e.svg?&style=for-the-badge&logo=apachemaven&logoColor=white" target="_blank"></a>

<a href="https://tomcat.apache.org/" target="_blank"><img src="https://img.shields.io/badge/Apache%20Tomcat-F8DC75.svg?&style=for-the-badge&logo=apachetomcat&logoColor=black" target="_blank"></a>

<a href="https://www.docker.com/" target="_blank"><img src="https://img.shields.io/badge/Docker-2496ED.svg?&style=for-the-badge&logo=docker&logoColor=white" target="_blank"></a>
<a href="https://www.postgresql.org/" target="_blank"><img src="https://img.shields.io/badge/PostgreSQL-4169E1.svg?&style=for-the-badge&logo=postgresql&logoColor=white" target="_blank"></a>
<a href="https://flywaydb.org/" target="_blank"><img src="https://img.shields.io/badge/Flyway-CC0200.svg?&style=for-the-badge&logo=flyway&logoColor=white" target="_blank"></a>

<a href="https://projectlombok.org/" target="_blank"><img src="https://img.shields.io/badge/Lombok-a4a4a4.svg?&style=for-the-badge&logo=lombok&logoColor=black" target="_blank"></a>
<a href="https://github.com/jwtk/jjwt" target="_blank"><img src="https://img.shields.io/badge/JJWT-a4a4a4.svg?&style=for-the-badge&logo=JJWT&logoColor=black" target="_blank"></a>

<a href="https://junit.org/junit5/" target="_blank"><img src="https://img.shields.io/badge/JUnit%205-25A162.svg?&style=for-the-badge&logo=junit5&logoColor=white" target="_blank"></a>
<a href="https://site.mockito.org/" target="_blank"><img src="https://img.shields.io/badge/Mockito-C5D9C8.svg?&style=for-the-badge" target="_blank"></a>
<a href="https://www.postman.com/" target="_blank"><img src="https://img.shields.io/badge/postman-ff6c37.svg?&style=for-the-badge&logo=postman&logoColor=white" target="_blank"></a>
<a href="https://en.wikipedia.org/wiki/Unit_testing" target="_blank"><img src="https://img.shields.io/badge/Unit%20Tests-5a61d6.svg?&style=for-the-badge&logo=unittest&logoColor=white" target="_blank"></a>

## Funcionalidades

### API de gerenciamento de usuário

- `Cadastrar Usuário - POST /api/v1/users`: Cadastrar usuário enviando as informações **name**, **email**, 
**password** e **confirmPassword** em um JSON no corpo da requisição.<br>
    - O password é salvo criptografado no banco de dados usando BCryp.

  Segue abaixo um exemplo do corpo da requisição.
    ```json
    {
      "name": "Lorem Ipsum",
      "email": "lorem@email.com",
      "password": "1234567890",
      "confirmPassword": "1234567890"
    }
    ```
  Em caso de sucesso a resposta tem status 201 com um JSON no corpo da resposta contendo **id**, **name**, **email**,
  **status** e **createdAt** do usuário cadastrado.

  Segue abaixo um exemplo do corpo da resposta.
    ```json
    {
      "id": 150,
      "name": "Lorem Ipsum",
      "email": "lorem@email.com",
      "status": "ENABLED",
      "createdAt": "2023-12-10T10:00:00"
    }
    ```

### API de gerenciamento de autenticação

- `Sign In de Usuário - POST /api/v1/auth/signin`: Sign In de usuário enviando as informações **username** 
e **password** em um JSON no corpo da requisição.<br>

  Segue abaixo um exemplo do corpo da requisição.
    ```json
    {
      "username": "lorem@email.com",
      "password": "1234567890"
    }
    ```
  Em caso de sucesso a resposta tem status 200 com um JSON no corpo da resposta contendo **token** e **type**, 
onde **token** é um JWT que deve ser enviado em todas as requisições que requerem usuário autenticado, e **type** é 
o tipo do token, no caso desse sistema é o tipo Bearer. 

  Segue abaixo um exemplo do corpo da resposta.
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJZb3VyIFJldmlldyBBUEkiLCJzdWIiOiJsb3JlbUBlbWFpbC5jb20iLCJpYXQiOjE3MDMyNjM1NTgsImV4cCI6MTcwMzI2NTM1OH0.zGt6_Essq4jBPMzSIzniWJ-l0IONB1RyqvOrmRfBr9M",
      "type": "Bearer"
    }
    ```

## Diagramas

### Diagrama entidade relacionamento

```mermaid
---
    title: Database Schema
---
    erDiagram
        USER {
            bigserial id PK
            varchar(150) name
            varchar(320) email
            varchar(255) password
            varchar(50) status
            timestamp created_at
        }
        AUTHORITY {
            serial id PK
            varchar(100) name
        }
        USER_AUTHORITIES {
            bigint user_id FK
            int authority_id FK
        }
        
        USER }o--o{ USER_AUTHORITIES : has
        AUTHORITY }o--o{ USER_AUTHORITIES : allows
```