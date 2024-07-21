# Barbershop Web Platform

## Table of Contents

- [Screenshots](#screenshots)
- [Description](#description)
- [What I've learned](#what-ive-learned)
- [Prerequisites ](#prerequisites)
- [How to use](#how-to-use)

## Screenshots
<p>
  <img style="max-width: 100%; height: auto;" alt="workspace, user-controller" src="https://github.com/user-attachments/assets/52f84fd4-c18d-447e-b2fc-4aab18fefc37">
</p>

<p>
  <img style="max-width: 100%; height: auto;" alt="user-reg order-controller order-reserv" src="https://github.com/user-attachments/assets/8877708f-4c37-478b-8b11-efa2d8442140">
</p>

<p>
  <img style="max-width: 100%; height: auto;" alt="u-avatar" src="https://github.com/user-attachments/assets/418b674a-4752-4d08-95d0-b9e6299cd1c9">
</p>

## Description
- REST API for a hair salon management, where a customer can make an appointment for a haircut at a specific hairdresser and a hair salon
- CRUD operations with different endpoints (user, workspace, orders, barbershops) with role based (user, BARBER, ADMIN) limitations
- storing users' avatars in the object storage system MinIO
- sending automatic e-mails to notify clients about a hair salon closing. 

### Tech Stack
- Spring Framework (Boot, REST, JDBC, Security)
- Docker, Testcontainers
- JUnit 5, Mockito, Mailhog
- Flyway
- Swagger
- PostgreSQL


## What I've learned
- how to load flyway migrations
- sending e-mails to users in spring framework
- storing data in object storage systems like MinIO, AWS S3
- how to protect the endpoints using spring security
- using dockerfile, docker compose to automate configuration, build, bootstrap of the app. 
- how to keep track of the app health, using spring actuator, logback, sentry DSN
- how to use testcontainers and to test service, controller, database layers 
- documenting rest-api, using Spring doc OpenAPI

## Prerequisites
- Java 17
- Docker

## How to use
- clone the project
- build the project with **gradle :bootJar**
- launch the project with **docker-compose up**



