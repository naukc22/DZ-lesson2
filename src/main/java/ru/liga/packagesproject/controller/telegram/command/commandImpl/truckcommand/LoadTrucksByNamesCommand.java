package ru.liga.packagesproject.controller.telegram.command.commandImpl.truckcommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.liga.packagesproject.controller.telegram.command.BotCommand;
import ru.liga.packagesproject.dto.Truck;
import ru.liga.packagesproject.dto.TruckLoadingProcessSettings;
import ru.liga.packagesproject.dto.enums.LoadingMode;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandRequest;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandResponse;
import ru.liga.packagesproject.service.impl.TruckServiceImpl;
import ru.liga.packagesproject.service.util.FileUtils;

import java.io.File;
import java.util.List;

/**
 * Команда телеграм бота для загрузки посылок по именам в траки. Создает временный файл на сервере.
 * Возвращает json файл с загруженными траками. После отправки файл с сервера удаляется.
 */
@Controller
public class LoadTrucksByNamesCommand implements BotCommand {

    private final TruckServiceImpl truckServiceImpl;


    @Autowired
    public LoadTrucksByNamesCommand(TruckServiceImpl truckServiceImpl) {
        this.truckServiceImpl = truckServiceImpl;
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
            List<Truck> loadedTrucks = truckServiceImpl.loadPackagesToTrucks(packageNames, settings);
            File tempFileWithLoadedTrucks = FileUtils.generateTempFileForLoadingTrucks();
            truckServiceImpl.writeTrucksToJsonFile(loadedTrucks, tempFileWithLoadedTrucks.getPath());

            return new TelegramBotCommandResponse("Траки успешно загружены.", tempFileWithLoadedTrucks);

        } catch (Exception e) {
            return new TelegramBotCommandResponse("Ошибка при загрузке траков: " + e.getMessage());
        }
    }

}
