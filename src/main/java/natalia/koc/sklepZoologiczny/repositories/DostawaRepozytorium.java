package natalia.koc.sklepZoologiczny.repositories;

import natalia.koc.sklepZoologiczny.domain.Dostawa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DostawaRepozytorium extends JpaRepository<Dostawa, Integer> {
}