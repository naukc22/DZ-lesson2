package ru.liga.packagesproject.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Repository;
import ru.liga.packagesproject.dto.PackageDto;
import ru.liga.packagesproject.mapper.PackageMapper;
import ru.liga.packagesproject.models.Package;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class PackageRepository {

    private static final String STORAGE_FILE_PATH = "data/package-storage.json";

    private final Map<String, Package> packageStorage;

    public PackageRepository() {
        this.packageStorage = loadPackagesFromFile();
    }

    /**
     * Выгрузка данных в оперативную память из базы (json файл).
     */
    private Map<String, Package> loadPackagesFromFile() {

        Map<String, Package> storage = new HashMap<>();

        if (new File(STORAGE_FILE_PATH).exists()) {

            try (FileReader reader = new FileReader(STORAGE_FILE_PATH)) {
                Type listType = new TypeToken<List<PackageDto>>() {
                }.getType();
                List<PackageDto> packageDtoList = new Gson().fromJson(reader, listType);
                storage = packageDtoList.stream()
                        .map(PackageMapper::toEntity)
                        .collect(Collectors.toMap(Package::getName, p -> p));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        } else {
            System.err.println("Package storage file not found in resources");
        }

        return storage;
    }

    /**
     * Обновление базы данных (json файл). Вызывается каждый раз, при изменении мапы - packageStorage, которая держит базу данных в оперативной памяти.
     */
    private void uploadPackagesFromMapToFile() {
        List<PackageDto> packageDtoList = packageStorage.values().stream()
                .map(PackageMapper::toDto)
                .collect(Collectors.toList());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(STORAGE_FILE_PATH)) {
            gson.toJson(packageDtoList, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPackage(Package pack) {
        packageStorage.put(pack.getName(), pack);
        uploadPackagesFromMapToFile();
    }

    public Package findByName(String name) {
        return packageStorage.get(name);
    }

    public List<Package> findBySymbol(char symbol) {
        return findAll().values().stream()
                .filter(pack -> pack.getSymbol() == symbol)
                .collect(Collectors.toList());
    }

    public void updatePackage(String name, Package updatedPackage) {
        if (packageStorage.containsKey(name)) {
            packageStorage.put(name, updatedPackage);
            uploadPackagesFromMapToFile();
        }
    }

    public void removePackage(String name) {
        if (packageStorage.remove(name) != null) {
            uploadPackagesFromMapToFile();
        }
    }

    public Map<String, Package> findAll() {
        return packageStorage;
    }


}
