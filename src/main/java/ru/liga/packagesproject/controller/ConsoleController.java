package ru.liga.packagesproject.controller;

import ru.liga.packagesproject.controller.command.MenuCommand;
import ru.liga.packagesproject.controller.command.MenuCommandFactory;

import java.util.Scanner;

public class ConsoleController {

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

