package ru.liga.packagesproject.controller.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.liga.packagesproject.exception.PackageAlreadyExistsException;
import ru.liga.packagesproject.exception.PackageNotFoundException;
import ru.liga.packagesproject.service.impl.PackageServiceImpl;

import java.util.List;

@ShellComponent
public class PackageShellController {

    private final PackageServiceImpl packageServiceImpl;

    @Autowired
    public PackageShellController(PackageServiceImpl packageServiceImpl) {
        this.packageServiceImpl = packageServiceImpl;
    }

    @ShellMethod("Добавить новую посылку")
    public void addPackage(
            @ShellOption(help = "Имя посылки, используется для идентификации посылки") String name,
            @ShellOption(help = "Символ, который будет использоваться для визуального представления посылки") char symbol,
            @ShellOption(help = "Форма посылки в виде списка строк, каждая строка представляет один уровень посылки (пример : ****,*  *,****") List<String> form
    ) {
        try {
            packageServiceImpl.create(name, symbol, form);
        } catch (PackageAlreadyExistsException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Посылка добавлена: " + name);
    }

    @ShellMethod("Редактировать посылку")
    public void editPackage(
            @ShellOption(help = "Имя посылки, которую необходимо редактировать") String name,
            @ShellOption(help = "Новый символ для представления посылки") char newSymbol,
            @ShellOption(help = "Новая форма посылки в виде списка строк") List<String> newForm
    ) {
        try {
            packageServiceImpl.update(name, newSymbol, newForm);
            System.out.println("Посылка обновлена: " + name);
        } catch (PackageNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    @ShellMethod("Удалить посылку")
    public void removePackage(@ShellOption(help = "Имя посылки, которую необходимо удалить") String name) {
        try {
            packageServiceImpl.remove(name);
            System.out.println("Посылка удалена: " + name);
        } catch (PackageNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @ShellMethod("Показать все посылки")
    public void listPackages() {
        packageServiceImpl.findAll().forEach(System.out::println);
    }
}
