package ru.liga.packagesproject.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.liga.packagesproject.dto.PackageDto;
import ru.liga.packagesproject.exception.PackageAlreadyExistsException;
import ru.liga.packagesproject.exception.PackageNotFoundException;
import ru.liga.packagesproject.model.Package;
import ru.liga.packagesproject.service.impl.PackageServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
public class PackageRestController {

    private final PackageServiceImpl packageServiceImpl;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody PackageDto packageDto) {
        try {
            Package newPackage = packageServiceImpl.create(packageDto.getName(), packageDto.getSymbol(), packageDto.getForm());
            return ResponseEntity.ok("Посылка добавлена: " + newPackage.getName());
        } catch (PackageAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{name}")
    public ResponseEntity<String> update(@RequestBody PackageDto packageDto) {
        try {
            packageServiceImpl.update(packageDto.getName(), packageDto.getSymbol(), packageDto.getForm());
            return ResponseEntity.ok("Посылка обновлена: " + packageDto.getName());
        } catch (PackageNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> delete(@PathVariable String name) {
        try {
            packageServiceImpl.remove(name);
            return ResponseEntity.ok("Посылка удалена: " + name);
        } catch (PackageNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public List<PackageDto> findAll() {
        return packageServiceImpl.findAll();
    }
}
