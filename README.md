# Step 8: Scheduling & Async (profile `sched`)

## Идея
Периодически находим просроченные задачи и обрабатываем их в фоне.

## Что добавлено
- `SchedulingConfig` с `@EnableScheduling` и `@EnableAsync` (только для профиля `sched`).
- `OverdueTaskScheduler`:
  - каждые `app.scheduling.overdue.fixedDelay` мс (по умолчанию 60 сек) обходит пользователей,
  - берёт pending-задачи через `TaskServiceApi`,
  - фильтрует просроченные (targetDate < now, не deleted),
  - **если активен `amqp`** — публикует `TaskOverdueEvent` в exchange `task.events` с routing key `task.overdue`,
  - **иначе** — записывает уведомление напрямую в БД (фолбэк).
- `TaskOverdueEvent` — DTO события.
- `application-sched.properties` — настройки профиля `sched`.
- `docker-compose.sched.yml` — пример запуска `pg + app` (без брокера).

## Запуск (без брокера)
```bash
docker-compose -f docker-compose.sched.yml build --no-cache
docker-compose -f docker-compose.sched.yml up
```
В этом режиме уведомления о просрочках пишутся напрямую в БД.

## Запуск (с брокером)
Активируй `amqp` вместе с `sched` (см. Step 7 docker-compose):
```bash
SPRING_PROFILES_ACTIVE=pg,sched,amqp docker-compose -f docker-compose.rabbit.yml build --no-cache
docker-compose -f docker-compose.rabbit.yml up
```
Теперь просрочки публикуются как сообщения `task.overdue`.

## Настройки
`app.scheduling.overdue.fixedDelay=60000` (мс) — можно переопределять через `-Dapp.scheduling.overdue.fixedDelay=15000`.
