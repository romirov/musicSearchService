package ru.mss.biz

import ru.mss.biz.groups.operation
import ru.mss.biz.groups.stubs
import ru.mss.biz.validation.*
import ru.mss.biz.workers.*
import ru.mss.common.MssContext
import ru.mss.common.MssCorSettings
import ru.mss.common.models.MssCommand
import ru.mss.common.models.MssState
import ru.mss.common.models.MssTopicId
import ru.mss.lib.chain
import ru.mss.lib.rootChain
import ru.mss.lib.worker
import ru.mss.stubs.MssTopicStub.prepareResult

class MssTopicProcessor(
    @Suppress("unused")
    private val corSettings: MssCorSettings = MssCorSettings.NONE
) {
    suspend fun exec(ctx: MssContext) = BusinessChain.exec(ctx.apply { settings = corSettings })

    companion object {
        private val BusinessChain = rootChain<MssContext> {
            initStatus("Инициализация статуса")
            initRepo("Инициализация репозитория")

            operation("Создание объявления", MssCommand.CREATE) {
                stubs("Обработка стабов") {
                    stubCreateSuccess("Имитация успешной обработки")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в topicValidating") { topicValidating = topicRequest.deepCopy() }
                    worker("Очистка id") { topicValidating.id = MssTopicId.NONE }
                    worker("Очистка заголовка") { topicValidating.title = topicValidating.title.trim() }
                    worker("Очистка описания") { topicValidating.description = topicValidating.description.trim() }
                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validateTitleHasContent("Проверка символов")
                    validateDescriptionNotEmpty("Проверка, что описание не пусто")
                    validateDescriptionHasContent("Проверка символов")

                    finishTopicValidation("Завершение проверок")
                }
                chain {
                    title = "Логика сохранения"
                    repoPrepareCreate("Подготовка объекта для сохранения")
                    repoCreate("Создание топика в БД")
                }
                prepareResult("Подготовка ответа")
            }
            operation("Получить объявление", MssCommand.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в topicValidating") { topicValidating = topicRequest.deepCopy() }
                    worker("Очистка id") { topicValidating.id = MssTopicId(topicValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishTopicValidation("Успешное завершение процедуры валидации")
                }
                chain {
                    title = "Логика чтения"
                    repoRead("Чтение топика из БД")
                    worker {
                        title = "Подготовка ответа для Read"
                        on { state == MssState.RUNNING }
                        handle { adRepoDone = adRepoRead }
                    }
                }
                prepareResult("Подготовка ответа")
            }
            operation("Изменить объявление", MssCommand.UPDATE) {
                stubs("Обработка стабов") {
                    stubUpdateSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в topicValidating") { topicValidating = topicRequest.deepCopy() }
                    worker("Очистка id") { topicValidating.id = MssTopicId(topicValidating.id.asString().trim()) }
                    worker("Очистка заголовка") { topicValidating.title = topicValidating.title.trim() }
                    worker("Очистка описания") { topicValidating.description = topicValidating.description.trim() }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateTitleNotEmpty("Проверка на непустой заголовок")
                    validateTitleHasContent("Проверка на наличие содержания в заголовке")
                    validateDescriptionNotEmpty("Проверка на непустое описание")
                    validateDescriptionHasContent("Проверка на наличие содержания в описании")

                    finishTopicValidation("Успешное завершение процедуры валидации")
                }
                chain {
                    title = "Логика сохранения"
                    repoRead("Чтение топика из БД")
                    repoPrepareUpdate("Подготовка объекта для обновления")
                    repoUpdate("Обновление топика в БД")
                }
                prepareResult("Подготовка ответа")
            }
            operation("Удалить объявление", MssCommand.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в topicValidating") {
                        topicValidating = topicRequest.deepCopy()
                    }
                    worker("Очистка id") { topicValidating.id = MssTopicId(topicValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    finishTopicValidation("Успешное завершение процедуры валидации")
                }
                chain {
                    title = "Логика удаления"
                    repoRead("Чтение топика из БД")
                    repoPrepareDelete("Подготовка объекта для удаления")
                    repoDelete("Удаление топика из БД")
                }
                prepareResult("Подготовка ответа")
            }
            operation("Поиск объявлений", MssCommand.SEARCH) {
                stubs("Обработка стабов") {
                    stubSearchSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adFilterValidating") { topicFilterValidating = topicFilterRequest.copy() }

                    finishTopicFilterValidation("Успешное завершение процедуры валидации")
                }
                repoSearch("Поиск топика в БД по фильтру")
                prepareResult("Подготовка ответа")
            }
        }.build()
    }
}