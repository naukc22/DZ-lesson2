package ru.liga.packagesproject.controller.telegram.command;

import ru.liga.packagesproject.dto.telegram.TelegramBotCommandRequest;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandResponse;

public interface BotCommand {

    /**
     * Выполняет команду бота
     *
     * @param request объект commandArgs для получения информации о запросе
     * @return commandTelegramBotResponse (тест и файл если есть)
     */

    TelegramBotCommandResponse execute(TelegramBotCommandRequest request);
}
