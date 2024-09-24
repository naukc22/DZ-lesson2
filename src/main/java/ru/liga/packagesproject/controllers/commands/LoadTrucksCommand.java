package ru.liga.packagesproject.controllers.commands;

import ru.liga.packagesproject.models.LoadingMode;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.models.Truck;
import ru.liga.packagesproject.services.IO.input.InputReader;
import ru.liga.packagesproject.services.IO.input.PackageFileReader;
import ru.liga.packagesproject.services.IO.output.TruckJsonWriter;
import ru.liga.packagesproject.services.TruckService;
import ru.liga.packagesproject.validators.InputDataValidator;

import java.util.List;
import java.util.Scanner;

public class LoadTrucksCommand implements MenuCommand {

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Введите путь к файлу с посылками:");
        String filePath = scanner.nextLine();

        System.out.println("Введите путь к файлу для вывода:");
        String fileDestination = scanner.nextLine();

        System.out.println("Выберите тип загрузки (EFFECTIVE, BALANCED):");
        String loadingStrategyInput = scanner.nextLine();

        System.out.println("Введите количество траков для загрузки:");
        int truckCount = Integer.parseInt(scanner.nextLine());

        InputDataValidator inputDataValidator = new InputDataValidator();
        inputDataValidator.validateInputData(filePath, fileDestination, loadingStrategyInput, truckCount);

        LoadingMode loadingMode = LoadingMode.valueOf(loadingStrategyInput.toUpperCase());
        TruckService truckService = new TruckService();

        InputReader<Package> packageReader = new PackageFileReader();
        List<Package> packages = packageReader.read(filePath);

        List<Truck> loadedTrucks = truckService.loadPackages(packages, loadingMode, truckCount);
        truckService.printAllTrucks(loadedTrucks);

        TruckJsonWriter truckJsonWriter = new TruckJsonWriter();
        truckJsonWriter.write(loadedTrucks, fileDestination);
    }
}

