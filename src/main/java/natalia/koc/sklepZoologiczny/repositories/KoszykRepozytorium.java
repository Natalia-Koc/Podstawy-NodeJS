package natalia.koc.sklepZoologiczny.repositories;

import natalia.koc.sklepZoologiczny.domain.Koszyk;
import natalia.koc.sklepZoologiczny.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KoszykRepozytorium extends JpaRepository<Koszyk, Integer> {
    List<Koszyk> findAllByUser(User user);
    Koszyk findAllByUserOrNazwa(User user, String nazwa);
    Koszyk findByUserAndNazwa(User user, String nazwa);
    List<Koszyk> deleteAllByUser(User user);
}
