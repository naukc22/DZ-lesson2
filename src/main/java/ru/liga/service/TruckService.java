package ru.liga.service;

import java.util.List;

public class TruckService {



    public char[][] createEmptyTruck(int truckHeight, int truckWidth) {
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

    public void printTruck (char[][] truck) {
        for (char[] row : truck) {
            for (char cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }
    }

}
