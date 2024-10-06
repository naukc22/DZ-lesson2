package ru.liga.packagesproject.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.liga.packagesproject.dto.PackageDto;
import ru.liga.packagesproject.exception.PackageAlreadyExistsException;
import ru.liga.packagesproject.mapper.PackageMapper;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.service.DefaultPackageService;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/packages")
public class PackageRestController {

    private final DefaultPackageService defaultPackageService;

    @Autowired
    public PackageRestController(DefaultPackageService defaultPackageService) {
        this.defaultPackageService = defaultPackageService;
    }

    @PostMapping
    public ResponseEntity<String> addPackage(@RequestBody PackageDto packageDto) {
        try {
            Package newPackage = defaultPackageService.createPackage(packageDto.getName(), packageDto.getSymbol(), packageDto.getForm());
            return ResponseEntity.ok("Посылка добавлена: " + newPackage.getName());
        } catch (PackageAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{name}")
    public String editPackage(
            @PathVariable String name,
            @RequestBody PackageDto packageDto
    ) {
        defaultPackageService.updatePackage(name, packageDto.getSymbol(), packageDto.getForm());
        return "Посылка обновлена: " + name;
    }

    @DeleteMapping("/{name}")
    public String removePackage(@PathVariable String name) {
        defaultPackageService.deletePackage(name);
        return "Посылка удалена: " + name;
    }

    @GetMapping
    public List<PackageDto> listPackages() {
        Iterable<Package> allPackages = defaultPackageService.findAllPackages();
        return StreamSupport.stream(allPackages.spliterator(), false)
                .map(PackageMapper::toDto)
                .toList();
    }
}
