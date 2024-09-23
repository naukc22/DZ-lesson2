package ru.liga.packagesproject.controller.command;

import ru.liga.packagesproject.model.LoadingMode;
import ru.liga.packagesproject.model.Package;
import ru.liga.packagesproject.model.Truck;
import ru.liga.packagesproject.service.TruckService;
import ru.liga.packagesproject.util.PackageFileReader;
import ru.liga.packagesproject.validator.InputDataValidator;

import java.util.List;
import java.util.Scanner;

public class LoadTrucksCommand implements MenuCommand {

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Введите путь к файлу с посылками:");
        String filePath = scanner.nextLine();

        System.out.println("Выберите тип загрузки (EFFECTIVE, BALANCED):");
        String loadingStrategyInput = scanner.nextLine();

        System.out.println("Введите количество траков для загрузки:");
        int truckCount = Integer.parseInt(scanner.nextLine());

        InputDataValidator inputDataValidator = new InputDataValidator();
        inputDataValidator.validateInputData(filePath, loadingStrategyInput, truckCount);

        LoadingMode loadingMode = LoadingMode.valueOf(loadingStrategyInput.toUpperCase());
        TruckService truckService = new TruckService();

        PackageFileReader packageFileReader = new PackageFileReader();
        List<Package> packages = packageFileReader.readPackagesFromFile(filePath);

        List<Truck> loadedTrucks = truckService.loadPackages(packages, loadingMode, truckCount);
        truckService.printAllTrucks(loadedTrucks);
    }
}

