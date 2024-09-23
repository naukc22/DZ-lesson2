package ru.liga.packagesproject.controller.command;

import java.util.Scanner;

public class ExitCommand implements MenuCommand {
    @Override
    public void execute(Scanner scanner) {
        System.out.println("Завершение программы.");
        System.exit(0);
    }
}

