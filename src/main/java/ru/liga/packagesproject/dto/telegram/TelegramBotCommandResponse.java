package ru.liga.packagesproject.dto.telegram;

import lombok.Getter;

import java.io.File;

@Getter
public class TelegramBotCommandResponse {

    private final String message;
    private File file;
    private final boolean hasFile;

    public TelegramBotCommandResponse(String message) {
        this.message = message;
        this.hasFile = false;
    }

    public TelegramBotCommandResponse(String message, File file) {
        this.message = message;
        this.file = file;
        this.hasFile = true;
    }

    public boolean hasFile() {
        return hasFile;
    }

}
