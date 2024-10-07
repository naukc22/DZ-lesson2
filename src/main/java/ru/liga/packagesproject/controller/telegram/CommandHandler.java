package ru.liga.packagesproject.controller.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.liga.packagesproject.controller.telegram.command.*;

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
            EditPackageCommand editPackageCommand
    ) {
        commands.put("/add", addPackageCommand);
        commands.put("/list", listPackagesCommand);
        commands.put("/remove", removePackageCommand);
        commands.put("/edit", editPackageCommand);

    }

    public String handleCommand(String command, String[] args) {
        BotCommand botCommand = commands.get(command);
        if (botCommand != null) {
            return botCommand.execute(args);
        } else {
            return "Неизвестная команда. Используйте /add, /list, /remove, /edit.";
        }
    }

}
