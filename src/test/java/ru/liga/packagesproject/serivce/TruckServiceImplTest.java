package ru.liga.packagesproject.serivce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.liga.packagesproject.dto.TruckBodyDto;
import ru.liga.packagesproject.dto.TruckDto;
import ru.liga.packagesproject.dto.Truck;
import ru.liga.packagesproject.dto.TruckLoadingProcessSettings;
import ru.liga.packagesproject.service.IO.input.InputReader;
import ru.liga.packagesproject.service.IO.output.OutputWriter;
import ru.liga.packagesproject.service.impl.PackageServiceImpl;
import ru.liga.packagesproject.service.impl.TruckServiceImpl;
import ru.liga.packagesproject.service.truckunloading.impl.DefaultTruckUnloader;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class TruckServiceImplTest {

    @Mock
    private PackageServiceImpl packageServiceImpl;

    @Mock
    private DefaultTruckUnloader defaultTruckUnloader;

    @Mock
    private InputReader<TruckBodyDto> truckBodiesJsonReader;

    @Mock
    private OutputWriter<TruckDto> truckJsonWriter;

    @InjectMocks
    private TruckServiceImpl truckServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testLoadPackagesToTrucks_PackageNames_NoValidPackages() {
        TruckLoadingProcessSettings settings = mock(TruckLoadingProcessSettings.class);
        when(packageServiceImpl.findByName(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> truckServiceImpl.loadPackagesToTrucks(new String[]{"InvalidPackage"}, settings))
                .isInstanceOf(RuntimeException.class);

        verify(packageServiceImpl, times(1)).findByName("InvalidPackage");
    }

    @Test
    public void testUnloadTrucksFromJsonFile_Success() {
        TruckBodyDto truckBodyDto = new TruckBodyDto(new char[2][2]);
        Truck truck = new Truck(2, 2);
        when(truckBodiesJsonReader.read(anyString())).thenReturn(List.of(truckBodyDto));
        when(defaultTruckUnloader.unloadTruck(any(TruckBodyDto.class))).thenReturn(truck);

        List<Truck> trucks = truckServiceImpl.unloadTrucksFromJsonFile("filePath");

        assertThat(trucks).containsExactly(truck);
        verify(truckBodiesJsonReader, times(1)).read("filePath");
        verify(defaultTruckUnloader, times(1)).unloadTruck(truckBodyDto);
    }

    @Test
    public void testWriteTrucksToJsonFile() {
        List<Truck> trucks = List.of(new Truck(2, 2));
        doNothing().when(truckJsonWriter).write(anyList(), anyString());

        truckServiceImpl.writeTrucksToJsonFile(trucks, "filePathDestination");

        verify(truckJsonWriter, times(1)).write(anyList(), eq("filePathDestination"));
    }

}

