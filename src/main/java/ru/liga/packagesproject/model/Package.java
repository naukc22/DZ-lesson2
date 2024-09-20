package ru.liga.packagesproject.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public class Package {

    private final int[][] shape;

    public int getHeight() {
        return shape.length;
    }

    public Package(int[][] shape) {
        this.shape = shape;
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

    @Override
    public String toString() {
        return "Тип " + shape[0][0]; // Или любой другой способ уникальной идентификации
    }
}
