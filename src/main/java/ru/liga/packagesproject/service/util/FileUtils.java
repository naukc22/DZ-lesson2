package ru.liga.packagesproject.service.util;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Random;

@Component
public class FileUtils {

    private final static String DIRECTORY_FOR_TEMP_FILES = "src/main/resources/temp/";

    public static File generateTempFileForLoadingTrucks() {
        String tempFileName = "loaded_trucks " + new Random().nextInt() + ".json";
        return new File(DIRECTORY_FOR_TEMP_FILES + tempFileName);
    }

    public static File generateTxtTempFileForPackages() {
        String tempFileName = "packages " + new Random().nextInt() + ".txt";
        return new File(DIRECTORY_FOR_TEMP_FILES + tempFileName);
    }
}
