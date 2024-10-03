package ru.liga.packagesproject.services.truckloading.truckloadingstrategies;

import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.models.Truck;

import java.util.List;

public interface LoadingStrategy {              // ВОПРОС Или лучше абстрактный класс вместо интерфейса?

    List<Truck> loadPackages(List<Package> packages, List<Truck> trucks);

    /**
     * Проходится по текущему траку сверху внизу, слева направо. Если нашлась свободная ячейка, вызывается метод tryLoadPackage()
     *
     * @param truck текущий грузовик
     * @param pack  посылка, которую нужно загрузить
     * @return true, если посылка успешно загружена, иначе false
     */
    default boolean findSpaceForLoadingPackageIntoTruckAndTryToLoad(Truck truck, Package pack) {
        for (int row = truck.getHeight() - 1; row >= 0; row--) {
            for (int column = 0; column < truck.getWidth(); column++) {
                if (!truck.isCellOccupied(row, column)) {
                    if (tryLoadPackage(truck, pack, row, column)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Сортируем посылки в порядке убывания площади, чтобы максимальные посылки были первыми.
     *
     * @param packages список посылок
     */
    default void sortPackagesByAreaInDescendingOrder(List<Package> packages) {
        packages.sort((p1, p2) -> Integer.compare(p2.getArea(), p1.getArea()));
    }

    default void sortTrucksByAreaInDescendingOrder(List<Truck> trucks) {
        trucks.sort((p1, p2) -> Integer.compare(p2.getArea(), p1.getArea()));
    }

    /**
     * Вычисляются ширина и высота свободного места. Далее сравниваются габариты посылки с габаритами свободного места
     * Если посылка влезает, вызывается метод погрузки.
     *
     * @param truck трак, в которые будет совершена попытка погрузки
     * @param pack  посылка, которую нужно попробовать вместить
     * @param row   начальная строка для загрузки
     * @param col   начальный столбец для загрузки
     * @return true, если посылка была успешно загружена, иначе false
     */
    default boolean tryLoadPackage(Truck truck, Package pack, int row, int col) {
        int possibleCapacityWidth = calculatePossibleCapacityWidth(truck, col, row);
        int possibleCapacityHeight = calculatePossibleCapacityHeight(truck, row, col);

        int packageWidth = pack.getWidth();
        int packageHeight = pack.getHeight();

        if (isPackageFitInCapacity(packageWidth, possibleCapacityWidth, packageHeight, possibleCapacityHeight) && hasValidSupport(truck, pack, row, col)) {
            truck.loadPackage(row, col, pack);
            return true;
        }

        return false;
    }

    private boolean isPackageFitInCapacity(
            int packageWidth,
            int possibleCapacityWidth,
            int packageHeight,
            int possibleCapacityHeight
    ) {
        return packageWidth <= possibleCapacityWidth && packageHeight <= possibleCapacityHeight;
    }

    private boolean hasValidSupport(Truck truck, Package pack, int row, int column) {
        int baseLength = pack.getBaseLength();

        if (row == truck.getHeight() - 1) return true;

        int supportCount = 0;
        for (int col = 0; col < baseLength; col++) {
            if (row == truck.getHeight() || truck.getBody()[row + 1][column + col] != ' ') {
                supportCount++;
            }
        }

        return supportCount >= (baseLength + 1) / 2;
    }

    private int calculatePossibleCapacityWidth(Truck truck, int column, int row) {
        int possibleCapacityWidth = 1;
        for (int w = 1; w < truck.getWidth() - column; w++) {
            if (truck.getBody()[row][column + w] == ' ') {
                possibleCapacityWidth++;
            } else {
                break;
            }
        }
        return possibleCapacityWidth;
    }

    private int calculatePossibleCapacityHeight(Truck truck, int row, int column) {
        int possibleCapacityHeight = 1;
        for (int h = 1; h < truck.getHeight(); h++) {
            if (row - h >= 0) {
                if (truck.getBody()[row - h][column] == ' ') {
                    possibleCapacityHeight++;
                }
            }
        }
        return possibleCapacityHeight;
    }
}
