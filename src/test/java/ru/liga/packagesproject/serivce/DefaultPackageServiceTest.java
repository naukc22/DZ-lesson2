package ru.liga.packagesproject.serivce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.liga.packagesproject.exception.PackageAlreadyExistsException;
import ru.liga.packagesproject.exception.PackageNotFoundException;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.repository.PackageRepository;
import ru.liga.packagesproject.service.impl.DefaultPackageService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DefaultPackageServiceTest {

    @Mock
    private PackageRepository packageRepository;

    @InjectMocks
    private DefaultPackageService defaultPackageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllPackages() {
        List<Package> mockPackages = List.of(new Package("Test1", 'X', List.of("XX")), new Package("Test2", 'O', List.of("OO")));
        when(packageRepository.findAll()).thenReturn(mockPackages);

        Iterable<Package> result = defaultPackageService.findAllPackages();

        assertThat(result).containsExactlyElementsOf(mockPackages);
        verify(packageRepository, times(1)).findAll();
    }

    @Test
    public void testFindPackageByName_Found() {
        Package mockPackage = new Package("Test", 'X', List.of("XX"));
        when(packageRepository.findByNameIgnoreCase("Test")).thenReturn(Optional.of(mockPackage));

        Optional<Package> result = defaultPackageService.findPackageByName("Test");

        assertThat(result).isPresent().contains(mockPackage);
        verify(packageRepository, times(1)).findByNameIgnoreCase("Test");
    }

    @Test
    public void testFindPackageByName_NotFound() {
        when(packageRepository.findByNameIgnoreCase("Test")).thenReturn(Optional.empty());

        Optional<Package> result = defaultPackageService.findPackageByName("Test");

        assertThat(result).isEmpty();
        verify(packageRepository, times(1)).findByNameIgnoreCase("Test");
    }

    @Test
    public void testFindPackageByForm_Found() {
        Package mockPackage = new Package("Test", 'X', List.of("XX"));
        when(packageRepository.findAll()).thenReturn(List.of(mockPackage));

        Optional<Package> result = defaultPackageService.findPackageByForm(List.of("XX"));

        assertThat(result).isPresent().contains(mockPackage);
        verify(packageRepository, times(1)).findAll();
    }

    @Test
    public void testFindPackageByForm_NotFound() {
        when(packageRepository.findAll()).thenReturn(List.of());

        Optional<Package> result = defaultPackageService.findPackageByForm(List.of("XX"));

        assertThat(result).isEmpty();
        verify(packageRepository, times(1)).findAll();
    }

    @Test
    public void testCreatePackage_Success() throws PackageAlreadyExistsException {
        Package mockPackage = new Package("Test", 'X', List.of("XX"));
        when(packageRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(packageRepository.save(any(Package.class))).thenReturn(mockPackage);

        Package result = defaultPackageService.createPackage("Test", 'X', List.of("XX"));

        assertThat(result).isEqualTo(mockPackage);
        verify(packageRepository, times(1)).findByNameIgnoreCase("Test");
        verify(packageRepository, times(1)).save(any(Package.class));
    }

    @Test
    public void testCreatePackage_AlreadyExists() {
        when(packageRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(new Package("Test", 'X', List.of("XX"))));

        assertThatThrownBy(() -> defaultPackageService.createPackage("Test", 'X', List.of("XX")))
                .isInstanceOf(PackageAlreadyExistsException.class)
                .hasMessageContaining("Test");

        verify(packageRepository, times(1)).findByNameIgnoreCase("Test");
        verify(packageRepository, times(0)).save(any(Package.class));
    }

    @Test
    public void testUpdatePackage_Success() throws PackageNotFoundException {
        Package mockPackage = new Package("Test", 'X', List.of("XX"));
        when(packageRepository.findByNameIgnoreCase("Test")).thenReturn(Optional.of(mockPackage));

        defaultPackageService.updatePackage("Test", 'O', List.of("OO"));

        assertThat(mockPackage.getSymbol()).isEqualTo('O');
        assertThat(mockPackage.getForm()).containsExactly("OO");
        verify(packageRepository, times(1)).findByNameIgnoreCase("Test");
    }

    @Test
    public void testUpdatePackage_NotFound() {
        when(packageRepository.findByNameIgnoreCase("Test")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> defaultPackageService.updatePackage("Test", 'O', List.of("OO")))
                .isInstanceOf(PackageNotFoundException.class)
                .hasMessageContaining("Test");

        verify(packageRepository, times(1)).findByNameIgnoreCase("Test");
    }

    @Test
    public void testRemovePackage_Success() throws PackageNotFoundException {
        when(packageRepository.removeByNameIgnoreCase("Test")).thenReturn(1);

        defaultPackageService.removePackage("Test");

        verify(packageRepository, times(1)).removeByNameIgnoreCase("Test");
    }

    @Test
    public void testRemovePackage_NotFound() {
        when(packageRepository.removeByNameIgnoreCase("Test")).thenReturn(0);

        assertThatThrownBy(() -> defaultPackageService.removePackage("Test"))
                .isInstanceOf(PackageNotFoundException.class)
                .hasMessageContaining("Test");

        verify(packageRepository, times(1)).removeByNameIgnoreCase("Test");
    }
}