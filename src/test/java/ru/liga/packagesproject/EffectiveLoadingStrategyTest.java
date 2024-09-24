package ru.liga.packagesproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.models.Truck;
import ru.liga.packagesproject.services.truckLoadingStrategies.EffectiveLoadingStrategy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EffectiveLoadingStrategyTest {

    private EffectiveLoadingStrategy effectiveLoadingStrategy;

    @BeforeEach
    void setUp() {
        effectiveLoadingStrategy = new EffectiveLoadingStrategy(); // Размер трака 6x6
    }

    @Test
    void testLoadPackagesSuccessfully() {
        // Подготовка данных для теста
        List<Package> packages = new ArrayList<>();
        packages.add(new Package(new int[][]{{1, 1}, {1, 1}})); // Посылка 2x2
        packages.add(new Package(new int[][]{{1, 1, 1}})); // Посылка 3x1

        // Загрузка посылок в один грузовик
        List<Truck> trucks = effectiveLoadingStrategy.loadPackages(packages, 1, 6, 6);

        // Проверка результатов
        assertEquals(1, trucks.size());
        Truck truck = trucks.get(0);

        // Проверяем, что все посылки успешно загрузились
        assertEquals(7, truck.getCurrentLoad()); // 2x2 = 4 + 3x1 = 3 -> всего 7 ячеек занято
    }

    @Test
    void testLoadPackagesMultipleTrucks() {
        // Подготовка данных для теста
        List<Package> packages = new ArrayList<>();
        packages.add(new Package(new int[][]{{1, 1, 1, 1, 1, 1}})); // Посылка 6x1
        packages.add(new Package(new int[][]{{1, 1}, {1, 1}, {1, 1}})); // Посылка 2x3

        // Загрузка посылок в два грузовика
        List<Truck> trucks = effectiveLoadingStrategy.loadPackages(packages, 2, 6, 6);

        // Проверка результатов
        assertEquals(2, trucks.size());

        // Проверяем, что каждая посылка была загружена
        Truck truck1 = trucks.get(0);
        Truck truck2 = trucks.get(1);

        assertEquals(6, truck1.getCurrentLoad()); // В первом грузовике посылка 6x1
        assertEquals(6, truck2.getCurrentLoad()); // Во втором грузовике посылка 2x3
    }

    @Test
    void testLoadPackagesErrorWhenNotEnoughSpace() {
        // Подготовка данных для теста
        List<Package> packages = new ArrayList<>();
        packages.add(new Package(new int[][]{{1, 1, 1, 1, 1, 1, 1}})); // Посылка 7x1 - слишком большая для грузовика 6x6

        // Проверяем, что выбрасывается исключение, если посылка не влезает
        assertThrows(RuntimeException.class, () -> effectiveLoadingStrategy.loadPackages(packages, 1, 6, 6));
    }

    @Test
    void testLoadMultiplePackagesWithSorting() {
        // Подготовка данных для теста
        List<Package> packages = new ArrayList<>();
        packages.add(new Package(new int[][]{{1}}));  // Посылка 1x1
        packages.add(new Package(new int[][]{{1, 1}, {1, 1}}));  // Посылка 2x2
        packages.add(new Package(new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}));  // Посылка 3x3

        // Загрузка посылок в один грузовик
        List<Truck> trucks = effectiveLoadingStrategy.loadPackages(packages, 1, 6, 6);

        // Проверка результатов
        assertEquals(1, trucks.size());
        Truck truck = trucks.get(0);

        // Проверяем, что все посылки успешно загружены
        assertEquals(14, truck.getCurrentLoad()); // 1 + 4 + 9 = 14 ячеек
    }

}

