package ru.liga.validation;

public class InputDataValidator {

    public static boolean validateInput(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: java Main <file_path> <loading_mode> <truck_width> <truck_height>");
            return false;
        }

        // Проверка режима загрузки
        String loadingMode = args[1];
        if (!loadingMode.equals("-S") && !loadingMode.equals("-F")) {
            System.out.println("Ошибка: неверный флаг режима загрузки. Используйте -S для одиночной загрузки или -F для полной загрузки.");
            return false;
        }

        // Проверка ширины и высоты кузова
        try {
            int truckWidth = Integer.parseInt(args[2]);
            int truckHeight = Integer.parseInt(args[3]);

            if (truckWidth <= 0 || truckHeight <= 0) {
                System.out.println("Ошибка: ширина и высота кузова должны быть положительными числами.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ширина и высота кузова должны быть целыми числами.");
            return false;
        }

        return true; // Если все проверки пройдены
    }

}
