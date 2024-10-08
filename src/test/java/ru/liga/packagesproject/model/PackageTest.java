package ru.liga.packagesproject.model;

import org.junit.jupiter.api.Test;
import ru.liga.packagesproject.models.Package;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PackageTest {

    @Test
    public void testGetFormAsCharArray() {
        List<String> form = Arrays.asList("XX", "X ", "XX");
        Package aPackage = new Package("testPackage", 'X', form);

        char[][] expectedForm = {
                {'X', 'X'},
                {'X', ' '},
                {'X', 'X'}
        };

        char[][] resultForm = aPackage.getFormAsCharArray();

        assertThat(resultForm).isDeepEqualTo(expectedForm);
    }

    @Test
    public void testSetFormAsCharArray() {
        Package aPackage = new Package();
        char[][] formArray = {
                {'X', 'X'},
                {'X', ' '},
                {'X', 'X'}
        };

        aPackage.setFormAsCharArray(formArray);
        List<String> expectedForm = Arrays.asList("XX", "X ", "XX");

        assertThat(aPackage.getForm()).isEqualTo(expectedForm);
    }

    @Test
    public void testGetHeight() {
        List<String> form = Arrays.asList("XX", "X ", "XX");
        Package aPackage = new Package("testPackage", 'X', form);

        int expectedHeight = 3;

        assertThat(aPackage.getHeight()).isEqualTo(expectedHeight);
    }

    @Test
    public void testGetWidth() {
        List<String> form = Arrays.asList("XX", "X ", "XXX");
        Package aPackage = new Package("testPackage", 'X', form);

        int expectedWidth = 3;

        assertThat(aPackage.getWidth()).isEqualTo(expectedWidth);
    }

    @Test
    public void testGetArea() {
        List<String> form = Arrays.asList("XX", "X ", "XX");
        Package aPackage = new Package("testPackage", 'X', form);

        int expectedArea = 5;

        assertThat(aPackage.getArea()).isEqualTo(expectedArea);
    }
}
