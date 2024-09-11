package ru.liga;

import ru.liga.service.*;
import ru.liga.validation.InputDataValidator;
import ru.liga.validation.PackageValidator;

import java.io.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        if (!InputDataValidator.validateInput(args)) {
            return;  // Если валидация не прошла, завершить выполнение
        }

        String filePath = args[0];
        String loadingMode = args[1];
        int truckWidth = Integer.parseInt(args[2]);
        int truckHeight = Integer.parseInt(args[3]);

        LoadingPackages loadingPackages;

        switch (loadingMode) {
            case "-S" : loadingPackages = new SingleLoadingPackages(truckWidth, truckHeight); break;
            case "-F" : loadingPackages = new FullLoadingPackages(truckWidth, truckHeight); break;
            default:
                System.out.println("Ошибка: неверный флаг режима загрузки. Используйте -S для одиночной загрузки или -F для полной загрузки.");
                return;
        }

        try {
            List<int[][]> packages = FileReaderService.readPackagesFromFile(filePath);
            List<int[][]> validPackages = PackageValidator.filterValidPackages(packages, truckHeight, truckWidth);

            if (validPackages.isEmpty()) {
                System.out.println("Нет посылок, которые могут быть загружены.");
                return;
            }

            List<char[][]> loadedTrucks = loadingPackages.loadPackages(validPackages);

            TruckService truckService = new TruckService();
            truckService.printAllTrucks(loadedTrucks);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
