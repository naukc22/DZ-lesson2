package ru.liga.packagesproject.mapper;

import ru.liga.packagesproject.dto.PackageDto;
import ru.liga.packagesproject.models.Package;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PackageMapper {

    public static PackageDto toDto(Package pack) {
        List<String> form = Arrays.stream(pack.getForm())
                .map(String::new)
                .collect(Collectors.toList());
        return new PackageDto(pack.getName(), pack.getSymbol(), form);
    }

    public static Package toEntity(PackageDto packageDto) {
        return new Package(packageDto.getName(), packageDto.getSymbol(), packageDto.getForm());
    }

}
