package ru.liga.packagesproject.controllers.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.liga.packagesproject.controllers.LoadingMode;
import ru.liga.packagesproject.models.Truck;
import ru.liga.packagesproject.models.TruckLoadingProcessSettings;
import ru.liga.packagesproject.services.TruckService;

import java.util.List;

@ShellComponent
public class TruckShellController {

    private final TruckService truckService;

    @Autowired
    public TruckShellController(TruckService truckService) {
        this.truckService = truckService;
    }

    @ShellMethod("Load packages into trucks by package names")
    public void loadTrucksByNames(
            @ShellOption(help = "Comma-separated list of package names") String packageNames,
            @ShellOption(help = "Loading mode (EFFECTIVE or BALANCED)") LoadingMode loadingMode,
            @ShellOption(help = "Comma-separated list of truck sizes in the format WIDTHxHEIGHT (example: 6x6,4x3,5x5)") String truckSizes
    ) {

        String[] packageNamesArray = packageNames.split(",");
        var loadingSettings = new TruckLoadingProcessSettings(truckSizes.split(","), loadingMode);

        List<Truck> loadedTrucks = truckService.loadPackagesToTrucksByNames(packageNamesArray, loadingSettings);
        truckService.printAllTrucks(loadedTrucks);
    }

    @ShellMethod("Load packages into trucks from file")
    public void loadTrucksFromFile(
            @ShellOption(help = "Path to the file with packages") String filePath,
            @ShellOption(help = "Loading mode (EFFECTIVE or BALANCED)") LoadingMode loadingMode,
            @ShellOption(help = "Comma-separated list of truck sizes in the format WIDTHxHEIGHT (example: 6x6,4x3,5x5)") String truckSizes
    ) {
        TruckLoadingProcessSettings settings = new TruckLoadingProcessSettings(truckSizes.split(" "), loadingMode);

        List<Truck> trucks = truckService.loadPackagesToTrucksFromFile(filePath, settings);
        truckService.printAllTrucks(trucks);


    }

//    @ShellMethod("Unload all trucks and display the list of packages")
//    public void unloadAllTrucks() {
//        truckService.unloadAllTrucks();
//    }
//
//    @ShellMethod("Show current state of all trucks")
//    public void showTrucksState() {
//        String trucksState = truckService.getTrucksState();
//        System.out.println(trucksState);
//    }
}
