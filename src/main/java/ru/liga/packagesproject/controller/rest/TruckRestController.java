package ru.liga.packagesproject.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.liga.packagesproject.dto.ResponseMessage;
import ru.liga.packagesproject.dto.Truck;
import ru.liga.packagesproject.dto.TruckLoadingProcessSettings;
import ru.liga.packagesproject.dto.enums.LoadingMode;
import ru.liga.packagesproject.service.impl.TruckServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/trucks")
@RequiredArgsConstructor
public class TruckRestController {

    private final TruckServiceImpl truckServiceImpl;

    /**
     * Загружает траки по именам посылок.
     *
     * @param packageNames Список имен посылок
     * @param loadingMode  Метод загрузки (EFFECTIVE или BALANCED)
     * @param truckSizes   Список размеров траков
     * @return Ответ с результатами загрузки
     */
    @PostMapping("/loadByNames")
    public ResponseEntity<ResponseMessage> loadTrucksByNames(
            @RequestParam String packageNames,
            @RequestParam LoadingMode loadingMode,
            @RequestParam String truckSizes) {
        String[] packageNamesArray = packageNames.split(",");
        TruckLoadingProcessSettings loadingSettings = new TruckLoadingProcessSettings(truckSizes.split(","), loadingMode);

        List<Truck> loadedTrucks = truckServiceImpl.loadPackagesToTrucks(packageNamesArray, loadingSettings);
        truckServiceImpl.writeTrucksToJsonFile(loadedTrucks, "loaded_trucks.json");

        return ResponseEntity.ok(new ResponseMessage("Траки успешно загружены.", loadedTrucks));
    }

    /**
     * Загружает траки из файла.
     *
     * @param file        Файл с посылками
     * @param loadingMode Метод загрузки (EFFECTIVE или BALANCED)
     * @param truckSizes  Список размеров траков
     * @return Ответ с результатами загрузки
     */
    @PostMapping("/loadFromFile")
    public ResponseEntity<ResponseMessage> loadTrucksFromFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam LoadingMode loadingMode,
            @RequestParam String truckSizes) {

        TruckLoadingProcessSettings settings = new TruckLoadingProcessSettings(truckSizes.split(","), loadingMode);
        List<Truck> loadedTrucks = truckServiceImpl.loadPackagesToTrucks(file.getOriginalFilename(), settings);
        truckServiceImpl.writeTrucksToJsonFile(loadedTrucks, "loaded_trucks_from_file.json");

        return ResponseEntity.ok(new ResponseMessage("Траки успешно загружены.", loadedTrucks));
    }

    /**
     * Разгружает траки.
     *
     * @param filePath Путь до JSON файла с траками
     * @return Ответ с результатами разгрузки
     */
    @PostMapping("/unload")
    public ResponseEntity<ResponseMessage> unloadAllTrucks(@RequestParam String filePath) {
        List<Truck> unloadedTrucks = truckServiceImpl.unloadTrucksFromJsonFile(filePath);
        truckServiceImpl.writeTrucksToJsonFile(unloadedTrucks, "unloaded_trucks.json"); // Путь к файлу можно изменить

        return ResponseEntity.ok(new ResponseMessage("Траки успешно разгружены.", unloadedTrucks));
    }
}
