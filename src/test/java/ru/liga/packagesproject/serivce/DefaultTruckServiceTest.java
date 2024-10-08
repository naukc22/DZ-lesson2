package ru.liga.packagesproject.serivce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.liga.packagesproject.dto.TruckBodyDto;
import ru.liga.packagesproject.dto.TruckDto;
import ru.liga.packagesproject.models.Truck;
import ru.liga.packagesproject.models.TruckLoadingProcessSettings;
import ru.liga.packagesproject.service.IO.input.InputReader;
import ru.liga.packagesproject.service.IO.output.OutputWriter;
import ru.liga.packagesproject.service.impl.DefaultPackageService;
import ru.liga.packagesproject.service.impl.DefaultTruckService;
import ru.liga.packagesproject.service.truckloading.truckloadingstrategies.LoadingStrategy;
import ru.liga.packagesproject.service.truckunloading.TruckUnloader;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class DefaultTruckServiceTest {

    @Mock
    private DefaultPackageService defaultPackageService;

    @Mock
    private TruckUnloader truckUnloader;

    @Mock
    private InputReader<TruckBodyDto> truckBodiesJsonReader;

    @Mock
    private OutputWriter<TruckDto> truckJsonWriter;

    @InjectMocks
    private DefaultTruckService defaultTruckService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testLoadPackagesToTrucks_PackageNames_NoValidPackages() {
        TruckLoadingProcessSettings settings = mock(TruckLoadingProcessSettings.class);
        when(defaultPackageService.findPackageByName(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> defaultTruckService.loadPackagesToTrucks(new String[]{"InvalidPackage"}, settings))
                .isInstanceOf(RuntimeException.class);

        verify(defaultPackageService, times(1)).findPackageByName("InvalidPackage");
    }

    @Test
    public void testUnloadTrucksFromJsonFile_Success() {
        TruckBodyDto truckBodyDto = new TruckBodyDto(new char[2][2]);
        Truck truck = new Truck(2, 2);
        when(truckBodiesJsonReader.read(anyString())).thenReturn(List.of(truckBodyDto));
        when(truckUnloader.unloadTruck(any(TruckBodyDto.class))).thenReturn(truck);

        List<Truck> trucks = defaultTruckService.unloadTrucksFromJsonFile("filePath");

        assertThat(trucks).containsExactly(truck);
        verify(truckBodiesJsonReader, times(1)).read("filePath");
        verify(truckUnloader, times(1)).unloadTruck(truckBodyDto);
    }

    @Test
    public void testWriteTrucksToJsonFile() {
        List<Truck> trucks = List.of(new Truck(2, 2));
        doNothing().when(truckJsonWriter).write(anyList(), anyString());

        defaultTruckService.writeTrucksToJsonFile(trucks, "filePathDestination");

        verify(truckJsonWriter, times(1)).write(anyList(), eq("filePathDestination"));
    }

}

