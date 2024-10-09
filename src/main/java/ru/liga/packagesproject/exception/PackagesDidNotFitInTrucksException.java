package ru.liga.packagesproject.exception;

import java.util.List;

public class PackagesDidNotFitInTrucksException extends RuntimeException {
    public PackagesDidNotFitInTrucksException(List<String> packageNames) {
        super("Посылки не поместилась ни в одном из возможных траков : " + packageNames);
    }
}
