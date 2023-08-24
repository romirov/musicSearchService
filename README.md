# 202306-mss

Учебный проект курса
[Kotlin Backend Developer](https://otus.ru/lessons/kotlin/?int_source=courses_catalog&int_term=programming).
Поток курса 2023-06.

MusicSearchService -- это площадка, на которой пользователи выставляют предложения и потребности. Задача
площадки -- предоставить наиболее подходящие варианты в обоих случаях: для предложения -- набор вариантов с
потребностями, для потребностей -- набор вариантов с предложениями.

## Визуальная схема фронтенда

![Макет фронта](imgs/MusicSearchService.png)

## Документация

1. Маркетинг
    1. [Заинтересанты](./docs/01-marketing/01-stakeholders.md)
    2. [Целевая аудитория](./docs/01-marketing/02-target-audience.md)
    3. [Конкурентный анализ](./docs/01-marketing/03-concurrency.md)
    4. [Пользовательские истории](./docs/01-marketing/04-user-stories.md)
2. DevOps
    1. [Схема инфраструктуры](./docs/02-devops/01-infrastruture.md)
    2. [Схема мониторинга](./docs/02-devops/02-monitoring.md)
3. Тесты
4. Архитектура
    1. [Компонентная схема](./docs/04-architecture/01-arch.md)
    2. [Интеграционная схема](./docs/04-architecture/02-integration.md)
    3. [Описание API](./docs/04-architecture/03-api.md)

# Структура проекта

## Подпроекты для занятий по языку Kotlin

1. [m1l1-quickstart](m1l1-quickstart) - Вводное занятие, создание первой программы на Kotlin

[//]: # (6. [m2l2-testing]&#40;m2l2-testing&#41; - Тестирование проекта, TDD, MDD)

[//]: # ()

[//]: # (## Транспортные модели, API)

[//]: # ()

[//]: # (1. [specs]&#40;specs&#41; - описание API в форме OpenAPI-спецификаций)

[//]: # (2. [api-v1-jackson]&#40;api-v1-jackson&#41; - Генерация первой версии транспортных модеелй с)

[//]: # (   Jackson)

[//]: # (4. [common]&#40;common&#41; - модуль с общими классами для модулей проекта. В частности, там)

[//]: # (   располагаются внутренние модели и контекст.)

[//]: # (5. [mappers-v1]&#40;mappers-v1&#41; - Мапер между внутренними моделями и моделями API v1)

[//]: # ()

[//]: # (## Фреймворки и транспорты)

[//]: # ()

[//]: # (1. [app-ktor]&#40;app-ktor&#41; - Приложение на Ktor JVM/Native)

[//]: # (1. [app-rabbit]&#40;app-rabbit&#41; - Микросервис на RabbitMQ)

[//]: # ()

[//]: # (## Модули бизнес-логики)

[//]: # ()

[//]: # (1. [stubs]&#40;stubs&#41; - Стабы для ответов сервиса)

[//]: # (1. [biz]&#40;biz&#41; - Модуль бизнес-логики приложения)

[//]: # ()

[//]: # (## Хранение, репозитории, базы данных)

[//]: # ()

[//]: # (1. [repo-tests]&#40;repo-tests&#41; - Базовые тесты для репозиториев всех баз данных)

[//]: # (2. [repo-inmemory]&#40;repo-inmemory&#41; - Репозиторий на базе кэша в памяти для тестирования)

[//]: # (3. [repo-postgresql]&#40;repo-postgresql&#41; - Репозиторий на базе PostgreSQL)

[//]: # (### Функции &#40;эндпониты&#41;)

[//]: # ()

[//]: # (1. CRUDS &#40;create, read, update, delete, search&#41; для тем &#40;topic&#41;)

[//]: # ()

[//]: # (### Описание сущности topic)

[//]: # ()

[//]: # (1. Info)

[//]: # (    1. Title)

[//]: # (    2. Description)

[//]: # (    3. Owner)

[//]: # (    4. Status)

[//]: # (2. TopicSide: Questioner/Respondent)

[//]: # (3. TopicType &#40;country music, ...&#41;)

[//]: # (4. TopicId - идентификатор топика)
