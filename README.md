# Step 2: Unit Tests for Step 1

## Описание
В этом шаге добавлены unit-тесты для Step 1 с использованием JUnit 5 и Mockito.  
Покрываются сервисы и контроллеры для задач, пользователей и уведомлений.  

## Стек технологий
- Java 17
- Spring Boot 3
- Maven
- JUnit 5
- Mockito (для mock объектов)
- MockMvc (для тестирования контроллеров)

## Проверяемые сценарии

### TaskServiceTest
- Создание и получение задачи
- Получение pending задач
- Soft delete

### UserServiceTest
- Регистрация пользователя
- Логин
- Логин несуществующего пользователя

### NotificationServiceTest
- Создание уведомления
- Получение всех и pending уведомлений
- Пометка уведомления как прочитанного

### Контроллеры
- `TaskControllerTest` – проверка POST, GET, DELETE для задач
- `UserControllerTest` – проверка регистрации и логина через MockMvc
- `NotificationControllerTest` – проверка GET и GET pending через MockMvc

## Запуск тестов

1. Установить Java 17 и Maven.
2. Выполнить команду:
```bash
mvn test
