package ru.liga.packagesproject.exception;

public class PackageAlreadyExistsException extends Exception{
    public PackageAlreadyExistsException(String packageName) {
        super("Посылка с именем " + packageName + " уже существует.");
    }

}
