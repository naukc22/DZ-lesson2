package ru.liga.packagesproject.controller;

import ru.liga.packagesproject.model.Package;
import ru.liga.packagesproject.model.Truck;
import ru.liga.packagesproject.service.TruckService;
import ru.liga.packagesproject.util.PackageFileReader;
import ru.liga.packagesproject.util.JsonReader;
import ru.liga.packagesproject.model.LoadingMode;
import ru.liga.packagesproject.validator.InputDataValidator;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ConsoleController {

    private final InputDataValidator inputDataValidator;

    public ConsoleController() {
        this.inputDataValidator = new InputDataValidator();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            showMenu();

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    showScriptForLoadingTruck(scanner);
                    break;
                case "2":
                    showScriptForCountingPackagesInTruck(scanner);
                    break;
                case "0":
                    System.out.println("Завершение программы.");
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
                    break;
            }
        }
    }

    private static void showMenu() {
        System.out.println("Выберите опцию:");
        System.out.println("1. Загрузить траки");
        System.out.println("2. Посчитать посылки в траке");
        System.out.println("0. Выход");
    }

    private void showScriptForLoadingTruck(Scanner scanner) {
        System.out.println("Введите путь к файлу с посылками:");
        String filePath = scanner.nextLine();

        System.out.println("Выберите тип загрузки (E - EFFECTIVE, B - BALANCED):");
        String loadingStrategyInput = scanner.nextLine();

        System.out.println("Введите количество траков для загрузки:");
        int truckCount = Integer.parseInt(scanner.nextLine());

        List<String> errorsInInput = inputDataValidator.validateInputData(filePath, loadingStrategyInput, truckCount);

        if (!errorsInInput.isEmpty()) {
            errorsInInput.forEach(System.out::println);
            return;
        }

        LoadingMode loadingMode = LoadingMode.valueOf(loadingStrategyInput.toUpperCase());
        TruckService truckService = new TruckService();

        PackageFileReader packageFileReader = new PackageFileReader();
        List<Package> packages;
        try {
            packages = packageFileReader.readPackagesFromFile(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        List<Truck> loadedTrucks = truckService.loadPackages(packages, loadingMode, truckCount);
        truckService.printAllTrucks(loadedTrucks);

    }

    private void showScriptForCountingPackagesInTruck(Scanner scanner) {
        System.out.println("Введите путь к файлу с траками для подсчета посылок:");
        String filePath = scanner.nextLine();

        try {
            JsonReader jsonReader = new JsonReader();
            List<Truck> trucks = jsonReader.loadTrucksFromFile(filePath);

            TruckService truckService = new TruckService();

            truckService.countAndPrintPackagesFromTruckList(trucks);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
