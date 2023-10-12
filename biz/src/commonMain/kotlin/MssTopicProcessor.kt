package ru.mss.biz

import ru.mss.biz.groups.operation
import ru.mss.biz.groups.stubs
import ru.mss.biz.validation.*
import ru.mss.biz.workers.*
import ru.mss.common.MssContext
import ru.mss.common.MssCorSettings
import ru.mss.common.models.MssCommand
import ru.mss.common.models.MssTopicId
import ru.mss.lib.rootChain
import ru.mss.lib.worker

class MssTopicProcessor (
    @Suppress("unused")
    private val corSettings: MssCorSettings = MssCorSettings.NONE
) {
    suspend fun exec(ctx: MssContext) = BusinessChain.exec(ctx)

    companion object {
        private val BusinessChain = rootChain<MssContext> {
            initStatus("Инициализация статуса")

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
                        topicValidating = topicRequest.deepCopy() }
                    worker("Очистка id") { topicValidating.id = MssTopicId(topicValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    finishTopicValidation("Успешное завершение процедуры валидации")
                }
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
            }
        }.build()
    }
}