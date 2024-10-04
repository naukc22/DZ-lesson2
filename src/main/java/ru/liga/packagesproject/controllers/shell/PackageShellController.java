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
            @ShellOption(help = "Имя посылки, используется для идентификации посылки") String name,
            @ShellOption(help = "Символ, который будет использоваться для визуального представления посылки") char symbol,
            @ShellOption(help = "Форма посылки в виде списка строк, каждая строка представляет один уровень посылки (пример : ****,*  *,****") List<String> form
    ) {
        packageService.addPackage(name, symbol, form);
        System.out.println("Посылка добавлена: " + name);
    }

    @ShellMethod("Редактировать посылку")
    public void editPackage(
            @ShellOption(help = "Имя посылки, которую необходимо редактировать") String name,
            @ShellOption(help = "Новый символ для представления посылки") char newSymbol,
            @ShellOption(help = "Новая форма посылки в виде списка строк") List<String> newForm
    ) {
        packageService.editPackage(name, newSymbol, newForm);
        System.out.println("Посылка обновлена: " + name);
    }

    @ShellMethod("Удалить посылку")
    public void removePackage(@ShellOption(help = "Имя посылки, которую необходимо удалить") String name) {
        packageService.removePackage(name);
        System.out.println("Посылка удалена: " + name);
    }

    @ShellMethod("Показать все посылки")
    public void listPackages() {
        packageService.getAllPackagesList().forEach(System.out::println);
    }
}
