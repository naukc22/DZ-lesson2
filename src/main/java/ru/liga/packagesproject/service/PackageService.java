package ru.liga.packagesproject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.packagesproject.exception.PackageNotFoundException;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.repository.PackageRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PackageService {

    private final PackageRepository packageRepository;

    public PackageService(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    public void addPackage(String name, char symbol, List<String> formStr) {
        Package pack = new Package(name, symbol, formStr);
        if (packageRepository.findByName(pack.getName()) != null) {
            throw new IllegalArgumentException("Посылка с таким именем уже существует");
        }
        packageRepository.addPackage(pack);
        log.info("Добавлена посылка: {}", pack);
    }

    public Package getPackageByName(String name) {
        Package pack = packageRepository.findByName(name);
        if (pack == null) {
            throw new PackageNotFoundException("Посылка не найдена: " + name);
        }
        return pack;
    }

    public List<Package> getPackagesBySymbol(char symbol) {
        List<Package> packages = packageRepository.findBySymbol(symbol);
        if (packages.isEmpty()) {
            throw new PackageNotFoundException("Посылки с символом " + symbol + " не найдены.");
        }
        return packages;
    }

    public void editPackage(String name, char newSymbol, List<String> newFormStr) {
        Package pack = getPackageByName(name);
        pack.setForm(convertFormListToArray(newFormStr));
        pack.setSymbol(newSymbol);
        packageRepository.updatePackage(name, pack);
        log.info("Изменена посылка: {}", pack);
    }

    public void removePackage(String name) {
        if (packageRepository.findByName(name) == null) {
            throw new RuntimeException("Посылка не найдена : " + name);
        }
        packageRepository.removePackage(name);
    }

    public List<Package> getAllPackagesList() {
        return List.copyOf(packageRepository.findAll().values());
    }

    public Optional<Package> getPackageByForm(List<String> formStr) {
        char[][] form = convertFormListToArray(formStr);

        return packageRepository.findAll().values().stream()
                .filter(pack -> Arrays.deepEquals(pack.getForm(), form))
                .findFirst();
    }

    private char[][] convertFormListToArray(List<String> formStr) {
        return formStr.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }
}

