package ru.liga;

import java.io.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        if (!Validator.validateInput(args)) {
            return;  // Если валидация не прошла, завершить выполнение
        }

        String filePath = args[0];
        String loadingMode = args[1];
        int truckWidth = Integer.parseInt(args[2]);
        int truckHeight = Integer.parseInt(args[3]);

        LoadTrucks packageTruck;

        switch (loadingMode) {
            case "-S" : packageTruck = new SingleLoadTruck(truckWidth, truckHeight); break;
            case "-F" : packageTruck = new LoadTrucks(truckWidth, truckHeight); break;
            default:
                System.out.println("Ошибка: неверный флаг режима загрузки. Используйте -S для одиночной загрузки или -F для полной загрузки.");
                return;
        }

        Validator validator = new Validator(truckWidth,truckHeight);

        try {
            List<int[][]> packages = packageTruck.readPackagesFromFile(filePath);
            List<int[][]> validPackages = validator.filterValidPackages(packages);

            if (validPackages.isEmpty()) {
                System.out.println("Нет посылок, которые могут быть загружены.");
                return;
            }

            List<char[][]> loadedTrucks = packageTruck.loadTrucks(validPackages);
            packageTruck.printAllTrucks(loadedTrucks);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
