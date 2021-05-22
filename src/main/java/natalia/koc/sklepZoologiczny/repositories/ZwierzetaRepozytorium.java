package natalia.koc.sklepZoologiczny.repositories;

import natalia.koc.sklepZoologiczny.domain.Zwierzeta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZwierzetaRepozytorium extends JpaRepository<Zwierzeta, Integer> {
}
