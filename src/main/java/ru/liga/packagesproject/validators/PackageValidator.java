package ru.liga.packagesproject.validators;

import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.models.Package;

import java.util.Iterator;
import java.util.List;

/**
 * Класс для валидации посылок на основе их размера по сравнению с размерами кузова трака.
 * Осуществляет проверку, помещается ли посылка в кузов, и удаляет неподходящие посылки.
 */
@Slf4j
public class PackageValidator {

    /**
     * Выполняет сортировку списка посылок, удаляя те, которые не помещаются в кузов.
     * Логирует информацию о процессе удаления.
     *
     * @param packages    список {@link Package}, представляющий посылки.
     * @param truckHeight высота кузова трака.
     * @param truckWidth  ширина кузова трака.
     */
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

    private static boolean isValidPackage(Package pack, int truckHeight, int truckWidth) {
        int packageWidth = pack.getWidth();
        int packageHeight = pack.getHeight();
        return packageWidth <= truckWidth && packageHeight <= truckHeight;
    }



//    /**
//     * Проверяет, если длины массивов внутри двумерного массива одинаковые, то возвращает true.
//     *
//     * @param form форма посылки.
//     */
//    public static boolean isHasSquareForm(char[][] form) {
//        int lengthOfFirstLine = form[0].length;
//        for (char[] chars : form) {
//            if (lengthOfFirstLine != chars.length) {
//                return false;
//            }
//        }
//        return true;
//    }

}
