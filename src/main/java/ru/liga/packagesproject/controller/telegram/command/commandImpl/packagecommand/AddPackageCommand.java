package ru.liga.packagesproject.controller.telegram.command.commandImpl.packagecommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.liga.packagesproject.controller.telegram.command.BotCommand;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandRequest;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandResponse;
import ru.liga.packagesproject.exception.PackageAlreadyExistsException;
import ru.liga.packagesproject.service.impl.PackageServiceImpl;

import java.util.List;

@Service
public class AddPackageCommand implements BotCommand {

    private final PackageServiceImpl packageService;

    @Autowired
    public AddPackageCommand(PackageServiceImpl packageService) {
        this.packageService = packageService;
    }

    @Override
    public TelegramBotCommandResponse execute(TelegramBotCommandRequest request) {
        String[] commandArgs = request.getArgs();
        if (commandArgs.length < 3) {
            return new TelegramBotCommandResponse("Использование: /add <name> <symbol> <form>");
        }
        String name = commandArgs[0];
        char symbol = commandArgs[1].charAt(0);
        List<String> form = List.of(commandArgs[2].split(","));

        try {
            packageService.create(name, symbol, form);
            return new TelegramBotCommandResponse("Посылка успешно добавлена: " + name);
        } catch (PackageAlreadyExistsException e) {
            return new TelegramBotCommandResponse(e.getMessage());
        }
    }
}
