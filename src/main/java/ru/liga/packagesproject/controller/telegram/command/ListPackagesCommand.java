package ru.liga.packagesproject.controller.telegram.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public String execute(String[] args) {
        Iterable<Package> packages = packageService.findAllPackages();
        if (!packages.iterator().hasNext()) {
            return "Нет доступных пакетов.";
        }

        List<String> packagesStr = StreamSupport.stream(packages.spliterator(), false)
                .map(Package::getName)
                .toList();

        return "Доступные посылки:\n" + String.join("\n", packagesStr);
    }
}

