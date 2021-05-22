package natalia.koc.sklepZoologiczny.repositories;

import natalia.koc.sklepZoologiczny.domain.Profil;
import natalia.koc.sklepZoologiczny.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilRepozytorium extends JpaRepository<Profil, Integer> {
    Profil findByUser(User user);
    Profil findByTelefon(String telefon);
}
