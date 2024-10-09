package ru.liga.packagesproject.controller.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.packagesproject.controller.telegram.command.BotCommand;
import ru.liga.packagesproject.controller.telegram.command.commandImpl.packagecommand.AddPackageCommand;
import ru.liga.packagesproject.controller.telegram.command.commandImpl.packagecommand.EditPackageCommand;
import ru.liga.packagesproject.controller.telegram.command.commandImpl.packagecommand.ListPackagesCommand;
import ru.liga.packagesproject.controller.telegram.command.commandImpl.packagecommand.RemovePackageCommand;
import ru.liga.packagesproject.controller.telegram.command.commandImpl.truckcommand.LoadTrucksByNamesCommand;
import ru.liga.packagesproject.controller.telegram.command.commandImpl.truckcommand.LoadTrucksFromFileCommand;
import ru.liga.packagesproject.controller.telegram.command.commandImpl.truckcommand.UnloadTrucksCommand;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandRequest;
import ru.liga.packagesproject.dto.telegram.TelegramBotCommandResponse;
import ru.liga.packagesproject.util.TelegramBotUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class CommandHandler {

    private final Map<String, BotCommand> commands = new HashMap<>();

    @Autowired
    public CommandHandler(
            AddPackageCommand addPackageCommand,
            ListPackagesCommand listPackagesCommand,
            RemovePackageCommand removePackageCommand,
            EditPackageCommand editPackageCommand,
            LoadTrucksByNamesCommand loadTrucksByNamesCommand,
            LoadTrucksFromFileCommand loadTrucksFromFileCommand,
            UnloadTrucksCommand unloadTrucksCommand
    ) {
        commands.put("/add", addPackageCommand);
        commands.put("/list", listPackagesCommand);
        commands.put("/remove", removePackageCommand);
        commands.put("/edit", editPackageCommand);
        commands.put("/load_truck_by_names", loadTrucksByNamesCommand);
        commands.put("/load_truck_from_file", loadTrucksFromFileCommand);
        commands.put("/unload_trucks", unloadTrucksCommand);
    }

    /**
     * Метод вызывает команду, в зависимости от переданного TelegramBotCommandRequest
     *
     * @param request класс представляющий данные для запроса
     * @param update  класс update из Telegram API
     * @return TelegramBotCommandResponse
     */
    public TelegramBotCommandResponse handleCommand(TelegramBotCommandRequest request, Update update) {

        String command = TelegramBotUtils.getCommandFromUpdate(update);
        BotCommand botCommand = commands.get(command);

        if (botCommand != null) {
            return botCommand.execute(request);
        } else {
            return new TelegramBotCommandResponse("Неизвестная команда. Используйте: \n/add, \n/list, \n/remove, \n/edit, \n/load_truck_by_names, \n/load_truck_from_file, , \n/unload_trucks");
        }


    }

}
