# (main) Step 1: Basic REST API with In-Memory Storage

## Описание
В этом шаге реализован простой REST API на Spring Boot с in-memory хранилищем для задач, пользователей и уведомлений.  
Данные хранятся в `List`/`Map`, база данных пока не используется.  

## Стек технологий
- Java 17
- Spring Boot 3
- Maven
- REST API
- In-memory репозитории (HashMap, ConcurrentHashMap)

## REST API Endpoints

### Users
- `POST /users` – регистрация пользователя  
  **Body:** `{"username":"alice","displayName":"Alice"}`
- `GET /users/login?username=alice` – "логин" (получение пользователя)

### Tasks
- `POST /tasks` – создать задачу  
  **Body:** `{"userId":1,"title":"Task1","dueDate":"2025-09-05T12:00:00"}`
- `GET /tasks?userId=1` – все задачи пользователя
- `GET /tasks/pending?userId=1` – только pending задачи
- `DELETE /tasks/{id}` – soft delete (не удаляет из базы, помечает как deleted)

### Notifications
- `GET /notifications?userId=1` – все уведомления пользователя
- `GET /notifications/pending?userId=1` – только непрочитанные уведомления

## Запуск проекта

1. Установить Java 17 и Maven.
2. Собрать и запустить:
```bash
./mvnw spring-boot:run
3. Приложение будет доступно на http://localhost:8080.
