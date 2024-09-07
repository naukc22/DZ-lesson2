package ru.liga;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LoadTrucks {

    int truckWidth;
    int truckHeight;

    public LoadTrucks(int truckWidth, int truckHeight) {
        this.truckWidth = truckWidth;
        this.truckHeight = truckHeight;
    }
    
    public char[][] createEmptyTruck() {
        char[][] truck = new char[truckHeight + 1][truckWidth + 2];
        for (int i = 0; i < truckHeight + 1; i++) {
            for (int j = 0; j < truckWidth + 2; j++) {
                if (i == truckHeight || j == 0 || j == truckWidth + 1) {
                    truck[i][j] = '+';
                } else {
                    truck[i][j] = ' ';
                }
            }
        }
        return truck;
    }

    public void printAllTrucks(List<char[][]> trucks) {
        int truckNumber = 1;
        for (char[][] truck : trucks) {
            System.out.println("Truck " + truckNumber + ":");
            printTruck(truck);
            truckNumber++;
        }
    }

    //  Печать трака
    public void printTruck (char[][] truck) {
        for (char[] row : truck) {
            for (char cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }
    }

    // Метод для чтения посылок из файла
    public List<int[][]> readPackagesFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        List<int[][]> packages = new ArrayList<>();
        String line;
        List<String> packageLines = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                if (!packageLines.isEmpty()) {
                    packages.add(convertToPackage(packageLines));
                    packageLines.clear();
                }
            } else {
                packageLines.add(line);
            }
        }
        // Добавляем последнюю посылку
        if (!packageLines.isEmpty()) {
            packages.add(convertToPackage(packageLines));
        }

        return packages;
    }

    // Основной алгоритм загрузки посылок в кузов
    public List<char[][]> loadTrucks(List<int[][]> packages) {
        sortPackages(packages);
        List<char[][]> trucks = new ArrayList<>();
        char[][] currentTruck = createEmptyTruck();
        trucks.add(currentTruck);

        for (int i = truckHeight - 1; i >= 0 ; i--) {

            for (int j = 1; j <= truckWidth; j++) {

                if (!packages.isEmpty() && j == truckWidth && i == 0) {
                    currentTruck = createEmptyTruck();
                    trucks.add(currentTruck);
                    i = truckHeight - 1;
                    j = 0;
                }
                if (currentTruck[i][j] != ' ') {
                    continue;
                }

                // Вычисляем максимальную ширину и высотку посылки которая сюда поместиться
                int possibleWidth = 1;
                for (int w = 1; w < truckWidth + 1 - j; w++) {
                    if (currentTruck[i][j + w] == ' ') {
                        possibleWidth++;
                    } else {
                        break;
                    }
                }
                int possibleHeight = 1;
                for (int h = 1; h < truckWidth; h++) {
                    if (i - h >= 0) {
                        if (currentTruck[i - h][j] == ' ') {
                            possibleHeight++;
                        }
                    }
                }

                // Ищем и грузим подходящую под размер посылку
                boolean loaded = loadSuitablePackage(packages, possibleWidth, possibleHeight, currentTruck, i, j);

                // Если не удалось загрузить в текущий контейнер, создаем новый
                if (!loaded && !packages.isEmpty() && j == truckWidth && i == 0) {
                    currentTruck = createEmptyTruck();
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
    public void sortPackages(List<int[][]> packages) {
        packages.sort(Comparator.comparingInt(matrix -> matrix[0][0]));
        Collections.reverse(packages);
    }

    // Метод для конвертации строкового представления посылки в массив
    public int[][] convertToPackage(List<String> packageLines) {
        int[][] pack = new int[packageLines.size()][];

        for (int i = 0; i < packageLines.size(); i++) {
            String line = packageLines.get(i);
            pack[i] = new int[line.length()];
            for (int j = 0; j < line.length(); j++) {
                pack[i][j] = Character.getNumericValue(line.charAt(j));
            }
        }
        return pack;
    }

}
