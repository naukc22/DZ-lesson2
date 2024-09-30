package ru.liga.packagesproject.controllers.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.liga.packagesproject.services.PackageService;

import java.util.List;

@ShellComponent
public class PackageShellController {

    private final PackageService packageService;

    @Autowired
    public PackageShellController(PackageService packageService) {
        this.packageService = packageService;
    }

    @ShellMethod("Добавить новую посылку")
    public void addPackage(
            @ShellOption String name,
            @ShellOption char symbol,
            @ShellOption List<String> form
    ) {
        packageService.addPackage(name, symbol, form);
        System.out.println("Посылка добавлена: " + name);
    }

    @ShellMethod("Редактировать посылку")
    public void editPackage(
            @ShellOption String name,
            @ShellOption char newSymbol,
            @ShellOption List<String> newForm
    ) {
        packageService.editPackage(name, newSymbol, newForm);
        System.out.println("Посылка обновлена: " + name);
    }

    @ShellMethod("Удалить посылку")
    public void removePackage(@ShellOption String name) {
        packageService.removePackage(name);
        System.out.println("Посылка удалена: " + name);
    }

    @ShellMethod("Показать все посылки")
    public void listPackages() {
        packageService.getAllPackagesList().forEach(System.out::println);
    }
}
