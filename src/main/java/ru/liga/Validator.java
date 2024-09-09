package ru.liga;

import java.util.Arrays;
import java.util.List;

public class Validator {

    private final int truckWidth;
    private final int truckHeight;

    public Validator(int truckWidth, int truckHeight) {
        this.truckWidth = truckWidth;
        this.truckHeight = truckHeight;
    }

    private boolean isValidPackage(int[][] pack) {
        int packageWidth = Arrays.stream(pack)
                .mapToInt(arr -> arr.length)
                .max()
                .orElse(0);

        int packageHeight = pack.length;

        return packageWidth <= truckWidth && packageHeight <= truckHeight;
    }

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

    public List<int[][]> filterValidPackages(List<int[][]> packages) {

        for (int i = 0; i < packages.size(); i++) {
            if (!isValidPackage(packages.get(i))) {
                packages.remove(packages.get(i));
                int number = i + 1;
                System.out.println("Посылка #" + number + " удалена: размер больше, чем кузов.");
            }
        }

        return packages;
    }
}
