package ru.liga.packagesproject.exception;

import java.util.Arrays;

public class AllPackageNamesAreNotValid extends RuntimeException {
    public AllPackageNamesAreNotValid(String[] packageNames) {
        super("Все имена посылок невалидные : " + Arrays.toString(packageNames));
    }
}
