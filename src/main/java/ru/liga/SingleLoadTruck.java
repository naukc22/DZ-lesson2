package ru.liga;

import java.util.ArrayList;
import java.util.List;

public class SingleLoadTruck extends LoadTrucks {

    public SingleLoadTruck(int truckWidth, int truckHeight) {
        super(truckWidth, truckHeight);
    }

    @Override
    public List<char[][]> loadTrucks(List<int[][]> packages) {

        List<char[][]> trucks = new ArrayList<>();

        for (int[][] pack : packages) {
            char[][] currentTruck = createEmptyTruck();
            super.loadPackage(currentTruck, super.truckHeight - 1, 1, pack);
            trucks.add(currentTruck);
        }

        return trucks;
    }
}

