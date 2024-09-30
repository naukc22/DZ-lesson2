package ru.liga.packagesproject.models;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Slf4j
public class Truck {
    private final int height;
    private final int width;
    private char[][] body;
    private List<Package> loadedPackages;

    public Truck(int height, int width) {
        this.height = height;
        this.width = width;
        initializeBody();
        loadedPackages = new ArrayList<>();
    }

    private void initializeBody() {
        body = new char[height][width];
        fillBodyWithWhitespace();
    }

    public Truck(char[][] body) {     // TODO добавить посылки, которые определяются при разгрузке
        this.body = body;
        this.width = body[0].length;
        this.height = body.length;
    }

    private void fillBodyWithWhitespace() {
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                body[row][column] = ' ';
            }
        }
    }
    /**
     * Пытается загрузить посылку в грузовик на указанной позиции.
     *
     * @param pack посылка, которую нужно попробовать вместить
     * @param row  начальная строка для загрузки
     * @param col  начальный столбец для загрузки
     * @return true, если посылка была успешно загружена, иначе false
     */
    public boolean tryLoadPackage(Package pack, int row, int col) {
        int possibleCapacityWidth = calculatePossibleCapacityWidth(col, row);
        int possibleCapacityHeight = calculatePossibleCapacityHeight(row, col);

        int packageWidth = pack.getWidth();
        int packageHeight = pack.getHeight();

        if (isPackageFitInCapacity(row, col, pack, packageWidth, possibleCapacityWidth, packageHeight, possibleCapacityHeight)) {
            loadPackage(row, col, pack);
            log.info("Посылка загружена в {} на позицию ({}, {}). Текущая нагрузка: {}", this, row, col, getCurrentLoad());
            return true;
        }

        return false;
    }

    public int getCurrentLoad() {
        int load = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (body[row][col] != ' ') {
                    load++;
                }
            }
        }
        log.debug("Текущая нагрузка грузовика: {}", load);
        return load;
    }

    public void printTruckToConsole() {
        for (char[] row : body) {
            System.out.print("+");
            for (char cell : row) {
                System.out.print(cell);
            }
            System.out.println("+");
        }
        System.out.println("+".repeat(width + 2));
    }

    private boolean isPackageFitInCapacity(
            int row,
            int col,
            Package pack,
            int packageWidth,
            int possibleCapacityWidth,
            int packageHeight,
            int possibleCapacityHeight
    ) {
        return packageWidth <= possibleCapacityWidth && packageHeight <= possibleCapacityHeight && hasValidSupport(pack, row, col);
    }

    /**
     * Проверяет, занята ли ячейка в указанной позиции.
     *
     * @param row строка ячейки
     * @param col столбец ячейки
     * @return true, если ячейка занята, иначе false
     */
    public boolean isCellOccupied(int row, int col) {
        return body[row][col] != ' ';
    }

    private int calculatePossibleCapacityWidth(int column, int row) {
        int possibleCapacityWidth = 1;
        for (int w = 1; w < width - column; w++) {
            if (body[row][column + w] == ' ') {
                possibleCapacityWidth++;
            } else {
                break;
            }
        }
        return possibleCapacityWidth;
    }

    private int calculatePossibleCapacityHeight(int row, int column) {
        int possibleCapacityHeight = 1;
        for (int h = 1; h < height; h++) {
            if (row - h >= 0) {
                if (body[row - h][column] == ' ') {
                    possibleCapacityHeight++;
                }
            }
        }
        return possibleCapacityHeight;
    }

    private boolean hasValidSupport(Package pack, int row, int column) {
        int baseLength = pack.getBaseLength();

        if (row == height - 1) return true;

        int supportCount = 0;
        for (int col = 0; col < baseLength; col++) {
            if (row == height || body[row + 1][column + col] != ' ') {
                supportCount++;
            }
        }

        return supportCount >= (baseLength + 1) / 2;
    }

    private void loadPackage(int i, int j, Package pack) {
        char[][] shape = pack.getForm();
        int packHeight = pack.getHeight();

        for (int row = 0; row < packHeight; row++) {
            int packRow = packHeight - 1 - row;
            System.arraycopy(shape[packRow], 0, body[i - row], j, shape[packRow].length);
        }

        loadedPackages.add(pack);
    }

    public int getArea() {
        return width * height;
    }

}
