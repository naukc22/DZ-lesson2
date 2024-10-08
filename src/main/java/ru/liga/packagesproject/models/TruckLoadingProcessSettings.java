package ru.liga.packagesproject.models;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.enums.LoadingMode;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class TruckLoadingProcessSettings {
    private final List<TruckSize> truckSizes;
    private final LoadingMode loadingMode;
    private final int truckCount;

    public TruckLoadingProcessSettings(String[] truckSizeStrings, LoadingMode loadingMode) {
        this.truckSizes = new ArrayList<>();
        for (String sizeString : truckSizeStrings) {
            truckSizes.add(new TruckSize(sizeString));
        }
        this.loadingMode = loadingMode;
        this.truckCount = truckSizes.size();
    }


    @Getter
    public static class TruckSize {
        private final int width;
        private final int height;

        public TruckSize(String sizeString) {
            String[] parts = sizeString.split("x");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid truck size format, expected WIDTHxHEIGHT (e.g., 6x6)");
            }
            this.width = Integer.parseInt(parts[0]);
            this.height = Integer.parseInt(parts[1]);
        }

        @Override
        public String toString() {
            return width + "x" + height;
        }

    }
}
