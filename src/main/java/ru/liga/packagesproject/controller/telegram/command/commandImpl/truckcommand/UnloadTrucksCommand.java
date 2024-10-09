package ru.liga.packagesproject.controller.telegram.command.commandImpl.truckcommand;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.liga.packagesproject.controller.telegram.command.BotCommand;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandRequest;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandResponse;
import ru.liga.packagesproject.model.Truck;
import ru.liga.packagesproject.service.impl.DefaultTruckService;
import ru.liga.packagesproject.util.FileUtils;

import java.io.File;
import java.util.List;

/**
 * Класс команда которая выполняет разгрузку траков. Обращается к TruckService для выполнения методами unloadTrucksFromJsonFile
 */
@Controller
public class UnloadTrucksCommand implements BotCommand {

    private final DefaultTruckService defaultTruckService;

    @Autowired
    public UnloadTrucksCommand(DefaultTruckService defaultTruckService) {
        this.defaultTruckService = defaultTruckService;
    }

    @Override
    public TelegramBotCommandResponse execute(TelegramBotCommandRequest request) {
        String[] commandArgs = request.getArgs();
        if (commandArgs.length > 0) {
            return new TelegramBotCommandResponse("Использование: /unload_trucks");
        }

        if (request.hasFile()) {

            List<Truck> unloadTrucks = defaultTruckService.unloadTrucksFromJsonFile(request.getFile().getPath());
            request.getFile().delete();

            File tempFileWithLoadedTrucks = FileUtils.generateTempFileForLoadingTrucks();
            defaultTruckService.writeTrucksToJsonFile(unloadTrucks, tempFileWithLoadedTrucks.getPath());

            return new TelegramBotCommandResponse("Траки успешно разгружены.", tempFileWithLoadedTrucks);

        } else {
            return new TelegramBotCommandResponse("Отсутствует файл с траками");
        }

    }

}
