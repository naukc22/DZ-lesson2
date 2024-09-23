package ru.liga.packagesproject.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Truck {
    private static final char TRUCK_BOARD_SYMBOL = '+';

    private final int height;
    private final int width;
    @Getter
    private char[][] body;

    public Truck(int height, int width) {
        this.height = height;
        this.width = width;
        initializeBody();
    }

    private void initializeBody() {
        body = new char[height + 1][width + 2];
        fillBodyWithBoarderAndWhitespace();
    }

    public Truck(char[][] body) {
        this.body = body;
        this.width = body[0].length;
        this.height = body.length;
    }

    public boolean tryLoadPackage(List<Package> packages, int row, int col) {
        for (Package pack : packages) {
            int packageWidth = pack.getWidth();
            int packageHeight = pack.getHeight();

            int possibleCapacityWidth = calculatePossibleCapacityWidth(col, row);
            int possibleCapacityHeight = calculatePossibleCapacityHeight(row, col);

            if (isPackageFitInCapacity(row, col, pack, packageWidth, possibleCapacityWidth, packageHeight, possibleCapacityHeight)) {
                loadPackage(row, col, pack);
                packages.remove(pack);
                log.info("Посылка загружена в грузовик на позицию ({}, {}). Текущая нагрузка: {}", row, col, getCurrentLoad());
                return true;
            }
        }
        return false;
    }

    public int getCurrentLoad() {
        int load = 0;
        for (int row = 0; row <= height; row++) {
            for (int col = 0; col <= width; col++) {
                if (body[row][col] != ' ' && body[row][col] != TRUCK_BOARD_SYMBOL) {
                    load++;
                }
            }
        }
        log.debug("Текущая нагрузка грузовика: {}", load);
        return load;
    }

    public void printTruck() {
        for (char[] row : body) {
            for (char cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }
    }

    private boolean isPackageFitInCapacity(int row, int col, Package pack, int packageWidth, int possibleCapacityWidth, int packageHeight, int possibleCapacityHeight) {
        return packageWidth <= possibleCapacityWidth && packageHeight <= possibleCapacityHeight && hasValidSupport(pack, row, col);
    }

    private void fillBodyWithBoarderAndWhitespace() {
        for (int row = 0; row < height + 1; row++) {
            for (int column = 0; column < width + 2; column++) {
                if (isTruckBorder(row, column)) {
                    body[row][column] = TRUCK_BOARD_SYMBOL; // Границы
                } else {
                    body[row][column] = ' '; // Внутреннее пространство для посылок
                }
            }
        }
    }

    private boolean isTruckBorder(int row, int column) {
        return row == height || column == 0 || column == width + 1;
    }


    public boolean isCurrentCellOccupied(int row, int col) {
        return body[row][col] != ' ';
    }

    private int calculatePossibleCapacityWidth(int column, int row) {
        int possibleCapacityWidth = 1;
        for (int w = 1; w < width + 1 - column; w++) {
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

    private boolean hasValidSupport(Package pack, int i, int j) {
        int baseLength = pack.getBaseLength();

        int supportCount = 0;
        for (int col = 0; col < baseLength; col++) {
            if (i == height || body[i + 1][j + col] != ' ') {
                supportCount++;
            }
        }

        return supportCount >= (baseLength + 1) / 2;
    }

    private void loadPackage(int i, int j, Package pack) {
        int[][] shape = pack.getShape();
        int packHeight = pack.getHeight();

        for (int row = 0; row < packHeight; row++) {
            int packRow = packHeight - 1 - row;

            for (int col = 0; col < shape[packRow].length; col++) {
                body[i - row][j + col] = (char) (shape[packRow][col] + '0');
            }
        }
    }

}
