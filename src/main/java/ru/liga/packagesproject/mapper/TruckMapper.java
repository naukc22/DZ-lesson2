package ru.liga.packagesproject.mapper;

import org.springframework.stereotype.Service;
import ru.liga.packagesproject.dto.TruckDto;
import ru.liga.packagesproject.models.Truck;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TruckMapper {

    public static TruckDto toDTO(Truck truck) {
        List<String> truckBody = Arrays.stream(truck.getBody()).map(String::valueOf).toList();

        Map<String, Integer> packageMap = new HashMap<>();

        truck.getLoadedPackages().forEach((pack, count) -> {
            packageMap.put(pack.getName(), count);
        });

        return new TruckDto(truckBody, packageMap);
    }

}
