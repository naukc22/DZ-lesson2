package ru.liga.packagesproject.exception;

import java.util.List;

public class AllPackagesAreNotValid extends RuntimeException {
    public AllPackagesAreNotValid(List<List<String>> packagesStr) {
        super("Валидация завершилась неудачей. Все посылки недействительны : " + packagesStr);
    }
}
