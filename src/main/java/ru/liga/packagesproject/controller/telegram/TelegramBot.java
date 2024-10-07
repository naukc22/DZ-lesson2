package ru.liga.packagesproject.controller.telegram;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            String[] input = message.getText().split(" ", 2);
            String command = input[0];
            String[] args = input.length > 1 ? input[1].split(" ") : new String[0];

            String responseText = commandHandler.handleCommand(command, args);

            SendMessage response = new SendMessage();
            response.setChatId(chatId);
            response.setText(responseText);

            try {
                execute(response);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
    }

}
