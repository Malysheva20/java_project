# Step 6: Caching with Redis (profile `redis`)

## Что добавляем
- Spring Cache + Redis для кэширования выборок задач.
- Профиль `redis` включает `@EnableCaching` и настраивает TTL через `RedisCacheManager`.

## Файлы в этом шаге
- `pom.xml` — добавлены зависимости `spring-boot-starter-data-redis` и `spring-boot-starter-cache`.
- `src/main/resources/application-redis.properties` — настройки подключения к Redis.
- `src/main/java/com/example/demo/config/RedisConfig.java` — `@EnableCaching`, TTL по кэшеям.

## Как это работает
1. Аннотируй методы сервисов задач (в `TaskServicePg`/`TaskServiceH2`):
   - `@Cacheable(cacheNames="task:get", key="#id")` — `get(Long id)`
   - `@Cacheable(cacheNames="task:all", key="#userId")` — `getAll(Long userId)`
   - `@Cacheable(cacheNames="task:pending", key="#userId")` — `getPending(Long userId)`
   - `@CacheEvict(cacheNames={"task:all","task:pending"}, key="#t.userId")` — в `create(Task t)`
   - `@CacheEvict(cacheNames={"task:get"}, key="#id")` + (опционально) инвалидация списков по пользователю — в `delete(Long id)`
2. Кэш активируется только при профиле `redis`. В остальных профилях аннотации не работают (нет `@EnableCaching`).

## TTL (настраивается в `RedisConfig`)
- `task:get` — 10 минут
- `task:all`, `task:pending` — 2 минуты
- По умолчанию — 5 минут

## Docker Compose (app + db + redis)
Создай `docker-compose.redis.yml` рядом с `pom.xml`:
```yaml
version: "3.9"
services:
  db:
    image: postgres:16
    environment:
      POSTGRES_DB: appdb
      POSTGRES_USER: appuser
      POSTGRES_PASSWORD: apppass
    ports: ["5432:5432"]

  redis:
    image: redis:7-alpine
    ports: ["6379:6379"]
    command: ["redis-server","--save",""]
    healthcheck:
      test: ["CMD","redis-cli","ping"]
      interval: 5s
      timeout: 3s
      retries: 10

  app:
    build: .
    depends_on:
      db:
        condition: service_started
      redis:
        condition: service_healthy
    environment:
      SPRING_PROFILES_ACTIVE: "pg,redis"
      DB_HOST: db
      DB_PORT: 5432
      DB_NAME: appdb
      DB_USER: appuser
      DB_PASSWORD: apppass
      REDIS_HOST: redis
      REDIS_PORT: 6379
    ports: ["8080:8080"]
```

Запуск:
```bash
docker-compose -f docker-compose.redis.yml build --no-cache
docker-compose -f docker-compose.redis.yml up
```

## Локальный запуск
```bash
docker run -p 6379:6379 -d --name step6-redis redis:7-alpine redis-server --save ""
export SPRING_PROFILES_ACTIVE=pg,redis
export REDIS_HOST=localhost
export REDIS_PORT=6379
mvn spring-boot:run
```

## Сохранение прошлых шагов
- `inmemory`, `h2`, `pg` остаются без изменений.
- Кэш включается отдельным профилем `redis`, который можно комбинировать с `pg` или `h2`.
