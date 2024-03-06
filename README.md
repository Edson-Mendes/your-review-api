<h1 align="center">Your Review API</h1>

![Badge Em Desenvolvimento](https://img.shields.io/static/v1?label=Status&message=Em%20Desenvolvimento&color=yellow&style=for-the-badge)
![Badge Java](https://img.shields.io/static/v1?label=Java&message=17&color=437291&style=for-the-badge&logo=openjdk&logoColor=437291)
![Badge Spring](https://img.shields.io/static/v1?label=SpringBoot&message=v3.2.0&color=brightgreen&style=for-the-badge&logo=SpringBoot)
![Badge Postgresql](https://img.shields.io/static/v1?label=PostgreSQL&message=v15.3&color=blue&style=for-the-badge&logo=PostgreSQL)
![Badge Redis](https://img.shields.io/static/v1?label=Redis&message=v7.2.3&color=DC382D&style=for-the-badge&logo=redis)

## Resumo do projeto

Sistema onde o usuário pode dar seu voto e opinião sobre filmes, ler as avaliações de outros usuários sobre algum filme, 
pesquisar pelos filmes mais bem avaliados (ou pior avaliados).

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
<a href="https://redis.io/" target="_blank"><img src="https://img.shields.io/badge/redis-DC382D.svg?style=for-the-badge&logo=redis&logoColor=white" target="_blank"></a>
<a href="https://flywaydb.org/" target="_blank"><img src="https://img.shields.io/badge/Flyway-CC0200.svg?&style=for-the-badge&logo=flyway&logoColor=white" target="_blank"></a>

<a href="https://projectlombok.org/" target="_blank"><img src="https://img.shields.io/badge/Lombok-a4a4a4.svg?&style=for-the-badge&logo=lombok&logoColor=black" target="_blank"></a>
<a href="https://github.com/jwtk/jjwt" target="_blank"><img src="https://img.shields.io/badge/JJWT-a4a4a4.svg?&style=for-the-badge&logo=JJWT&logoColor=black" target="_blank"></a>

<a href="https://junit.org/junit5/" target="_blank"><img src="https://img.shields.io/badge/JUnit%205-25A162.svg?&style=for-the-badge&logo=junit5&logoColor=white" target="_blank"></a>
<a href="https://site.mockito.org/" target="_blank"><img src="https://img.shields.io/badge/Mockito-C5D9C8.svg?&style=for-the-badge" target="_blank"></a>
<a href="https://www.postman.com/" target="_blank"><img src="https://img.shields.io/badge/postman-ff6c37.svg?&style=for-the-badge&logo=postman&logoColor=white" target="_blank"></a>
<a href="https://en.wikipedia.org/wiki/Unit_testing" target="_blank"><img src="https://img.shields.io/badge/Unit%20Tests-5a61d6.svg?&style=for-the-badge&logo=unittest&logoColor=white" target="_blank"></a>
<a href="https://en.wikipedia.org/wiki/Integration_testing" target="_blank"><img src="https://img.shields.io/badge/Integration%20Tests-5a61d6.svg?&style=for-the-badge&logo=unittest&logoColor=white" target="_blank"></a>

<a href="https://developer.themoviedb.org/docs/getting-started" target="_blank"><img src="https://img.shields.io/badge/TMDb%20API-01B4E4.svg?&style=for-the-badge&logo=themoviedatabase&logoColor=white" target="_blank"></a>

## Funcionalidades

### API de gerenciamento de usuário

- `Cadastrar Usuário - POST /api/v1/users`: Cadastrar usuário enviando as informações **name**, **email**, 
**password** e **confirmPassword** em um JSON no corpo da requisição.<br>
    - O password é salvo criptografado no banco de dados usando BCryp.

  Segue abaixo um exemplo do corpo da requisição.
    ```json
    {
      "name": "John Doe",
      "email": "john.doe@email.com",
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
      "name": "John Doe",
      "email": "john.doe@email.com",
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
      "username": "john.doe@email.com",
      "password": "1234567890"
    }
    ```
  Em caso de sucesso a resposta tem status 200 com um JSON no corpo da resposta contendo **token** e **type**, 
onde **token** é um JWT que deve ser enviado em todas as requisições que requerem usuário autenticado, e **type** é 
o tipo do token, no caso desse sistema é o tipo Bearer. 

  Segue abaixo um exemplo do corpo da resposta.
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJZb3VyIFJldmlldyBBUEkiLCJzdWIiOiJqb2huLmRvZUBlbWFpbC5jb20iLCJpYXQiOjE3MDk3NDUwMzcsImV4cCI6MTcwOTgzMTQzN30.UdB0UBvF6prrluYVWJ3eSN_-W2fe8-5ks1fNrR_xKt8",
      "type": "Bearer"
    }
    ```

- `Refresh token - GET /api/v1/auth/refresh`: Refresh o JWT de autenticação, como o token tem duração limitada o usuário pode solicitar um novo token através desse endpoint sem precisar enviar as credenciais novamente.

  - É necessário estar autenticado.

  Em caso de sucesso a resposta tem status 200 com um JSON no corpo da resposta.
  
  Segue abaixo um exemplo do corpo da resposta.
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJZb3VyIFJldmlldyBBUEkiLCJzdWIiOiJqb2huLmRvZUBlbWFpbC5jb20iLCJpYXQiOjE3MDk3NDUwNjMsImV4cCI6MTcwOTgzMTQ2M30.0NnAvB6f_tVkkAeqhIDgg216wjRetZyEE8d57AYXiFA",
      "type": "Bearer"
    }
    ```

### API de gerenciamento de filme

- `Buscar filme por nome - GET /api/v1/movies?name={movieName}&page={pageNumber}`: Buscar filme por nome informando 
o nome do filme como query string. A busca é paginada de forma zero-based (começa em 0), caso o cliente queira outra página da pesquisa deve passar a 
query param **page**, o valor padrão de page é 0. O tamanho da página solicitada (page size) é 20.

  Em caso de sucesso a resposta tem status 200 com um JSON no corpo da resposta.

  Segue abaixo um exemplo do corpo da resposta para a requisição **GET /api/v1/movies?name=lord&page=0**
    ```json
    {
      "content": [
        {
          "id": "512450",
          "title": "Lord",
          "releaseDate": "2011-09-23",
          "posterPath": "/1T9hYEf3bN0ldVVgJYetb2sWJcU.jpg"
        },
        {
          "id": "1210885",
          "title": "Lord",
          "releaseDate": "2023-05-06",
          "posterPath": "/4T7QFBSeV4pFRx6TYtxNHmlu4aD.jpg"
        },
        {
          "id": "122",
          "title": "The Lord of the Rings: The Return of the King",
          "releaseDate": "2003-12-01",
          "posterPath": "/rCzpDGLbOoPwLjy3OAm5NUPOTrC.jpg"
        },
        {
          "id": "39102",
          "title": "Dragon Ball Z: Lord Slug",
          "releaseDate": "1991-03-19",
          "posterPath": "/1lmwZTsqwTtvd3m60pyQfhGM2Ut.jpg"
        },
        {
          "id": "120",
          "title": "The Lord of the Rings: The Fellowship of the Ring",
          "releaseDate": "2001-12-18",
          "posterPath": "/6oom5QYQ2yQTMJIbnvbkBL9cHo6.jpg"
        },
        {
          "id": "121",
          "title": "The Lord of the Rings: The Two Towers",
          "releaseDate": "2002-12-18",
          "posterPath": "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg"
        },
        {
          "id": "16407",
          "title": "Dragon Lord",
          "releaseDate": "1982-01-21",
          "posterPath": "/qjQhm2jwV4DSTauOydFJVhVyMS3.jpg"
        },
        {
          "id": "67530",
          "title": "The Lords of Discipline",
          "releaseDate": "1983-02-18",
          "posterPath": "/dFItkg67I2W83tCvs5ktuJZyXHw.jpg"
        },
        {
          "id": "853387",
          "title": "Lord of Misrule",
          "releaseDate": "2023-10-26",
          "posterPath": "/eCNJuGsCNdf2yf4F3UcDg1WZTbo.jpg"
        },
        {
          "id": "369552",
          "title": "L.O.R.D: Legend of Ravaging Dynasties",
          "releaseDate": "2016-09-29",
          "posterPath": "/qIUDi1XDAREkarkwNzOOBgUzqyC.jpg"
        },
        {
          "id": "1830",
          "title": "Lord of War",
          "releaseDate": "2005-09-16",
          "posterPath": "/3MGQD4yXokufNlW1AyRXdiy7ytP.jpg"
        },
        {
          "id": "512731",
          "title": "The White Storm 2: Drug Lords",
          "releaseDate": "2019-07-04",
          "posterPath": "/ja9oIIP33Pgm9295Gn67zBz8Vjy.jpg"
        },
        {
          "id": "840098",
          "title": "Lord of the Ants",
          "releaseDate": "2022-09-08",
          "posterPath": "/uPf3jwm4hRRXFyaGYGYPZhpDJd3.jpg"
        },
        {
          "id": "570269",
          "title": "Trinity Seven: Heaven's Library & Crimson Lord",
          "releaseDate": "2019-03-29",
          "posterPath": "/vh4f6N3VdgzmYnRK3VctwBZWdv9.jpg"
        },
        {
          "id": "630044",
          "title": "L.O.R.D: Legend of Ravaging Dynasties 2",
          "releaseDate": "2020-12-02",
          "posterPath": "/43MlbzxB5OohgvPHmPIvRi4MCVn.jpg"
        },
        {
          "id": "9520",
          "title": "The Thief Lord",
          "releaseDate": "2006-01-04",
          "posterPath": "/mLTON0o7tEk37UQMcnIWpngq1ZK.jpg"
        },
        {
          "id": "920429",
          "title": "The Great and Terrible Day of the Lord",
          "releaseDate": "2021-12-28",
          "posterPath": "/6EyEGGhkaDZLCjxRIRoeUm248G8.jpg"
        },
        {
          "id": "123",
          "title": "The Lord of the Rings",
          "releaseDate": "1978-11-15",
          "posterPath": "/liW0mjvTyLs7UCumaHhx3PpU4VT.jpg"
        },
        {
          "id": "437297",
          "title": "Lord, Give Me Patience",
          "releaseDate": "2017-06-16",
          "posterPath": "/c0UfnzFW7Vk7qX2rYij2G5ahB1z.jpg"
        },
        {
          "id": "888917",
          "title": "Lords of Scam",
          "releaseDate": "2021-11-03",
          "posterPath": "/tPcrO8DJAP0lPeoTiQHRsNBD2EC.jpg"
        }
      ],
      "pageable": {
        "pageNumber": 0,
        "pageSize": 20,
        "sort": {
          "sorted": false,
          "empty": true,
          "unsorted": true
        },
          "offset": 0,
          "paged": true,
          "unpaged": false
      },
      "last": false,
      "totalPages": 34,
      "totalElements": 664,
      "size": 20,
      "number": 0,
      "sort": {
        "sorted": false,
        "empty": true,
        "unsorted": true
      },
      "first": true,
      "numberOfElements": 20,
      "empty": false
    }
    ```

- `Buscar filme por id - GET /api/v1/movies/{id}`: Buscar filme por {id}, onde id é o identificador do filme.

  Em caso de sucesso a resposta tem status 200 com um JSON no corpo da resposta contendo informações detalhadas sobre 
o filme **id**, **title**, **overview**, **releaseDate**, **originalLanguage** e **posterPath**.

  Segue abaixo um exemplo do corpo da resposta para a requisição **GET /api/v1/movies/120**
    ```json
    {
      "id": "120",
      "title": "The Lord of the Rings: The Fellowship of the Ring",
      "overview": "Young hobbit Frodo Baggins, after inheriting a mysterious ring from his uncle Bilbo,must leave his home in order to keep it from falling into the hands of its evil creator. Along the way, a fellowship is formed to protect the ringbearer and make sure that the ring arrives at its final destination: Mt. Doom, the only place where it can be destroyed.",
      "releaseDate": "2001-12-18",
      "originalLanguage": "en",
      "posterPath": "/6oom5QYQ2yQTMJIbnvbkBL9cHo6.jpg"
    }
    ```

### API de gerenciamento de avaliação

- `Cadastrar avaliação - POST /api/v1/reviews`: Cadastrar avaliação enviando as informações **vote**, **opinion** (opcional) 
e **movieId** em um JSON no corpo da requisição.

  - É necessário estar autenticado.

  Segue abaixo um exemplo do corpo da requisição.
    ```json
    {
      "vote": 9,
      "opinion": "o filme capta a profundidade da mitologia de Tolkien, mantendo a essência da história original enquanto faz algumas adaptações necessárias para a transição para o meio cinematográfico. A direção de Peter Jackson foi fundamental para consolidar a grandiosidade da obra.",
      "movieId": "120"
    }
    ```

  Em caso de sucesso a resposta tem status 201 com um JSON no corpo da resposta contendo **id**, **vote**, **opinion**,
  **userId**, **movieId** e **createdAt** da avaliação cadastrada.
    ```json
    {
      "id": 5,
      "vote": 9,
      "opinion": "o filme capta a profundidade da mitologia de Tolkien, mantendo a essência da história original enquanto faz algumas adaptações necessárias para a transição para o meio cinematográfico. A direção de Peter Jackson foi fundamental para consolidar a grandiosidade da obra.",
      "userId": 150,
      "movieId": "120",
      "createdAt": "2024-03-06T14:03:04.873359142"
    }
    ```
  
- `Buscar avaliações por ID do filme - GET /api/v1/reviews?movieId={movieId}&page={pageNumber}`: Buscar avaliações por 
id do filme informando o id do filme como query string. A busca é paginada de forma zero-based (começa em 0), 
caso o cliente queira outra página da pesquisa deve passar a query param **page**, o valor padrão de page é 0. 
O tamanho da página solicitada (page size) é 20.

  - É necessário estar autenticado.

  Em caso de sucesso a resposta tem status 200 com um JSON no corpo da resposta.

  Segue abaixo um exemplo do corpo da resposta para a requisição **GET /api/v1/reviews?movieId=299536&page=0**
    ```json
    {
      "content": [
        {
          "id": 3,
          "vote": 8,
          "opinion": "É um épico cinematográfico que reúne uma variedade impressionante de super-heróis da Marvel em uma batalha contra o poderoso vilão Thanos.",
          "user": {
            "id": 150,
            "name": "John Doe",
            "email": "john.doe@email.com"
          },
          "movieId": "299536"
        },
        {
          "id": 4,
          "vote": 9,
          "opinion": "O filme é repleto de ação espetacular, efeitos visuais impressionantes e uma trama intricada que tece várias narrativas.",
          "user": {
            "id": 151,
            "name": "Jane Doe",
            "email": "jane.doe@email.com"
          },
          "movieId": "299536"
        }
      ],
      "pageable": {
        "pageNumber": 0,
        "pageSize": 20,
        "sort": {
          "sorted": false,
          "empty": true,
          "unsorted": true
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
      },
      "totalPages": 1,
      "totalElements": 2,
      "last": true,
      "size": 20,
      "number": 0,
      "sort": {
        "sorted": false,
        "empty": true,
        "unsorted": true
      },
      "numberOfElements": 2,
      "first": true,
      "empty": false
    }
    ```

- `Buscar avaliação por ID - GET /api/v1/reviews/{reviewID}`: Buscar review por reviewID, onde *reviewID* é o identificador da avaliação.

  - É necessário estar autenticado.

  Em caso de sucesso a resposta tem status 200 com um JSON no corpo da resposta contendo informações detalhadas sobre 
a avaliação **id**, **vote**, **opinion**, **userId**, **movie** (com **id**, **title**, **releaseDate** e **posterPath**) e **createdAt**.

  Segue abaixo um exemplo com corpo da resposta para a requisição *GET /api/v1/reviews/3*.
  ```json
  {
    "id": 3,
    "vote": 8,
    "opinion": "É um épico cinematográfico que reúne uma variedade impressionante de super-heróis da Marvel em uma batalha contra o poderoso vilão Thanos.",
    "userId": 150,
    "movie": {
      "id": "299536",
      "title": "Avengers: Infinity War",
      "releaseDate": "2018-04-25",
      "posterPath": "/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg"
    },
    "createdAt": "2024-03-06T14:23:27.583647"
  }
  ```

- `Atualizar avaliação por reviewID - PUT /api/v1/reviews/{reviewID}`: Atualizar review por *reviewID*, onde *reviewID* é o identificador da avaliação e enviando as informações **vote** e **opinion** (opcional) em um JSON no corpo da requisição.

  - É necessário estar autenticado.

  Segue abaixo um exemplo do corpo da requisição.
    ```json
    {
      "vote": 7,
      "opinion": "o filme capta a profundidade da mitologia de Tolkien, mantendo a essência da história original enquanto faz algumas adaptações necessárias para a transição para o meio cinematográfico. A direção de Peter Jackson foi fundamental para consolidar a grandiosidade da obra."
    }
    ```

  Em caso de sucesso a resposta tem status 200 com um JSON no corpo da resposta contendo **id**, **vote**, **opinion**,
  **userId**, **movieId** e **createdAt** da avaliação cadastrada.
    ```json
    {
      "id": 5,
      "vote": 7,
      "opinion": "o filme capta a profundidade da mitologia de Tolkien, mantendo a essência da história original enquanto faz algumas adaptações necessárias para a transição para o meio cinematográfico. A direção de Peter Jackson foi fundamental para consolidar a grandiosidade da obra.",
      "userId": 150,
      "movieId": "120",
      "createdAt": "2024-03-06T14:03:04.873359142"
    }
    ```

- `Deletar avaliação por reviewID - DELETE /api/v1/reviews/{reviewID}`: Deletar review por *reviewID*, onde *reviewID* é o identificador da avaliação.

  - É necessário estar autenticado.

  Em caso de sucesso a resposta tem status 204.

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
            varchar(320) email UK
            varchar(255) password
            varchar(50) status
            timestamp created_at
        }
        AUTHORITY {
            serial id PK
            varchar(100) name UK
        }
        USER_AUTHORITIES {
            bigint user_id FK
            int authority_id FK
        }
        MOVIE_VOTES {
            bigserial id PK
            varchar(100) movie_id UK
            bigint vote_total
            bigint vote_count
        }
        REVIEW {
            bigserial id PK
            int vote
            text opinion
            bigint user_id FK
            bigint movie_votes_id FK
        }
        
        USER }o--o{ USER_AUTHORITIES : has
        AUTHORITY }o--o{ USER_AUTHORITIES : allows
        REVIEW }|--|| MOVIE_VOTES : belongs
        USER ||--o{ REVIEW : makes
```
