package ru.liga.packagesproject.service.IO.output.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.packagesproject.dto.TruckDto;
import ru.liga.packagesproject.service.IO.output.OutputWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Класс для записи списка грузовиков в файл в формате JSON.
 */
@Slf4j
@Service
public class TruckJsonWriter implements OutputWriter<TruckDto> {

    /**
     * Записывает список грузовиков в файл по указанному пути в формате JSON.
     *
     * @param truckDtoList дто траков для записи в файл
     * @param destination  путь к файлу для записи
     * @throws RuntimeException если возникла ошибка ввода/вывода
     */
    @Override
    public void write(List<TruckDto> truckDtoList, String destination) {
        log.info("Записываем грузовики в JSON файл: {}", destination);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(destination)) {
            gson.toJson(truckDtoList, writer);
            log.info("Запись завершена успешно.");
        } catch (IOException e) {
            log.error("Ошибка при записи файла: {}", destination, e);
            throw new RuntimeException(e);
        }
    }

}

