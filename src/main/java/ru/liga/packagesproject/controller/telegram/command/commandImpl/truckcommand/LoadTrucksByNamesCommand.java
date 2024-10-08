package ru.liga.packagesproject.controller.telegram.command.commandImpl.truckcommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.liga.packagesproject.controller.telegram.command.BotCommand;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandRequest;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandResponse;
import ru.liga.packagesproject.enums.LoadingMode;
import ru.liga.packagesproject.models.Truck;
import ru.liga.packagesproject.models.TruckLoadingProcessSettings;
import ru.liga.packagesproject.service.impl.DefaultTruckService;
import ru.liga.packagesproject.util.TelegramBotUtils;
import ru.liga.packagesproject.util.FileUtils;

import java.io.File;
import java.util.List;

@Controller
public class LoadTrucksByNamesCommand implements BotCommand {

    private final DefaultTruckService defaultTruckService;


    @Autowired
    public LoadTrucksByNamesCommand(DefaultTruckService defaultTruckService) {
        this.defaultTruckService = defaultTruckService;
    }

    @Override
    public TelegramBotCommandResponse execute(TelegramBotCommandRequest request) {
        String[] commandArgs = request.getArgs();
        if (commandArgs.length < 3) {
            return new TelegramBotCommandResponse("Использование: /loadTrucksByNames <список_имен_посылок> <метод_загрузки> <размеры_траков>");
        }

        try {
            String[] packageNames = commandArgs[0].split(",");
            LoadingMode loadingMode = LoadingMode.valueOf(commandArgs[1].toUpperCase());
            String[] truckSizes = commandArgs[2].split(",");

            TruckLoadingProcessSettings settings = new TruckLoadingProcessSettings(truckSizes, loadingMode);
            List<Truck> loadedTrucks = defaultTruckService.loadPackagesToTrucks(packageNames, settings);
            File tempFileWithLoadedTrucks = FileUtils.generateTempFileForLoadingTrucks();
            defaultTruckService.writeTrucksToJsonFile(loadedTrucks, tempFileWithLoadedTrucks.getPath());

            return new TelegramBotCommandResponse("Траки успешно загружены.", tempFileWithLoadedTrucks);

        } catch (Exception e) {
            return new TelegramBotCommandResponse("Ошибка при загрузке траков: " + e.getMessage());
        }
    }

}
