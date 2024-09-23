package ru.liga.packagesproject.controller.command;

import ru.liga.packagesproject.model.Truck;
import ru.liga.packagesproject.service.TruckService;
import ru.liga.packagesproject.util.JsonReader;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class CountPackagesCommand implements MenuCommand {
    @Override
    public void execute(Scanner scanner) {
        System.out.println("Введите путь к файлу с траками для подсчета посылок:");
        String filePath = scanner.nextLine();

        try {
            JsonReader jsonReader = new JsonReader();
            List<Truck> trucks = jsonReader.loadTrucksFromJson(filePath);

            TruckService truckService = new TruckService();
            truckService.countAndPrintPackagesFromTruckList(trucks);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла: " + filePath, e);  // TODO перенести в JsonReader
        }
    }
}

