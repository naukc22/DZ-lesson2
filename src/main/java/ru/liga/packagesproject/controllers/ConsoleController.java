package ru.liga.packagesproject.controllers;

import ru.liga.packagesproject.controllers.commands.MenuCommand;
import ru.liga.packagesproject.controllers.commands.MenuCommandFactory;

import java.util.Scanner;

/**
 * Основной контроллер консольного интерфейса, отвечающий за взаимодействие с пользователем.
 * Предоставляет меню для выбора команд и выполняет соответствующие команды на основе пользовательского ввода.
 */
public class ConsoleController {

    /**
     * Запускает консольное меню и ожидает ввод пользователя.
     * В зависимости от выбранной опции вызывает соответствующую команду.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        MenuCommandFactory menuCommandFactory = new MenuCommandFactory();
        while (true) {
            showMenu();

            String input = scanner.nextLine();
            MenuCommand command = menuCommandFactory.getMenuCommand(input);
            command.execute(scanner);
        }
    }

    private static void showMenu() {
        System.out.println("Выберите опцию:");
        System.out.println("1. Загрузить траки");
        System.out.println("2. Посчитать посылки в траках");
        System.out.println("0. Выход");
    }
}

