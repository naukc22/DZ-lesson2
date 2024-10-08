package ru.liga.packagesproject.dto.telegram;

import lombok.Getter;

import java.io.File;

@Getter
public class TelegramBotCommandRequest {

    private final String[] args;
    private File file;
    private final boolean hasFile;

    public TelegramBotCommandRequest(String[] args) {
        this.args = args;
        this.hasFile = false;
    }

    public TelegramBotCommandRequest(String[] args, File file) {
        this.args = args;
        this.file = file;
        this.hasFile = true;
    }

    public boolean hasFile() {
        return hasFile;
    }
}
