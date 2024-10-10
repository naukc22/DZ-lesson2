package ru.liga.packagesproject.service.truckloading;

import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.dto.enums.LoadingMode;

/**
 * Factory class для создания объектов {@link LoadingStrategyService} на основе режима загрузки {@link LoadingMode}.
 */
@Slf4j
public class LoadingStrategyFactory {

    /**
     * Возвращает стратегию загрузки на основе указанного режима загрузки.
     *
     * @param mode режим загрузки, см. {@link LoadingMode}
     * @return объект стратегии загрузки {@link LoadingStrategyService}, соответствующий переданному режиму
     */
    public static LoadingStrategyService getStrategyFromLoadingMode(LoadingMode mode) {
        return switch (mode) {
            case EFFECTIVE -> new EffectiveLoadingStrategyService();
            case BALANCED -> new BalancedLoadingStrategyService();
        };
    }
}