package ru.liga.packagesproject.controller.telegram;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandRequest;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandResponse;
import ru.liga.packagesproject.util.TelegramBotUtils;

import java.io.File;

@Slf4j
@Getter
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;
    private final CommandHandler commandHandler;


    public TelegramBot(
            @Value("${telegram.bot.name}") String botUsername,
            @Value("${telegram.bot.token}") String botToken,
            TelegramBotsApi telegramBotsApi,
            CommandHandler commandHandler
    ) throws TelegramApiException {
        this.botUsername = botUsername;
        this.botToken = botToken;
        telegramBotsApi.registerBot(this);
        this.commandHandler = commandHandler;
    }

    /**
     * Этот метод вызывается при получении обновлений через метод GetUpdates.
     *
     * @param update Получено обновление
     */
    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Получено обновление");
        if (update.hasMessage()) {

            TelegramBotCommandRequest telegramBotCommandRequest = prepareRequest(update);

            TelegramBotCommandResponse response = commandHandler.handleCommand(telegramBotCommandRequest, update);

            String chatId = update.getMessage().getChatId().toString();
            TelegramBotUtils.sendMessage(this, chatId, response.getMessage());

            if (response.hasFile()) {
                TelegramBotUtils.sendFile(this, chatId, response.getFile(), "Результат");
                response.getFile().delete();
            }
        }
    }

    private TelegramBotCommandRequest prepareRequest(Update update) {
        String[] commandArgs = TelegramBotUtils.getCommandArgumentsFromUpdate(update);

        if (update.getMessage().hasDocument()) {
            File file = TelegramBotUtils.downloadFileFromTelegram(this, update);
            return new TelegramBotCommandRequest(commandArgs, file);
        } else {
            return new TelegramBotCommandRequest(commandArgs);
        }
    }
}
