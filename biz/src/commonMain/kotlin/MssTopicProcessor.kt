package ru.mss.biz

import ru.mss.biz.groups.operation
import ru.mss.biz.groups.stubs
import ru.mss.biz.workers.*
import ru.mss.common.MssContext
import ru.mss.common.MssCorSettings
import ru.mss.common.models.MssCommand
import ru.mss.common.models.MssState
import ru.mss.common.models.MssTopicStatus
import ru.mss.common.models.MssWorkMode
import ru.mss.lib.rootChain
import ru.mss.stubs.MssTopicStub

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
            }
            operation("Получить объявление", MssCommand.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
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
            }
            operation("Удалить объявление", MssCommand.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
            }
            operation("Поиск объявлений", MssCommand.SEARCH) {
                stubs("Обработка стабов") {
                    stubSearchSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }

            }
        }.build()
    }
}