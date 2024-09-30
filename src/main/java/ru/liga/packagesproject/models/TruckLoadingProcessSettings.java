package ru.liga.packagesproject.models;

import lombok.Data;
import lombok.Getter;
import ru.liga.packagesproject.controllers.LoadingMode;

import java.util.ArrayList;
import java.util.List;

@Data
public class TruckLoadingProcessSettings {
    private final List<TruckSize> truckSizes;
    private final LoadingMode loadingMode;
    private final int truckCount;

    public TruckLoadingProcessSettings(String[] truckSizeStrings, int truckCount, LoadingMode loadingMode) {
        this.truckSizes = new ArrayList<>();
        for (String sizeString : truckSizeStrings) {
            truckSizes.add(new TruckSize(sizeString));
        }
        this.truckCount = truckCount;
        this.loadingMode = loadingMode;
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
