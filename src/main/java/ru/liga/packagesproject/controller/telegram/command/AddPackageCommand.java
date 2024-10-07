package ru.liga.packagesproject.controller.telegram.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.liga.packagesproject.exception.PackageAlreadyExistsException;
import ru.liga.packagesproject.service.impl.DefaultPackageService;

import java.util.List;

@Service
public class AddPackageCommand implements BotCommand{

    private final DefaultPackageService packageService;

    @Autowired
    public AddPackageCommand(DefaultPackageService packageService) {
        this.packageService = packageService;
    }

    @Override
    public String execute(String[] args) {
        if (args.length < 3) {
            return "Использование: /add <name> <symbol> <form>";
        }
        String name = args[0];
        char symbol = args[1].charAt(0);
        List<String> form = List.of(args[2].split(","));

        try {
            packageService.createPackage(name, symbol, form);
            return "Посылка успешно добавлена: " + name;
        } catch (PackageAlreadyExistsException e) {
            return e.getMessage();
        }
    }

}
