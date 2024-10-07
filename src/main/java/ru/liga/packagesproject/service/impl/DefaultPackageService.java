package ru.liga.packagesproject.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.liga.packagesproject.exception.PackageAlreadyExistsException;
import ru.liga.packagesproject.exception.PackageNotFoundException;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.repository.PackageRepository;
import ru.liga.packagesproject.service.PackageService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DefaultPackageService implements PackageService {

    private final PackageRepository packageRepository;

    @Autowired
    public DefaultPackageService(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public Iterable<Package> findAllPackages() {
        Iterable<Package> packages = packageRepository.findAll();
        log.info("Получено посылок: {}", ((Collection<?>) packages).size());
        return packages;
    }

    @Override
    public Optional<Package> findPackageByName(String packageName) {
        log.info("Поиск посылки по имени: {}", packageName);
        Optional<Package> foundPackage = packageRepository.findByNameIgnoreCase(packageName);
        if (foundPackage.isEmpty()) {
            log.warn("Посылка с именем {} не найдена", packageName);
        } else {
            log.info("Посылка найдена: {}", foundPackage.get());
        }
        return foundPackage;
    }

    @Override
    public Optional<Package> findPackageByForm(List<String> packageForm) {
        log.info("Поиск посылки по форме: {}", packageForm);
        Optional<Package> foundPackage = packageRepository.findByForm(packageForm);
        if (foundPackage.isEmpty()) {
            log.warn("Посылка с формой {} не найдена", packageForm);
        } else {
            log.info("Посылка найдена: {}", foundPackage.get());
        }
        return foundPackage;
    }


    public Iterable<Package> findPackagesBySymbol(char symbol) {
        log.info("Поиск посылки по символу: {}", symbol);

        Iterable<Package> foundedPackage = packageRepository.findBySymbol(symbol);

        if (!foundedPackage.iterator().hasNext()) {
            log.warn("Посылок с символом  ' {} ' не найдена", symbol);
        }
        return foundedPackage;
    }

    @Override
    public Package createPackage(String name, char symbol, List<String> form) throws PackageAlreadyExistsException {
        log.info("Создание новой посылки с именем: {}", name);
        if (packageRepository.findByNameIgnoreCase(name).isPresent()) {
            log.warn("Посылка с именем '{}' уже существует", name);
            throw new PackageAlreadyExistsException(name);
        }
        Package createdPackage = new Package(name, symbol, form);
        return packageRepository.save(createdPackage);
    }

    @Override
    @Transactional
    public void updatePackage(String name, char symbol, List<String> form) throws PackageNotFoundException {
        log.info("Обновление посылки с именем: {}", name);

        this.packageRepository.findByNameIgnoreCase(name)
                .ifPresentOrElse(p -> {
                    p.setSymbol(symbol);
                    p.setFormAsCharArray(convertFormStrToCharArray(form));
                    log.info("Посылка успешно обновлена: {}", p);
                }, () -> {
                    throw new PackageNotFoundException(name);
                });

    }

    @Override
    @Transactional
    public void removePackage(String name) throws PackageNotFoundException {
        log.info("Удаление посылки с именем: {}", name);
        if (packageRepository.removeByNameIgnoreCase(name) > 0) {
            log.info("Посылка с именем {} успешно удалена", name);
        } else {
            throw new PackageNotFoundException(name);
        }
    }


    private char[][] convertFormStrToCharArray(List<String> formStr) {
        return formStr.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }
}

