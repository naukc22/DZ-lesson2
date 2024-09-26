package ru.liga.packagesproject.controllers.commands;

/**
 * Фабрика для создания команд меню на основе пользовательского ввода.
 */
public class MenuCommandFactory {

    /**
     * Возвращает команду на основе выбранной опции меню.
     *
     * @param input строка, введенная пользователем.
     * @return объект команды, реализующей интерфейс {@link MenuCommand}.
     */
    public MenuCommand getMenuCommand(String input) {
        return switch (input) {
            case "1" -> new LoadTrucksCommand();
            case "2" -> new CountPackagesCommand();
            case "0" -> new ExitCommand();
            default -> new InvalidCommand();
        };
    }
}

