package ru.liga.packagesproject.controller.telegram.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.liga.packagesproject.service.impl.DefaultPackageService;

import java.util.List;

@Controller
public class EditPackageCommand implements BotCommand {

    private final DefaultPackageService packageService;

    @Autowired
    public EditPackageCommand(DefaultPackageService packageService) {
        this.packageService = packageService;
    }

    @Override
    public String execute(String[] args) {
        if (args.length < 3) {
            return "Использование: /edit <name> <symbol> <form>";
        }
        String name = args[0];
        char symbol = args[1].charAt(0);
        List<String> form = List.of(args[2].split(","));

        try {
            packageService.updatePackage(name, symbol, form);
            return "Посылка успешно обновлена: " + name;
        } catch (Exception e) {
            return "Ошибка при обновлении посылки: " + e.getMessage();
        }
    }

}
