package ru.liga.packagesproject.service.IO.input;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.packagesproject.dto.TruckBodyDto;
import ru.liga.packagesproject.models.Truck;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для чтения списка грузовиков из файла в формате JSON.
 */
@Slf4j
@Service
public class TruckBodiesJsonReader implements InputReader<TruckBodyDto> {

    /**
     * Читает список грузовиков из указанного файла JSON.
     *
     * @param filePath путь к файлу с данными о грузовиках
     * @return список объектов {@link Truck}, прочитанных из файла
     * @throws RuntimeException если возникла ошибка чтения файла или файл не найден
     */
    @Override
    public List<TruckBodyDto> read(String filePath) {
        log.info("Начинаем загрузку грузовиков из файла: {}", filePath);
        Gson gson = new Gson();
        List<TruckBodyDto> truckList = new ArrayList<>();

        try (FileReader reader = new FileReader(filePath)) {
            Type listOfTrucksType = new TypeToken<List<List<String>>>() {
            }.getType();
            List<List<String>> trucksData = gson.fromJson(reader, listOfTrucksType);

            for (List<String> truckBodyStr : trucksData) {
                char[][] body = convertToTruckBodyChar(truckBodyStr);
                TruckBodyDto truckBody = new TruckBodyDto(body);
                truckList.add(truckBody);
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

