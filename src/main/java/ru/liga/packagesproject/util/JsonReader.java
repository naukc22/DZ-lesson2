package ru.liga.packagesproject.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lombok.extern.log4j.Log4j2;
import ru.liga.packagesproject.model.Truck;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class JsonReader {
    public List<Truck> loadTrucksFromFile(String filePath) throws IOException {
        log.info("Начинаем загрузку грузовиков из файла: {}", filePath);

        Gson gson = new Gson();
        FileReader reader = new FileReader(filePath);

        JsonElement jsonElement = gson.fromJson(reader, JsonElement.class);
        JsonArray trucksArray = jsonElement.getAsJsonObject().getAsJsonArray("trucks");

        List<Truck> trucks = new ArrayList<>();

        for (JsonElement truckElement : trucksArray) {
            JsonArray truckRows = truckElement.getAsJsonArray();
            int height = truckRows.size();
            int width = truckRows.get(0).getAsJsonArray().size();
            char[][] body = new char[height][width];

            for (int row = 0; row < height; row++) {
                JsonArray truckRow = truckRows.get(row).getAsJsonArray();
                for (int col = 0; col < width; col++) {
                    body[row][col] = truckRow.get(col).getAsString().charAt(0);
                }
            }

            trucks.add(new Truck(body));
            log.debug("Добавлен грузовик с высотой {} и шириной {}", height, width);
        }

        log.info("Загрузка грузовиков завершена. Всего грузовиков: {}", trucks.size());
        return trucks;

    }
}
