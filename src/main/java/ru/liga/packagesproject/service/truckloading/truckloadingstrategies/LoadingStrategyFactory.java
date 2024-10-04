package ru.liga.packagesproject.service.truckloading.truckloadingstrategies;

import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.controllers.LoadingMode;

/**
 * Factory class для создания объектов {@link LoadingStrategy} на основе режима загрузки {@link LoadingMode}.
 */
@Slf4j
public class LoadingStrategyFactory {

    /**
     * Возвращает стратегию загрузки на основе указанного режима загрузки.
     *
     * @param mode режим загрузки, см. {@link LoadingMode}
     * @return объект стратегии загрузки {@link LoadingStrategy}, соответствующий переданному режиму
     */
    public static LoadingStrategy getStrategyFromLoadingMode(LoadingMode mode) {
        return switch (mode) {
            case EFFECTIVE -> new EffectiveLoadingStrategy();
            case BALANCED -> new BalancedLoadingStrategy();
        };
    }
}