package ru.liga.packagesproject.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.packagesproject.dto.TruckBodyDto;
import ru.liga.packagesproject.dto.TruckDto;
import ru.liga.packagesproject.mapper.TruckMapper;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.models.Truck;
import ru.liga.packagesproject.models.TruckLoadingProcessSettings;
import ru.liga.packagesproject.service.IO.input.InputReader;
import ru.liga.packagesproject.service.IO.input.impl.PackageFileReader;
import ru.liga.packagesproject.service.IO.output.OutputWriter;
import ru.liga.packagesproject.service.TruckService;
import ru.liga.packagesproject.service.truckloading.truckloadingstrategies.LoadingStrategy;
import ru.liga.packagesproject.service.truckloading.truckloadingstrategies.LoadingStrategyFactory;
import ru.liga.packagesproject.service.truckunloading.TruckUnloader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления грузовиками - погрузка и разгрузка.
 */
@Slf4j
@Service
public class DefaultTruckService implements TruckService {

    private final DefaultPackageService defaultPackageService;
    private final TruckUnloader truckUnloader;
    private final InputReader<TruckBodyDto> truckBodiesJsonReader;
    private final OutputWriter<TruckDto> truckJsonWriter;

    public DefaultTruckService(DefaultPackageService defaultPackageService, TruckUnloader truckUnloader, InputReader<TruckBodyDto> truckBodiesJsonReader, OutputWriter<TruckDto> truckJsonWriter) {
        this.defaultPackageService = defaultPackageService;
        this.truckUnloader = truckUnloader;
        this.truckBodiesJsonReader = truckBodiesJsonReader;
        this.truckJsonWriter = truckJsonWriter;
    }

    /**
     * Принимает список из названий посылок. Проверяет их наличие в базе.
     * Все валидные посылки загружает в траки, размеры которых также передаются в виде TruckLoadingProcessSettings
     *
     * @param packageNames список названий посылок для загрузки
     * @param settings     параметры процесса загрузки траков
     * @return список загруженных грузовиков
     */
    public List<Truck> loadPackagesToTrucks(String[] packageNames, TruckLoadingProcessSettings settings) {
        List<Package> loadedPackages = new ArrayList<>();

        for (String packageName : packageNames) {
            Optional<Package> optionalPackage = defaultPackageService.findPackageByName(packageName);
            if (optionalPackage.isPresent()) {
                loadedPackages.add(optionalPackage.get());
            } else {
                log.info("Посылка {} не найдена и будет пропущена.", packageName);
            }
        }

        if (loadedPackages.isEmpty()) {
            log.info("Не нашлось подходящих посылок для работы");
            throw new RuntimeException();
        }

        return executeLoading(loadedPackages, settings);
    }

    /**
     * Принимает путь до файла с посылками. Вызывает ридер для считывания посылок из файла. Проверяет их наличие в базе.
     * Все валидные посылки загружает в траки, размеры которых также приходят в виде TruckLoadingProcessSettings
     *
     * @param filePath список названий посылок для загрузки
     * @param settings параметры процесса загрузки траков
     * @return список загруженных грузовиков
     */
    public List<Truck> loadPackagesToTrucks(String filePath, TruckLoadingProcessSettings settings) {

        PackageFileReader fileReader = new PackageFileReader();
        List<List<String>> packagesStr = fileReader.read(filePath);

        List<Package> packagesToLoad = new ArrayList<>();

        for (List<String> packageStr : packagesStr) {
            Optional<Package> foundPackage = defaultPackageService.findPackageByForm(packageStr);
            if (foundPackage.isPresent()) {
                packagesToLoad.add(foundPackage.get());
            } else {
                log.info("Посылка {} не найдена и будет пропущена.", packageStr);
            }
        }

        if (packagesToLoad.isEmpty()) {
            throw new RuntimeException("Валидация завершилась неудачей. Все посылки недействительны.");
        }

        return executeLoading(packagesToLoad, settings);
    }

    private List<Truck> executeLoading(List<Package> packagesToLoad, TruckLoadingProcessSettings settings) {
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
     * Метод разгрузки посылок из трака. Принимает на вход пусть до JSON файла в траками.
     * Сканирует их. Возвращает список из объектов Truck с загруженными в них посылками,
     * в такой же последовательности как они были в передаваемом файле
     *
     * @param filePath путь до JSON файла с информацией о грузовиках
     * @return список траков с загруженными посылками.
     */
    public List<Truck> unloadTrucksFromJsonFile(String filePath) {
        List<TruckBodyDto> truckBodies = truckBodiesJsonReader.read(filePath);
        List<Truck> trucks = new ArrayList<>();
        for (TruckBodyDto truckBody : truckBodies) {
            trucks.add(truckUnloader.unloadTruck(truckBody));
        }
        return trucks;
    }

    public void writeTrucksToJsonFile(List<Truck> trucksForWriting, String filePathDestination) {
        List<TruckDto> truckDtoList = trucksForWriting.stream().map(TruckMapper::toDTO).toList();

        truckJsonWriter.write(truckDtoList, filePathDestination);
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
            System.out.println("Тело");
            truck.printTruckBodyToConsole();
            System.out.println("Загруженные посылки: ");
            truck.getLoadedPackages().forEach((key, value) -> System.out.println(key.getName() + " : " + value));

            truckNumber++;
        }
    }

}
