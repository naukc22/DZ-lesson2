package ru.liga.packagesproject;

import org.junit.jupiter.api.Test;
import ru.liga.packagesproject.models.Truck;
import ru.liga.packagesproject.services.PackageCounterService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PackageCounterServiceTest {

    @Test
    void testCountPackages_singleTruck() {
        char[][] truckBody = {
                {'+', '+', '+', '+', '+', '+'},
                {'+', '1', '1', ' ', ' ', '+'},
                {'+', '3', '3', '3', ' ', '+'},
                {'+', '+', '+', '+', '+', '+'}
        };

        Truck truck = new Truck(truckBody);
        Map<Character, Integer> packageCounts = PackageCounterService.countPackages(truck);

        assertEquals(2, packageCounts.get('1'));
        assertEquals(1, packageCounts.get('3'));
    }

    @Test
    void testCountPackages_multiplePackages() {
        char[][] truckBody = {
                {'+', '+', '+', '+', '+', '+'},
                {'+', '2', '2', ' ', ' ', '+'},
                {'+', '3', '3', '3', ' ', '+'},
                {'+', '2', '2', ' ', ' ', '+'},
                {'+', '+', '+', '+', '+', '+'}
        };

        Truck truck = new Truck(truckBody);
        Map<Character, Integer> packageCounts = PackageCounterService.countPackages(truck);

        assertEquals(2, packageCounts.get('2'));
        assertEquals(1, packageCounts.get('3'));

    }

    @Test
    void testCountPackages_noPackages() {
        char[][] truckBody = {
                {'+', '+', '+', '+', '+', '+'},
                {'+', ' ', ' ', ' ', ' ', '+'},
                {'+', ' ', ' ', ' ', ' ', '+'},
                {'+', '+', '+', '+', '+', '+'}
        };

        Truck truck = new Truck(truckBody);
        Map<Character, Integer> packageCounts = PackageCounterService.countPackages(truck);

        assertEquals(0, packageCounts.size());
    }
}
