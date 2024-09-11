package ru.liga.service;

import java.util.*;

public class FullLoadingPackages implements LoadingPackages{

    int truckWidth;
    int truckHeight;
    TruckService truckService;

    public FullLoadingPackages(int truckWidth, int truckHeight) {
        this.truckWidth = truckWidth;
        this.truckHeight = truckHeight;
        truckService = new TruckService();
    }

    // Основной алгоритм загрузки посылок в кузов
    public List<char[][]> loadPackages(List<int[][]> packages) {
        sortPackages(packages);
        List<char[][]> trucks = new ArrayList<>();
        char[][] currentTruck = truckService.createEmptyTruck(truckHeight, truckWidth);
        trucks.add(currentTruck);

        for (int i = truckHeight - 1; i >= 0 ; i--) {

            for (int j = 1; j <= truckWidth; j++) {

                if (!packages.isEmpty() && j == truckWidth && i == 0) {
                    currentTruck = truckService.createEmptyTruck(truckHeight, truckWidth);
                    trucks.add(currentTruck);
                    i = truckHeight - 1;
                    j = 0;
                }
                if (currentTruck[i][j] != ' ') {
                    continue;
                }

                // Вычисляем максимальную ширину и высотку посылки которая сюда поместиться
                int possibleCapacityWidth = 1;
                for (int w = 1; w < truckWidth + 1 - j; w++) {
                    if (currentTruck[i][j + w] == ' ') {
                        possibleCapacityWidth++;
                    } else {
                        break;
                    }
                }
                int possibleCapacityHeight = 1;
                for (int h = 1; h < truckWidth; h++) {
                    if (i - h >= 0) {
                        if (currentTruck[i - h][j] == ' ') {
                            possibleCapacityHeight++;
                        }
                    }
                }

                // Ищем и грузим подходящую под размер посылку
                boolean loaded = loadSuitablePackage(packages, possibleCapacityWidth, possibleCapacityHeight, currentTruck, i, j);

                // Если не удалось загрузить в текущий контейнер, создаем новый
                if (!loaded && !packages.isEmpty() && j == truckWidth && i == 0) {
                    currentTruck = truckService.createEmptyTruck(truckHeight, truckWidth);
                    trucks.add(currentTruck);
                    i = truckHeight - 1;
                    j = 0;
                }
            }
        }
        return trucks;
    }

    private boolean loadSuitablePackage(List<int[][]> packages, int possibleWidth, int possibleHeight, char[][] truck, int i, int j) {
        for (int[][] pack : packages) {
            int length = Arrays.stream(pack)
                    .mapToInt(arr -> arr.length)
                    .max()
                    .orElse(0);

            // Проверяем, подходит ли посылка по ширине и высоте
            if (length <= possibleWidth && pack.length <= possibleHeight && hasValidSupport(pack, truck, i, j)) {
                loadPackage(truck, i, j, pack);
                packages.remove(pack);
                return true;
            }
        }
        return false;
    }

    private boolean hasValidSupport(int[][] pack, char[][] truck, int i, int j) {
        int baseLength = pack[pack.length - 1].length;

        int supportCount = 0;

        for (int col = 0; col < baseLength; col++) {
            if (i == truckHeight || truck[i + 1][j + col] != ' ') {
                supportCount++;
            }
        }

        // Посылка должна иметь поддержку не менее чем для половины своей длины
        return supportCount >= (baseLength + 1) / 2;
    }

    // Загрузка определенной посылки
    protected void loadPackage(char[][] truck, int i, int j, int[][] pack) {
        int packHeight = pack.length;

        for (int row = 0; row < packHeight; row++) {
            int packRow = packHeight - 1 - row;

            for (int col = 0; col < pack[packRow].length; col++) {
                truck[i - row][j + col] = (char) (pack[packRow][col] + '0');
            }
        }
    }

    // Метод для сортировки посылок по возрастанию
    private void sortPackages(List<int[][]> packages) {
        packages.sort(Comparator.comparingInt(matrix -> matrix[0][0]));
        Collections.reverse(packages);
    }


}
