# Step 7: Messaging with RabbitMQ (profile `amqp`)

## Цель
- Поднять брокер сообщений **RabbitMQ**.
- Публиковать событие при **создании задачи**.
- **Notification service** получает обновления **ТОЛЬКО** из брокера и сохраняет уведомления.
- Обработчик слушает очередь и асинхронно обрабатывает сообщения.

## Что добавлено
- Зависимости: `spring-boot-starter-amqp`, `spring-boot-starter-aop` (аспект публикации).
- Профиль `amqp`: `application-amqp.properties`.
- `RabbitConfig` (`exchange`, `queue`, `binding`, `RabbitTemplate` c JSON-конвертером).
- `TaskCreatePublishAspect`: перехватывает `TaskServiceApi.create(..)` и публикует `TaskCreatedEvent`.
- `NotificationEventListener`: `@RabbitListener` обрабатывает событие и сохраняет `NotificationEntity`.
- `docker-compose.rabbit.yml`: `db` + `rabbit` + `app` (профили `pg,amqp`).

## Запуск
```bash
docker-compose -f docker-compose.rabbit.yml build --no-cache
docker-compose -f docker-compose.rabbit.yml up
# Rabbit UI: http://localhost:15672 (guest/guest)
```

## Проверка
```bash
# создать пользователя
curl -X POST http://localhost:8080/users -H "Content-Type: application/json" -d '{"username":"alice","displayName":"Alice"}'
# создать задачу (опубликуется событие)
curl -X POST http://localhost:8080/tasks -H "Content-Type: application/json" -d '{"userId":1,"title":"Buy milk"}'
# проверить уведомления (созданы listener'ом)
curl "http://localhost:8080/notifications?userId=1"
```

## Профили
- БД: `pg` (или `h2`), кэш: `redis` (опционально), брокер: `amqp`.
- Все старые реализации остаются, новый функционал включается добавлением профиля `amqp`.

