package ru.liga.packagesproject.service.IO.input.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.packagesproject.dto.TruckBodyDto;
import ru.liga.packagesproject.service.IO.input.InputReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для чтения списка грузовиков из файла в формате JSON.
 */
@Slf4j
@Service
public class TruckBodiesJsonReader implements InputReader<TruckBodyDto> {

    private final ObjectMapper objectMapper;

    public TruckBodiesJsonReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Читает список грузовиков из указанного файла JSON.
     *
     * @param filePath путь к файлу с данными о грузовиках
     * @return список объектов {@link TruckBodyDto}, прочитанных из файла
     * @throws RuntimeException если возникла ошибка чтения файла или файл не найден
     */
    @Override
    public List<TruckBodyDto> read(String filePath) {
        log.info("Начинаем загрузку грузовиков из файла: {}", filePath);
        List<TruckBodyDto> truckList = new ArrayList<>();

        try {
            List<List<String>> trucksData = objectMapper.readValue(new File(filePath), new TypeReference<>() {
            });

            for (List<String> truckBodyStr : trucksData) {
                char[][] body = convertToTruckBodyChar(truckBodyStr);
                TruckBodyDto truckBody = new TruckBodyDto(body);
                truckList.add(truckBody);
            }

            log.info("Загрузка грузовиков завершена. Всего грузовиков: {}", truckList.size());
        } catch (IOException e) {
            log.error("Ошибка при чтении файла: {}", filePath, e);
            throw new RuntimeException(e);
        }

        return truckList;
    }

    /**
     * Преобразует список строк в матрицу символов, убирая стенки из плюсов.
     *
     * @param truckLines список строк, представляющих тело грузовика, включая стенки
     * @return матрица символов, содержащая только внутреннюю часть без стенок
     */
    private char[][] convertToTruckBodyChar(List<String> truckLines) {
        int height = truckLines.size() - 1;
        int width = truckLines.get(0).length() - 2;

        char[][] body = new char[height][width];

        for (int i = 0; i < height; i++) {
            String row = truckLines.get(i);
            body[i] = row.substring(1, row.length() - 1).toCharArray();
        }

        return body;
    }
}
