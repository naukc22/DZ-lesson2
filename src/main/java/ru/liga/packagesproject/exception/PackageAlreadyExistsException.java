package ru.liga.packagesproject.exception;

public class PackageAlreadyExistsException extends RuntimeException {
    public PackageAlreadyExistsException(String packageName) {
        super("Посылка с именем " + packageName + " уже существует.");
    }

}
