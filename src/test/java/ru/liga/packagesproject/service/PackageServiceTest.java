package ru.liga.packagesproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.liga.packagesproject.exception.PackageNotFoundException;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.repository.PackageRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PackageServiceTest {

    @Mock
    private PackageRepository packageRepository;

    @InjectMocks
    private PackageService packageService;

    private Package examplePackage;

    @BeforeEach
    void setUp() {
        examplePackage = new Package("Package1", 'A', List.of("###", "# #", "###"));
    }

    @Test
    void addPackage_shouldAddPackage() {
        when(packageRepository.findByName("Package1")).thenReturn(null);

        packageService.addPackage("Package1", 'A', List.of("###", "# #", "###"));

        verify(packageRepository, times(1)).addPackage(any(Package.class));
    }

    @Test
    void addPackage_shouldThrowException_whenPackageWithNameAlreadyExists() {
        when(packageRepository.findByName("Package1")).thenReturn(examplePackage);

        assertThatThrownBy(() -> packageService.addPackage("Package1", 'A', List.of("###", "# #", "###")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Посылка с таким именем уже существует");
    }

    @Test
    void getPackageByName_shouldReturnPackage_whenPackageExists() {
        when(packageRepository.findByName("Package1")).thenReturn(examplePackage);

        Package result = packageService.getPackageByName("Package1");

        assertThat(result).isEqualTo(examplePackage);
    }

    @Test
    void getPackageByName_shouldThrowException_whenPackageNotFound() {
        when(packageRepository.findByName("Package1")).thenReturn(null);

        assertThatThrownBy(() -> packageService.getPackageByName("Package1"))
                .isInstanceOf(PackageNotFoundException.class)
                .hasMessageContaining("Посылка не найдена: Package1");
    }

    @Test
    void getPackagesBySymbol_shouldReturnPackages_whenPackagesExist() {
        when(packageRepository.findBySymbol('A')).thenReturn(List.of(examplePackage));

        List<Package> result = packageService.getPackagesBySymbol('A');

        assertThat(result).containsExactly(examplePackage);
    }

    @Test
    void getPackagesBySymbol_shouldThrowException_whenPackagesNotFound() {
        when(packageRepository.findBySymbol('A')).thenReturn(List.of());

        assertThatThrownBy(() -> packageService.getPackagesBySymbol('A'))
                .isInstanceOf(PackageNotFoundException.class)
                .hasMessageContaining("Посылки с символом A не найдены.");
    }

    @Test
    void editPackage_shouldEditPackage() {
        when(packageRepository.findByName("Package1")).thenReturn(examplePackage);

        packageService.editPackage("Package1", 'B', List.of("##", "##"));

        verify(packageRepository, times(1)).updatePackage(eq("Package1"), any(Package.class));
    }

    @Test
    void removePackage_shouldRemovePackage_whenPackageExists() {
        when(packageRepository.findByName("Package1")).thenReturn(examplePackage);

        packageService.removePackage("Package1");

        verify(packageRepository, times(1)).removePackage("Package1");
    }

    @Test
    void removePackage_shouldThrowException_whenPackageNotFound() {
        when(packageRepository.findByName("Package1")).thenReturn(null);

        assertThatThrownBy(() -> packageService.removePackage("Package1"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Посылка не найдена : Package1");
    }

    @Test
    void getAllPackagesList_shouldReturnAllPackages() {
        when(packageRepository.findAll()).thenReturn(Map.of("Package1", examplePackage));

        List<Package> result = packageService.getAllPackagesList();

        assertThat(result).containsExactly(examplePackage);
    }

    @Test
    void getPackageByForm_shouldReturnPackage_whenFormMatches() {
        when(packageRepository.findAll()).thenReturn(Map.of("Package1", examplePackage));

        Optional<Package> result = packageService.getPackageByForm(List.of("###", "# #", "###"));

        assertThat(result).isPresent().contains(examplePackage);
    }

    @Test
    void getPackageByForm_shouldReturnEmpty_whenFormDoesNotMatch() {
        when(packageRepository.findAll()).thenReturn(Map.of("Package1", examplePackage));

        Optional<Package> result = packageService.getPackageByForm(List.of("##", "##"));

        assertThat(result).isEmpty();
    }
}
