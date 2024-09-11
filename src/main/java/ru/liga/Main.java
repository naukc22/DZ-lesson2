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
        TruckService truckService = new TruckService();

        LoadingPackages loadingPackages = new SingleLoadingPackages(truckWidth, truckHeight);

        if (loadingMode.equals("-F")) {
            loadingPackages = new FullLoadingPackages(truckWidth, truckHeight);
        }

        try {
            List<int[][]> packages = FileReaderService.readPackagesFromFile(filePath);
            List<int[][]> validPackages = PackageValidator.sortAndGetValidPackages(packages, truckHeight, truckWidth);

            if (validPackages.isEmpty()) {
                System.out.println("Нет посылок, которые могут быть загружены.");
                return;
            }

            List<char[][]> loadedTrucks = loadingPackages.loadPackages(validPackages);

            truckService.printAllTrucks(loadedTrucks);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
