package ru.liga.packagesproject.controller.telegram.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.liga.packagesproject.service.impl.DefaultPackageService;

@Controller
public class RemovePackageCommand implements BotCommand {

    private final DefaultPackageService packageService;

    @Autowired
    public RemovePackageCommand(DefaultPackageService packageService) {
        this.packageService = packageService;
    }

    @Override
    public String execute(String[] args) {
        if (args.length < 1) {
            return "Использование: /remove <name>";
        }
        String name = args[0];

        try {
            packageService.removePackage(name);
            return "Посылка успешно удалена: " + name;
        } catch (Exception e) {
            return "Ошибка при удалении посылки: " + e.getMessage();
        }
    }

}
