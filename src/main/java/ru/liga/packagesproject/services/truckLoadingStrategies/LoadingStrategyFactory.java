package ru.liga.packagesproject.services.truckLoadingStrategies;

import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.models.LoadingMode;

@Slf4j
public class LoadingStrategyFactory {
    public static LoadingStrategy getStrategyFromLoadingMode(LoadingMode mode) {
        return switch (mode) {
            case EFFECTIVE -> new EffectiveLoadingStrategy();
            case BALANCED -> new BalancedLoadingStrategy();
        };
    }
}