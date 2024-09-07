package ru.liga;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoadTrucksTest {

    @Test
    void testCreateEmptyTruck() {
        LoadTrucks loader = new LoadTrucks(5, 3);
        char[][] truck = loader.createEmptyTruck();

        assertEquals(4, truck.length); // 3 высота кузова + 1 граница
        assertEquals(7, truck[0].length); // 5 ширина + 2 границы по бокам

        // Проверка границ (должны быть '+')
        for (int i = 0; i < truck.length; i++) {
            assertEquals('+', truck[i][0]);
            assertEquals('+', truck[i][6]);
        }
        for (int j = 0; j < truck[0].length; j++) {
            assertEquals('+', truck[3][j]);
        }
    }

    @Test
    void testLoadEmptyPackageList() {
        LoadTrucks loader = new LoadTrucks(5, 3);
        List<int[][]> packages = new ArrayList<>();

        List<char[][]> trucks = loader.loadTrucks(packages);

        // Проверяем, что один пустой грузовик был создан
        assertEquals(1, trucks.size());
        char[][] truck = trucks.get(0);

        // Проверяем, что грузовик пуст
        assertEquals('+', truck[0][0]); // граница кузова
        assertEquals(' ', truck[1][1]); // внутри пусто
    }

    @Test
    void testLoadSinglePackage() {
        LoadTrucks loader = new LoadTrucks(5, 3);
        List<int[][]> packages = new ArrayList<>();

        int[][] pack1 = {
                {1, 1},
                {1, 1}
        };
        packages.add(pack1);

        List<char[][]> trucks = loader.loadTrucks(packages);

        // Один грузовик должен быть создан
        assertEquals(1, trucks.size());
        char[][] truck = trucks.get(0);

        // Проверяем, что посылка загружена в нижний левый угол
        assertEquals('1', truck[2][1]);
        assertEquals('1', truck[2][2]);
        assertEquals('1', truck[1][1]);
        assertEquals('1', truck[1][2]);
    }

    @Test
    void testLoadPackagesThatRequireMultipleTrucks() {
        LoadTrucks loader = new LoadTrucks(5, 3);
        List<int[][]> packages = new ArrayList<>();

        int[][] pack1 = {
                {9,9,9},
                {9,9,9},
                {9,9,9}
        };
        int[][] pack2 = {
                {2, 2, 2}
        };
        int[][] pack3 = {
                {3, 3, 3, 3}
        };

        packages.add(pack1);
        packages.add(pack2);
        packages.add(pack3);

        List<char[][]> trucks = loader.loadTrucks(packages);

        // Должно быть создано два грузовика
        assertEquals(2, trucks.size());

        // Проверяем, что первый грузовик заполнен
        char[][] truck1 = trucks.get(0);
        assertEquals('9', truck1[2][1]);
        assertEquals('9', truck1[0][1]);

        // Проверяем второй грузовик
        char[][] truck2 = trucks.get(1);
        assertEquals('3', truck2[2][1]);
        assertEquals('2', truck2[1][1]);
    }

    @Test
    void testLoadPackagesDifferentSizes() {
        LoadTrucks loader = new LoadTrucks(5, 3);
        List<int[][]> packages = new ArrayList<>();

        int[][] pack1 = {
                {1}
        };
        int[][] pack2 = {
                {2, 2}
        };
        int[][] pack3 = {
                {3, 3, 3}
        };

        packages.add(pack1);
        packages.add(pack2);
        packages.add(pack3);

        List<char[][]> trucks = loader.loadTrucks(packages);

        // Один грузовик должен быть создан
        assertEquals(1, trucks.size());
        char[][] truck = trucks.get(0);

        // Проверяем загрузку разных посылок
        assertEquals('3', truck[2][1]); // Третья посылка
        assertEquals('2', truck[2][4]); // Вторая посылка
        assertEquals('1', truck[1][1]); // Первая посылка
    }

}