package ru.liga.packagesproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.liga.packagesproject.models.Package;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<Package, String> {
    Optional<Package> findByNameIgnoreCase(String name);
    Optional<Package> findByForm(List<String> name);
    Iterable<Package> findBySymbol(char name);

}
