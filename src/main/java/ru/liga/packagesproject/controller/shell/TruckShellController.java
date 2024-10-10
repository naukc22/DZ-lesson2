package ru.liga.packagesproject.controller.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.liga.packagesproject.dto.Truck;
import ru.liga.packagesproject.dto.TruckLoadingProcessSettings;
import ru.liga.packagesproject.dto.enums.LoadingMode;
import ru.liga.packagesproject.service.impl.TruckServiceImpl;

import java.util.List;

@ShellComponent
public class TruckShellController {

    private final TruckServiceImpl truckServiceImpl;

    @Autowired
    public TruckShellController(TruckServiceImpl truckServiceImpl) {
        this.truckServiceImpl = truckServiceImpl;
    }

    @ShellMethod("Метод загрузки траков посылками по списку имен")
    public void loadTrucksByNames(
            @ShellOption(help = "Список имен посылок, разделенных запятой") String packageNames,
            @ShellOption(help = "Путь до json файла для записи загруженных траков") String filePathDestination,
            @ShellOption(help = "Метод загрузки (EFFECTIVE or BALANCED)") LoadingMode loadingMode,
            @ShellOption(help = "Список размеров траков для загрузки, разделенных запятыми в формате ШИРИНАxВЫСОТА (пример: 6x6,4x3,5x5)") String truckSizes
    ) {
        String[] packageNamesArray = packageNames.split(",");
        var loadingSettings = new TruckLoadingProcessSettings(truckSizes.split(","), loadingMode);

        List<Truck> loadedTrucks = truckServiceImpl.loadPackagesToTrucks(packageNamesArray, loadingSettings);
        truckServiceImpl.printAllTrucks(loadedTrucks);
        truckServiceImpl.writeTrucksToJsonFile(loadedTrucks, filePathDestination);
    }

    @ShellMethod("Метод загрузки траков посылками из файла")
    public void loadTrucksFromFile(
            @ShellOption(help = "Путь до файла с посылками") String filePath,
            @ShellOption(help = "Путь до json файла для записи загруженных траков") String filePathDestination,
            @ShellOption(help = "Метод загрузки (EFFECTIVE or BALANCED)") LoadingMode loadingMode,
            @ShellOption(help = "Список размеров траков для загрузки, разделенных запятыми в формате ШИРИНАxВЫСОТА (пример: 6x6,4x3,5x5)") String truckSizes
    ) {
        TruckLoadingProcessSettings settings = new TruckLoadingProcessSettings(truckSizes.split(" "), loadingMode);
        List<Truck> loadedTrucks = truckServiceImpl.loadPackagesToTrucks(filePath, settings);
        truckServiceImpl.printAllTrucks(loadedTrucks);
        truckServiceImpl.writeTrucksToJsonFile(loadedTrucks, filePathDestination);

    }

    @ShellMethod("Метод разгрузки траков")
    public void unloadAllTrucks(
            @ShellOption(help = "Пусть до JSON файла с траками") String filePath
    ) {
        List<Truck> trucks = truckServiceImpl.unloadTrucksFromJsonFile(filePath);
        truckServiceImpl.printAllTrucks(trucks);
    }
}
