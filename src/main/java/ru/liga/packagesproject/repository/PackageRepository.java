package ru.liga.packagesproject.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Repository;
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
                Type listType = new TypeToken<Map<String, Package>>() {
                }.getType();
                storage = new Gson().fromJson(reader, listType);

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
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(STORAGE_FILE_PATH)) {
            gson.toJson(packageStorage, writer);
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
