package ru.liga.packagesproject.controllers.commands;

import java.util.Scanner;

/**
 * Команда для завершения работы приложения.
 */
public class ExitCommand implements MenuCommand {

    /**
     * Завершает выполнение программы.
     *
     * @param scanner объект {@link Scanner} для чтения ввода пользователя (не используется в данной команде).
     */
    @Override
    public void execute(Scanner scanner) {
        System.out.println("Завершение программы.");
        System.exit(0);
    }
}

