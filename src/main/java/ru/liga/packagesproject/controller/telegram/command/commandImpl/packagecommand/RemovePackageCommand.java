package ru.liga.packagesproject.controller.telegram.command.commandImpl.packagecommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.liga.packagesproject.controller.telegram.command.BotCommand;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandRequest;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandResponse;
import ru.liga.packagesproject.service.impl.PackageServiceImpl;

@Controller
public class RemovePackageCommand implements BotCommand {

    private final PackageServiceImpl packageService;

    @Autowired
    public RemovePackageCommand(PackageServiceImpl packageService) {
        this.packageService = packageService;
    }

    @Override
    public TelegramBotCommandResponse execute(TelegramBotCommandRequest request) {
        String[] commandArgs = request.getArgs();
        if (commandArgs.length < 1) {
            return new TelegramBotCommandResponse("Использование: /remove <name>");
        }
        String name = commandArgs[0];

        try {
            packageService.remove(name);
            return new TelegramBotCommandResponse("Посылка успешно удалена: " + name);
        } catch (Exception e) {
            return new TelegramBotCommandResponse("Ошибка при удалении посылки: " + e.getMessage());
        }
    }
}
