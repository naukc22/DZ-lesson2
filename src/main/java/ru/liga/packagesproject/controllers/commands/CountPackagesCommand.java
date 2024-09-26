package ru.liga.packagesproject.controllers.commands;

import ru.liga.packagesproject.models.Truck;
import ru.liga.packagesproject.services.IO.input.InputReader;
import ru.liga.packagesproject.services.IO.input.TruckJsonReader;
import ru.liga.packagesproject.services.TruckService;

import java.util.List;
import java.util.Scanner;

/**
 * Команда для подсчета количества посылок в траках, загруженных из файла.
 * Ожидает от пользователя путь к файлу с траками.
 */
public class CountPackagesCommand implements MenuCommand {
    /**
     * Выполняет команду подсчета посылок в траках.
     *
     * @param scanner объект {@link Scanner} для чтения ввода пользователя.
     */
    @Override
    public void execute(Scanner scanner) {
        System.out.println("Введите путь к файлу с траками для подсчета посылок:");
        String filePath = scanner.nextLine();

        InputReader<Truck> truckReader = new TruckJsonReader();
        List<Truck> trucks = truckReader.read(filePath);

        TruckService truckService = new TruckService();
        truckService.countAndPrintPackagesFromTruckList(trucks);

    }
}

