package ru.liga.validation;

import java.util.Arrays;
import java.util.List;

public class PackageValidator {


    private static boolean isValidPackage(int[][] pack, int truckHeight, int truckWidth) {
        int packageWidth = Arrays.stream(pack)
                .mapToInt(arr -> arr.length)
                .max()
                .orElse(0);

        int packageHeight = pack.length;

        return packageWidth <= truckWidth && packageHeight <= truckHeight;
    }


    public static List<int[][]> sortAndGetValidPackages(List<int[][]> packages, int truckHeight, int truckWidth) {

        for (int i = 0; i < packages.size(); i++) {
            if (!isValidPackage(packages.get(i), truckHeight, truckWidth)) {
                packages.remove(packages.get(i));
                int number = i + 1;
                System.out.println("Посылка #" + number + " удалена: размер больше, чем кузов.");
            }
        }

        return packages;
    }
}
