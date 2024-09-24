package ru.liga.packagesproject.models;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class Package {

    private final int[][] shape;

    public Package(List<String> packageLines) {
        this.shape = convertStringsToPackage(packageLines);
    }

    public Package(int[][] shape) {
        this.shape = shape;
    }

    public int getHeight() {
        return shape.length;
    }

    public int getWidth() {
        return Arrays.stream(shape)
                .mapToInt(arr -> arr.length)
                .max()
                .orElse(0);
    }

    public int getBaseLength() {
        return shape[shape.length - 1].length;
    }

    public int getArea() {
        return Arrays.stream(shape)
                .flatMapToInt(Arrays::stream)
                .map(cell -> cell != 0 ? 1 : 0)
                .sum();
    }

    private static int[][] convertStringsToPackage(List<String> packageLines) {
        int[][] shape = new int[packageLines.size()][];
        for (int i = 0; i < packageLines.size(); i++) {
            String line = packageLines.get(i);
            shape[i] = new int[line.length()];
            for (int j = 0; j < line.length(); j++) {
                shape[i][j] = Character.getNumericValue(line.charAt(j));
            }
        }
        return shape;
    }

    @Override
    public String toString() {
        return "Тип " + shape[0][0]; // Или любой другой способ уникальной идентификации
    }
}
