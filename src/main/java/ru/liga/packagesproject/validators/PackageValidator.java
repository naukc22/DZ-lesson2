package ru.liga.packagesproject.validators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.services.PackageService;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для валидации посылок на основе их размера по сравнению с размерами кузова трака.
 * Осуществляет проверку, помещается ли посылка в кузов, и удаляет неподходящие посылки.
 */
@Slf4j
@Component
public class PackageValidator {

    private final PackageService packageService;

    public PackageValidator(PackageService packageService) {
        this.packageService = packageService;
    }


//    /**
//     * Проверяет, если длины массивов внутри двумерного массива одинаковые, то возвращает true.
//     *
//     * @param form форма посылки.
//     */
//    public static boolean isHasSquareForm(char[][] form) {
//        int lengthOfFirstLine = form[0].length;
//        for (char[] chars : form) {
//            if (lengthOfFirstLine != chars.length) {
//                return false;
//            }
//        }
//        return true;
//    }

}
