package ru.liga.packagesproject.services.IO.output;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.dto.TruckDTO;
import ru.liga.packagesproject.dto.TrucksSetDTO;
import ru.liga.packagesproject.models.Truck;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Slf4j
public class TruckJsonWriter implements OutputWriter<List<Truck>> {

    @Override
    public void write(List<Truck> trucks, String destination) {
        log.info("Записываем грузовики в JSON файл: {}", destination);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        TrucksSetDTO trucksDTO = new TrucksSetDTO();
        trucksDTO.setTrucks(
                trucks.stream()
                        .map(truck -> {
                            TruckDTO truckDTO = new TruckDTO();
                            truckDTO.setBody(truck.getBody());
                            return truckDTO;
                        })
                        .toList()
        );

        try (FileWriter writer = new FileWriter(destination)) {
            gson.toJson(trucksDTO, writer);
            log.info("Запись завершена успешно.");
        } catch (IOException e) {
            log.error("Ошибка при записи файла: {}", destination, e);
            throw new RuntimeException(e);
        }
    }
}

