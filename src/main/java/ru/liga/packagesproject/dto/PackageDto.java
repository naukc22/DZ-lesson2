package ru.liga.packagesproject.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageDto {
    private String name;
    private char symbol;
    private List<String> form;
}
