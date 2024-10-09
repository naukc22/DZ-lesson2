package ru.liga.packagesproject.dto;

import lombok.Getter;
import lombok.Setter;
import ru.liga.packagesproject.model.Truck;

import java.util.List;

/**
 * DTO для ответа REST-контроллера.
 */
@Setter
@Getter
public class ResponseMessage {
    private String message;
    private List<Truck> trucks;

    public ResponseMessage(String message, List<Truck> trucks) {
        this.message = message;
        this.trucks = trucks;
    }

}
