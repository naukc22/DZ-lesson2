package ru.liga.packagesproject.services.IO.input;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.models.Truck;
import ru.liga.packagesproject.models.dto.TruckDTO;
import ru.liga.packagesproject.models.dto.TrucksSetDTO;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для чтения списка грузовиков из файла в формате JSON.
 */
@Slf4j
public class TruckJsonReader implements InputReader<Truck> {

    /**
     * Читает список грузовиков из указанного файла JSON.
     *
     * @param filePath путь к файлу с данными о грузовиках
     * @return список объектов {@link Truck}, прочитанных из файла
     * @throws RuntimeException если возникла ошибка чтения файла или файл не найден
     */
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

