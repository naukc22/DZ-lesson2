package ru.liga.packagesproject.controllers.commands;

import java.util.Scanner;

/**
 * Интерфейс для всех команд, используемых в консольном меню.
 */
public interface MenuCommand {
    void execute(Scanner scanner);
}
