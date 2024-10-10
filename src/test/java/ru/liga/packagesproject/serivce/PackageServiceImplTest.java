package ru.liga.packagesproject.serivce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.liga.packagesproject.exception.PackageAlreadyExistsException;
import ru.liga.packagesproject.exception.PackageNotFoundException;
import ru.liga.packagesproject.model.Package;
import ru.liga.packagesproject.repository.PackageRepository;
import ru.liga.packagesproject.service.impl.PackageServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PackageServiceImplTest {

    @Mock
    private PackageRepository packageRepository;

    @InjectMocks
    private PackageServiceImpl packageServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByName_Found() {
        Package mockPackage = new Package("Test", 'X', List.of("XX"));
        when(packageRepository.findByNameIgnoreCase("Test")).thenReturn(Optional.of(mockPackage));

        Optional<Package> result = packageServiceImpl.findByName("Test");

        assertThat(result).isPresent().contains(mockPackage);
        verify(packageRepository, times(1)).findByNameIgnoreCase("Test");
    }

    @Test
    public void testFindByName_NotFound() {
        when(packageRepository.findByNameIgnoreCase("Test")).thenReturn(Optional.empty());

        Optional<Package> result = packageServiceImpl.findByName("Test");

        assertThat(result).isEmpty();
        verify(packageRepository, times(1)).findByNameIgnoreCase("Test");
    }

    @Test
    public void testFindByForm_Found() {
        Package mockPackage = new Package("Test", 'X', List.of("XX"));
        when(packageRepository.findAll()).thenReturn(List.of(mockPackage));

        Optional<Package> result = packageServiceImpl.findByForm(List.of("XX"));

        assertThat(result).isPresent().contains(mockPackage);
        verify(packageRepository, times(1)).findAll();
    }

    @Test
    public void testFindByForm_NotFound() {
        when(packageRepository.findAll()).thenReturn(List.of());

        Optional<Package> result = packageServiceImpl.findByForm(List.of("XX"));

        assertThat(result).isEmpty();
        verify(packageRepository, times(1)).findAll();
    }

    @Test
    public void testCreate_Success() throws PackageAlreadyExistsException {
        Package mockPackage = new Package("Test", 'X', List.of("XX"));
        when(packageRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(packageRepository.save(any(Package.class))).thenReturn(mockPackage);

        Package result = packageServiceImpl.create("Test", 'X', List.of("XX"));

        assertThat(result).isEqualTo(mockPackage);
        verify(packageRepository, times(1)).findByNameIgnoreCase("Test");
        verify(packageRepository, times(1)).save(any(Package.class));
    }

    @Test
    public void testCreate_AlreadyExists() {
        when(packageRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(new Package("Test", 'X', List.of("XX"))));

        assertThatThrownBy(() -> packageServiceImpl.create("Test", 'X', List.of("XX")))
                .isInstanceOf(PackageAlreadyExistsException.class)
                .hasMessageContaining("Test");

        verify(packageRepository, times(1)).findByNameIgnoreCase("Test");
        verify(packageRepository, times(0)).save(any(Package.class));
    }

    @Test
    public void testUpdate_Success() throws PackageNotFoundException {
        Package mockPackage = new Package("Test", 'X', List.of("XX"));
        when(packageRepository.findByNameIgnoreCase("Test")).thenReturn(Optional.of(mockPackage));

        packageServiceImpl.update("Test", 'O', List.of("OO"));

        assertThat(mockPackage.getSymbol()).isEqualTo('O');
        assertThat(mockPackage.getForm()).containsExactly("OO");
        verify(packageRepository, times(1)).findByNameIgnoreCase("Test");
    }

    @Test
    public void testUpdate_NotFound() {
        when(packageRepository.findByNameIgnoreCase("Test")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> packageServiceImpl.update("Test", 'O', List.of("OO")))
                .isInstanceOf(PackageNotFoundException.class)
                .hasMessageContaining("Test");

        verify(packageRepository, times(1)).findByNameIgnoreCase("Test");
    }

    @Test
    public void testRemove_Success() throws PackageNotFoundException {
        when(packageRepository.removeByNameIgnoreCase("Test")).thenReturn(1);

        packageServiceImpl.remove("Test");

        verify(packageRepository, times(1)).removeByNameIgnoreCase("Test");
    }

    @Test
    public void testRemove_NotFound() {
        when(packageRepository.removeByNameIgnoreCase("Test")).thenReturn(0);

        assertThatThrownBy(() -> packageServiceImpl.remove("Test"))
                .isInstanceOf(PackageNotFoundException.class)
                .hasMessageContaining("Test");

        verify(packageRepository, times(1)).removeByNameIgnoreCase("Test");
    }
}