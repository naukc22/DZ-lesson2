package ru.liga.packagesproject.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.liga.packagesproject.dto.PackageDto;
import ru.liga.packagesproject.exception.PackageAlreadyExistsException;
import ru.liga.packagesproject.exception.PackageNotFoundException;
import ru.liga.packagesproject.mapper.PackageMapper;
import ru.liga.packagesproject.model.Package;
import ru.liga.packagesproject.repository.PackageRepository;
import ru.liga.packagesproject.service.PackageService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;

    @Autowired
    public PackageServiceImpl(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public List<PackageDto> findAll() {
        Iterable<Package> packages = packageRepository.findAll();
        log.info("Получено посылок: {}", ((Collection<?>) packages).size());
        return StreamSupport.stream(packages.spliterator(), false)
                .map(PackageMapper::toDto)
                .toList();
    }

    @Override
    public Optional<Package> findByName(String packageName) {
        log.info("Поиск посылки по имени: {}", packageName);
        Optional<Package> foundPackage = packageRepository.findByNameIgnoreCase(packageName);
        if (foundPackage.isEmpty()) {
            log.error("Посылка с именем {} не найдена", packageName);
        }
        return foundPackage;
    }

    @Override
    public Optional<Package> findByForm(List<String> packageForm) {
        log.info("Поиск посылки по форме: {}", packageForm);
        List<PackageDto> allPackages = findAll();

        Optional<PackageDto> resultPackageDto = allPackages.stream()
                .filter(pkg -> pkg.getForm().equals(packageForm))
                .findFirst();

        if (resultPackageDto.isEmpty()) {
            log.error("Посылка с формой {} не найдена", packageForm);
        }
        return resultPackageDto.map(this::convertToPackage);
    }

    @Override
    public Iterable<Package> findBySymbol(char symbol) {
        log.info("Поиск посылки по символу: {}", symbol);

        Iterable<Package> foundPackage = packageRepository.findBySymbol(symbol);

        if (!foundPackage.iterator().hasNext()) {
            log.warn("Посылок с символом  ' {} ' не найдена", symbol);
        }
        return foundPackage;
    }

    @Override
    public Package create(String name, char symbol, List<String> form) throws PackageAlreadyExistsException {
        log.info("Создание новой посылки с именем: {}", name);
        if (packageRepository.findByNameIgnoreCase(name).isPresent()) {
            log.error("Посылка с именем '{}' уже существует", name);
            throw new PackageAlreadyExistsException(name);
        }
        Package createdPackage = new Package(name, symbol, form);
        return packageRepository.save(createdPackage);
    }

    @Override
    @Transactional
    public void update(String name, char symbol, List<String> form) throws PackageNotFoundException {
        log.info("Обновление посылки с именем: {}", name);

        packageRepository.findByNameIgnoreCase(name)
                .ifPresentOrElse(p -> {
                    p.setSymbol(symbol);
                    p.setFormAsCharArray(convertFormStrToCharArray(form));
                    log.info("Посылка успешно обновлена: {}", p);
                }, () -> {
                    log.error("Посылку с именем '{}' не удалось обновить, потому что она не существует", name);
                    throw new PackageNotFoundException(name);
                });

    }

    @Override
    @Transactional
    public void remove(String name) throws PackageNotFoundException {
        log.info("Удаление посылки с именем: {}", name);
        if (packageRepository.removeByNameIgnoreCase(name) > 0) {
            log.info("Посылка с именем {} успешно удалена", name);
        } else {
            log.error("Посылку с именем '{}' не удалось удалить, потому что она не существует", name);
            throw new PackageNotFoundException(name);
        }
    }


    private char[][] convertFormStrToCharArray(List<String> formStr) {
        return formStr.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    private Package convertToPackage(PackageDto packageDto) {
        return new Package(
                packageDto.getName(),
                packageDto.getSymbol(),
                packageDto.getForm()
        );
    }
}

