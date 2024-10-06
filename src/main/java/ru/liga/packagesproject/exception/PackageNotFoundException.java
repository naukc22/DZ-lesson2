package ru.liga.packagesproject.exception;

public class PackageNotFoundException extends RuntimeException {
    public PackageNotFoundException(String packageName) {
        super("Посылка с именем " + packageName + " не найдена");
    }
}
