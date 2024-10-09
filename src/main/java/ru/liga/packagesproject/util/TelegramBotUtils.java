package ru.liga.packagesproject.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.packagesproject.controller.telegram.TelegramBot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * Util класс для работы с Telegram Bot, предоставляющий методы
 * для отправки сообщений и файлов, а также обработки обновлений.
 */
@Component
@Slf4j
public class TelegramBotUtils {

    /**
     * Отправляет текстовое сообщение в указанный чат.
     *
     * @param telegramBot экземпляр TelegramBot для отправки сообщения
     * @param chatId     идентификатор чата, куда будет отправлено сообщение
     * @param text       текст сообщения
     */
    public static void sendMessage(TelegramBot telegramBot, String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Отправляет файл в указанный чат с подписью.
     *
     * @param telegramBot экземпляр TelegramBot для отправки файла
     * @param chatId     идентификатор чата, куда будет отправлен файл
     * @param file       файл для отправки
     * @param caption    подпись к файлу
     */
    public static void sendFile(TelegramBot telegramBot, String chatId, File file, String caption) {
        SendDocument sendDocumentRequest = new SendDocument();
        sendDocumentRequest.setChatId(chatId);
        sendDocumentRequest.setDocument(new InputFile(file));
        sendDocumentRequest.setCaption(caption);

        try {
            telegramBot.execute(sendDocumentRequest);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Извлекает команду из обновления (message или caption).
     *
     * @param update объект обновления от Telegram
     * @return строка, содержащая команду
     */
    public static String getCommandFromUpdate(Update update) {
        if (update.getMessage().hasText()) {
            String[] input = update.getMessage().getText().split(" ", 2);
            return input[0];
        }
        if (update.getMessage().getCaption() != null) {
            String[] input = update.getMessage().getCaption().split(" ", 2);
            return input[0];
        }
        return "";
    }

    /**
     * Извлекает аргументы команды из обновления (message или caption).
     *
     * @param update объект обновления от Telegram
     * @return массив строк, содержащий аргументы команды
     */
    public static String[] getCommandArgumentsFromUpdate(Update update) {
        if (update.getMessage().hasText()) {
            String[] input = update.getMessage().getText().split(" ", 2);
            return input.length > 1 ? input[1].split(" ") : new String[0];
        }
        if (update.getMessage().getCaption() != null) {
            String[] input = update.getMessage().getCaption().split(" ", 2);
            return input.length > 1 ? input[1].split(" ") : new String[0];
        }
        return new String[0];
    }

    /**
     * Загружает файл из Telegram и сохраняет его на диск.
     *
     * @param telegramBot экземпляр TelegramBot для загрузки файла
     * @param update      объект обновления от Telegram, содержащий файл
     * @return загруженный файл или null, если произошла ошибка
     */
    public static File downloadFileFromTelegram(TelegramBot telegramBot, Update update) {
        File downloadedFile = null;

        try {
            GetFile getFile = new GetFile();

            Document document = update.getMessage().getDocument();

            getFile.setFileId(document.getFileId());
            org.telegram.telegrambots.meta.api.objects.File telegramFile = telegramBot.execute(getFile);

            String filePath = FileUtils.generateTxtTempFileForPackages().getPath();

            downloadedFile = new java.io.File(filePath);

            try (InputStream is = telegramBot.downloadFileAsStream(telegramFile)) {
                Files.copy(is, downloadedFile.toPath()); // Сохраняем файл на диск
            }

        } catch (TelegramApiException | IOException e) {
            log.error(e.getMessage());
        }

        return downloadedFile;
    }


}
