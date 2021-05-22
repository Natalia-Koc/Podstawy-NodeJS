package natalia.koc.sklepZoologiczny.repositories;

import natalia.koc.sklepZoologiczny.domain.Kategoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KategoriaRepozytorium extends JpaRepository<Kategoria, Integer> {
    Kategoria findByName(String name);
    Kategoria findFirstByName(String name);
    Kategoria deleteByName(String name);
}
