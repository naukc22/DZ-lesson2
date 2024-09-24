package ru.liga.packagesproject.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TrucksSetDTO {
    private List<TruckDTO> trucks;
}
