package ru.liga.packagesproject.mapper;

import org.springframework.stereotype.Service;
import ru.liga.packagesproject.dto.PackageDto;
import ru.liga.packagesproject.model.Package;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PackageMapper {

    public static PackageDto toDto(Package pack) {
        List<String> form = Arrays.stream(pack.getFormAsCharArray())
                .map(String::new)
                .collect(Collectors.toList());
        return new PackageDto(pack.getName(), pack.getSymbol(), form);
    }

    public static Package toEntity(PackageDto packageDto) {
        return new Package(packageDto.getName(), packageDto.getSymbol(), packageDto.getForm());
    }

}
