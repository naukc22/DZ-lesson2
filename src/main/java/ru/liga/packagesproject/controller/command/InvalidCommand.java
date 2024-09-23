package ru.liga.packagesproject.controller.command;

import java.util.Scanner;

public class InvalidCommand implements MenuCommand {
    @Override
    public void execute(Scanner scanner) {
        System.out.println("Неверный выбор. Попробуйте снова.");
    }
}

