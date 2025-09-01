# Step 4: Docker Support (H2 profile)

В этом шаге мы добавляем поддержку Docker для запуска приложения в контейнере. 
База данных **H2 (in-memory)** работает внутри самого приложения, поэтому в `docker-compose.yml` достаточно одного сервиса `app`.

> **Важно:** На Step 5 будет добавлен PostgreSQL и тогда `docker-compose` будет поднимать **app + db**. 
> В Step 4 — только приложение с профилем `h2`.

---

## Файлы
- `Dockerfile` — multi-stage сборка (Maven → JRE). Сначала собирает jar, затем запускает его на лёгком JRE.
- `docker-compose.yml` — поднимает сервис `app` с активным профилем `h2`.

Структура (положи эти файлы в корень проекта Step 3/Step 2):
```
.
├── pom.xml
├── src/
├── Dockerfile
└── docker-compose.yml
```

---

## Быстрый старт

### 0) Предусловия
- Установлены Docker и Docker Compose (или `docker compose` встроенный в Docker).
- В корне проекта лежат `pom.xml` и `src` (код Step 3).

### 1) Собрать и запустить через Docker Compose
```bash
docker compose build
docker compose up
```
Приложение станет доступно по адресу: http://localhost:8080

Логи:
```bash
docker compose logs -f
```

Остановить:
```bash
docker compose down
```

### 2) Тест запроса (пример)
```bash
curl "http://localhost:8080/users/login?username=alice"
```

Создать задачу:
```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"title":"Do homework","dueDate":"2025-09-10T12:00:00"}'
```

---

## Переменные окружения
- `SPRING_PROFILES_ACTIVE` — по умолчанию `h2` (установлено в Dockerfile и docker-compose). 
- `JAVA_OPTS` — дополнительные флаги JVM, можно пробросить через `docker-compose.yml`.

Пример:
```yaml
environment:
  SPRING_PROFILES_ACTIVE: "h2"
  JAVA_OPTS: "-Xms256m -Xmx512m"
```

---

## Частые проблемы

1. **"target/*.jar not found"**  
   Убедись, что рядом с Dockerfile есть `pom.xml` и каталог `src`, чтобы стадия сборки смогла выполнить `mvn package`.

2. **Порт 8080 занят**  
   Измени публикацию порта в `docker-compose.yml`, например `- "9090:8080"`.

3. **Нужен профиль `inmemory` вместо `h2`**  
   Замени переменную окружения `SPRING_PROFILES_ACTIVE` на `inmemory` в `docker-compose.yml`.

---

## Что изменится на Step 5
На следующем шаге мы добавим **PostgreSQL** и переработаем `docker-compose.yml`, чтобы поднимались два сервиса: `db` (Postgres) и `app`. 
Также добавим миграции с Flyway. В этом шаге (Step 4) мы не меняем базу — только контейнеризуем приложение с H2.
