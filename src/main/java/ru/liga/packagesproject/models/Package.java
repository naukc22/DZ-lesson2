package ru.liga.packagesproject.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

/**
 * Класс, представляющий посылку с её формой, названием, и символом из которого она состоит..
 */

@Getter
@Setter
public class Package {

    private String name;
    private char symbol;
    private char[][] form;

    public Package(String name, char symbol, List<String> packageLines) {
        this.name = name;
        this.symbol = symbol;
        this.form = convertStringsToPackageForm(packageLines);
    }

//    public Package(char[][] form) {
//        this.form = form;
//    }

    private static char[][] convertStringsToPackageForm(List<String> packageLines) {
        char[][] shape = new char[packageLines.size()][];
        for (int i = 0; i < packageLines.size(); i++) {
            String line = packageLines.get(i);
            shape[i] = line.toCharArray();
        }
        return shape;
    }

    public int getHeight() {
        return form.length;
    }

    public int getWidth() {
        return Arrays.stream(form)
                .mapToInt(arr -> arr.length)
                .max()
                .orElse(0);
    }

    public int getBaseLength() {
        return form[form.length - 1].length;
    }

    public int getArea() {
        int area = 0;

        for (char[] row : form) {
            for (char cell : row) {
                if (cell != ' ') {
                    area++;
                }
            }
        }

        return area;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name).append("\n");
        sb.append("Symbol: ").append(symbol).append("\n");
        sb.append("Form:\n");
        for (char[] line : form) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

}
