package ru.liga.packagesproject.controller.telegram.command.commandImpl.packagecommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.liga.packagesproject.controller.telegram.command.BotCommand;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandRequest;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandResponse;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.service.impl.DefaultPackageService;

import java.util.List;
import java.util.stream.StreamSupport;

@Controller
public class ListPackagesCommand implements BotCommand {

    private final DefaultPackageService packageService;

    @Autowired
    public ListPackagesCommand(DefaultPackageService packageService) {
        this.packageService = packageService;
    }

    @Override
    public TelegramBotCommandResponse execute(TelegramBotCommandRequest request) {
        Iterable<Package> packages = packageService.findAllPackages();
        if (!packages.iterator().hasNext()) {
            return new TelegramBotCommandResponse("Нет доступных пакетов.");
        }

        List<String> packagesStr = StreamSupport.stream(packages.spliterator(), false)
                .map(Package::getName)
                .toList();

        return new TelegramBotCommandResponse("Доступные посылки:\n" + String.join("\n", packagesStr));
    }
}

