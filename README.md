# Device Challenge

## Deliverables

 - All features implemented ✅
 - 95% test coverage ✅
 - Swagger documentation ✅
 - Easily executable docker-compose ✅
 - Extra: **Deployment** ✅
 - Extra: Integration tests from controller to containerized DB ✅

## How to run

### Build:

 - Java 21
 - Docker
 - Docker compose

### Running:

 - `./gradlew build`

 - `docker-compose up --build -d`

 - Go to [localhost:8080/swagger-ui.html](localhost:8080/swagger-ui.html)

## Notes

 - Given production readiness is one of the evaluation points, I decided to deploy the API. It can be accessed through this link: http://device-app-env.eba-pa8i3uva.us-east-2.elasticbeanstalk.com/swagger-ui/index.html
 - As unit test approach, I have chosen the classicist/Detroit school because of limited development time and preference for testing the domain logic as priority. [Source here](https://medium.com/@adrianbooth/test-driven-development-wars-detroit-vs-london-classicist-vs-mockist-9956c78ae95f)
 - Initially I was intending to use the hexagonal architecture, but I noticed it would be an over engineering given the project has a pretty simple domain logic. In real life projects I would think about this more seriously, once the project might grow tremendously in some cases.
 - The application is currently deployed in AWS
 - To run the Spring app only, some adjusts in application.properties have to be made

## Things I would like to improve

 - Initially my intention was use auth with JWT and Spring Security, but unfortunately I couldn't do that in time.
 - Controllers exception handlers could be written better in my point of view.
 - I used the default methods of JPA repositories, I would like to have experimented custom queries for performance.
 - I wanted to create database migrations instead of use auto ddl from hibernate.



