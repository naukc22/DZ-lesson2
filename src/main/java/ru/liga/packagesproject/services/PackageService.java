package ru.liga.packagesproject.services;

import org.springframework.stereotype.Service;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.repository.PackageRepository;

import java.util.Collection;
import java.util.List;

@Service
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
    }

    public Package getPackageByName(String name) {
        Package pack = packageRepository.findByName(name);
        if (pack == null) {
            throw new IllegalArgumentException("Посылка не найдена");
        }
        return pack;
    }

    public void editPackage(String name, char newSymbol, List<String> newFormStr) {
        char[][] newForm = new char[newFormStr.size()][];
        for (int i = 0; i < newFormStr.size(); i++) {
            String line = newFormStr.get(i);
            newForm[i] = line.toCharArray();
        }
        Package pack = getPackageByName(name);
        pack.setForm(newForm);
        pack.setSymbol(newSymbol);
        packageRepository.updatePackage(name, pack);
    }

    public void removePackage(String name) {
        if (packageRepository.findByName(name) == null) {
            throw new IllegalArgumentException("Посылка не найдена");
        }
        packageRepository.removePackage(name);
    }

    public Collection<Package> listAllPackages() {
        return packageRepository.findAll().values();
    }
}

