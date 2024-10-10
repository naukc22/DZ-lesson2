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
 * Команда телеграм бота для загрузки посылок из TXT файла в траки.
 * Скаченный файл содержится в TelegramBotCommandRequest.
 * После парсинга файл удаляется.
 * Для результата создает временный файл на сервере.
 * Возвращает json файл с загруженными траками. После отправки файл с сервера удаляется.
 */
@Controller
public class LoadTrucksFromFileCommand implements BotCommand {

    private final TruckServiceImpl truckServiceImpl;

    @Autowired
    public LoadTrucksFromFileCommand(TruckServiceImpl truckServiceImpl) {
        this.truckServiceImpl = truckServiceImpl;
    }


    @Override
    public TelegramBotCommandResponse execute(TelegramBotCommandRequest request) {
        String[] commandArgs = request.getArgs();
        if (commandArgs.length < 2) {
            return new TelegramBotCommandResponse("Использование: /loadTrucksByNames <метод_загрузки> <размеры_траков>");
        }

        if (request.hasFile()) {

            LoadingMode loadingMode = LoadingMode.valueOf(commandArgs[0].toUpperCase());
            String[] truckSizes = commandArgs[1].split(",");

            TruckLoadingProcessSettings settings = new TruckLoadingProcessSettings(truckSizes, loadingMode);
            List<Truck> loadedTrucks = truckServiceImpl.loadPackagesToTrucks(request.getFile().getPath(), settings);
            request.getFile().delete();

            File tempFile = FileUtils.generateTempFileForLoadingTrucks();
            truckServiceImpl.writeTrucksToJsonFile(loadedTrucks, tempFile.getPath());

            return new TelegramBotCommandResponse("Траки успешно загружены.", tempFile);

        } else {
            return new TelegramBotCommandResponse("Отсутствует файл с посылками");
        }

    }


}
