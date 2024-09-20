package ru.liga.packagesproject.model;

import ru.liga.packagesproject.service.truckLoadingStrategies.BalancedLoadingStrategy;
import ru.liga.packagesproject.service.truckLoadingStrategies.EffectiveLoadingStrategy;
import ru.liga.packagesproject.service.truckLoadingStrategies.LoadingStrategy;

public enum LoadingMode {
    E {
        @Override
        public LoadingStrategy getStrategyFromMode(int truckWidth, int truckHeight) {
            return new EffectiveLoadingStrategy(truckWidth, truckHeight);
        }
    },
    B {
        @Override
        public LoadingStrategy getStrategyFromMode(int truckWidth, int truckHeight) {
            return new BalancedLoadingStrategy(truckWidth, truckHeight);
        }
    };

    // Абстрактный метод, который будет реализован в каждом элементе enum
    public abstract LoadingStrategy getStrategyFromMode(int truckWidth, int truckHeight);
}