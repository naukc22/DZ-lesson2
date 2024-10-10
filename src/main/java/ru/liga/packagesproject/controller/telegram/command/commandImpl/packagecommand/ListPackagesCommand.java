package ru.liga.packagesproject.controller.telegram.command.commandImpl.packagecommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.liga.packagesproject.controller.telegram.command.BotCommand;
import ru.liga.packagesproject.dto.PackageDto;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandRequest;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandResponse;
import ru.liga.packagesproject.service.impl.PackageServiceImpl;

import java.util.List;

@Controller
public class ListPackagesCommand implements BotCommand {

    private final PackageServiceImpl packageService;

    @Autowired
    public ListPackagesCommand(PackageServiceImpl packageService) {
        this.packageService = packageService;
    }

    @Override
    public TelegramBotCommandResponse execute(TelegramBotCommandRequest request) {
        List<PackageDto> packages = packageService.findAll();
        if (packages.isEmpty()) {
            return new TelegramBotCommandResponse("Нет доступных пакетов.");
        }

        List<String> packagesStr = packages.stream().map(PackageDto::getName).toList();

        return new TelegramBotCommandResponse(String.format("Доступные посылки:%n%s", String.join("\n", packagesStr)));
    }
}

