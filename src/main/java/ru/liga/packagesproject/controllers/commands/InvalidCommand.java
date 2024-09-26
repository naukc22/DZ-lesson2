package ru.liga.packagesproject.controllers.commands;

import java.util.Scanner;

/**
 * Команда, выполняемая в случае неверного ввода пользователя.
 * Сообщает пользователю об ошибке и предлагает повторить ввод.
 */
public class InvalidCommand implements MenuCommand {

    /**
     * Выводит сообщение об ошибке выбора и предлагает повторить попытку.
     *
     * @param scanner объект {@link Scanner} для чтения ввода пользователя (не используется в данной команде).
     */
    @Override
    public void execute(Scanner scanner) {
        System.out.println("Неверный выбор. Попробуйте снова.");
    }
}

