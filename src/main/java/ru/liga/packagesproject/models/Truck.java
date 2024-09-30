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

    public Truck(char[][] body) {     // TODO добавить посылки, которые определяются при разгрузке
        this.body = body;
        this.width = body[0].length;
        this.height = body.length;
    }

    private void initializeBody() {
        body = new char[height][width];
        fillBodyWithWhitespace();
    }

    private void fillBodyWithWhitespace() {
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                body[row][column] = ' ';
            }
        }
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

    public void loadPackage(int i, int j, Package pack) {
        char[][] shape = pack.getForm();
        int packHeight = pack.getHeight();

        for (int row = 0; row < packHeight; row++) {
            int packRow = packHeight - 1 - row;
            System.arraycopy(shape[packRow], 0, body[i - row], j, shape[packRow].length);
        }

        loadedPackages.add(pack);
        log.info("Посылка загружена в {} на позицию ({}, {}). Текущая нагрузка: {}", this, i, j, getCurrentLoad());
    }

    public int getArea() {
        return width * height;
    }

}
