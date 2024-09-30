package ru.liga.packagesproject.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.models.Truck;
import ru.liga.packagesproject.models.TruckLoadingProcessSettings;
import ru.liga.packagesproject.repository.PackageRepository;
import ru.liga.packagesproject.services.IO.input.PackageFileReader;
import ru.liga.packagesproject.services.truckloading.truckloadingstrategies.LoadingStrategy;
import ru.liga.packagesproject.services.truckloading.truckloadingstrategies.LoadingStrategyFactory;
import ru.liga.packagesproject.validators.PackageValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления загрузкой посылок в грузовики и вывода информации о загруженных грузовиках.
 */
@Slf4j
@Service
public class TruckService {

    private final PackageService packageService;

    @Autowired
    public TruckService(PackageService packageService, PackageValidator packageValidator) {
        this.packageService = packageService;
    }

    public List<Truck> loadPackagesToTrucksByNames(String[] packageNames, TruckLoadingProcessSettings settings) {
        List<Package> loadedPackages = new ArrayList<>();

        for (String packageName : packageNames) {
            Package pack = packageService.getPackageByName(packageName);
            if (pack != null) {
                loadedPackages.add(pack);
            } else {
                log.info("Package {} not found", packageName);
            }
        }

        if (loadedPackages.isEmpty()) {
            log.info("No packages found");
            throw new RuntimeException();
        }

        return loadTrucks(loadedPackages, settings);
    }

    public List<Truck> loadPackagesToTrucksFromFile(String filePath, TruckLoadingProcessSettings settings) {

        PackageFileReader fileReader = new PackageFileReader();
        List<List<String>> packagesStr = fileReader.read(filePath);

        List<Package> packagesToLoad = new ArrayList<>();

        for (List<String> packageStr : packagesStr) {
            Optional<Package> packageByForm = packageService.getPackageByForm(packageStr);
            packageByForm.ifPresent(packagesToLoad::add);
        }

        if (packagesToLoad.isEmpty()) {
            throw new RuntimeException("Валидация завершилась неудачей. Все посылки недействительны.");
        }

        return loadTrucks(packagesToLoad, settings);
    }

    /**
     * Загружает посылки в грузовики на основе указанного режима загрузки и количества грузовиков.
     *
     * @param packagesToLoad список посылок для загрузки
     * @param settings       параметры процесса загрузки траков
     * @return список загруженных грузовиков
     */
    private List<Truck> loadTrucks(List<Package> packagesToLoad, TruckLoadingProcessSettings settings) {
        log.info("Начинаем загрузку посылок. Режим загрузки: {}, Количество грузовиков: {}", settings.getLoadingMode(), settings.getTruckCount());
        List<Truck> emptyTrucksForLoading = new ArrayList<>();

        for (TruckLoadingProcessSettings.TruckSize truckSize : settings.getTruckSizes()) {
            emptyTrucksForLoading.add(new Truck(truckSize.getWidth(), truckSize.getHeight()));
        }

        LoadingStrategy strategy = LoadingStrategyFactory.getStrategyFromLoadingMode(settings.getLoadingMode());
        List<Truck> trucks = strategy.loadPackages(packagesToLoad, emptyTrucksForLoading);

        log.info("Загрузка посылок завершена. Загружено грузовиков: {}", trucks.size());
        return trucks;
    }

    /**
     * Выводит информацию о всех грузовиках на консоль.
     *
     * @param trucks список грузовиков
     */
    public void printAllTrucks(List<Truck> trucks) {
        int truckNumber = 1;
        for (Truck truck : trucks) {
            log.info("Печать информации о грузовике {}", truckNumber);
            System.out.println("Truck " + truckNumber + ":");
            truck.printTruckToConsole();
            truckNumber++;
        }
    }

//    /**
//     * Подсчитывает количество посылок в каждом грузовике из списка и выводит результат на консоль.
//     *
//     * @param trucks список грузовиков
//     */
//    public void countAndPrintPackagesFromTruckList(List<Truck> trucks) {
//        for (int i = 0; i < trucks.size(); i++) {
//            Truck truck = trucks.get(i);
//            log.info("Обработка грузовика {}", (i + 1));
//            System.out.println("Трак " + (i + 1) + ":");
//            truck.printTruckToConsole();
//
//            Map<Character, Integer> packageCounts = PackageCounterService.countPackages(truck);
//            log.debug("Результат подсчета посылок для грузовика {}: {}", (i + 1), packageCounts);
//            System.out.println("Результат подсчета посылок:");
//            PackageCounterService.printPackageCounts(packageCounts);
//            System.out.println();
//        }
//    }

}
