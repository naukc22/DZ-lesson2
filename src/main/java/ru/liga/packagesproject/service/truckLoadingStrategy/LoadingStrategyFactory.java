package ru.liga.packagesproject.service.truckLoadingStrategy;

import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.model.LoadingMode;

@Slf4j
public class LoadingStrategyFactory {
    public static LoadingStrategy getStrategyFromLoadingMode(LoadingMode mode, int truckWidth, int truckHeight) {
        return switch (mode) {
            case EFFECTIVE -> new EffectiveLoadingStrategy(truckWidth, truckHeight);
            case BALANCED -> new BalancedLoadingStrategy(truckWidth, truckHeight);
        };
    }
}