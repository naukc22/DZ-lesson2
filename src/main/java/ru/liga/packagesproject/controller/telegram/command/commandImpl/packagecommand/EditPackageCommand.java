package ru.liga.packagesproject.controller.telegram.command.commandImpl.packagecommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.liga.packagesproject.controller.telegram.command.BotCommand;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandRequest;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandResponse;
import ru.liga.packagesproject.service.impl.PackageServiceImpl;

import java.util.List;

@Controller
public class EditPackageCommand implements BotCommand {

    private final PackageServiceImpl packageService;

    @Autowired
    public EditPackageCommand(PackageServiceImpl packageService) {
        this.packageService = packageService;
    }


    @Override
    public TelegramBotCommandResponse execute(TelegramBotCommandRequest request) {
        String[] commandArgs = request.getArgs();
        if (commandArgs.length < 3) {
            return new TelegramBotCommandResponse("Использование: /edit <name> <symbol> <form>");
        }
        String name = commandArgs[0];
        char symbol = commandArgs[1].charAt(0);
        List<String> form = List.of(commandArgs[2].split(","));

        try {
            packageService.update(name, symbol, form);
            return new TelegramBotCommandResponse("Посылка успешно обновлена: " + name);
        } catch (Exception e) {
            return new TelegramBotCommandResponse("Ошибка при обновлении посылки: " + e.getMessage());
        }
    }
}
