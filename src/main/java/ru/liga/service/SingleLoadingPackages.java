package ru.liga.service;

import java.util.ArrayList;
import java.util.List;

public class SingleLoadingPackages extends FullLoadingPackages implements LoadingPackages {

    public SingleLoadingPackages(int truckWidth, int truckHeight) {
        super(truckWidth, truckHeight);
    }

    @Override
    public List<char[][]> loadPackages(List<int[][]> packages) {

        List<char[][]> trucks = new ArrayList<>();

        for (int[][] pack : packages) {
            char[][] currentTruck = truckService.createEmptyTruck(truckHeight, truckWidth);
            super.loadPackage(currentTruck, super.truckHeight - 1, 1, pack);
            trucks.add(currentTruck);
        }

        return trucks;
    }
}

