package ru.liga.packagesproject.services.IO.input;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.models.dto.TruckDTO;
import ru.liga.packagesproject.models.dto.TrucksSetDTO;
import ru.liga.packagesproject.models.Truck;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TruckJsonReader implements InputReader<Truck> {

    @Override
    public List<Truck> read(String filePath) {
        log.info("Начинаем загрузку грузовиков из файла: {}", filePath);
        Gson gson = new Gson();
        List<Truck> truckList = new ArrayList<>();

        try (FileReader reader = new FileReader(filePath)) {
            TrucksSetDTO trucksDTO = gson.fromJson(reader, TrucksSetDTO.class);

            for (TruckDTO truckDTO : trucksDTO.getTrucks()) {
                truckList.add(new Truck(truckDTO.getBody()));
            }

            log.info("Загрузка грузовиков завершена. Всего грузовиков: {}", truckList.size());
        } catch (FileNotFoundException e) {
            log.error("Файл не найден: {}", filePath, e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("Ошибка при чтении файла", e);
            throw new RuntimeException(e);
        }

        return truckList;
    }
}

