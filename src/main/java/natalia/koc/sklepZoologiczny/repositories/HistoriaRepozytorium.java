package natalia.koc.sklepZoologiczny.repositories;

import natalia.koc.sklepZoologiczny.domain.Historia;
import natalia.koc.sklepZoologiczny.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistoriaRepozytorium extends JpaRepository<Historia, Integer> {
    List<Historia> findAllByUserAndCzyUserUsunal(User user, Boolean nie);
    List<Historia> findAllByCzyAdminUsunal(Boolean wartosc);
}
