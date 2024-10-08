package ru.liga.packagesproject.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.models.Truck;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TruckTest {

    private Truck truck;

    @BeforeEach
    public void setUp() {
        truck = new Truck(3, 3);
    }

    @Test
    public void testInitializeBody() {
        char[][] expectedBody = {
                {' ', ' ', ' '},
                {' ', ' ', ' '},
                {' ', ' ', ' '}
        };

        assertThat(truck.getBody()).isDeepEqualTo(expectedBody);
    }

    @Test
    public void testGetCurrentLoadWhenEmpty() {
        assertThat(truck.getCurrentLoad()).isEqualTo(0);
    }

    @Test
    public void testLoadPackage() {
        Package aPackage = new Package("Test", 'X', List.of("XX", "X "));
        truck.loadPackage(2, 0, aPackage);

        char[][] expectedBody = {
                {' ', ' ', ' '},
                {'X', 'X', ' '},
                {'X', ' ', ' '}
        };

        assertThat(truck.getBody()).isDeepEqualTo(expectedBody);
        assertThat(truck.getCurrentLoad()).isEqualTo(3);
    }

    @Test
    public void testIsCellOccupied() {
        Package aPackage = new Package("Test", 'X', List.of("XX", "X "));
        truck.loadPackage(2, 0, aPackage);

        assertThat(truck.isCellOccupied(2, 0)).isTrue();
        assertThat(truck.isCellOccupied(0, 0)).isFalse();
    }

    @Test
    public void testGetArea() {
        assertThat(truck.getArea()).isEqualTo(9);
    }

    @Test
    public void testTruckConstructorWithBodyAndLoadedPackages() {
        char[][] body = {
                {'X', ' ', ' '},
                {'X', 'X', ' '},
                {' ', ' ', ' '}
        };

        Map<Package, Integer> loadedPackages = new HashMap<>();
        Package aPackage = new Package("Test", 'X', List.of("XX", "X "));
        loadedPackages.put(aPackage, 1);

        Truck truckWithBody = new Truck(body, loadedPackages);

        assertThat(truckWithBody.getBody()).isDeepEqualTo(body);
        assertThat(truckWithBody.getHeight()).isEqualTo(3);
        assertThat(truckWithBody.getWidth()).isEqualTo(3);
    }
}
