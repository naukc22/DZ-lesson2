package ru.liga.packagesproject.validator;

import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.model.Package;

import java.util.Iterator;
import java.util.List;

@Slf4j
public class PackageValidator {

    private static boolean isValidPackage(Package pack, int truckHeight, int truckWidth) {
        int packageWidth = pack.getWidth();
        int packageHeight = pack.getHeight();
        return packageWidth <= truckWidth && packageHeight <= truckHeight;
    }


    public static void sortValidPackages(List<Package> packages, int truckHeight, int truckWidth) {
        log.debug("Начало сортировки посылок. Высота кузова: {}, Ширина кузова: {}", truckHeight, truckWidth);

        Iterator<Package> iterator = packages.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            Package pack = iterator.next();
            index++;
            if (!isValidPackage(pack, truckHeight, truckWidth)) {
                iterator.remove();
                log.info("Посылка #{} удалена: размер больше, чем кузов.", index);
            }
        }

        log.info("Сортировка посылок завершена. Оставшиеся посылки: {}", packages.size());
    }
}
