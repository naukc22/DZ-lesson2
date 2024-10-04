package ru.liga.packagesproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class TruckDto {

    private List<String> body;
    private Map<String, Integer> loadedPackages;

}
