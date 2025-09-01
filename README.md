# Step 5: PostgreSQL + Flyway (profile `pg`)

В этом шаге мы переключаемся с H2 на **PostgreSQL**, добавляем **Flyway** для миграций БД, и пишем новые тесты с **Mockito**, мокая JPA-репозитории.

## Что добавлено
- Профиль **`pg`** для запуска приложения с PostgreSQL.
- `application-postgres.properties` с конфигурацией подключения.
- **Flyway** миграции: `db/migration/V1__init.sql`, `V2__indexes.sql`.
- Новые сервисы под профиль `pg`: `TaskServicePg`, `UserServicePg`, `NotificationServicePg`.
- Тесты c Mockito: мокают JPA-репозитории и проверяют маппинг/логику.
- `docker-compose.postgres.yml` для поднятия контейнеров `db` (Postgres) + `app` (Spring Boot).

> Старые реализации НЕ удаляются: остаются профили `inmemory` и `h2` из предыдущих шагов.

---

## Зависимости (pom.xml)
- Добавлены:
  - `org.postgresql:postgresql`
  - `org.flywaydb:flyway-core`

`pom.xml` в этом архиве уже содержит нужные зависимости.

---

## Конфигурация профиля `pg`
`src/main/resources/application-postgres.properties`:
```
spring.datasource.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:appdb}
spring.datasource.username=${DB_USER:appuser}
spring.datasource.password=${DB_PASSWORD:apppass}
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
```

---

## Flyway
Миграции лежат в `src/main/resources/db/migration/` и применяются автоматически при старте приложения с профилем `pg`.

- `V1__init.sql` — создание таблиц `users`, `tasks`, `notifications` и внешних ключей.
- `V2__indexes.sql` — полезные индексы по частым фильтрам.

---

## Как запустить (Docker Compose)
В корне проекта рядом с `pom.xml` и `src` положи `docker-compose.postgres.yml` и запусти:

```bash
docker compose -f docker-compose.postgres.yml build --no-cache
docker compose -f docker-compose.postgres.yml up
```

Приложение стартует на `http://localhost:8080`, БД — `localhost:5432`.

- Профиль активируется через переменную окружения: `SPRING_PROFILES_ACTIVE=pg`.
- Приложение ждёт, пока Postgres станет здоровым (healthcheck).

Остановить:
```bash
docker compose -f docker-compose.postgres.yml down
```

---

## Как запустить локально (без Docker)
1) Подними локально Postgres и создай БД/пользователя:
```sql
CREATE DATABASE appdb;
CREATE USER appuser WITH ENCRYPTED PASSWORD 'apppass';
GRANT ALL PRIVILEGES ON DATABASE appdb TO appuser;
```

2) Экспортируй переменные окружения (опционально) и запусти Spring Boot:
```bash
export SPRING_PROFILES_ACTIVE=pg
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=appdb
export DB_USER=appuser
export DB_PASSWORD=apppass

mvn spring-boot:run
```

При старте Flyway выполнит миграции `V1`, `V2`.

---

## Тесты (Mockito)
Тесты используют мок-репозитории (например, `TaskJpaRepository`) — это быстрые unit-тесты, не требующие реальной БД.  
Запуск:
```bash
mvn test
```

Примеры тестов:
- `TaskServicePgTest` — проверка маппинга и soft delete с моками.
- `UserServicePgTest` — проверка `register`, `login`.
- `NotificationServicePgTest` — проверка `pending` и create.

---

## Профили в проекте
- `inmemory` — реализация на коллекциях (Step 1/2)
- `h2` — JPA + H2 (Step 3)
- `pg` — JPA + PostgreSQL + Flyway (Step 5)

Контроллеры зависят от интерфейсов сервисов, поэтому при переключении профилей менять код контроллеров не нужно.
