package ru.liga.packagesproject.models;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.liga.packagesproject.util.StringListConverter;

import java.util.Arrays;
import java.util.List;

/**
 * Класс, представляющий посылку с её формой, названием и символом из которого она состоит.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(schema = "loading", name = "packages")
public class Package {

    @Id
    private String name;

    private char symbol;

    @Convert(converter = StringListConverter.class)
    private List<String> form;

    public Package(String name, char symbol, List<String> form) {
        this.name = name;
        this.symbol = symbol;
        this.form = form;
    }

    public char[][] getFormAsCharArray() {
        if (form == null) {
            return new char[0][];
        }

        char[][] formCharArray = new char[form.size()][];
        for (int i = 0; i < form.size(); i++) {
            formCharArray[i] = form.get(i).toCharArray();
        }
        return formCharArray;
    }

    public void setFormAsCharArray(char[][] formArray) {
        this.form = convertCharArrayToList(formArray);
    }

    private List<String> convertCharArrayToList(char[][] formArray) {
        return Arrays.stream(formArray)
                .map(String::new)
                .toList();
    }


    public int getHeight() {
        return getFormAsCharArray().length;
    }

    public int getWidth() {
        return Arrays.stream(getFormAsCharArray())
                .mapToInt(arr -> arr.length)
                .max()
                .orElse(0);
    }

    public int getBaseLength() {
        return getFormAsCharArray()[getFormAsCharArray().length - 1].length;
    }

    public int getArea() {
        int area = 0;
        for (char[] row : getFormAsCharArray()) {
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
        for (char[] line : getFormAsCharArray()) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}
